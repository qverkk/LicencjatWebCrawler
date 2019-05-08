package com.marek.testing.ui.presentation

import com.marek.testing.ui.controllers.GetPresenter
import com.marek.webcrawler.ui.FXMLView

class GetView : FXMLView() {

    init {
        loadParent("ui/GetTest.fxml", GetPresenter())
    }
}