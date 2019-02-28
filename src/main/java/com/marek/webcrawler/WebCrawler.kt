package com.marek.webcrawler

import com.marek.webcrawler.methods.WebsiteHelper
import com.marek.webcrawler.tree.Node
import com.marek.webcrawler.tree.VisitedTree

fun main() {
    val webHelper = WebsiteHelper()
    val link = "http://www.kul.pl/"
    val node = Node(link, null)
    val tree = VisitedTree(node)
    webHelper.getWebsiteUrls(link, tree)
    tree.print(node)
}