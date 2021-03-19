package generator.controller

private const val DEFAULT_NOTE = "C"

open class NoteController {

    private var currentNote = DEFAULT_NOTE

    fun getNote(): String { return currentNote }

    fun changeNote(note: String) { currentNote = note }

}


