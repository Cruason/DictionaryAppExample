package com.example.dictionaryapp.domain.usecase

import com.example.dictionaryapp.domain.model.WordInfo
import com.example.dictionaryapp.domain.repository.WordInfoRepository
import com.example.dictionaryapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetWordInfo(
    private val repository: WordInfoRepository
) {

    operator fun invoke(word:String): Flow<Resource<List<WordInfo>>>{
        if(word.isBlank()){
            return flow{}
        }
        return repository.getWordInfo(word)
    }
}