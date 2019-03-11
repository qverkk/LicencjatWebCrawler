package com.marek.webcrawler.tree

import java.util.*

class VisitedTree(val root: Node?) {

    fun getDepth(node: Node): Int {
        var depth = 0
        var tmp = node
        while (tmp.parent != null) {
            depth++
            tmp = tmp.parent!!
        }
        return depth
    }

    @Synchronized
    fun getUrl(url: String, node: Node): Node? {
        val stack = Stack<Node>()
        stack.push(node)
        while (!stack.empty()) {
            val tmp = stack.pop()
            if (tmp.data == url) {
                return tmp
            }

            val children = tmp.children
            children.forEach {
                stack.push(it)
            }
        }
        return null
    }

    @Synchronized
    fun addUrl(parentUrl: String, newUrl: String) {
        val node = getUrl(parentUrl, root ?: return) ?: return
        node.addChild(newUrl)
    }

    fun print(node: Node) {
        val stack = Stack<Node>()
        stack.push(node)

        while (!stack.empty()) {
            val pop = stack.pop()
            val depth = getDepth(pop)

            for (i in 0..depth) {
                print("-")
            }
            println(pop.data)

            val children = pop.children
            children.forEach {
                stack.push(it)
            }
        }
    }

    fun getAsList(node: Node): List<String> {
        val list = mutableListOf<String>()
        val stack = Stack<Node>()
        stack.push(node)

        while (!stack.empty()) {
            val pop = stack.pop()
            val depth = getDepth(pop)

            var tmp = ""
            for (i in 0..depth) {
                tmp += "-"
            }
            tmp += pop.data
            list.add(tmp)

            val children = pop.children
            children.forEach {
                stack.push(it)
            }
        }
        return list.toList()
    }
}