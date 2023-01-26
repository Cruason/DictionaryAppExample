package com.example.dictionaryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dictionaryapp.ui.DictionaryTheme
import com.example.dictionaryapp.ui.WordInfoItem
import com.example.dictionaryapp.ui.WordInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DictionaryTheme{
                val viewModel:WordInfoViewModel = hiltViewModel()
                val state = viewModel.state
                val scaffoldState = rememberScaffoldState()

                LaunchedEffect(key1 = true){
                    viewModel.eventFlow.collectLatest { event->
                        when(event){
                            is WordInfoViewModel.UIEvent.ShowSnackBar -> {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = event.message
                                )
                            }
                        }
                    }
                }

                Scaffold(
                    scaffoldState = scaffoldState,
                    content = { paddingValues ->
                        Box(
                            modifier = Modifier
                                .padding(paddingValues)
                                .background(MaterialTheme.colors.background)
                        ){
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            ) {
                                TextField(
                                    value = viewModel.searchQuery.value,
                                    onValueChange = viewModel::onSearch,
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = {
                                        Text(text = "Search ...")
                                    }
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize()
                                ){
                                    items(state.value.wordInfoItems.size){i->
                                        val wordInfo = state.value.wordInfoItems[i]
                                        if(i>0){
                                            Spacer(modifier = Modifier.height(8.dp))
                                        }
                                        WordInfoItem(wordInfo = wordInfo)
                                        if(i<state.value.wordInfoItems.size - 1){
                                            Divider()
                                        }
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}