package com.marek.webcrawler.tree

class Node {

    var data: String = ""
    var parent: Node? = null
    var children: MutableList<Node> = mutableListOf()

    constructor() {
        this.data = ""
        this.parent = null
    }

    constructor(data: String, parent: Node) {
        this.data = data
        this.parent = parent
    }

    fun addChild(data: String) {
        val child = Node(data, this)
        children.add(child)
    }

    fun isRoot(): Boolean {
        return parent == null
    }

    fun setParentNode(parent: Node) {
        parent.addChild(data)
        this.parent = parent
    }
}