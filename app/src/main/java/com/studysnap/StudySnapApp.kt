package com.studysnap

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.studysnap.navigation.Routes
import com.studysnap.ui.addnotes.AddNotesScreen
import com.studysnap.ui.addnotes.ProcessingScreen
import com.studysnap.ui.chat.ChatbotScreen
import com.studysnap.ui.flashcards.FlashcardsScreen
import com.studysnap.ui.home.HomeScreen
import com.studysnap.ui.quiz.QuizScreen
import com.studysnap.ui.quiz.ResultScreen
import com.studysnap.ui.stories.SubjectStoriesScreen

@Composable
fun StudySnapApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) { HomeScreen(navController = navController) }
        composable(Routes.ADD_NOTES) { AddNotesScreen(navController = navController) }
        composable(Routes.PROCESSING) { ProcessingScreen(navController = navController) }
        composable(
            Routes.SUBJECT_STORIES,
            arguments = listOf(navArgument("subjectId") { type = NavType.StringType })
        ) { entry ->
            SubjectStoriesScreen(
                subjectId = entry.arguments?.getString("subjectId") ?: "",
                navController = navController
            )
        }
        composable(
            Routes.QUIZ,
            arguments = listOf(
                navArgument("conceptId") { type = NavType.StringType },
                navArgument("subjectId") { type = NavType.StringType }
            )
        ) { entry ->
            QuizScreen(
                conceptId = entry.arguments?.getString("conceptId") ?: "",
                subjectId = entry.arguments?.getString("subjectId") ?: "",
                navController = navController
            )
        }
        composable(
            Routes.QUIZ_RESULT,
            arguments = listOf(
                navArgument("conceptId") { type = NavType.StringType },
                navArgument("subjectId") { type = NavType.StringType },
                navArgument("score") { type = NavType.IntType },
                navArgument("total") { type = NavType.IntType }
            )
        ) { entry ->
            ResultScreen(
                conceptId = entry.arguments?.getString("conceptId") ?: "",
                subjectId = entry.arguments?.getString("subjectId") ?: "",
                score = entry.arguments?.getInt("score") ?: 0,
                total = entry.arguments?.getInt("total") ?: 0,
                navController = navController
            )
        }
        composable(
            Routes.FLASHCARDS,
            arguments = listOf(navArgument("conceptId") { type = NavType.StringType })
        ) { entry ->
            FlashcardsScreen(conceptId = entry.arguments?.getString("conceptId") ?: "")
        }
        composable(
            Routes.CHAT,
            arguments = listOf(navArgument("subjectId") { type = NavType.StringType })
        ) { entry ->
            ChatbotScreen(subjectId = entry.arguments?.getString("subjectId") ?: "")
        }
    }
}