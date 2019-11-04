package com.mashup.friendlycoding.model

import com.mashup.friendlycoding.R
import com.mashup.friendlycoding.viewmodel.CodeBlock

class Stage(val map : Map, val princess : Princess, val monster: Monster?, val offeredBlock : ArrayList<CodeBlock>, val bossBattleBlock : ArrayList<CodeBlock>? = null)

class MapSettingModel {
    fun getStageInfo (stageNum : Int) : Stage {
        when (stageNum) {
            0 -> {
                // 맵 정보
                val mapList = arrayOf(
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

                // 기본 제공되는 블록
                val offeredBlock = arrayListOf(
                    CodeBlock("move"),
                    CodeBlock("turnLeft"),
                    CodeBlock("turnRight"),
                    CodeBlock("pickAxe"),
                    CodeBlock("for", type = 1),
                    CodeBlock("}", type = 4),
                    CodeBlock("if", type = 2),
                    CodeBlock("isPickAxe", type = 3, argument = 3)
                )

                val bossBattleBlock = arrayListOf(
                    CodeBlock("if", type = 2),
                    CodeBlock("while", type = 2),
                    CodeBlock("for", type = 1),
                    CodeBlock("}", type = 4),
                    CodeBlock("attack"),
                    CodeBlock("isAlive", type = 3, argument = 7),
                    CodeBlock("detectedFire", type = 3, argument = 5),
                    CodeBlock("detectedWater", type = 3, argument = 6)
                    )

                return Stage(Map(mapList), Princess(), Monster(100, 0, 0), offeredBlock, bossBattleBlock)
            }

            1-> {
                val mapList = arrayOf(
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

                // 기본 제공되는 블록
                val blockButton = arrayListOf(
                    CodeBlock("move"),
                    CodeBlock("turnLeft"),
                    CodeBlock("turnRight"),
                    CodeBlock("if", type = 2),
                    CodeBlock("for", type = 1),
                    CodeBlock("isMushroom", type = 3, argument = 4),
                    CodeBlock("eatMushRoom"),
                    CodeBlock("}")
                )

                return Stage(Map(mapList), Princess(), Monster(0, 0, 0), blockButton)
            }

            // 2 -> {} ...

            else -> {
                return Stage(Map(), Princess(), null, arrayListOf())
            }
        }
    }
}