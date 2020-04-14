package com.playtech.fontfixer.app

import com.playtech.fontfixer.view.MainView
import javafx.stage.Stage
import tornadofx.*

class MyApp: App(MainView::class, Styles::class) {
    override fun start(stage: Stage) {
        with(stage) {
            width = 300.0
            height = 400.0
        }
        super.start(stage)
    }
}