package com.pandroid.greenhaven.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val userId: String = "",
    val name: String = "",
    var img: String = "",
    val email: String = "",
    val phoneNumber: String = ""
) : Parcelable
