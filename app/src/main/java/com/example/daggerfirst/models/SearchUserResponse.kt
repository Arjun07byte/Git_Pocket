package com.example.daggerfirst.models

data class SearchUserResponse(
    val incomplete_results: Boolean,
    val items: List<SearchUserItem>,
    val total_count: Int
)