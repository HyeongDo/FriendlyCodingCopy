package com.mashup.friendlycoding.model

import android.view.View

data class MapItem(
    var resourceId : Int,
    var item_id:Int,
    var X:Int = 5,
    var Y:Int = 5,
    var visibility: Int = View.INVISIBLE
)