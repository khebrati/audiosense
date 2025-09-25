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
        loss < 20 -> "Normal"
        20 <= loss && loss < 40 -> "Mild Loss"
        40 <= loss && loss < 70 -> "Moderate Loss"
        70 <= loss && loss < 90 -> "Severe Loss"
        else -> "Profound Loss"
    }

fun getLossLevel(leftAC: Map<Int, Int>, rightAC: Map<Int, Int>): Int {
    val leftLevel = getLossLevel(leftAC)
    val rightLevel = getLossLevel(rightAC)
    return (leftLevel + rightLevel) / 2
}

fun getLossLevel(ac: Map<Int, Int>) = ac.values.average().toInt()
