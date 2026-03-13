package com.flanux.gitstars.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flanux.gitstars.data.GitHubRepository
import com.flanux.gitstars.data.StarredRepoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UiState {
    object Initial : UiState()
    object Loading : UiState()
    data class Success(val repos: List<StarredRepoItem>) : UiState()
    data class Error(val message: String) : UiState()
}

class MainViewModel(private val repository: GitHubRepository) : ViewModel() {
    
    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    private val _selectedLanguage = MutableStateFlow<String?>(null)
    val selectedLanguage: StateFlow<String?> = _selectedLanguage.asStateFlow()
    
    fun loadStarredRepos() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            
            repository.getStarredReposFromFollowing()
                .onSuccess { repos ->
                    _uiState.value = UiState.Success(repos)
                }
                .onFailure { error ->
                    _uiState.value = UiState.Error(
                        error.message ?: "Failed to load starred repos"
                    )
                }
        }
    }
    
    fun filterByLanguage(language: String?) {
        _selectedLanguage.value = language
    }
    
    fun getFilteredRepos(repos: List<StarredRepoItem>): List<StarredRepoItem> {
        return _selectedLanguage.value?.let { lang ->
            repos.filter { it.repo.language == lang }
        } ?: repos
    }
    
    fun getAvailableLanguages(repos: List<StarredRepoItem>): List<String> {
        return repos
            .mapNotNull { it.repo.language }
            .distinct()
            .sorted()
    }
}
