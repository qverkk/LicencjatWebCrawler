package com.marek.webcrawler.threads

import com.marek.webcrawler.config.Config
import com.marek.webcrawler.methods.WebsiteHelper

abstract class CrawlerThread(val number: Int) : Thread() {


    final override fun run() {
        val websiteHelper = WebsiteHelper()
        onStarted()
        while (!Thread.currentThread().isInterrupted) {
            if (Config.visitedWebsites.size > Config.maxNumberOfSearches || !Config.running) {
                break
            }
            val url = Config.urlList.poll()
            if (url != null) {
                onVisit(url)
                println("Thread$number visiting $url")
                websiteHelper.getWebsiteUrls(url, Config.visitedTree)
            }
        }
        onStopped()
    }

    abstract fun onVisit(url: String)

    abstract fun onStopped()

    abstract fun onStarted()
}

fun getDomain(url: String): String {
    return url
            .removePrefix("https://")
            .removePrefix("http://")
}