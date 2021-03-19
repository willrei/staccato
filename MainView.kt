package generator.view

import generator.app.Styles
import org.controlsfx.glyphfont.*
import generator.controller.*
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.*
import javafx.scene.layout.Priority
import org.jfugue.player.Player
import tornadofx.*

// constantes
private const val BOX_SPACING = 10
private const val VBOX_VERTICAL_MARGIN = 10.0
private const val VBOX_HORIZONTAL_MARGIN = 15.0
private const val APPEND_MODE = 0
private const val SUBSTITUTE_MODE = 1
private const val WHITESPACE = " "
private const val NEWLINE = "\n"
private const val EMPTY_STRING = ""
private const val DEFAULT_MIDI_NAME = "generated"
private const val WINDOW_DEFAULT_WIDTH = 600.0
private const val WINDOW_DEFAULT_HEIGHT = 250.0

open class MainView: View("Staccato Generator") {

    private val controller: GeneratorController by inject()
    private val inputWarningText = SimpleStringProperty()
    private val playerInformationText = SimpleStringProperty()
    private lateinit var midiPath: String

    private var fileMode: Int? = null
    private var textFromFile: String? = null

    // registro de ícones
    private val font: GlyphFont = GlyphFontRegistry.font("FontAwesome")

    private val fileRadioGroup = ToggleGroup()

    // referências para os controles utilizados na interface
    private var inputTextArea: TextArea by singleAssign()
    private var outputTextArea: TextArea by singleAssign()
    private var parseButton: Button by singleAssign()
    private var playButton: Button by singleAssign()
    private var pauseButton: Button by singleAssign()
    private var resumeButton: Button by singleAssign()
    private var stopButton: Button by singleAssign()
    private var midiButton: Button by singleAssign()
    private var midiText: TextField by singleAssign()
    private var appendRadioButton: RadioButton by singleAssign()
    private var substituteRadioButton: RadioButton by singleAssign()
    private var inputItem: DrawerItem by singleAssign()
    private var outputItem: DrawerItem by singleAssign()

    private val player = Player()
    private val managedPlayer = player.managedPlayer

    override val root = drawer {

        setPrefSize(WINDOW_DEFAULT_WIDTH, WINDOW_DEFAULT_HEIGHT)
        inputItem = item("Input") {

            vbox(BOX_SPACING) {

                vboxConstraints {

                    marginBottom = VBOX_VERTICAL_MARGIN
                    marginLeft = VBOX_HORIZONTAL_MARGIN
                    marginRight = VBOX_HORIZONTAL_MARGIN
                    marginTop = VBOX_VERTICAL_MARGIN

                }

                borderpane {

                    left = label(" Source Text", font.create(FontAwesome.Glyph.PENCIL)) { addClass(Styles.label) }
                    right = label(inputWarningText) { addClass(Styles.informationLabel) }

                }

                inputTextArea = textarea {

                    isWrapText = true
                    isFocusTraversable = false
                    addClass(Styles.textArea)
                    useMaxHeight = true
                    vboxConstraints { vGrow = Priority.ALWAYS }

                    /* listener para desativar o botão de parse e o radio de append se não houver texto na caixa
                       de input */
                    textProperty().addListener { _, _, _ ->

                        when (inputTextArea.text) {

                            WHITESPACE -> inputWarningText.value = "is a whitespace"
                            NEWLINE -> inputWarningText.value = "is a newline"
                            else -> inputWarningText.value = EMPTY_STRING

                        }

                        if (isNotEmpty(inputTextArea.text)) {

                            enable(parseButton)
                            enable(appendRadioButton)

                        } else {

                            disable(parseButton)
                            disable(appendRadioButton)

                        }

                        if (appendRadioButton.isDisable) {

                            select(substituteRadioButton)
                            fileMode = SUBSTITUTE_MODE

                        }
                    }
                }

                borderpane {

                    left = hbox(BOX_SPACING) {

                        addClass(Styles.hbox)
                        parseButton = button("Parse Text") {

                            isFocusTraversable = false
                            addClass(Styles.boldButton)
                            tooltip("Parse the text to generate the Staccato string | Shift+Enter")
                            shortcut("Shift+Enter")
                            action {

                                outputTextArea.text = controller.buildText(inputTextArea.text)
                                expand(outputItem)

                            }
                        }
                    }

                    right = hbox(BOX_SPACING) {

                        addClass(Styles.hbox)
                        appendRadioButton = radiobutton("Append", fileRadioGroup) {

                            isFocusTraversable = false
                            addClass(Styles.boldButton)
                            tooltip("Select this to append the text from the file to the text in the box")
                            action { fileMode = APPEND_MODE }

                        }

                        substituteRadioButton = radiobutton("Substitute", fileRadioGroup) {

                            isFocusTraversable = false
                            addClass(Styles.boldButton)
                            tooltip("Select this to substitute the text in the box with the text from the file")
                            action { fileMode = SUBSTITUTE_MODE }

                        }

                        button("Choose File...") {

                            isFocusTraversable = false
                            addClass(Styles.boldButton)
                            tooltip("Choose a text file to substitute or append | Ctrl+Shift+C")
                            shortcut("Ctrl+Shift+C")
                            action {

                                textFromFile = controller.readFile()
                                if (isNotEmpty(textFromFile)) {

                                    when(fileMode) {

                                        APPEND_MODE -> inputTextArea.text += textFromFile
                                        SUBSTITUTE_MODE -> inputTextArea.text = textFromFile

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        outputItem = item("Output") {

            vbox(BOX_SPACING) {

                vboxConstraints {

                    marginBottom = VBOX_VERTICAL_MARGIN
                    marginLeft = VBOX_HORIZONTAL_MARGIN
                    marginRight = VBOX_HORIZONTAL_MARGIN
                    marginTop = VBOX_VERTICAL_MARGIN

                }

                borderpane {

                    left = label(" Generated String", font.create(FontAwesome.Glyph.BELL)) { addClass(Styles.label) }
                    right = label(playerInformationText) { addClass(Styles.informationLabel) }

                }

                outputTextArea = textarea {

                    isWrapText = true
                    isFocusTraversable = false
                    addClass(Styles.textArea)
                    useMaxHeight = true
                    vboxConstraints { vGrow = Priority.ALWAYS }
                    isEditable = false

                    // listener para desativar os botões de tocar e exportar se não houver texto de output
                    textProperty().addListener { _, _, _ ->

                        if (isNotEmpty(outputTextArea.text)) {

                            enable(playButton)
                            enable(midiButton)

                        } else {

                            disable(playButton)
                            disable(midiButton)

                        }
                    }
                }

                borderpane {

                    left = hbox(BOX_SPACING) {

                        addClass(Styles.hbox)
                        playButton = button(" P", font.create(FontAwesome.Glyph.PLAY)) {

                            isFocusTraversable = false
                            addClass(Styles.boldButton)
                            tooltip("Play the song generated by the text | Shift+Ctrl+P")
                            shortcut("Shift+Ctrl+P")
                            action {

                                playerInformationText.value = "playing"
                                disable(playButton)
                                enable(pauseButton)
                                enable(stopButton)
                                runAsync {

                                    controller.playSong(outputTextArea.text, player)

                                } ui {

                                    playerInformationText.value = EMPTY_STRING
                                    enable(playButton)
                                    disable(pauseButton)
                                    disable(resumeButton)
                                    disable(stopButton)

                                }
                            }
                        }

                        pauseButton = button(" P", font.create(FontAwesome.Glyph.PAUSE)) {

                            isFocusTraversable = false
                            addClass(Styles.boldButton)
                            tooltip("Pause the song that is currently playing | Shift+P")
                            shortcut("Shift+P")
                            action {

                                disable(pauseButton)
                                enable(resumeButton)
                                playerInformationText.value = "paused"
                                controller.pauseSong(managedPlayer)

                            }
                        }

                        resumeButton = button(" R", font.create(FontAwesome.Glyph.SHARE)) {

                            isFocusTraversable = false
                            addClass(Styles.boldButton)
                            tooltip("Resume the song that is currently paused | Shift+R")
                            shortcut("Shift+R")
                            action {

                                disable(resumeButton)
                                enable(pauseButton)
                                playerInformationText.value = "playing"
                                controller.resumeSong(managedPlayer)

                            }
                        }

                        stopButton = button(" S", font.create(FontAwesome.Glyph.STOP)) {

                            isFocusTraversable = false
                            addClass(Styles.boldButton)
                            tooltip("Stop the song that is currently playing/paused | Shift+S")
                            shortcut("Shift+S")
                            action {

                                controller.stopSong(managedPlayer)
                                playerInformationText.value = EMPTY_STRING

                            }
                        }
                    }

                    right = hbox {

                        addClass(Styles.hbox)
                        midiText = textfield {
                            isFocusTraversable = false
                            promptText = "filename"
                        }

                        midiButton = button("Export MIDI...") {

                            isFocusTraversable = false
                            tooltip("Export the song to a MIDI file | Shift+M")
                            shortcut("Shift+M")
                            addClass(Styles.boldButton)
                            action {

                                if (isNotEmpty(midiText.text)) {
                                    midiPath = controller.createMIDI(outputTextArea.text, midiText.text)
                                } else {
                                    midiPath = controller.createMIDI(outputTextArea.text, DEFAULT_MIDI_NAME)
                                }
                                playerInformationText.value = "saved .mid file to $midiPath"

                            }
                        }
                    }
                }
            }
        }

        // configuração inicial da interface
        expand(inputItem)
        disable(parseButton)
        disable(appendRadioButton)
        disable(playButton)
        disable(pauseButton)
        disable(resumeButton)
        disable(stopButton)
        disable(midiButton)
        select(substituteRadioButton)
        fileMode = SUBSTITUTE_MODE
    }

    // utilizada para impedir o envio de uma string vazia ou nula para o player
    private fun isNotEmpty(text: String?): Boolean { return (text != null && text != EMPTY_STRING) }

    // funções para desabilitar certos controles da interface
    private fun disable(button: Button) { button.isDisable = true }
    private fun disable(radioButton: RadioButton) { radioButton.isDisable = true }

    // funções para habilitar certos controles da interface
    private fun enable(button: Button) { button.isDisable = false }
    private fun enable(radioButton: RadioButton) { radioButton.isDisable = false }

    // função para expandir um item da gaveta
    private fun expand(drawerItem: DrawerItem) { drawerItem.expanded = true }

    // função para selecionar um radio button
    private fun select(radioButton: RadioButton) { radioButton.isSelected = true }

}