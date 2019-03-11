package com.marek.webcrawler.threads

import com.marek.webcrawler.config.Config
import com.marek.webcrawler.methods.WebsiteHelper

class CrawlerThread : Thread() {

    override fun run() {
        val websiteHelper = WebsiteHelper()

        while (!Thread.currentThread().isInterrupted) {
            //println("Visited hosts ${Config.visitedWebsites.size} where max size is ${Config.maxNumberOfSearches}")
            if (Config.visitedWebsites.size > Config.maxNumberOfSearches || !Config.running)
                break
            val host = Config.urlList.poll()
            if (host != null)
                websiteHelper.getWebsiteUrls(host, Config.visitedTree)
        }
    }
}

fun getDomain(url: String): String {
    if (url.startsWith("https://")) {
        return replaceStrings(url, "https://")
    } else if (url.startsWith("http://")) {
        return replaceStrings(url, "http://")
    }
    return ""
}

private fun replaceStrings(url: String, replacer: String): String {
    val tmp = url.replace(replacer, "")
    var result = ""
    for (c in tmp) {
        if (c == '/')
            break
        result += c
    }
    return result
}
