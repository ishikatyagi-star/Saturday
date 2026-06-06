# StudySnap — iQOO Hackathon 2026 Build Plan
> Campus OS Track | Gurugram | 8-Hour Sprint | INR 1.8L Prize Pool

---

## 🧠 Product Vision

**StudySnap** is a phone-first AI study companion that transforms raw student inputs — screenshots, PDFs, lecture notes, WhatsApp forwards — into a complete, personalized learning loop.

> *"From chaos to comic to quiz to clarity — in one app."*

The core thesis: students don't lack content. They lack **structured, engaging, personalized processing** of content they already have. StudySnap closes that gap entirely on-device, with AI doing the heavy lifting.

---

## 🎯 The Problem

| Pain Point | Current Reality | StudySnap Fix |
|---|---|---|
| Notes are unstructured | Screenshots rot in gallery | Auto-extracted, structured, stored |
| Studying is passive | Re-reading doesn't work | Comic stories + quizzes force active recall |
| Weak areas are invisible | Student guesses what to revise | AI tracks performance, targets gaps |
| Flashcards are boring | Anki feels like homework | Tinder-swipe UX, dopamine loop |
| Doubts need a tutor | Tutors cost money, aren't 24/7 | Context-aware chatbot grounded in YOUR notes |

---

## 📱 Core Features

### Feature 1 — Notes to Comic Stories
**The Instagram Stories learning experience**

**Input sources:**
- Camera capture (lecture whiteboard, textbook page)
- Screenshot share (WhatsApp forwards, PDF screenshots)
- Manual text paste
- PDF upload

**Processing pipeline:**
1. ML Kit OCR → raw text extraction from image/screenshot
2. On-device LLM (Gemma 3 via MediaPipe) → concept extraction + chunking
3. LLM structures content into: `{ concept_title, summary, key_points[], analogy }`
4. Gemini API (Flash) → generates comic-style panel illustrations per concept
5. Rendered as Instagram Stories UI

**UI Pattern:**
- Home screen: Subject bubbles (like Instagram profile circles)
- Tap subject → horizontal swipeable story cards
- Each card = one concept, rendered as 2–3 panel comic strip
- Progress ring around subject bubble (% concepts covered)

**Subjects auto-detected** from note content (Physics, Chemistry, DSA, Economics, etc.)

---

### Feature 2 — Quiz Generator
**Test yourself before the exam tests you**

**Processing pipeline:**
1. Uses same structured concept data from Feature 1
2. On-device LLM generates questions per concept:
   - MCQ (4 options)
   - True/False
   - Fill in the blank
3. Student attempts quiz → results stored locally
4. Performance map updated: `{ concept_id, attempts, correct_rate, last_attempted }`

**UI Pattern:**
- "Quiz Me" button on each subject or concept story
- Card-flip reveal for answers
- End screen: Score + breakdown by concept
- Weak area badge shown on subject bubble

---

### Feature 3 — Smart Flashcards (Tinder-Style)
**Spaced repetition with dopamine**

**Processing pipeline:**
1. On-device LLM generates flashcard pairs from notes: `{ front: question, back: answer }`
2. Weak areas from Quiz (Feature 2) are **prioritized** — shown more frequently
3. Student swipes:
   - **Swipe Right** → "Got it" — deprioritized, shown less often
   - **Swipe Left** → "Review Later" — re-queued, shown again sooner
4. Spaced repetition algorithm adjusts card frequency based on swipe history

**UI Pattern:**
- Tinder-style card stack
- Front: question or concept term
- Tap to flip → answer/explanation
- Swipe gesture with haptic feedback
- Daily streak counter ("🔥 5 day streak")

---

### Feature 4 — Doubt Chatbot
**A tutor that actually knows your notes**

**Processing pipeline:**
1. On-device LLM (Gemma 3) loaded with:
   - Student's extracted notes as context
   - Quiz performance history (weak/strong areas)
   - Flashcard swipe history
2. Student asks natural language question
3. LLM answers **grounded in the student's own notes** — not generic internet knowledge
4. If question is outside notes scope → LLM flags it clearly

**Context it knows:**
- "You got Newton's 3rd Law wrong twice — want me to explain it differently?"
- "Based on your notes on Chapter 4..."
- "You marked this flashcard as 'Review Later' — here's a simpler breakdown"

**UI Pattern:**
- Chat bubble interface (bottom of screen)
- Persistent floating button across all screens
- Conversation history per subject
- Suggested questions based on weak areas

---

## 🏗️ Technical Architecture

### Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI Framework | Jetpack Compose |
| OCR | ML Kit Text Recognition v2 (on-device) |
| On-device LLM | Gemma 3 (1B or 2B) via MediaPipe LLM Inference API |
| Image Generation | Gemini Flash API (gemini-2.0-flash) |
| Local Storage | Room Database |
| Image Loading | Coil 3 |
| Networking | Retrofit + OkHttp (for Gemini API calls only) |
| Dependency Injection | Hilt |
| Navigation | Compose Navigation |

---

### LLM Strategy — On-Device vs API

```
┌─────────────────────────────────────────────────────┐
│                   USER INPUT                        │
│         (screenshot / text / PDF)                   │
└────────────────────┬────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────┐
│          ML Kit OCR  (ON-DEVICE)                    │
│          Raw text extraction                        │
└────────────────────┬────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────┐
│       Gemma 3 via MediaPipe  (ON-DEVICE)            │
│                                                     │
│  • Concept extraction & chunking                    │
│  • Quiz question generation                         │
│  • Flashcard pair generation                        │
│  • Doubt chatbot responses                          │
│  • Structured JSON output for all features          │
└────────────────────┬────────────────────────────────┘
                     │
          ┌──────────┴──────────┐
          │                     │
          ▼                     ▼
┌──────────────────┐  ┌─────────────────────────────┐
│  LOCAL STORAGE   │  │   Gemini Flash API (CLOUD)  │
│  Room Database   │  │                             │
│                  │  │  • Comic panel illustration │
│  • Notes         │  │  • Subject cover art        │
│  • Concepts      │  │  • Story card backgrounds   │
│  • Quiz results  │  │                             │
│  • Flashcards    │  │  Input: concept summary     │
│  • Chat history  │  │  Output: comic-style image  │
└──────────────────┘  └─────────────────────────────┘
```

---

### Gemma 3 Prompt Templates

**Concept Extraction Prompt:**
```
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
{{RAW_TEXT}}
```

**Quiz Generation Prompt:**
```
Generate quiz questions for this concept. Return ONLY valid JSON.

Concept: {{CONCEPT_TITLE}}
Summary: {{CONCEPT_SUMMARY}}

Format:
{
  "questions": [
    {
      "type": "mcq",
      "question": "question text",
      "options": ["A", "B", "C", "D"],
      "correct": 0,
      "explanation": "why this is correct"
    }
  ]
}

Generate 3 questions: 2 MCQ, 1 true/false.
```

**Flashcard Generation Prompt:**
```
Generate flashcards for spaced repetition. Return ONLY valid JSON.

Concept: {{CONCEPT_TITLE}}
Key Points: {{KEY_POINTS}}

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
```

**Chatbot System Prompt:**
```
You are StudySnap AI, a helpful study assistant for a student.
You have access to the student's notes below. Answer questions based ONLY on these notes.
If a question is outside the notes, say so clearly and answer from general knowledge.

Student's weak areas: {{WEAK_AREAS}}
Student's strong areas: {{STRONG_AREAS}}

Student Notes Context:
{{NOTES_CONTEXT}}

Be concise, friendly, use analogies. Max 3 paragraphs per answer.
```

---

### Gemini Flash API — Comic Image Generation

**Endpoint:** `POST https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-preview-image-generation:generateContent`

**Prompt template for each comic panel:**
```
Create a simple educational comic panel illustration.
Style: Clean, colorful, manga-inspired, suitable for students.
No text in the image.
Scene: {{COMIC_PROMPT_FROM_LLM}}
Subject: {{CONCEPT_TITLE}}
Keep it simple, clear, visually engaging.
```

**Caching strategy:** Generated images stored in Room DB as Base64 blobs. Never regenerate for same concept.

---

### Data Models (Room Database)

```kotlin
// Subject
@Entity
data class Subject(
    @PrimaryKey val id: String,
    val name: String,
    val detectedFrom: String, // "ocr" | "manual"
    val coverImageBase64: String?,
    val createdAt: Long
)

// Concept
@Entity
data class Concept(
    @PrimaryKey val id: String,
    val subjectId: String,
    val title: String,
    val summary: String,
    val keyPoints: String,       // JSON array string
    val analogy: String,
    val comicImageBase64: String?,
    val createdAt: Long
)

// Quiz Result
@Entity
data class QuizResult(
    @PrimaryKey val id: String,
    val conceptId: String,
    val subjectId: String,
    val score: Int,
    val totalQuestions: Int,
    val attemptedAt: Long
)

// Flashcard
@Entity
data class Flashcard(
    @PrimaryKey val id: String,
    val conceptId: String,
    val front: String,
    val back: String,
    val difficulty: String,
    val swipeHistory: String,    // JSON array: ["right","left","right"]
    val nextReviewAt: Long,      // spaced repetition timestamp
    val masteryScore: Float      // 0.0 to 1.0
)

// Chat Message
@Entity
data class ChatMessage(
    @PrimaryKey val id: String,
    val subjectId: String?,
    val role: String,            // "user" | "assistant"
    val content: String,
    val timestamp: Long
)
```

---

## 🎨 UI Architecture — Screen Map

```
MainActivity
│
├── HomeScreen
│   ├── Subject bubbles (Instagram story circles)
│   ├── "+ Add Notes" FAB
│   └── Chatbot floating button
│
├── AddNotesScreen
│   ├── Camera capture
│   ├── Gallery pick
│   └── Text paste
│       └── ProcessingScreen (OCR → LLM → done)
│
├── SubjectStoriesScreen (subject selected)
│   ├── Story viewer (horizontal swipe)
│   │   ├── Comic panel card
│   │   ├── Key points overlay
│   │   └── "Quiz this concept" button
│   └── "Quiz all" button
│
├── QuizScreen
│   ├── Question card
│   ├── Options / input
│   ├── Reveal answer + explanation
│   └── Results screen (score + weak areas)
│
├── FlashcardsScreen
│   ├── Swipeable card stack
│   ├── Flip animation (front → back)
│   ├── Swipe right (got it) / left (review later)
│   └── Session complete screen
│
└── ChatbotScreen
    ├── Conversation history
    ├── Suggested questions chips
    └── Text input
```

---

## ⏱️ 8-Hour Build Timeline

### Hour 1 — Foundation (8:00–9:00)
- [ ] Project setup: Kotlin + Compose + Hilt + Room + Retrofit
- [ ] Add dependencies: ML Kit, MediaPipe, Coil, Gemini API client
- [ ] Download Gemma 3 1B model file (do this first — it takes time)
- [ ] Room DB schema + DAOs for all 5 entities
- [ ] Basic navigation graph

### Hour 2 — OCR + LLM Pipeline (9:00–10:00)
- [ ] ML Kit OCR integration (camera + gallery input)
- [ ] MediaPipe LLM Inference API setup with Gemma 3
- [ ] Concept extraction prompt → parse JSON response
- [ ] Store extracted Subject + Concepts in Room DB
- [ ] ProcessingScreen with progress indicator

### Hour 3 — Comic Stories UI (10:00–11:00)
- [ ] Home screen: Subject bubble row (Compose LazyRow)
- [ ] Progress ring around each bubble
- [ ] Story viewer screen: horizontal pager (Compose HorizontalPager)
- [ ] Comic card layout: image top, key points bottom
- [ ] Gemini Flash API call for image generation per concept
- [ ] Image caching in Room DB

### Hour 4 — Quiz Feature (11:00–12:00)
- [ ] Quiz generation prompt → parse MCQ/TF JSON
- [ ] QuizScreen: question card + options
- [ ] Answer reveal with explanation
- [ ] Score tracking → QuizResult stored in Room
- [ ] Weak/strong area computation from results

### Hour 5 — Flashcards Feature (12:00–13:00)
- [ ] Flashcard generation prompt → parse JSON
- [ ] Tinder-style swipe UI (Compose drag gesture)
- [ ] Card flip animation (front → back)
- [ ] Swipe right/left logic + haptic feedback
- [ ] Spaced repetition: nextReviewAt calculation
- [ ] Prioritize weak-area cards in deck order

### Hour 6 — Chatbot Feature (13:00–14:00)
- [ ] ChatbotScreen: conversation UI
- [ ] System prompt assembly (notes context + weak areas)
- [ ] On-device Gemma 3 inference for chat responses
- [ ] Suggested questions chips based on weak areas
- [ ] Floating chatbot button on all screens
- [ ] Conversation history persistence

### Hour 7 — Polish + Demo Loop (14:00–15:00)
- [ ] Fix crashes and edge cases
- [ ] Loading states for all async operations
- [ ] Empty states (no notes yet, etc.)
- [ ] Home screen widget (today's flashcard count)
- [ ] Demo data pre-loaded (one subject, 3 concepts, ready to show)
- [ ] App icon + name

### Hour 8 — Demo Prep + Submission (15:00–16:00)
- [ ] Record demo video (the 3-minute loop)
- [ ] Prepare live demo flow (exact steps, no surprises)
- [ ] APK build + install on demo device
- [ ] Pitch one-pager in Google Docs / Slides

---

## 🎬 Demo Loop (The 3-Minute Magic Moment)

> This is what you show judges. Practice it 5 times.

**Step 1 — Input (30 sec)**
> "Here's a WhatsApp screenshot of Newton's Laws from my professor."
- Open StudySnap → tap "+" → pick screenshot from gallery
- Show processing animation: "Reading notes... Understanding concepts... Generating stories..."

**Step 2 — Comic Stories (45 sec)**
> "StudySnap turned that into a visual story — like Instagram, but for learning."
- Show home screen: "Physics" bubble appears with progress ring
- Tap it → swipe through 3 comic story cards
- Each card: AI-generated illustration + concept explained simply

**Step 3 — Quiz (30 sec)**
> "Now it quizzes me on what I just learned."
- Tap "Quiz Me" → answer 3 questions
- Show result: "You got Newton's 2nd Law wrong — that's a weak area."

**Step 4 — Flashcards (30 sec)**
> "It automatically creates flashcards, prioritizing my weak spots."
- Go to Flashcards → show weak area card first
- Swipe right on one ("got it") → swipe left on another ("review later")
- Show haptic response + animation

**Step 5 — Chatbot (30 sec)**
> "And I can ask doubts — it answers from MY notes, not generic internet."
- Open chatbot → suggested question: "Explain Newton's 2nd Law differently"
- Show response grounded in the student's own notes
- Ask follow-up: "Why did I get that question wrong?"

**Step 6 — Punchline (15 sec)**
> "One screenshot. Five minutes. Physics class: conquered."

---

## 🏆 Judging Rubric Alignment

| Criterion | Weight | How StudySnap Wins |
|---|---|---|
| **Phone-First** | 25% | Share sheet input, camera-first, Instagram Stories UI, Tinder swipe, floating chatbot — 100% designed for thumb, not mouse |
| **Office Kit** | 25% | Notes sync to Google Docs, Quiz results → Google Sheets tracker, Weak areas summary exportable |
| **AI-Native** | 20% | OCR + on-device LLM + image generation API = AI is not a feature, it IS the product |
| **Problem Fit** | 20% | Every student in every college has this exact problem every single day |
| **Craft** | 10% | Instagram Stories UX, Tinder swipe, comic art, streak counter — judges will feel the polish |

### Office Kit Integration Points
- **Google Docs:** Export full structured notes from any subject as a formatted Doc
- **Google Sheets:** Weekly quiz performance tracker (concept vs. score over time)
- **Google Drive:** Backup all generated comics and notes

---

## 📦 Dependencies (build.gradle)

```kotlin
// Jetpack Compose
implementation(platform("androidx.compose:compose-bom:2024.12.01"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.foundation:foundation")

// Navigation
implementation("androidx.navigation:navigation-compose:2.8.5")

// Hilt DI
implementation("com.google.dagger:hilt-android:2.54")
kapt("com.google.dagger:hilt-compiler:2.54")
implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

// Room Database
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
kapt("androidx.room:room-compiler:2.6.1")

// ML Kit OCR
implementation("com.google.mlkit:text-recognition:16.0.1")

// MediaPipe LLM (Gemma on-device)
implementation("com.google.mediapipe:tasks-genai:0.10.22")

// Gemini API (image generation)
implementation("com.google.ai.client.generativeai:generativeai:0.9.0")

// Networking
implementation("com.squareup.retrofit2:retrofit:2.11.0")
implementation("com.squareup.retrofit2:converter-gson:2.11.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

// Image Loading
implementation("io.coil-kt.coil3:coil-compose:3.0.4")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

// DataStore (for settings/API key storage)
implementation("androidx.datastore:datastore-preferences:1.1.1")
```

---

## 🔑 API Keys & Config

```kotlin
// local.properties (never commit to git)
GEMINI_API_KEY=your_gemini_api_key_here

// BuildConfig access
object AppConfig {
    const val GEMINI_API_KEY = BuildConfig.GEMINI_API_KEY
    const val GEMMA_MODEL_PATH = "gemma3-1b-it-int4.task" // in assets/
    const val MAX_TOKENS = 1024
    const val GEMINI_IMAGE_MODEL = "gemini-2.0-flash-preview-image-generation"
}
```

---

## 🚨 Risk Register & Mitigations

| Risk | Likelihood | Mitigation |
|---|---|---|
| Gemma 3 model download slow | High | Download model in Hour 1 immediately |
| LLM JSON parsing fails | Medium | Wrap in try-catch, retry with stricter prompt, fallback to regex extraction |
| Gemini image API rate limit | Low | Cache all images in Room, never regenerate same concept |
| Tinder swipe gesture jank | Medium | Use existing Compose swipe-to-dismiss modifier, don't build from scratch |
| OCR fails on handwritten text | Medium | Show manual text input fallback always |
| Out of time | High | Features 1+2 are the demo. Features 3+4 are bonus. Ship in order. |

---

## ✅ Minimum Viable Demo (If Time Runs Out)

If at Hour 6 you're behind, ship this and only this:

1. **Screenshot → OCR → Concept extraction** (Feature 1 pipeline)
2. **Instagram Stories UI** with comic panels (even with placeholder images)
3. **Quiz** for one subject (Feature 2)

Features 3 (Flashcards) and 4 (Chatbot) can be shown as mockups/prototypes in the pitch deck if not built. The **core magic is Feature 1** — make that perfect.

---

## 💬 One-Line Pitches

- **Product:** *"Turn any screenshot into a comic story, quiz, and flashcard — before your next class."*
- **For judges:** *"StudySnap is the student Jarvis that learns from your notes, not the internet."*
- **For demo opener:** *"One photo. Your entire chapter — understood, tested, and memorized."*

---

## 🗂️ Folder Structure

```
StudySnap/
├── app/
│   ├── src/main/
│   │   ├── java/com/studysnap/
│   │   │   ├── data/
│   │   │   │   ├── db/          # Room DB, DAOs, entities
│   │   │   │   ├── repository/  # SubjectRepo, QuizRepo, etc.
│   │   │   │   └── api/         # Gemini API client
│   │   │   ├── domain/
│   │   │   │   ├── model/       # Domain models
│   │   │   │   └── usecase/     # ExtractConceptsUseCase, etc.
│   │   │   ├── ui/
│   │   │   │   ├── home/        # HomeScreen, SubjectBubble
│   │   │   │   ├── stories/     # StoryViewer, ComicCard
│   │   │   │   ├── quiz/        # QuizScreen, ResultScreen
│   │   │   │   ├── flashcards/  # FlashcardScreen, SwipeCard
│   │   │   │   ├── chat/        # ChatbotScreen, MessageBubble
│   │   │   │   └── theme/       # Colors, Typography, Theme
│   │   │   ├── ml/
│   │   │   │   ├── OcrManager.kt
│   │   │   │   └── LlmManager.kt
│   │   │   └── di/              # Hilt modules
│   │   └── assets/
│   │       └── gemma3-1b-it-int4.task
│   └── build.gradle.kts
├── local.properties             # API keys (gitignored)
└── STUDYSNAP_BUILD_PLAN.md     # This file
```

---

*Built for iQOO Hackathon 2026 | Campus OS Track | StudySnap Team*
*Last updated: Day of hackathon — execute relentlessly.*
