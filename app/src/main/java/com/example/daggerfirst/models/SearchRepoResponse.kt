package com.example.daggerfirst.models

data class SearchRepoResponse(
    val incomplete_results: Boolean,
    val items: List<SearchRepoResponseItem>,
    val total_count: Int
)