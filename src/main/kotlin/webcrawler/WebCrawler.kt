package com.marek.webcrawler

import com.marek.webcrawler.config.Config
import com.marek.webcrawler.ui.presentation.MainView
import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage

class WebCrawler : Application() {

    val config = Config()

    override fun start(primaryStage: Stage?) {
        val view = MainView(config)

        val scene = Scene(view.parent)
        primaryStage?.scene = scene
        primaryStage?.show()
        primaryStage?.setOnCloseRequest {
            config.applicationClosed = true
        }
    }
}
/*fun main() {
    val webHelper = WebsiteHelper()
    val link = "http://www.kul.pl/"
    val node = Node(link, null)
    val tree = VisitedTree(node)
    webHelper.getWebsiteUrls(link, tree)
    tree.print(node)

    MainView()
}*/
