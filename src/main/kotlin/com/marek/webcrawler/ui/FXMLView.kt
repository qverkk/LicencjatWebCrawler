package com.marek.webcrawler.ui

import javafx.fxml.FXMLLoader
import javafx.scene.Parent

open class FXMLView {

    var parent: Parent? = null

    fun loadParent(path: String, controller: Any? = null) {
        val resource = javaClass.classLoader.getResource(path)
        val fxmlLoader = FXMLLoader(resource)

        if (controller != null)
            fxmlLoader.setController(controller)

        parent = fxmlLoader.load()
    }
}