package edu.pw.aicatching.network

class MainRepository constructor(private val aiCatchingApiService: AICatchingApiService) {
    fun getWardrobe() = aiCatchingApiService.getWardrobe()
}
