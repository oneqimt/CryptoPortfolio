package com.imtmobileapps.cryptoportfolio.util

import android.content.Context
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.room.TypeConverter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.imtmobileapps.cryptoportfolio.R
import com.imtmobileapps.cryptoportfolio.model.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import java.math.BigDecimal
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

fun addRequiredToHint(activity: AppCompatActivity, hint: String):String{
   
    val asterisk = activity.getString(R.string.asteriskred)
    
    return "$hint $asterisk"
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

fun createSignUp() : SignUp{
    val state = State(3, "Arizona", "USA", "AZ")
    val person = Person(
        personId = 0,
        firstName = "Danny",
        lastName = "Ducher",
        email = "danny@gmail.com",
        phone = "2220987659",
        address = "440 Nice Park Rd",
        city = "Hermosa beach",
        zip = "86325",
        state = state
    
    )
    
    val auth = Auth(
        auth_id = 0,
        username = "time2",
        password = "time55",
        person_id = 0,
        role = CoinApp.ROLE_USER,
        enabled = CoinApp.ENABLED
    )
    
    val signUp = SignUp(
        person = person,
        auth = auth
    )
    
    return signUp
}

fun createEmptySignUp() : SignUp{
    // DEFAULT state set id to  = 3
    val state = State(3, "", "", "")
    val person = Person(
        personId = 0,
        firstName = "",
        lastName = "",
        email = "",
        phone = "",
        address = "",
        city = "",
        zip = "",
        state = state
    
    )
    
    val auth = Auth(
        auth_id = 0,
        username = "",
        password = "",
        person_id = 0,
        role = CoinApp.ROLE_USER,
        enabled = CoinApp.ENABLED
    )
    
    val signUp = SignUp(
        person = person,
        auth = auth
    )
    
    return signUp
}


@BindingAdapter("android:imageUrl")
fun loadImage(view: ImageView, url: String?) {
    view.loadImage(url, getProgressDrawable(view.context))
}

@GlideModule
class AppGlideModule : AppGlideModule()

class BigDecimalDoubleTypeConverter {
    
    @TypeConverter
    fun bigDecimalToDouble(input: BigDecimal?): Double {
        return input?.toDouble() ?: 0.0
    }
    
    @TypeConverter
    fun stringToBigDecimal(input: Double?): BigDecimal {
        if (input == null) return BigDecimal.ZERO
        return BigDecimal.valueOf(input) ?: BigDecimal.ZERO
    }
    
}
