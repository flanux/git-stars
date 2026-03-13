package com.flanux.gitstars.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class GitHubRepository(private val token: String) {
    
    private val apiService: GitHubApiService
    
    init {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        
        apiService = retrofit.create(GitHubApiService::class.java)
    }
    
    private fun authHeader() = "Bearer $token"
    
    suspend fun getFollowingUsers(): Result<List<GitHubUser>> = withContext(Dispatchers.IO) {
        try {
            val users = apiService.getFollowing(authHeader())
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getStarredReposFromFollowing(): Result<List<StarredRepoItem>> = withContext(Dispatchers.IO) {
        try {
            val following = apiService.getFollowing(authHeader())
            val allStarredRepos = mutableListOf<StarredRepoItem>()
            
            // Fetch starred repos for each person you follow
            following.forEach { user ->
                try {
                    val starredRepos = apiService.getUserStarredRepos(
                        username = user.login,
                        token = authHeader(),
                        perPage = 30, // Limit per user to avoid rate limits
                        sort = "created"
                    )
                    
                    starredRepos.forEach { repo ->
                        allStarredRepos.add(
                            StarredRepoItem(
                                repo = repo,
                                starredBy = user.login
                            )
                        )
                    }
                } catch (e: Exception) {
                    // Skip user if their stars are private or error occurs
                }
            }
            
            // Sort by most recent stars first
            Result.success(allStarredRepos.sortedByDescending { it.repo.stars })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getMyStarredRepos(): Result<List<GitHubRepo>> = withContext(Dispatchers.IO) {
        try {
            val repos = apiService.getMyStarredRepos(authHeader())
            Result.success(repos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getCurrentUser(): Result<GitHubUser> = withContext(Dispatchers.IO) {
        try {
            val user = apiService.getCurrentUser(authHeader())
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
