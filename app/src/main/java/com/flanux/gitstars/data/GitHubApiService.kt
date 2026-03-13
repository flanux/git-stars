package com.flanux.gitstars.data

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApiService {
    
    @GET("user/following")
    suspend fun getFollowing(
        @Header("Authorization") token: String,
        @Query("per_page") perPage: Int = 100,
        @Query("page") page: Int = 1
    ): List<GitHubUser>
    
    @GET("users/{username}/starred")
    suspend fun getUserStarredRepos(
        @Path("username") username: String,
        @Header("Authorization") token: String,
        @Query("per_page") perPage: Int = 100,
        @Query("page") page: Int = 1,
        @Query("sort") sort: String = "created" // created, updated
    ): List<GitHubRepo>
    
    @GET("user/starred")
    suspend fun getMyStarredRepos(
        @Header("Authorization") token: String,
        @Query("per_page") perPage: Int = 100,
        @Query("page") page: Int = 1
    ): List<GitHubRepo>
    
    @GET("user")
    suspend fun getCurrentUser(
        @Header("Authorization") token: String
    ): GitHubUser
}
