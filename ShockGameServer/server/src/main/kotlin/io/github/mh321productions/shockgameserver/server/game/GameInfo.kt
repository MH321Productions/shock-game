package io.github.mh321productions.shockgameserver.server.game

data class GameInfo(
    val questions: List<String>,
    val answers: List<String>,
    val players: MutableList<Player>,
    val numberOfRounds: Int
) {
    fun validate() {
        val neededCardsToStart = players.count() * 10
        val neededCardsPerRound = players.count() * parseQuestions().maxOf { it.second } //Note: Fairly conservative estimation

        val msg = when {
            questions.isEmpty() -> "No questions were given."
            answers.isEmpty() -> "No answers were given."
            players.isEmpty() -> "No players were given."
            numberOfRounds < 1 -> "There must be at least 1 round."
            neededCardsToStart >= answers.count() -> "Not enough cards for the number of players."
            neededCardsToStart + (neededCardsPerRound * numberOfRounds) >= answers.count() -> "Not enough cards for all rounds."
            else -> ""
        }

        if (msg.isNotEmpty()) throw IllegalArgumentException(msg)
    }

    private fun parseQuestions() = questions
        .map {
            Pair(it, it
                .split("{x}")
                .dropLastWhile(String::isEmpty)
                .toTypedArray()
                .size - 1
            )
        }
}