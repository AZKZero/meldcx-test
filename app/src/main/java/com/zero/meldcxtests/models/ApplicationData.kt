package com.zero.meldcxtests.models

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ApplicationData(
    @IgnoredOnParcel var icon: Drawable? = null,
    var name: String,
    val packageName: String,
) : Parcelable