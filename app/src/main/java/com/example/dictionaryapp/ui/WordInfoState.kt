package com.example.dictionaryapp.ui

import com.example.dictionaryapp.domain.model.WordInfo

data class WordInfoState(
    val wordInfoItems: List<WordInfo> = emptyList(),
    val isLoading: Boolean = false
)
