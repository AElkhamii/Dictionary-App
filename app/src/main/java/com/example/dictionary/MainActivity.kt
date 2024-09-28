package com.example.dictionary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dictionary.feature_dictionary.Presentation.WordInfoItem
import com.example.dictionary.feature_dictionary.Presentation.WordInfoViewModel
import com.example.dictionary.ui.theme.DictionaryTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            println("Change 2")
            println("Change 2")
            println("Change 4")
            DictionaryTheme {
                /** Define instance from viewmodel and state **/
                val viewModel: WordInfoViewModel = hiltViewModel()
                val state = viewModel.state.value

                /** Snack bar **/
                val snackbarHostState = remember { SnackbarHostState() }
                LaunchedEffect(key1 = true) {
                    viewModel.eventFlow.collectLatest {event ->
                        when(event){
                            is WordInfoViewModel.UIEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(
                                    event.message, duration = SnackbarDuration.Long
                                )
                            }
                        }
                    }
                }

                /** screen content **/
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .background(MaterialTheme.colorScheme.background) //the color of the back ground will change according to the phone operates on dark or light mode
                    ){
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            TextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = viewModel.searchQuery.value,
                                onValueChange = viewModel::onSearch,
                                placeholder = {Text(text = "Search...")}
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            LazyColumn(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(state.wordInfoItems.size){ i->
                                    val wordInfo = state.wordInfoItems[i]
                                    if(i > 0){
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                    WordInfoItem(wordInfo = wordInfo)
                                    if(i < state.wordInfoItems.size - 1){
                                        HorizontalDivider()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
