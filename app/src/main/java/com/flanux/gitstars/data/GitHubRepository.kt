package com.flanux.gitstars.data

import kotlinx.coroutines.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

class GitHubRepository(private val token: String) {

    private val apiService: GitHubApiService

    init {

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        val authInterceptor = Interceptor { chain ->

            val request = chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .addHeader("Accept", "application/vnd.github+json")
                .build()

            chain.proceed(request)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
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

    suspend fun getCurrentUser(): Result<GitHubUser> =
        safeCall {
            apiService.getCurrentUser()
        }

    suspend fun getFollowingUsers(): Result<List<GitHubUser>> =
        safeCall {

            val allUsers = mutableListOf<GitHubUser>()
            var page = 1

            while (true) {

                val users = apiService.getFollowing(
                    perPage = 100,
                    page = page
                )

                if (users.isEmpty()) break

                allUsers.addAll(users)
                page++
            }

            allUsers
        }

    suspend fun getMyStarredRepos(): Result<List<GitHubRepo>> =
        safeCall {
            apiService.getMyStarredRepos(perPage = 100)
        }

    suspend fun getStarredReposFromFollowing(): Result<List<StarredRepoItem>> =
        safeCall {

            val following = apiService.getFollowing(perPage = 100)

            coroutineScope {

                val jobs = following.take(10).map { user ->

                    async {

                        try {

                            val repos = apiService.getUserStarredRepos(
                                username = user.login,
                                perPage = 10
                            )

                            repos.map {
                                StarredRepoItem(
                                    repo = it,
                                    starredBy = user.login
                                )
                            }

                        } catch (e: Exception) {
                            emptyList()
                        }

                    }

                }

                jobs.awaitAll()
                    .flatten()
                    .sortedByDescending { it.repo.stars }

            }

        }

    private suspend fun <T> safeCall(
        block: suspend () -> T
    ): Result<T> =
        withContext(Dispatchers.IO) {

            try {
                Result.success(block())
            } catch (e: Exception) {
                Result.failure(e)
            }

        }

    // ---------------- API ----------------

    interface GitHubApiService {

        @GET("user")
        suspend fun getCurrentUser(): GitHubUser

        @GET("user/following")
        suspend fun getFollowing(
            @Query("per_page") perPage: Int = 100,
            @Query("page") page: Int = 1
        ): List<GitHubUser>

        @GET("user/starred")
        suspend fun getMyStarredRepos(
            @Query("per_page") perPage: Int = 100
        ): List<GitHubRepo>

        @GET("users/{username}/starred")
        suspend fun getUserStarredRepos(
            @Path("username") username: String,
            @Query("per_page") perPage: Int = 10,
            @Query("sort") sort: String = "created"
        ): List<GitHubRepo>
    }
}
