package com.playtech.fontfixer.view

import com.playtech.fontfixer.app.Styles
import javafx.geometry.Pos
import tornadofx.*

class MainView : View("Font Fixer App") {
    override val root = vbox(20, Pos.CENTER) {
        label(title) {
            addClass(Styles.heading)
        }
        checkbox("Override source file") {
            action {
                print("Checkbox clicked!")
            }
        }
    }
}