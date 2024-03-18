package com.example.task

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DailyNews(
    var title: String,
    val description: String,
    val bannerImage: String?, // Updated to accept nullable String
    val category: String,
    val region: String,
    val status: String,
    val language: String,
    val city: String,
    val country: String,
    val createdOn: String,
    val createdBy: String,
    val updatedOn: String,
    val updatedBy: String,
    var newsId: Int = -1
) : Parcelable
