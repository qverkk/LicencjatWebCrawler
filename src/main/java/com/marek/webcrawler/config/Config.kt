package com.marek.webcrawler.config

import com.marek.webcrawler.tree.VisitedTree
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.util.concurrent.LinkedBlockingQueue

class Config {
    var numberOfThreads: Int = 0
    var maxNumberOfSearches: Int = 0
    var startUrl: String = ""
    var running = false
    var applicationClosed = false

    companion object {
        val status: ObservableList<String> = FXCollections.observableArrayList()
        val visitedWebsites = mutableMapOf<String, Int>()
        val domainCount = emptyMap<String, Int>()
        val urlList = LinkedBlockingQueue<String>()
        val visitedTree = VisitedTree(null)
    }
}