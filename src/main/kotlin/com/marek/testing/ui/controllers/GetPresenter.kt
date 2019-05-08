package com.marek.testing.ui.controllers

import com.google.gson.Gson
import com.marek.testing.data.Post
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class GetPresenter : Initializable {

    @FXML
    private lateinit var urlTF: TextField
    @FXML
    private lateinit var getButton: Button
    @FXML
    private lateinit var resultList: ListView<String>
    @FXML
    private lateinit var delaySlider: Slider
    @FXML
    private lateinit var threadCheckbox: CheckBox
    @FXML
    private lateinit var delayLabel: Label

    private var running = false

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        setupGetButton()
        setupSlider()
    }

    private fun setupSlider() {
        delaySlider.valueProperty().addListener { _, _, newValue ->
            delayLabel.text = newValue.toString()
        }
    }

    private fun setupGetButton() {
        urlTF.promptText = "Tymczasowo wylaczone, domyslna strona to https://jsonplaceholder.typicode.com/posts"
        urlTF.isDisable = true
        getButton.setOnAction {
            if (running) {
                val alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Informacja"
                alert.headerText = "Uwaga!"
                alert.contentText = "Program jest już uruchomiony!"
                alert.showAndWait()
                return@setOnAction
            }
            if (!urlTF.isDisable && urlTF.text.isNullOrEmpty()) {
                val alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Pole URL jest puste"
                alert.headerText = "Błąd!"
                alert.contentText = "Pole URL jest puste"
                alert.showAndWait()
                return@setOnAction
            }
            val url = if (urlTF.isDisable)
                "https://jsonplaceholder.typicode.com/posts"
            else
                urlTF.text

            running = true
            if (!threadCheckbox.isSelected) {
                resultList.items.clear()
                resultList.items.add("Loading without thread...")
                getResponse(url)
            } else {
                Thread {
                    Runnable {
                        Platform.runLater {
                            resultList.items.clear()
                            resultList.items.add("Loading with thread...")
                        }
                        getResponse(url)
                    }.run()
                }.start()
            }
        }
    }

    private fun getResponse(urlString: String) {
        val url = URL(urlString)
        val con = url.openConnection() as HttpURLConnection
        con.requestMethod = "GET"
        con.setRequestProperty("Content-Type", "application/json")

        val status = con.responseCode
        if (status != 200)
            return

        val inputStream = BufferedReader(InputStreamReader(con.inputStream))
        val content = StringBuffer()
        inputStream.readLines().forEach {
            content.append(it)
        }
        inputStream.close()

        val gson = Gson()
        val values = gson.fromJson(content.toString(), Array<Post>::class.java).toList()

        values.forEach {
            Platform.runLater {
                resultList.items.add("ID: ${it.id}")
                resultList.items.add("User id: ${it.userId}")
                resultList.items.add("Title: ${it.title}")
                resultList.items.add("Body: ${it.body}")
                resultList.items.add("------------------------------------")
            }
            Thread.sleep(delaySlider.value.toLong())
        }
        running = false
    }
}
