# Saturday Hackathon Brief

## Overview

Saturday is the submitted concept for the **Campus OS** track of the iQOO Hackathon 2026 in Delhi NCR.[cite:1][file:15] The hackathon is an 8-hour, in-person, phone-first AI sprint at COWRKS, DLF Cybercity, Gurugram, with an INR 1,80,000 prize pool and iQOO phones provisioned to participants for the event.[cite:1]

The event format is hybrid: during **Red Light** windows, the build surface is the iQOO phone and laptops are restricted; during **Green Light** windows, both devices are allowed via iQOO Office Kit.[cite:1] The judging weight explicitly favors iQOO Office Kit usage and phone-first execution, which means the demo should feel native to the phone rather than like a laptop app compressed onto mobile.[cite:1]

## Hackathon facts

- Event: **iQOO Hackathon 2026 · Delhi NCR**.[cite:1]
- Date: **Saturday, 6 June 2026**.[cite:1]
- Location: **COWRKS, DLF Cybercity, Gurugram**.[cite:1]
- Format: **8-hour, in-person, phone-first sprint**.[cite:1]
- Participants: about **80 builders**.[cite:1]
- Prize pool: **INR 1,80,000**.[cite:1]
- Team size: **1–3 members**.[cite:1]
- Provisioning: iQOO phones are provided for the duration of the hackathon, along with AI credits and on-floor mentorship.[cite:1]

## Tracks

The event has **three curated problem statements plus one wildcard**.[cite:1]

| Track | Theme | What it focuses on |
|---|---|---|
| AgentKit | Student · Agents | AI agents that run student life, with phone as interface and laptop as muscle.[cite:1] |
| Campus OS | Campus · Mobile | A mobile-first operating system for campus life, designed for the iQOO phone display and daily student workflows.[cite:1] |
| BrandForge | Creator · Social | An autonomous AI social media engine and creator digital twin.[cite:1] |
| Open Innovation | Wildcard | Free-form AI + phone-first build.[cite:1] |

## Chosen idea

The submitted idea is **Saturday**, described as an **agentic Campus OS for students**.[file:15] The core problem framing is that students do not suffer from a lack of apps; they suffer from too many disconnected tools such as WhatsApp for updates, PDFs for notices, Drive for notes, calendars for reminders, Splitwise for hostel money, and separate portals for academics.[file:15]

The submission positions Saturday as a **student's Jarvis** that reads timetable changes, scans notes and notices, transcribes lectures, extracts assignment deadlines from screenshots, generates flashcards, tracks hostel expenses, surfaces campus events, and acts proactively through voice commands and agent behavior.[file:15][cite:21] It also explicitly includes iQOO Office Kit as the bridge for deeper work when the laptop is needed.[file:15]

## Core product thesis

The strongest product thesis is: **Saturday is not another campus dashboard; it is the execution layer for campus life**.[file:15] Existing student tools mostly show information, while Saturday is meant to understand context, convert messy campus inputs into structured actions, and help students before they ask.[file:15][cite:16]

This framing fits the Campus OS brief well because the track itself emphasizes a unified mobile-first platform that can fold academics, productivity, and campus life into one experience.[cite:1] The most compelling interpretation is therefore not a super-app with many tabs, but a proactive agent system that turns raw campus inputs into tasks, reminders, flashcards, and next actions.[file:15]

## Suggested MVP

The MVP should stay narrow and demo one magical loop well.[cite:16] The best loop is:

1. Student captures a screenshot, notice, note, or image.
2. Saturday extracts the useful information.
3. Saturday converts it into structured outputs such as deadlines, tasks, events, flashcards, or summaries.
4. Saturday surfaces what matters now in a clear **Today** view.
5. Saturday optionally hands work off through Office Kit when deeper desktop work is needed.[cite:1][file:15]

The most important surfaces are:

- **Capture** — input from screenshots, notices, notes, or camera images.
- **Today** — a prioritized dashboard of tasks, deadlines, and events.
- **Ask Saturday** — an assistant layer for command-style interaction and clarifications.
- **Optional bridge layer** — exports or transitions to desktop during Green Light.[file:15][cite:1]

## Judging criteria

The hackathon uses five live judging criteria.[cite:1]

| Criterion | Weight | What it means in practice |
|---|---:|---|
| iQOO Office Kit Usage | 25% | Use the phone-laptop bridge visibly and meaningfully; phone-bridge time is tracked by HackTracker.[cite:1] |
| Phone-First Execution | 25% | The demo must run on the iQOO phone and feel native to the device.[cite:1] |
| AI-Native Build | 20% | AI should be core to the product, not a bolt-on feature.[cite:1] |
| Problem Fit & Real-World Use | 20% | The product should clearly solve the chosen track problem for real Indian students.[cite:1] |
| Craft & Pitch | 10% | Demo clarity, polish, and ability to defend trade-offs matter in the final round.[cite:1] |

## What to optimize for

- **Phone-first feel** should be obvious within seconds of the demo.[cite:1]
- **AI should be central**, not decorative; the product must visibly understand and act on messy campus inputs.[cite:1][file:15]
- **Problem fit** should stay tightly focused on college students, not secondary user groups.[cite:16]
- **One magical flow** is better than many unfinished modules.[cite:16]
- **Office Kit usage** should be present in the workflow because it is directly rewarded in scoring.[cite:1]

## Key product messages

Use these lines consistently across the demo, pitch, and Q&A:

- **Saturday is the student Jarvis.**[cite:21]
- **Students do not need more apps; they need one intelligent layer.**[file:15]
- **The problem is fragmentation, not access.**[file:15]
- **Saturday turns campus context into action.**[file:15]
- **The phone is the control center; the laptop is the continuation point.**[cite:1]
- **This is not a campus dashboard. It is a campus execution layer.**[file:15]

## Winning demo flow

The best demo should show a realistic student moment rather than a menu tour.[cite:16] A strong sequence is:

1. Open the app on the iQOO phone.
2. Import a screenshot or notice image.
3. Show Saturday extracting a deadline, topic, or class-related task.
4. Convert the extracted information into action items or flashcards.
5. Show the **Today** screen prioritizing what matters.
6. Use Office Kit as the continuation bridge for deeper work or document handoff.[cite:1][file:15]

This demonstrates phone-first execution, AI-native behavior, real-world student value, and Office Kit alignment in one narrative.[cite:1]

## Simplified tech stack

The recommended fast stack is intentionally minimal because the build must happen under severe time pressure and the solution is expected to be a native Android app.[file:15]

| Layer | Recommended choice | Why |
|---|---|---|
| Language | Kotlin | Required and native to Android. |
| UI | Jetpack Compose | Fastest way to ship a polished native Android UI.[cite:40] |
| OCR | ML Kit Text Recognition v2 | Runs on-device and supports Devanagari and Latin scripts, which fits Hindi-English campus notices and screenshots.[cite:44] |
| On-device LLM | Llamatik / llama.cpp-based Kotlin integration | Supports local GGUF model inference on Android and aligns with the requirement that LLMs run on-phone.[cite:50] |
| Async | Coroutines | Lightweight and standard for Kotlin Android apps. |
| Storage | Local file storage, with Room only if necessary | Keeps the build simpler and faster than adding backend infrastructure.[cite:59] |

The best principle is to avoid unnecessary complexity such as backend APIs, auth, external databases, Firebase, or heavyweight architecture unless the core demo already works.[cite:16]

## Things to remember while building

- Build **one core workflow** first and polish it.[cite:16]
- Avoid making the app a broad module gallery; prioritize one end-to-end student action loop.[file:15]
- Use realistic student inputs such as screenshots, notices, or note images, because messy input makes the AI value obvious.[file:15]
- Keep latency low and outputs structured so the demo feels dependable.
- Treat the phone as the main surface and Office Kit as a continuation layer, not the reverse.[cite:1]
- If something must be cut, cut breadth before depth.[cite:16]

## Deck and pitch framing

The strongest one-line positioning is: **Saturday is a mobile-first agentic Campus OS that acts before students ask.**[cite:21][file:15]

Recommended slide narrative:

1. Problem — campus life is fragmented across apps and information streams.[file:15]
2. Insight — students need one intelligent layer, not more apps.[file:15]
3. Solution — Saturday as a proactive student agent.[file:15]
4. Differentiation — other apps inform; Saturday acts.[file:15]
5. Experience — real moments across the student day.
6. Architecture — raw campus input to structured actions.
7. iQOO fit — phone-first build plus Office Kit continuity.[cite:1]
8. Team — founders who already ship automation and AI systems.[cite:22]

The best visual tone for the deck is dark, high-contrast, startup-like, with strong mobile UI mockups and minimal text per slide.

## Q&A preparation

Likely judge questions and ideal answer direction:

| Question | Best answer direction |
|---|---|
| Why is this better than a campus super-app? | It is not just aggregation; it actively converts context into action.[file:15] |
| Why is AI necessary here? | Student inputs are unstructured; AI is needed to interpret screenshots, notices, notes, and lectures into usable actions.[file:15] |
| Why phone-first? | Students already make most campus decisions from their phones, especially quick daily actions.[cite:1] |
| Why does Office Kit matter? | The phone is best for capture and quick action; Office Kit helps with deeper work without breaking continuity.[cite:1] |
| What is the first use case? | Deadline extraction and action planning from screenshots/notices, because it is common, painful, and demoable.[file:15] |

## Final reminders

- Keep the story simple: **capture -> understand -> act**.[file:15]
- Show a product, not just a concept.
- Prove one behavior that feels proactive.
- Optimize every decision for the judging rubric, especially phone-first execution and Office Kit usage.[cite:1]
- Stay tightly focused on the real Indian student workflow described in the Campus OS prompt.[cite:1]

