package com.mashup.friendlycoding.model

import android.util.Log
import com.mashup.friendlycoding.ignoreBlanks

class RunModel : RunBaseModel() {
    var turnOff : Int = 0

    fun collisionCheck(): Boolean {   // 벽이나 보스와의 충돌 감지
        if (x < 10 && x > -1 && y < 10 && y > -1) {
            if (mMap.mapList!![y][x] == 1) {
                moveView.postValue(7)   // 벽이라면 졌다는 시그널 전송
                return true
            } else if (mMap.mapList!![y][x] == 2) {
                moveView.postValue(8)    // 이겼다면 이겼다는 시그널 전송
                return false
            } else if (y == mMonster?.y && x == mMonster?.x) {
                metBoss.postValue(true)  // 보스를 만나면 보스를 만났다는 시그널 전송
                return true
            }
        } else {
            moveView.postValue(7)     // 인덱스를 넘어갈 시
            return true
        }
        return false
    }

    fun run() {
        iterator = 0
        compileError = false
        IR = 0

        Log.e("괄호", "isEmpty : ${bracketStack.empty()}")
        if (bracketStack.isNotEmpty() || closingBracket != openingBracket) {
            moveView.postValue(-5)
            return
        }
        val run = RunThead()
        var open = 0
        while (open < mCodeBlock.value!!.size) {
            if (mCodeBlock.value!![open].type != 0) {
                open = compile(open)
            }
            open++
        }
        if (compileError) {
            moveView.postValue(-5)
        }
        else
            run.start()
    }

    inner class RunThead : Thread() {
        override fun run() {
            try {
                moveView.postValue(-2)
                sleep(speed)

                while (IR < mCodeBlock.value!!.size) {
                    if (metBoss.value == true && !isAttacking && mMonster!!.isAlive()) {
                        // 몬스터의 차례
                        mMonster?.attack()
                        Log.e("몬스터의 공격!", "${mMonster?.attackType}")
                        monsterAttack.postValue(mMonster?.attackType)
                        if (mMonster?.attackType != -1) {
                            when (mMonster?.attackType) {
                                0 -> Log.e("몬스터의", "불 공격!!!")
                                1 -> Log.e("몬스터의", "물 공격!!!")
                            }
                            sleep(speed)

                            if (coc[mMonster?.attackType!!] == -1) {   // 피하는 루틴이 없음
                                moveView.postValue(-6)   // 사망
                                return
                            } else {
                                IR = coc[mMonster?.attackType!!]  // 해당하는 것을 막으러 가자.
                                isAttacking = true
                            }
                        } else {
                            Log.e("몬스터", "휴식!")
                        }
                    }

                    if (iterator > 30) {
                        // 이런 게임 깨는데 루프를 30번 넘게 돌진 않겠지?
                        moveView.postValue(-4)
                        return
                    }

                    nowProcessing.postValue(IR)
                    turnOff = IR
                    Log.e("실행 중 : ", mCodeBlock.value!![IR].funcName + " ${mCodeBlock.value!![IR].type}")

                    when (ignoreBlanks(mCodeBlock.value!![IR].funcName)) {
                        // TODO : 0 유형 블록 (일반 함수)
                        /***
                         * 예)
                         * "함수이름" -> {
                         * ...
                         * sleep(speed)
                         * }
                         * ****/

                        "move();" -> {
                            movePrincess()
                            moveView.postValue(driction)
                            if (collisionCheck()) {
                                nowTerminated.postValue(IR)
                                return
                            }
                        }
                        "turnLeft();" -> {
                            //moveView.value = 1
                            rotate(false)
                            moveView.postValue(4)
                        }
                        "turnRight();" -> {
                            //  moveView.value = 2
                            rotate(true)
                            moveView.postValue(5)
                        }

                        "for(" -> {
//                            if (mCodeBlock.value!![IR].argument <= 0) {
//                                moveView.postValue(-5)
//                                nowTerminated.postValue(IR)
//                                return
//                            }
                            iterator = mCodeBlock.value!![IR].argument
                            iteratorStack.push(mCodeBlock.value!![IR].argument)
                            Log.e("반복", "${mCodeBlock.value!![IR].argument}")
                        }

                        "}" -> {
                            turnOff = IR
                            jumpTo = mCodeBlock.value!![IR].address
                            Log.e("jumpTo", "$jumpTo, $iterator to ${mCodeBlock.value!![jumpTo].argument} , ${ignoreBlanks(mCodeBlock.value!![jumpTo].funcName)}")

                            if (mCodeBlock.value!![jumpTo].type == 4) {
                                Log.e("요건", "while문")
                                IR = jumpTo - 1
                            }

                            if (mMonster != null && mCodeBlock.value!![jumpTo].type == 2 && isAttacking && mCodeBlock.value!![jumpTo].argument == mMonster!!.attackType) {
                                Log.e("몬스터", "공격 종료!")
                                monsterAttack.postValue(-1)
                                isAttacking = false
                            }

                            if (mCodeBlock.value!![jumpTo].type == 1) {
                                Log.e("for 가 날 열었어", "$jumpTo 의  ${mCodeBlock.value!![jumpTo].argument}")
                                if (mCodeBlock.value!![jumpTo].argument-- > 1) { nowTerminated.postValue(IR)
                                    IR = jumpTo
                                    Log.e("한 번 더!", "${mCodeBlock.value!![jumpTo].argument}   ${mCodeBlock.value!![IR].funcName}")
                                    iterator++
                                }
                                else {
                                    //mCodeBlock.value!![jumpTo].argument = iterator
                                    //mCodeBlock.value!![IR].argument = iterator
                                    mCodeBlock.value!![jumpTo].argument = iteratorStack.peek()
                                    iteratorStack.pop()
                                }
                            }
                        }

                        // 아이템 습득 부분
                        "pickAxe();" -> {
                            if (mMap.mapList!![y][x] == 3) {
                                mPrincess.pickAxe()
                                mMap.itemPicked(y, x)
                                moveView.postValue(6)
                            } else {
                                moveView.postValue(-3)
                                return
                            }
                            sleep(speed)
                        }

                        "eatMushroom();" -> {
                            Log.e("현재 좌표 = $y, $x" , "발밑 ${mMap.mapList!![y][x]}")
                            if(mMap.mapList!![y][x]%10 == 4){
                                mPrincess.eatMushroom()
                                changingView = mMap.mapList!![y][x]/10
                                changingViewAll = mMap.mapList!![y][x]
                                mMap.itemPicked(y, x)
                                moveView.postValue(6)
                            }else{
                                moveView.postValue(7)
                                return
                            }
                        }

                        "pickBook();" -> {
                            Log.e("책을 줍습니다.", "공주 밑엔 ${mMap.mapList!![y][x]}")
                            if (mMap.mapList!![y][x]%10 == 5) {
                                mPrincess.pickBook()
                                changingView = mMap.mapList!![y][x]/10
                                mMap.itemPicked(y, x)
                                moveView.postValue(6)
                            } else {
                                moveView.postValue(7)
                                return
                            }
                        }

                        "pickBranch();" -> {
                            if (mMap.mapList!![y][x]%10 == 6) {
                                mPrincess.pickBranch()
                                changingView = mMap.mapList!![y][x]/10
                                mMap.itemPicked(y, x)
                                moveView.postValue(6)
                            } else {
                                moveView.postValue(7)
                                return
                            }
                        }

                        // 보스전 부분
                        "attack();" -> {
                            mMonster?.monsterAttacked(mPrincess.DPS)
                            monsterAttacked.postValue(true)
                        }

                        "fireShield();" -> {
                            princessAction.postValue(9)
                        }

                        "iceShield();" -> {
                            princessAction.postValue(9)
                        }

                        else -> {
                            if (mCodeBlock.value!![IR].type == 2) {
                                Log.e("if", "입니다, ${mCodeBlock.value!![IR].argument}")
                                when (mCodeBlock.value!![IR].argument) {
                                    // TODO : 3번 블록 (boolean형 반환 함수) 중 if에 들어간 블록
                                    //  예)
                                    //  argument -> {
                                    //      if (...) {
                                    //      }
                                    //      else {
                                    //          IR = mCodeBlock.value!![IR].address
                                    //      }
                                    //  }

                                    0 -> {
                                        if (mMonster != null) {
                                            if (isAttacking && (mMonster!!.attackType == mCodeBlock.value!![IR].argument)) {
                                                Log.e("막았다!", "${mCodeBlock.value!![jumpTo].argument} 공격")
                                            }
                                            else {
                                                IR = mCodeBlock.value!![IR].address
                                            }
                                        }
                                    }

                                    1 -> {
                                        if (mMonster != null) {
                                            if (isAttacking && mMonster!!.attackType == mCodeBlock.value!![IR].argument) {
                                                Log.e("막았다!", "${mCodeBlock.value!![jumpTo].argument} 공격")
                                            } else {
                                                IR = mCodeBlock.value!![IR].address
                                            }
                                        }
                                    }

                                    3 -> {  // 곡괭이
                                        if (!mPrincess.isPickAxe) {
                                            Log.e("분기", "${mCodeBlock.value!![IR].address}로!")
                                            IR = mCodeBlock.value!![IR].address
                                        }
                                    }

                                    4 -> { //버섯
                                        if (mPrincess.mushroomCnt < 2) {
                                            Log.e("분기", "${mCodeBlock.value!![IR].address}로!")
                                            IR = mCodeBlock.value!![IR].address
                                        }
                                    }

                                    5 -> { //책
                                        if (!mPrincess.isBook) {
                                            Log.e("분기", "${mCodeBlock.value!![IR].address}로!")
                                            IR = mCodeBlock.value!![IR].address
                                        }
                                    }

                                    6 -> { //나무
                                        if (mPrincess.branchCnt < 2) {
                                            Log.e("분기", "${mCodeBlock.value!![IR].address}로!")
                                            IR = mCodeBlock.value!![IR].address
                                        }
                                    }
                                    else -> {

                                    }
                                }
                            }

                            else if (mCodeBlock.value!![IR].type == 4) {
                                jumpTo = mCodeBlock.value!![IR].address
                                when (mCodeBlock.value!![IR].argument) {
                                    // TODO : 3번 블록 (boolean형 반환 함수) 중 while에 들어간 블록
                                    //  예)
                                    //  argument -> {
                                    //      if (...) {
                                    //          IR = jumpTo
                                    //          iterator++
                                    //      }
                                    //      else {
                                    //      }
                                    //  }
                                    7 -> {   // isAlive
                                        if (!mMonster!!.isAlive()) {
                                            IR = jumpTo
                                            Log.e("죽었네!", "$jumpTo 로!")
                                            metBoss.postValue(false)
                                            iterator = 0
                                        } else {
                                            iterator++
                                        }
                                    }
                                }
                            }
                        }
                    }
                    sleep(speed)
                    nowTerminated.postValue(turnOff)
                    princessAction.postValue(0)
                    monsterAttacked.postValue(false)
                    IR++  // PC
                }
                moveView.postValue(-3)
            } catch (e: IndexOutOfBoundsException) {
                return
            }
        }
    }
}