package edu.pw.aicatching.repositories

import edu.pw.aicatching.network.AICatchingApiService

class MainRepository constructor(private val aiCatchingApiService: AICatchingApiService) {
    fun getWardrobe() = aiCatchingApiService.getWardrobe()
}
