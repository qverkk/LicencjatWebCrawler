package com.marek.webcrawler

import com.marek.webcrawler.methods.WebsiteHelper

fun main() {
    val webHelper = WebsiteHelper()
    webHelper.getWebsiteUrls("https://mvnrepository.com/artifact/org.jsoup/jsoup")
}