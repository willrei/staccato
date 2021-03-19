package generator.controller

private const val MAX_VOLUME = 16383
// volume default foi escolhido para ser uma potência de 2 porque o máximo "também é"
private const val DEFAULT_VOLUME = 4096
private const val JFUGUE_VOLUME_INDEX = 935

open class VolumeController {

    private var currentVolume = DEFAULT_VOLUME

    fun getVolume(): String {
        return ":CON($JFUGUE_VOLUME_INDEX, ${currentVolume - 1})"
    }

    fun changeVolume() {
        if (currentVolume > MAX_VOLUME) { currentVolume = DEFAULT_VOLUME }
        else { currentVolume *= 2 }
    }

    fun resetVolume() { currentVolume = DEFAULT_VOLUME }

}




