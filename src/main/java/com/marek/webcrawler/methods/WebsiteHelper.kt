package com.marek.webcrawler.methods

import org.jsoup.Connection
import org.jsoup.Jsoup

class WebsiteHelper {

    fun getWebsiteUrls(url: String): List<String> {
        val connection = connectTo(url)
        val document = connection.get()
        val list = mutableListOf<String>()
        val linkElements = document.getElementsByTag("a")
        for (linkElement in linkElements) {
            val hrefAttr = linkElement.attr("href")
            if (hrefAttr.startsWith("http")) {
                list.add(hrefAttr)
            }
        }
        return list
    }

    private fun connectTo(url: String): Connection {
        return Jsoup.connect(url)
    }
}