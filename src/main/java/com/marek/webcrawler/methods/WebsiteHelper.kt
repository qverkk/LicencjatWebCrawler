package com.marek.webcrawler.methods

import com.marek.webcrawler.config.Config
import com.marek.webcrawler.tree.VisitedTree
import com.marek.webcrawler.ui.controllers.logStatus
import org.jsoup.Connection
import org.jsoup.Jsoup

class WebsiteHelper {

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
                        if (!Config.visitedWebsites.contains(hrefAttr)) {
                            list.add(hrefAttr)
                            val count = Config.visitedWebsites[hrefAttr]
                            Config.visitedWebsites[hrefAttr] = count?.plus(1) ?: 1
                            tree.addUrl(url, hrefAttr)
                            /*if (visitedUrls.size < 10) {
                                getWebsiteUrls(hrefAttr, tree)
                            }*/
                        }
                    }
                }
            } catch (exception: Exception) {
                logStatus("Error connection to website $url")
            }

        }
        return list
    }

    private fun connectTo(url: String): Connection {
        return Jsoup.connect(url)
    }
}