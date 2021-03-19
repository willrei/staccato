package generator.app

import javafx.geometry.Pos
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*

private const val TEXTBOX_ROW_COUNT = 5
private const val TEXTBOX_COLUMN_COUNT = 30
private const val LABEL_FONT_SIZE = 15

open class Styles : Stylesheet() {

    companion object {

        val hbox by cssclass()
        val boldButton by cssclass()
        val textArea by cssclass()
        val label by cssclass()
        val informationLabel by cssclass()

    }

    init {

        hbox { alignment = Pos.CENTER_LEFT }

        boldButton { fontWeight = FontWeight.BOLD }

        textArea {

            prefRowCount = TEXTBOX_ROW_COUNT
            prefColumnCount = TEXTBOX_COLUMN_COUNT

        }

        label {

            fontWeight = FontWeight.BOLD
            fontSize = LABEL_FONT_SIZE.px

        }

        informationLabel {

            textFill = Color.DARKGRAY
            fontWeight = FontWeight.BOLD

        }

    }

}