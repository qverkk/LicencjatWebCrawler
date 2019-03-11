package com.marek.webcrawler.ui.presentation

import com.marek.webcrawler.config.Config
import com.marek.webcrawler.ui.FXMLView
import com.marek.webcrawler.ui.controllers.MainPresenter

class MainView(val config: Config) : FXMLView() {

    init {
        loadParent("ui/CrawlerGui.fxml", MainPresenter(config))
    }
}