package com.marek.webcrawler.ui.controllers

import com.marek.webcrawler.config.Config
import com.marek.webcrawler.ui.FXMLView
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.layout.VBox
import java.net.URL
import java.util.*

class ThreadPresenter(val config: Config, val mainPresenter: MainPresenter) : Initializable, FXMLView() {

    init {
        val parent = loadParent("ui/ThreadGui.fxml", this)
    }

    @FXML
    private lateinit var threadVbox: VBox

    @FXML
    private lateinit var threadNumberLabel: Label
    @FXML
    private lateinit var statusIndicatorLabel: Label

    @FXML
    private lateinit var visitedWebsiteTextArea: TextArea

    override fun initialize(location: URL?, resources: ResourceBundle?) {

    }

    fun updateVisitedUrl(url: String) {
        Platform.runLater {
            visitedWebsiteTextArea.text = url
        }
    }

    fun setThreadNumber(number: Int) {
        Platform.runLater {
            threadNumberLabel.text = "Thread $number"
        }
    }

    fun setColor(color: String, status: String) {
        Platform.runLater {
            statusIndicatorLabel.text = status
            statusIndicatorLabel.style = "-fx-background-color: $color"
        }
    }
}