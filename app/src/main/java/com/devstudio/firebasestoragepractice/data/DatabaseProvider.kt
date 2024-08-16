package com.devstudio.firebasestoragepractice.data

object DatabaseProvider {
    // initialize repository
    private val repository : UploadImageRepository by lazy {
        UploadImageRepository()
    }
    fun repository() = repository
}