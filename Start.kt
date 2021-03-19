package generator.app

import generator.view.MainView
import javafx.stage.Stage
import tornadofx.*

class Start: App(MainView::class, Styles::class) {

    override fun start(stage: Stage) {

        with(stage) {

            minWidth = 600.0
            minHeight = 250.0
            super.start(this)

        }
    }
}