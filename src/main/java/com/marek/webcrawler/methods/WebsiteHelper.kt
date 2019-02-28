package com.marek.webcrawler.methods

import com.marek.webcrawler.tree.VisitedTree
import org.jsoup.Connection
import org.jsoup.Jsoup

class WebsiteHelper {

    companion object {
        var visitedUrls: MutableList<String> = mutableListOf()
    }

    fun getWebsiteUrls(url: String, tree: VisitedTree): List<String> {
        val connection = connectTo(url)
        val response = connection.response()
        val list = mutableListOf<String>()
        if (response.statusCode() != 404) {
            try {
                val document = connection.get()
                val linkElements = document.getElementsByTag("a")
                for (linkElement in linkElements) {
                    val hrefAttr = linkElement.attr("href")
                    if (hrefAttr.startsWith("http")) {
                        if (!visitedUrls.contains(hrefAttr)) {
                            list.add(hrefAttr)
                            visitedUrls.add(hrefAttr)
                            tree.addUrl(url, hrefAttr)
                            if (visitedUrls.size < 10) {
                                getWebsiteUrls(hrefAttr, tree)
                            }
                        }
                    }
                }
            } catch (exception: Exception) {

            }

        }
        return list
    }

    private fun connectTo(url: String): Connection {
        return Jsoup.connect(url)
    }
}