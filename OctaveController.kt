package generator.controller

private const val DEFAULT_OCTAVE = 4
private const val MAX_OCTAVE = 10
// as notas La e Si não possuem a oitava máxima, então é necessário criar um controle
private const val HIGHER_NOTES = "AB"

open class OctaveController {

    private var currentOctave = DEFAULT_OCTAVE

    fun getOctave(note: String): String {
        return if (HIGHER_NOTES.contains(note) && currentOctave == MAX_OCTAVE) { (currentOctave - 1).toString() }
        else { currentOctave.toString() }
    }

    fun changeOctave() {
        if (currentOctave == MAX_OCTAVE) { currentOctave = DEFAULT_OCTAVE }
        else { currentOctave++ }
    }

    fun resetOctave() { currentOctave = DEFAULT_OCTAVE }

}