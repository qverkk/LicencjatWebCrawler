package com.marek.webcrawler.config

import com.marek.webcrawler.tree.VisitedTree
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.util.concurrent.LinkedBlockingQueue

class Config {
    var startUrl: String = ""
    var applicationClosed = false

    companion object {
        val status: ObservableList<String> = FXCollections.observableArrayList()
        val visitedWebsites = mutableMapOf<String, Int>()
        val domainCount = mutableMapOf<String, Int>()
        val urlList = LinkedBlockingQueue<String>()
        val visitedTree = VisitedTree(null)
        var numberOfThreads: Int = 0
        var maxNumberOfSearches: Int = 0
        var running = false
    }
}
