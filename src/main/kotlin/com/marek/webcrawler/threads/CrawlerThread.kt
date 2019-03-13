package com.marek.webcrawler.threads

import com.marek.webcrawler.config.Config
import com.marek.webcrawler.methods.WebsiteHelper
import com.marek.webcrawler.ui.controllers.MainPresenter
import com.marek.webcrawler.ui.controllers.ThreadPresenter
import javafx.application.Platform

class CrawlerThread(val number: Int, val mainPresenter: MainPresenter) : Thread() {

    private val threadPresenter = ThreadPresenter(mainPresenter.config, mainPresenter)

    override fun run() {
        val websiteHelper = WebsiteHelper()
        threadPresenter.setThreadNumber(number)

        while (!Thread.currentThread().isInterrupted) {
            if (Config.visitedWebsites.size > Config.maxNumberOfSearches || !Config.running) {
                threadPresenter.setColor("red", "Stopped")
                break
            }
            val host = Config.urlList.poll()
            if (host != null) {
                println("Thread$number visiting $host")
                threadPresenter.setColor("green", "Running")
                threadPresenter.updateVisitedUrl(host)
                val threadHbox = mainPresenter.getThreadHbox()
                if (threadHbox != null) {
                    Platform.runLater {
                        if (!threadHbox.children.contains(threadPresenter.parent)) {
                            threadHbox.children.add(threadPresenter.parent)
                        }
                    }
                }
                websiteHelper.getWebsiteUrls(host, Config.visitedTree)
            }
        }
        threadPresenter.setColor("red", "Stopped")
    }
}

fun getDomain(url: String): String {
    return url
            .removePrefix("https://")
            .removePrefix("http://")
}