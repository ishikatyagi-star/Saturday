package com.studysnap.ml

object PromptTemplates {

    fun conceptExtraction(rawText: String): String = """
You are a study assistant. Extract key concepts from the following student notes.
Return ONLY valid JSON, no other text.

Format:
{
  "subject": "detected subject name",
  "concepts": [
    {
      "id": "unique_id",
      "title": "concept title",
      "summary": "2-3 sentence plain English summary",
      "key_points": ["point 1", "point 2", "point 3"],
      "analogy": "one fun real-world analogy",
      "comic_prompt": "visual scene description for illustration"
    }
  ]
}

Notes:
${rawText.trim()}
""".trimIndent()

    fun quizGeneration(conceptTitle: String, conceptSummary: String): String = """
Generate quiz questions for this concept. Return ONLY valid JSON.

Concept: $conceptTitle
Summary: $conceptSummary

Format:
{
  "questions": [
    {
      "type": "mcq",
      "question": "question text",
      "options": ["A", "B", "C", "D"],
      "correct": 0,
      "explanation": "why this is correct"
    },
    {
      "type": "true_false",
      "question": "statement text",
      "correct": true,
      "explanation": "reason"
    },
    {
      "type": "mcq",
      "question": "question text",
      "options": ["A", "B", "C", "D"],
      "correct": 2,
      "explanation": "why this is correct"
    }
  ]
}

Generate exactly 3 questions: 2 MCQ, 1 true/false.
""".trimIndent()

    fun flashcardGeneration(conceptTitle: String, keyPoints: String): String = """
Generate flashcards for spaced repetition. Return ONLY valid JSON.

Concept: $conceptTitle
Key Points: $keyPoints

Format:
{
  "flashcards": [
    {
      "front": "question or term",
      "back": "answer or definition",
      "difficulty": "easy|medium|hard"
    }
  ]
}

Generate 5 flashcards.
""".trimIndent()

    fun chatbotSystemPrompt(notesContext: String, weakAreas: String, strongAreas: String): String = """
You are StudySnap AI, a helpful study assistant for a student.
You have access to the student's notes below. Answer questions based ONLY on these notes.
If a question is outside the notes, say so clearly and answer from general knowledge.

Student's weak areas: $weakAreas
Student's strong areas: $strongAreas

Student Notes Context:
$notesContext

Be concise, friendly, use analogies. Max 3 paragraphs per answer.
""".trimIndent()
}