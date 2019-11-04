package com.mashup.friendlycoding.viewmodel

import androidx.lifecycle.ViewModel
import com.mashup.friendlycoding.model.MapSettingModel

class MapSettingViewModel :ViewModel(){
    val mMapSettingModel = MapSettingModel()
    var offeredBlock = arrayListOf<CodeBlock>()
    var bossBattleBlock : ArrayList<CodeBlock>? = null
}