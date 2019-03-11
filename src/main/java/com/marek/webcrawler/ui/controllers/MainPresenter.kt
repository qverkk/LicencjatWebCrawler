package com.marek.webcrawler.ui.controllers

import com.marek.webcrawler.config.Config
import com.marek.webcrawler.methods.WebsiteHelper
import com.marek.webcrawler.tree.Node
import com.marek.webcrawler.tree.VisitedTree
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
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

    private val thread = Thread {
        Runnable {
            while (!Thread.interrupted()) {
                if (config.applicationClosed) {
                    break
                } else if (config.running) {
                    if (::startButton.isInitialized && !startButton.isDisabled)
                        startButton.isDisable = true
                    if (::threadsSlider.isInitialized && !threadsSlider.isDisabled)
                        threadsSlider.isDisable = true
                    if (::maxDepthSlider.isInitialized && !maxDepthSlider.isDisabled)
                        maxDepthSlider.isDisable = true
                    if (::websiteUrlTextField.isInitialized && !websiteUrlTextField.isDisabled)
                        websiteUrlTextField.isDisable = true
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

        initializeListeners()
        thread.start()
    }

    private fun initializeListeners() {
        threadsSlider.valueProperty().addListener { _, _, newValue ->
            val newIntValue = newValue.toInt()
            config.numberOfThreads = newIntValue
            threadsUsedLabel.text = newIntValue.toString()
            threadsSlider.value = newIntValue.toDouble()
        }

        maxDepthSlider.valueProperty().addListener { _, _, newValue ->
            val newIntValue = newValue.toInt()
            config.maxNumberOfSearches = newIntValue
            depthSearchLabel.text = newIntValue.toString()
            maxDepthSlider.value = newIntValue.toDouble()
        }

        startButton.setOnAction {
            val url = websiteUrlTextField.text
            if (!url.isNullOrEmpty()) {
                config.running = true
                config.startUrl = url
                logStatus("Starting...")
                kotlin.concurrent.thread(block = {
                    val webHelper = WebsiteHelper()
                    val node = Node(url, null)
                    val tree = VisitedTree(node)
                    webHelper.getWebsiteUrls(url, tree)
                    val list = tree.getAsList(node)
                    list.forEach {
                        logStatus(it)
                    }
                    config.running = false
                    logStatus("Finished...")
                })
            }
        }

        terminateButton.setOnAction {
            config.running = false
        }
    }
}

fun logStatus(status: String) {
    Platform.runLater {
        Config.status.add(status)
    }
}
