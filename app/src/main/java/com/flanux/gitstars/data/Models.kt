package com.flanux.gitstars.data

import com.google.gson.annotations.SerializedName

data class GitHubRepo(
    val id: Long,
    val name: String,
    @serializedName("full_name")
    val fullName: String,
    val description: String?,
    val language: String?,
    @SerializedName("stargazers_count")
    val stars: Int,
    @SerializedName("forks_count")
    val forks: Int,
    @SerializedName("html_url")
    val url: String,
    val owner: Owner,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    val topics: List<String>? = null 
) {
    data class Owner(
        val login: String,
        @SerializedName("avatar_url")
        val avatarUrl: String
    )
}

data class GitHubUser(
    val login: String,
    val name: String?,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    val bio: String?,
    @SerializedName("public_repos")
    val publicRepos: Int,
    val followers: Int,
    val following: Int
)

// For local storage
data class StarredRepoItem(
    val repo: GitHubRepo,
    val starredBy: String, // username who starred it
    val starredAt: Long = System.currentTimeMillis()
)
