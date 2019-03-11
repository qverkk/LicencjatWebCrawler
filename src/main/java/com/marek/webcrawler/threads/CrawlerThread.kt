package com.marek.webcrawler.threads

class CrawlerThread(val maxResults: Int) : Thread() {

    override fun run() {
        while (!Thread.currentThread().isInterrupted) {

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
}