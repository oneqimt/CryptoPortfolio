package com.imtmobileapps.cryptoportfolio.util

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.imtmobileapps.cryptoportfolio.R
import com.imtmobileapps.cryptoportfolio.model.Article
import com.imtmobileapps.cryptoportfolio.model.Source
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun getProgressDrawable(context: Context): CircularProgressDrawable {

    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}

// EXTENSION of ImageView
fun ImageView.loadImage(uri: String?, progressDrawable: CircularProgressDrawable) {

    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.mipmap.ic_error_image)


    GlideApp.with(context)
        .setDefaultRequestOptions(options)
        .load(uri)
        .into(this)
}

// https://medium.com/@kosta.palash/converting-date-time-considering-time-zone-android-b389ff9d5c49
/** Converting from String to Date **/
fun String.getDateWithServerTimeStamp(): Date? {
    val dateFormat = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        Locale.getDefault()
    )
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")  // IMP !!!
    try {
        return dateFormat.parse(this)
    } catch (e: ParseException) {
        return null
    }
}

/** Converting from Date to String**/
fun Date.getStringTimeStampWithDate(): String {
    val dateFormat = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        Locale.getDefault()
    )
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")
    return dateFormat.format(this)
}


fun getPublishedFormat(datestr: String?): String? {
    return datestr?.getDateWithServerTimeStamp().toString()
}

fun createEmptyList() : List<Article>{
    val source = Source("1", "Not available", "")
    
    val article = Article(
        0.0,
        0.0,
        0,
        "",
        "",
        "",
        "",
        "Not available",
        "",
        "",
        "Not available",
        "",
        source
    
    )
    
    val mylist = arrayListOf(article)
    
    return mylist
}

@BindingAdapter("android:imageUrl")
fun loadImage(view: ImageView, url: String?) {
    view.loadImage(url, getProgressDrawable(view.context))
}

@GlideModule
class AppGlideModule : AppGlideModule()
