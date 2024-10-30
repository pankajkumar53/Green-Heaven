package com.pandroid.greenhaven.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class PlantModel(
    val plantId: String = "",
    val title: String = "",
    var plantImage: String = "",
    var rating: String = "",
    var description: String = "",
    var price: String = "",
    var category: String = "",

    @field:JvmField
    var plantImages: @RawValue ArrayList<PlantImageUrlModel> = ArrayList()
) : Parcelable

