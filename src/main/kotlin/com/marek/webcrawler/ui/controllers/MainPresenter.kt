package com.marek.webcrawler.ui.controllers

import com.marek.webcrawler.config.Config
import com.marek.webcrawler.threads.CrawlerCoroutine
import com.marek.webcrawler.threads.CrawlerThread
import com.marek.webcrawler.tree.Node
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.layout.HBox
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL
import java.util.*

class MainPresenter(val config: Config) : Initializable {

    @FXML
    private lateinit var threadsSlider: Slider
    @FXML
    private lateinit var maxDepthSlider: Slider

    @FXML
    private lateinit var threadsUsedLabel: Label
    @FXML
    private lateinit var depthSearchLabel: Label

    @FXML
    private lateinit var startButton: Button
    @FXML
    private lateinit var terminateButton: Button

    @FXML
    private lateinit var websiteUrlTextField: TextField

    @FXML
    private lateinit var statusListView: ListView<String>

    @FXML
    private lateinit var coroutinesCheckbox: CheckBox

    @FXML
    private lateinit var threadsHbox: HBox

    private val thread = Thread {
        Runnable {
            while (!Thread.interrupted()) {
                if (config.applicationClosed) {
                    break
                } else if (Config.running) {
                    if (::startButton.isInitialized && !startButton.isDisabled)
                        startButton.isDisable = true
                    if (::threadsSlider.isInitialized && !threadsSlider.isDisabled)
                        threadsSlider.isDisable = true
                    if (::maxDepthSlider.isInitialized && !maxDepthSlider.isDisabled)
                        maxDepthSlider.isDisable = true
                    if (::websiteUrlTextField.isInitialized && !websiteUrlTextField.isDisabled)
                        websiteUrlTextField.isDisable = true
                    if (::coroutinesCheckbox.isInitialized && !coroutinesCheckbox.isDisabled)
                        coroutinesCheckbox.isDisable = true
                    if (::terminateButton.isInitialized && terminateButton.isDisabled)
                        terminateButton.isDisable = false
                } else {
                    if (::startButton.isInitialized && startButton.isDisabled)
                        startButton.isDisable = false
                    if (::threadsSlider.isInitialized && threadsSlider.isDisabled)
                        threadsSlider.isDisable = false
                    if (::maxDepthSlider.isInitialized && maxDepthSlider.isDisabled)
                        maxDepthSlider.isDisable = false
                    if (::websiteUrlTextField.isInitialized && websiteUrlTextField.isDisabled)
                        websiteUrlTextField.isDisable = false
                    if (::coroutinesCheckbox.isInitialized && coroutinesCheckbox.isDisabled)
                        coroutinesCheckbox.isDisable = false
                    if (::terminateButton.isInitialized && !terminateButton.isDisabled)
                        terminateButton.isDisable = true
                }
                Thread.sleep(50)
            }
        }.run()
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        threadsUsedLabel.text = threadsSlider.value.toInt().toString()
        depthSearchLabel.text = maxDepthSlider.value.toInt().toString()
        statusListView.items = Config.status
        Config.numberOfThreads = threadsSlider.value.toInt()
        Config.maxNumberOfSearches = maxDepthSlider.value.toInt()

        initializeListeners()
        thread.start()
    }

    private fun initializeListeners() {
        threadsSlider.valueProperty().addListener { _, _, newValue ->
            val newIntValue = newValue.toInt()
            Config.numberOfThreads = newIntValue
            threadsUsedLabel.text = newIntValue.toString()
            threadsSlider.value = newIntValue.toDouble()
        }

        maxDepthSlider.valueProperty().addListener { _, _, newValue ->
            val newIntValue = newValue.toInt()
            Config.maxNumberOfSearches = newIntValue
            depthSearchLabel.text = newIntValue.toString()
            maxDepthSlider.value = newIntValue.toDouble()
        }

        coroutinesCheckbox.setOnAction {
            if (coroutinesCheckbox.isSelected) {
                threadsSlider.max = 1000.0
            } else {
                threadsSlider.max = 10.0
            }
        }

        startButton.setOnAction {
            val url = websiteUrlTextField.text
            if (!url.isNullOrEmpty()) {
                Config.running = true

                Config.urlList.clear()
                Config.visitedWebsites.clear()
                Config.domainCount.clear()

                Config.urlList.put(url)
                Config.visitedTree.root = Node(url, null)
                config.startUrl = url
                threadsHbox.children.clear()
                logStatus("Starting...")

                if (coroutinesCheckbox.isSelected)
                    launchCoroutines()
                else
                    launchThreads()
            }
        }

        terminateButton.setOnAction {
            Config.running = false
        }
    }

    private fun launchThreads() {
        val startTime = System.currentTimeMillis()
        val threads = mutableListOf<CrawlerThread>()
        for (i in 0..(Config.numberOfThreads - 1)) {
            println(i)
            threads.add(object : CrawlerThread(i) {
                private val threadPresenter = ThreadPresenter(config, this@MainPresenter)

                override fun onVisit(url: String) {
                    threadPresenter.setColor("green", "Running")
                    threadPresenter.updateVisitedUrl(url)
                    Platform.runLater {
                        if (!threadsHbox.children.contains(threadPresenter.parent)) {
                            threadsHbox.children.add(threadPresenter.parent)
                        }
                    }
                }

                override fun onStarted() {
                    threadPresenter.setThreadNumber(number)
                }

                override fun onStopped() {
                    threadPresenter.setColor("red", "Stopped")
                }
            })
            threads[i].start()
        }

        kotlin.concurrent.thread {
            logStatus("Running...")
            while (true) {
                var counter = 0
                for (thread in threads) {
                    if (thread.state == Thread.State.WAITING || thread.state == Thread.State.TERMINATED)
                        counter++
                }
                if (counter == Config.numberOfThreads)
                    break
            }
            for (thread in threads) {
                thread.interrupt()
            }
            val list = Config.visitedTree.getAsList(Config.visitedTree.root!!)
            list.forEach {
                logStatus(it)
            }
            Config.running = false
            logStatus("Finished...")
            logStatus("In: ${System.currentTimeMillis() - startTime} ms")
        }
    }

    private fun launchCoroutines() {
        val startTime = System.currentTimeMillis()
        val coroutines = mutableListOf<CrawlerCoroutine>()
        for (i in 0..(Config.numberOfThreads - 1)) {
            coroutines.add(object : CrawlerCoroutine(i) {
                private val threadPresenter = ThreadPresenter(config, this@MainPresenter)

                override fun onVisit(url: String) {
                    threadPresenter.setColor("green", "Running")
                    threadPresenter.updateVisitedUrl(url)
                    Platform.runLater {
                        if (!threadsHbox.children.contains(threadPresenter.parent)) {
                            threadsHbox.children.add(threadPresenter.parent)
                        }
                    }
                }

                override fun onStarted() {
                    threadPresenter.setThreadNumber(number)
                }

                override fun onStopped() {
                    threadPresenter.setColor("red", "Stopped")
                }
            })
            GlobalScope.launch {
                coroutines[i].run()
            }
        }

        kotlin.concurrent.thread {
            logStatus("Running...")
            while (true) {
                var counter = 0
                for (coroutine in coroutines) {
                    if (!coroutine.running)
                        counter++
                }
                if (counter == Config.numberOfThreads)
                    break
            }
            val list = Config.visitedTree.getAsList(Config.visitedTree.root!!)
            list.forEach {
                logStatus(it)
            }
            Config.running = false
            logStatus("Finished...")
            logStatus("In: ${System.currentTimeMillis() - startTime} ms")

        }
    }
}

fun logStatus(status: String) {
    Platform.runLater {
        Config.status.add(status)
    }
}
