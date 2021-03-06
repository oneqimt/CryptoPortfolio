package com.imtmobileapps.cryptoportfolio.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Article(
    val activityHotness: Double?,
    val hotness: Double?,
    val words: Int,
    val _id: String?,
    val description: String?,
    val originalImageUrl: String?,
    val primaryCategory: String?,
    val publishedAt: String?,
    val sourceDomain: String?,
    val thumbnail: String?,
    val title: String?,
    val url: String?,
    val source: Source?

):Parcelable {
}