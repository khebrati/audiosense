package ir.khebrati.audiosense.domain.useCase.lossLevel


fun describeLossLevel(leftAC: Map<Int, Int>, rightAC: Map<Int, Int>): String {
    val level = getLossLevel(leftAC, rightAC)
    return describeLossLevel(level)
}

fun describeLossLevel(ac: Map<Int, Int>): String {
    val level = getLossLevel(ac)
    return describeLossLevel(level)
}

fun describeLossLevel(loss: Int) =
    when {
        loss <= 25 -> "Normal"
        26 <= loss && loss <= 40 -> "Mild Loss"
        41 <= loss && loss <= 60 -> "Moderate Loss"
        61 <= loss && loss <= 80 -> "Severe Loss"
        else -> "Profound Loss"
    }

fun getLossLevel(leftAC: Map<Int, Int>, rightAC: Map<Int, Int>): Int {
    val leftLevel = getLossLevel(leftAC)
    val rightLevel = getLossLevel(rightAC)
    return (leftLevel + rightLevel) / 2
}

fun getLossLevel(ac: Map<Int, Int>) = ac.values.average().toInt()
