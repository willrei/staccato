package generator.controller

private const val FIRST_INSTRUMENT = 0
private const val LAST_INSTRUMENT = 127
private const val AGOGO = 114
private const val HARPSICHORD = 7
private const val TUBULAR_BELLS = 15
private const val PAN_FLUTE = 76
private const val CHURCH_ORGAN = 20
private const val EXCLAMATION_POINT = "!"
private const val OTHER_VOGALS = "oOiIuU"
private const val DIGITS = "0123456789"
private const val NEWLINE = "\n"
private const val SEMICOLON = ";"
private const val COLON = ","

open class InstrumentController {

    private val pass: Unit = Unit
    private var currentInstrument = FIRST_INSTRUMENT

    fun getInstrument(): String { return "I$currentInstrument" }

    fun changeInstrument(selector: String) {

        when {
            selector == EXCLAMATION_POINT ->  currentInstrument = AGOGO
            OTHER_VOGALS.contains(selector) ->  currentInstrument = HARPSICHORD
            DIGITS.contains(selector) ->  currentInstrument += selector.toInt() % (LAST_INSTRUMENT + 1)
            selector == NEWLINE -> currentInstrument = TUBULAR_BELLS
            selector == SEMICOLON -> currentInstrument = PAN_FLUTE
            selector == COLON -> currentInstrument = CHURCH_ORGAN
            else -> pass
        }
    }

    fun resetInstrument() { currentInstrument = FIRST_INSTRUMENT }

}