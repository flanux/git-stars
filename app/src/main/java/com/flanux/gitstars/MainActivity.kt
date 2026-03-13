package com.flanux.gitstars

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.flanux.gitstars.data.GitHubRepository
import com.flanux.gitstars.ui.MainScreen
import com.flanux.gitstars.ui.MainViewModel
import com.flanux.gitstars.ui.theme.GitStarsTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val ComponentActivity.dataStore by preferencesDataStore(name = "settings")
private val TOKEN_KEY = stringPreferencesKey("github_token")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            GitStarsTheme {
                var token by remember { mutableStateOf<String?>(null) }
                val scope = rememberCoroutineScope()
                
                LaunchedEffect(Unit) {
                    // Load saved token
                    token = dataStore.data
                        .map { it[TOKEN_KEY] }
                        .first()
                }
                
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (token.isNullOrBlank()) {
                        TokenInputScreen(
                            onTokenSaved = { newToken ->
                                scope.launch {
                                    dataStore.edit { it[TOKEN_KEY] = newToken }
                                    token = newToken
                                }
                            }
                        )
                    } else {
                        val repository = remember(token) { 
                            GitHubRepository(token!!) 
                        }
                        val viewModel = remember(repository) { 
                            MainViewModel(repository) 
                        }
                        
                        val uiState by viewModel.uiState.collectAsState()
                        val selectedLanguage by viewModel.selectedLanguage.collectAsState()
                        
                        MainScreen(
                            uiState = uiState,
                            selectedLanguage = selectedLanguage,
                            onLoadRepos = { viewModel.loadStarredRepos() },
                            onLanguageFilter = { viewModel.filterByLanguage(it) },
                            getFilteredRepos = { viewModel.getFilteredRepos(it) },
                            getAvailableLanguages = { viewModel.getAvailableLanguages(it) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TokenInputScreen(onTokenSaved: (String) -> Unit) {
    var token by remember { mutableStateOf("") }
    var showInfo by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Git Stars ⭐",
            style = MaterialTheme.typography.headlineLarge
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "Enter your GitHub Personal Access Token",
            style = MaterialTheme.typography.bodyLarge
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = token,
            onValueChange = { token = it },
            label = { Text("GitHub Token") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        TextButton(onClick = { showInfo = !showInfo }) {
            Text("How to get a token?")
        }
        
        if (showInfo) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "1. Go to github.com → Settings\n" +
                                "2. Developer settings → Personal access tokens → Tokens (classic)\n" +
                                "3. Generate new token (classic)\n" +
                                "4. Select scopes: 'user:follow' and 'public_repo'\n" +
                                "5. Copy token and paste here",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = { onTokenSaved(token) },
            enabled = token.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Token")
        }
    }
}
