package com.marek.webcrawler.threads

import com.marek.webcrawler.config.Config
import com.marek.webcrawler.methods.WebsiteHelper

abstract class CrawlerCoroutine(val number: Int) {

    var running = false

    fun run() {
        val websiteHelper = WebsiteHelper()
        onStarted()
        while (Config.running) {
            if (Config.visitedWebsites.size > Config.maxNumberOfSearches) {
                running = false
                break
            }
            val url = Config.urlList.poll()
            if (url != null) {
                running = true
                onVisit(url)
                println("Coroutine$number visiting $url")
                websiteHelper.getWebsiteUrls(url, Config.visitedTree)
            } else {
                running = false
            }
        }
        running = false
        onStopped()
    }

    abstract fun onVisit(url: String)

    abstract fun onStopped()

    abstract fun onStarted()
}