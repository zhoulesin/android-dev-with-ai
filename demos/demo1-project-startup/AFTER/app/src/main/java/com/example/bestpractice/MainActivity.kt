package com.example.bestpractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bestpractice.core.designsystem.BestPracticeTheme
import com.example.bestpractice.feature.article.ArticleScreen
import com.example.bestpractice.feature.bookmark.BookmarkScreen
import com.example.bestpractice.feature.feed.FeedScreen
import com.example.bestpractice.feature.search.SearchScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BestPracticeTheme {
                AppNavHost()
            }
        }
    }
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "feed") {
        composable("feed") {
            FeedScreen(
                onArticleClick = { id -> navController.navigate("article/$id") },
                onBookmarksClick = { navController.navigate("bookmarks") },
                onSearchClick = { navController.navigate("search") }
            )
        }
        composable(
            "article/{articleId}",
            arguments = listOf(navArgument("articleId") { type = NavType.StringType })
        ) {
            ArticleScreen(onBack = { navController.popBackStack() })
        }
        composable("bookmarks") {
            BookmarkScreen(
                onArticleClick = { id -> navController.navigate("article/$id") },
                onBack = { navController.popBackStack() }
            )
        }
        composable("search") {
            SearchScreen(
                onArticleClick = { id -> navController.navigate("article/$id") },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
