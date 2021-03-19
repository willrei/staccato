package generator.controller

private const val EMPTY_STRING = ""
private const val WHITESPACE = " "
private const val WHITESPACE_CHAR = ' '
private const val QUESTION_MARK = "?"
private const val DOT = "."
private const val REST = "R"
private const val NOTES = "ABCDEFG"
private const val NOTE_REPEATERS = "abcdefgHJKLMNPQRSTVWXYZhjklmnpqrstvwxyz"
private const val INSTRUMENT_CHANGERS = "!ouiOUI0123456789\n;,"

class StaccatoBuilder {

    private lateinit var staccato: String
    private val pass: Unit = Unit
    private val instrument = InstrumentController()
    private val volume = VolumeController()
    private val octave = OctaveController()
    private val note = NoteController()

    fun getParsedText(text: String): String {

        staccato = EMPTY_STRING

        // para resetar os valores caso o parse seja feito novamente
        instrument.resetInstrument()
        addToStaccato(instrument.getInstrument())
        volume.resetVolume()
        addToStaccato(volume.getVolume())
        octave.resetOctave()

        val textLength = text.length
        val firstTextChar = text[0].toString()

        // é necessário verificar o que ocorre com o primeiro caractere do texto
        when {

            // testa se é uma nota
            NOTES.contains(firstTextChar) -> {
                note.changeNote(firstTextChar)
                addToStaccato(note.getNote() + octave.getOctave(note.getNote()))
            }

            // testa se é uma mudança de volume
            firstTextChar == WHITESPACE -> {
                volume.changeVolume()
                addToStaccato(volume.getVolume())
            }

            // testa se é uma mudança de instrumento
            INSTRUMENT_CHANGERS.contains(firstTextChar) -> {
                instrument.changeInstrument(firstTextChar)
                addToStaccato(instrument.getInstrument())
            }

            // testa se é uma mudança de oitava
            firstTextChar == QUESTION_MARK -> {
                when {
                    textLength == 1 -> { pass }
                    text[1].toString() == DOT -> { octave.changeOctave() }
                    else -> { pass }
                }
            }

            else -> pass
        }

        if (textLength == 1) { return staccato.trim(WHITESPACE_CHAR) }

        val maxIndex = textLength - 1
        for (index in 1 until textLength) {

            val currentChar = text[index].toString()
            when {

                NOTES.contains(currentChar) -> {
                    note.changeNote(currentChar)
                    addToStaccato(note.getNote() + octave.getOctave(note.getNote()))
                }

                // testa se ocorreu uma nota seguida de um repetidor de nota
                NOTE_REPEATERS.contains(currentChar) -> {
                    if (NOTES.contains(text[index - 1])) {
                        addToStaccato(note.getNote() + octave.getOctave(note.getNote()))
                    } else { addToStaccato(REST) }
                }

                currentChar == WHITESPACE -> {
                    volume.changeVolume()
                    addToStaccato(volume.getVolume())
                }

                INSTRUMENT_CHANGERS.contains(currentChar) -> {
                    instrument.changeInstrument(currentChar)
                    addToStaccato(instrument.getInstrument())
                }

                currentChar == QUESTION_MARK -> {
                    when {
                        index == maxIndex -> { pass }
                        text[index + 1].toString() == DOT -> { octave.changeOctave() }
                        else -> { pass }
                    }
                }

                // para verificar se já houve uma mudança de oitava, já que não é possível manipular diretamente
                // a variável utilizada no laço para incrementar duas vezes (ela é criada como uma "val", e não dá
                // pra mudar)
                currentChar == DOT -> {
                    when {
                        text[index - 1].toString() == QUESTION_MARK -> { pass }
                        NOTES.contains(text[index - 1]) -> { addToStaccato(note.getNote() + octave.getOctave(note.getNote())) }
                        else -> { addToStaccato(REST) }
                    }
                }

                else -> {
                    if (NOTES.contains(text[index - 1])) { addToStaccato(note.getNote() + octave.getOctave(note.getNote())) }
                    else { addToStaccato(REST) }
                }
            }
        }

        return staccato.trim(WHITESPACE_CHAR)
    }

    private fun addToStaccato(token: String) { staccato += " $token" }

}