package com.marek.webcrawler.methods

import com.marek.webcrawler.config.Config
import com.marek.webcrawler.threads.getDomain
import com.marek.webcrawler.tree.VisitedTree
import com.marek.webcrawler.ui.controllers.logStatus
import org.jsoup.Connection
import org.jsoup.Jsoup

class WebsiteHelper {

    fun getWebsiteUrls(url: String, tree: VisitedTree): MutableList<String> {
        val connection = connectTo(url)
        val response = connection.response()
        val list = mutableListOf<String>()
        if (response.statusCode() != 404) {
            try {
                val document = connection
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36")
                        .timeout(2000).get()
                val linkElements = document.getElementsByTag("a")
                for (linkElement in linkElements) {
                    val hrefAttr = linkElement.attr("href")
                    if (hrefAttr.startsWith("http")) {
                        if (!Config.visitedWebsites.contains(hrefAttr)) {
                            list.add(hrefAttr)
                            Config.urlList.put(hrefAttr)
                            tree.addUrl(url, hrefAttr)
                        }
                        if (Config.visitedWebsites.containsKey(hrefAttr)) {
                            val count = Config.visitedWebsites[hrefAttr]
                            Config.visitedWebsites[hrefAttr] = count?.plus(1) ?: 1
                        } else {
                            Config.visitedWebsites[hrefAttr] = 1
                        }
                    }
                }
                val domain = getDomain(url)
                var count = Config.domainCount[domain]
                Config.domainCount[domain] = count?.plus(1) ?: 1

                count = Config.visitedWebsites[url]
                Config.visitedWebsites[url] = count?.plus(1) ?: 1
            } catch (exception: Exception) {
                logStatus("Error connection to website $url")
                logStatus(exception.toString())
            }

        }
        return list
    }

    private fun connectTo(url: String): Connection {
        return Jsoup.connect(url)
    }
}