package com.example.dictionaryapp.data.repository

import com.example.dictionaryapp.data.local.WordInfoDao
import com.example.dictionaryapp.data.remote.DictionaryApi
import com.example.dictionaryapp.data.toWordInfo
import com.example.dictionaryapp.domain.model.WordInfo
import com.example.dictionaryapp.domain.repository.WordInfoRepository
import com.example.dictionaryapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class WordInfoRepositoryImpl(
    private val api:DictionaryApi,
    private val dao: WordInfoDao
): WordInfoRepository {
    override fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>> = flow {
        emit(Resource.Loading(emptyList()))

        val wordInfos = dao.getWordInfos(word).map { it.toWordInfo() }
        emit(Resource.Loading(wordInfos))

        try {
            val remoteWordInfos = api.getWordInfo(word)
            dao.deleteWordInfos(remoteWordInfos.map { it.word })
            println(remoteWordInfos)
            dao.insertWordInfos(remoteWordInfos.map { it.toWordInfoEntity() })
        }catch (e: HttpException){
            emit(Resource.Error(e.toString(), wordInfos))
        }catch (e: IOException){
            emit(Resource.Error(e.toString(),wordInfos))
        }

        val newWordInfo = dao.getWordInfos(word).map { it.toWordInfo() }
        emit(Resource.Success(newWordInfo))
    }
}