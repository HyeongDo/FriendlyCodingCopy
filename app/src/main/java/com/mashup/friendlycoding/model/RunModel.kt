package com.mashup.friendlycoding.model

import android.util.Log
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mashup.friendlycoding.ignoreBlanks
import com.mashup.friendlycoding.viewmodel.CodeBlock
import java.util.*
import kotlin.collections.ArrayList

class RunModel {
    private var moveView = MutableLiveData<Int>()
    private var nowProcessing = MutableLiveData<Int>()
    private var nowTerminated = MutableLiveData<Int>()
    private var mCodeBlock = MutableLiveData<ArrayList<CodeBlock>>()
    var monsterAttacked = MutableLiveData<Boolean>()
    var isLost = MutableLiveData<Boolean>()
    var isWin = MutableLiveData<Boolean>()
    var metBoss = MutableLiveData<Boolean>()
    var insertBlockAt = MutableLiveData<Int>()
    var insertedBlock : String? = null

    private var jumpTo = 0
    private var IR = 0  // 명령어 실행할 주소
    private var iterator = 0 // 반복자
    private var blockLevel = 0 // 들여쓰기 정도.
    private var bracketStack = Stack<Int>()  // 괄호 체크, 그와 동시에 jump 할 명령어 주소 얻기 위함
    var blockInsertMode = false

    // 공주의 좌표
    private var x = 0
    private var y = 9
    private var d = 1

    var mPrincess = Princess()
    var mMap = Map()
    var mMonster : Monster? = null

    fun init() {
        mCodeBlock.value = ArrayList()
        isLost.value = false
        isWin.value = false
    }

    fun getCodeBlock(): LiveData<ArrayList<CodeBlock>> {
        return mCodeBlock
    }

    fun getMoving(): LiveData<Int> {
        return moveView
    }

    fun getNowProcessing(): LiveData<Int> {
        return nowProcessing
    }

    fun getNowTerminated(): LiveData<Int> {
        return nowTerminated
    }

    fun movePrincess() {
        d %= 4
        when (d) {
            //goint up
            0 -> {
                y--
            }

            //going right
            1 -> {
                x++
            }

            //going down
            2 -> {
                y++
            }

            //going left
            3 -> {
                x--
            }
        }
    }

    fun rotate (LeftOrRight : Boolean) {
        if (!LeftOrRight) {   // 왼쪽으로
            d -= 1
            if (d < 0)
                d += 4
        }
        else {  // 오른쪽으로
            if (d == 3)
                d = 0
            else
                d++
        }
    }

    fun collisionCheck() {
        Log.e("(nowX", "(nowX  $x,,,$y")
        if (x < 10 && x > -1 && y < 10 && y > -1) {
            if (mMap.mapList!![y][x] == 1) {
                isLost.postValue(true)
            } else if (mMap.mapList!![y][x] == 2) {
                isWin.postValue(true)
            } else if (y == mMonster?.y && x == mMonster?.x) {
                metBoss.postValue(true)
            }
        }

        else {
            isLost.postValue(true)
        }
    }

    fun addNewBlock(codeBlock: CodeBlock) {
        if (blockInsertMode) {
            if (codeBlock.type == 3) {
                Log.e("블록을 추가합니다", "${codeBlock.funcName}  ${codeBlock.type}  ${codeBlock.argument}")
                mCodeBlock.value!![IR - 1].argument = codeBlock.argument
                insertedBlock = codeBlock.funcName
                insertBlockAt.postValue(IR - 1)
                blockInsertMode = false
                return
            }
            else
                return
        }

        val adding = CodeBlock(codeBlock.funcName, address = IR, type = codeBlock.type)
        if (adding.funcName == "}") {
            if (bracketStack.empty()) {
                return
            }
            if (bracketStack.peek() < 10000)
                adding.address = bracketStack.peek()  // jump할 주소
            else { // if인 경우엔 jump가 아니라 if의 주소를 바꿔야 한다
                mCodeBlock.value!![bracketStack.peek() - 10000].address = IR
            }

            bracketStack.pop()
            Log.e("block level", "decreases")
            blockLevel--
        }

        var tap = ""
        for (i in 0 until blockLevel) {
            tap += "    "
        }

        Log.e("${adding.funcName} ", "추가됨 $blockInsertMode")
        adding.funcName = tap + adding.funcName

        if (ignoreBlanks(adding.funcName) == "for" || ignoreBlanks(adding.funcName) == "while" || ignoreBlanks(
                adding.funcName
            ) == "if"
        ) {
            if (ignoreBlanks(adding.funcName) == "if")
                bracketStack.push(IR + 10000)
            else
                bracketStack.push(IR)
            blockLevel++
        }

        IR++
        val block = mCodeBlock.value
        block!!.add(adding)
        mCodeBlock.postValue(block)
        blockInsertMode = false
    }
//
//    fun updateBlock(position: Int, cnt: Int) {
//        val block = mCodeBlock.value
//        mCodeBlock.value!![position].count = cnt
//        mCodeBlock.postValue(block)
//    }

    fun clearBlock() {
        x = 0
        y = 9
        d = 1
        iterator = 0
        jumpTo = 0
        blockLevel = 0
        moveView.postValue(-1)
        val block = mCodeBlock.value
        mCodeBlock.value!!.clear()
        mCodeBlock.postValue(block)
    }

    fun run() {
        // 괄호 짝이 안 맞는 코드일 시 경고 메시지 출력
        if (!bracketStack.empty()) {
            Log.e("compile", "error")
            clearBlock()
            return
        }

        val run = RunThead()
        IR = 0
        run.start()
        iterator = 0
    }

    inner class RunThead : Thread() {
        lateinit var view: EditText
        override fun run() {
            try {
                moveView.postValue(-2)
                sleep(500)

                while (IR < mCodeBlock.value!!.size) {
                    nowProcessing.postValue(IR)
                    Log.e("실행 중 : ", mCodeBlock.value!![IR].funcName + " ${mCodeBlock.value!![IR].type}")
                    if (mMonster!!.hp <= 0) {
                        metBoss.postValue(false)
                    }

                    when (ignoreBlanks(mCodeBlock.value!![IR].funcName)) {
                        "move" -> {
                            movePrincess()
                            moveView.postValue(d)
                            collisionCheck()
                            Log.e("갑니다", "앞으로")
                            sleep(1000)
                        }

                        "turnLeft" -> {
                            //    moveView.value = 1
                            rotate(false)
                            moveView.postValue(4)
                            Log.e("돕니다", "왼쪽으로")
                            sleep(1000)
                        }

                        "turnRight" -> {
                            rotate(true)
                            //  moveView.value = 2
                            moveView.postValue(5)
                            Log.e("돕니다", "오른쪽으로")
                            sleep(1000)
                        }

                        "for" -> {
                            iterator = mCodeBlock.value!![IR].argument
                            Log.e("반복", "${mCodeBlock.value!![IR].argument}")
                            sleep(1000)
                        }

                        "}" -> {
                            jumpTo = mCodeBlock.value!![IR].address
                            Log.e("jumpTo", "$jumpTo, iterate ${mCodeBlock.value!![jumpTo].argument}")

                            if (mCodeBlock.value!![jumpTo].type == 2) {
                                when (mCodeBlock.value!![jumpTo].argument) {
                                    7 -> {
                                        if (mMonster!!.hp > 0) {
                                            IR = jumpTo
                                            Log.e("아직 안 죽었네", "$jumpTo 로!")
                                        }
                                    }
                                }
                            }

                            else if (mCodeBlock.value!![jumpTo].type == 1) {
                                if (mCodeBlock.value!![jumpTo].argument-- > 1) { // 이 조건 때문에 if에서 열린 블록이라도 반복을 진행하진 않을 것
                                    IR = jumpTo
                                    Log.e("한 번 더!", "${mCodeBlock.value!![jumpTo].argument}   ${mCodeBlock.value!![IR].funcName}")
                                } else {
                                    mCodeBlock.value!![jumpTo].argument = iterator // 원상 복구
                                }
                            }
                        }

                        "if" -> {
                            when (mCodeBlock.value!![IR].argument) {
                                1 -> {

                                }

                                3 -> {  // 곡괭이?
                                    if (!mPrincess.isPickAxe) {
                                        Log.e("분기", "${mCodeBlock.value!![IR].address}로!")
                                        IR = mCodeBlock.value!![IR].address
                                    }
                                }

                                else -> {

                                }
                            }
                            sleep(1000)
                        }

                        "else" -> {

                        }

                        "pickAxe" -> {
                            mPrincess.isPickAxe = true
                            sleep(1000)
                        }

                        "attack" -> {
                            mMonster!!.hp -= mPrincess.DPS
                            monsterAttacked.postValue(true)
                            sleep(1000)
                            monsterAttacked.postValue(false)
                        }
                    }

                    nowTerminated.postValue(IR)
                    IR++  // PC
                }
                moveView.postValue(-3)
            } catch (e: IndexOutOfBoundsException) {
                return
            }
            //clearBlock() 스테이지 클리어 실패 시
        }
    }
}