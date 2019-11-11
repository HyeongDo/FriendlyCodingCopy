package com.mashup.friendlycoding

import com.mashup.friendlycoding.model.MapDrawable

class Map (var mapList : Array<Array<Int>>? = null, var drawables: MapDrawable? = null) {
    fun pickAxe(y : Int, x : Int) {
        this.mapList!![y][x] = 0
    }

    fun pickBook(y: Int, x : Int) {
        this.mapList!![y][x] = 0
    }

    fun pickBranch(y: Int, x: Int){
        this.mapList!![y][x] = 0
    }

    fun eatMushroom(y: Int, x : Int){
        this.mapList!![y][x] = 0
    }
}