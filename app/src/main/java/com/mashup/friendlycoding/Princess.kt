package com.mashup.friendlycoding

//DPS : Damage Per Second 로 공격력을 의미
class Princess {
    val DPS = 10
    var isPickAxe = false
    var isBook = false
    var isMushroomPoision = false
    var mushroomCnt = 0
    var branchCnt = 0

    fun pickAxe() {
        this.isPickAxe = true
    }

    fun pickBook() {
        this.isBook = true
    }

    fun eatMushroom() {
        ++this.mushroomCnt
    }

    fun pickBranch() {
        ++this.branchCnt
    }

    fun eatMushroomPoision(){
        this.isMushroomPoision = true
    }
}