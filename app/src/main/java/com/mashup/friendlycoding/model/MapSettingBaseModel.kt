package com.mashup.friendlycoding.model

import java.util.*

open class MapSettingBaseModel {
    var random = Random()
    fun rand(from: Int, to: Int) : Int {
        return random.nextInt(to - from) + from
    }

    // Act 마다 반복되는 거 있으면 알아서 빼주세용
    var defaultCodeBlock_tutorial: ArrayList<CodeBlock> = arrayListOf(
        CodeBlock("move();"),
        CodeBlock("turnLeft();"),
        CodeBlock("turnRight();")
    )
    var defaultCodeBlock: ArrayList<CodeBlock> = arrayListOf(
        CodeBlock("move();"),
        CodeBlock("turnLeft();"),
        CodeBlock("turnRight();"),
        CodeBlock("for(", type = 1),
        CodeBlock("if()", type = 2),
        CodeBlock("while()", type = 2),
        CodeBlock("}", type = 4)
    )
    var stageCodeBlock0 = arrayListOf(
        CodeBlock("pickAxe();"),
        CodeBlock("isPickAxe()", type = 3, argument = 3))

    var stageCodeBlock2_1 = arrayListOf(
        CodeBlock("isBook()", type = 3, argument = 5),
        CodeBlock("pickBook();")
    )
    var stageCodeBlock2_2 = arrayListOf(
        CodeBlock("isMushroom()", type = 3, argument = 4),
        CodeBlock("eatMushroom();"),
        CodeBlock("isMushroomPoision()", type = 3, argument = 8)
    )
    var stageCodeBlock2_3 = arrayListOf(
        CodeBlock("isBranch()", type = 3, argument = 6),
        CodeBlock("pickBranch();")
    )

    /**
     * 보스 배틀 코드블락
     * **/

    var defaultBattleCodeBlock = arrayListOf(
        CodeBlock("if()", type = 2),
        CodeBlock("while()", type = 2),
        CodeBlock("for(", type = 1),
        CodeBlock("}", type = 4),
        CodeBlock("attack();"),
        CodeBlock("isAlive()", type = 3, argument = 7)
    )

    var battleCodeBlock0 = arrayListOf(
        CodeBlock("detectedFire()", type = 3, argument = 0),
        CodeBlock("detectedWater()", type = 3, argument = 1))

    val mapListActNull = arrayOf(
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        )
    var mapListAct0 = arrayOf(
        arrayOf(0, 0, 0, 0, 1, 0, 1, 0, 0, 0),
        arrayOf(1, 1, 0, 0, 1, 0, 1, 1, 1, 1),
        arrayOf(1, 0, 0, 0, 1, 0, 0, 0, 0, 0),
        arrayOf(1, 0, 1, 1, 1, 0, 1, 1, 1, 1),
        arrayOf(1, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(1, 0, 0, 0, 0, 1, 1, 1, 1, 0),
        arrayOf(1, 1, 1, 1, 0, 1, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 1, 0, 1, 0, 0),
        arrayOf(0, 1, 1, 1, 1, 1, 0, 1, 1, 1),
        arrayOf(0, 0, 3, 0, 0, 1, 0, 0, 0, 2)    // 클리어되는 곳을 일단 2로 설정함, 3은 곡괭이
    )
    var mapListAct1 = arrayOf(
        arrayOf(0, 0, 0, 0, 1, 0, 1, 0, 0, 0),
        arrayOf(1, 1, 0, 0, 1, 0, 1, 1, 1, 1),
        arrayOf(1, 0, 0, 0, 1, 0, 0, 0, 0, 0),
        arrayOf(1, 0, 1, 1, 1, 0, 1, 1, 1, 1),
        arrayOf(1, 0, 0, 4, 0, 0, 0, 0, 0, 0),
        arrayOf(1, 0, 0, 0, 0, 1, 1, 1, 1, 0),
        arrayOf(1, 1, 1, 1, 0, 1, 0, 0, 4, 0),
        arrayOf(0, 0, 0, 0, 0, 1, 0, 1, 0, 0),
        arrayOf(0, 1, 1, 1, 1, 1, 0, 1, 1, 1),
        arrayOf(0, 0, 4, 0, 0, 1, 0, 0, 0, 2)  // 4는 버섯
    )
    var mapListAct2_1 = arrayOf(
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 5, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 2)  // 5는 책
    )
    var mapListAct2_2 = arrayOf(
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 4, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 4, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 2)  // 4는 버섯
    )
    var mapListAct2_3 = arrayOf(
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 6, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 6, 0, 0, 0, 0, 0, 0, 0, 0),
        arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 2)  // 6은 나무
    )
}