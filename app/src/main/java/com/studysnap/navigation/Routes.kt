package com.studysnap.navigation

object Routes {
    const val HOME = "home"
    const val ADD_NOTES = "add_notes"
    const val PROCESSING = "processing"
    const val SUBJECT_STORIES = "stories/{subjectId}"
    const val QUIZ = "quiz/{conceptId}/{subjectId}"
    const val QUIZ_RESULT = "quiz_result/{conceptId}/{subjectId}/{score}/{total}"
    const val FLASHCARDS = "flashcards/{conceptId}"
    const val CHAT = "chat/{subjectId}"

    fun stories(subjectId: String) = "stories/$subjectId"
    fun quiz(conceptId: String, subjectId: String) = "quiz/$conceptId/$subjectId"
    fun quizResult(conceptId: String, subjectId: String, score: Int, total: Int) =
        "quiz_result/$conceptId/$subjectId/$score/$total"
    fun flashcards(conceptId: String) = "flashcards/$conceptId"
    fun chat(subjectId: String) = "chat/$subjectId"
}