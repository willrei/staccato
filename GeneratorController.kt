package generator.controller

import javafx.stage.FileChooser
import org.jfugue.midi.MidiFileManager
import org.jfugue.pattern.Pattern
import org.jfugue.player.ManagedPlayer
import tornadofx.*
import org.jfugue.player.Player
import java.io.File
import javax.sound.midi.Sequence
import javax.sound.midi.Sequencer

open class GeneratorController: Controller() {

    private val builder = StaccatoBuilder()

    fun buildText(text: String): String { return builder.getParsedText(text) }

    fun playSong(input: String, player: Player) { player.play(input) }

    fun pauseSong(managedPlayer: ManagedPlayer) { managedPlayer.pause() }

    fun resumeSong(managedPlayer: ManagedPlayer) { managedPlayer.resume() }

    fun stopSong(managedPlayer: ManagedPlayer) { managedPlayer.finish() }

    fun readFile(): String? {

        val fileList = chooseFile("Select File", arrayOf(FileChooser.ExtensionFilter("Text Files", "*.txt")))

        if (fileList.isNotEmpty()) { return fileList.get(0).readText() }
        else { return "" }

    }

    fun createMIDI(text: String, name: String): String {

        val pattern = Pattern(text)

        val dir = chooseDirectory("Select Directory")
        MidiFileManager.savePatternToMidi(pattern, File("$dir/$name"))
        return dir.toString()

    }

}


