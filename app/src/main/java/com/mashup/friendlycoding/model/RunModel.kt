package com.mashup.friendlycoding.model

import android.util.Log
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import com.mashup.friendlycoding.Map
import com.mashup.friendlycoding.Monster
import com.mashup.friendlycoding.Princess
import com.mashup.friendlycoding.ignoreBlanks
import java.util.*
import kotlin.collections.ArrayList

class RunModel {
    // PrincessViewModel
    var moveView = MutableLiveData<Int>()    // MainActivity에게 보내는 시그널 - 진행 중 상황. 코드 실행의 시작, 종료, 공주의 움직임 등.

    // CodeBlockViewModel
    var nowProcessing = MutableLiveData<Int>()   // MainActivity에게 보내는 시그널 - 현재 진행 중인 코드 번호
    var nowTerminated = MutableLiveData<Int>()   // MainActiivty에게 보내는 시그널 - 현재 진행 종료된 코드 번호
    var mCodeBlock = MutableLiveData<ArrayList<CodeBlock>>()    // 코드 블록, MainActivity가 보고 뷰의 수정과 스크롤이 일어남
    var insertBlockAt = MutableLiveData<Int>()  // MainActivity에게 보내는 시그널 - 코드 블록이 어디에 삽입될 지를 알려준다.

    // BattleViewModel
    var metBoss = MutableLiveData<Boolean>()  // MainActivity에게 보내는 시그널 - 플레이어가 보스를 만났는지 여부. 만났으면 뷰와 인풋코드블록을 바꾼다.
    var monsterAttack = MutableLiveData<Int>()  // MainActivity에게 보내는 시그널 - 보스의 공격 유형
    var princessAction = MutableLiveData<Int>() // MainActivity에게 보내는 시그널 - 보스전에서의 공주의 행동
    var monsterAttacked = MutableLiveData<Boolean>()    // MainActivity에게 보내는 시그널 - 보스가 공격당했는지 여부

    // ㄴ 사실 시그널 변수가 저렇게 많이 필요하진 않고
    // nowProcessing이나 nowTerminated 를 제외한 모든 MutableLiveData는 moveVIew 에 합칠 수도 있긴 합니다.
    // 하지만 그렇게 되면 PlayActivity에서 observe 하는 부분이 매우 길어지고
    // 그 시그널 넘버도 많아지면 굉장히 복잡하게 될 우려가 있기 때문에
    // 일부러 목적에 맞게 나눠놨습니다.
    // 합치려다가 진짜 엄두가 안 나서 포기한 거라고는 말 못합니다.

    var insertBlockPosition = 0
    var insertedBlock : String? = null   // 삽입된 코드 블록의 이름
    var changingView : String? = null    // 아이템을 주웠거나 아이템이 파괴됐을시 해당 아이템의 ID를 MainActivity에게 알려준다.

    private var jumpTo = 0 // jump 할 주소
    private var IR = 0  // 명령어 실행할 주소
    private var iterator = 0 // 반복자
    private var blockLevel = 0 // 들여쓰기 정도.
    private var bracketStack = Stack<Int>()  // 괄호 체크, 그와 동시에 jump 할 명령어 주소 얻기 위함
    private var tempBracketBuffer = 0   // 괄호가 삭제되거나 할 때를 대비해서 임시로 저장해두는 버퍼.
    private var coc = arrayOf(-1, -1 , -1, -1, -1) // 행동 수칙이 있는가?
    private var isAttacking = false  // 몬스터가 공격 중에 있는지
    private var isBossAlive = false
    private var speed = 500L

    // 공주의 좌표
    private var x = 0 // x좌표
    private var y = 9 // y좌표
    private var d = 1 // 방향 : 0-> 위, 1-> 오른쪽, 2-> 아래, 3-> 왼쪽

    var mPrincess = Princess()
    var mMap = Map()
    var mMonster : Monster? = null

    fun init() {
        mCodeBlock.value = ArrayList()
        insertBlockPosition = -1
        insertBlockAt.postValue(-1)
        metBoss.value = false
        monsterAttack.value = -1
        princessAction.value = 0
    }

    fun clearBlock() {
        x = 0
        y = 9
        d = 1
        iterator = 0
        jumpTo = 0
        blockLevel = 0
        moveView.postValue(-1)
        insertBlockAt.postValue(-1)
        val block = mCodeBlock.value
        block!!.clear()
        mCodeBlock.postValue(block)
        isBossAlive = false
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

    fun collisionCheck() {   // 벽이나 보스와의 충돌 감지
        Log.e("(nowX", "(nowX  $x,,,$y")
        if (x < 10 && x > -1 && y < 10 && y > -1) {
            if (mMap.mapList!![y][x] == 1) {
                moveView.postValue(7)   // 벽이라면 졌다는 시그널 전송
            } else if (mMap.mapList!![y][x] == 2) {
                moveView.postValue(8)    // 이겼다면 이겼다는 시그널 전송
            } else if (y == mMonster?.y && x == mMonster?.x) {
                metBoss.postValue(true)  // 보스를 만나면 보스를 만났다는 시그널 전송
            }
        }

        else {
            moveView.postValue(7)     // 인덱스를 넘어갈 시
        }
    }

    fun changeBlockLevel(OpenOrClose : Boolean) {
        if (!OpenOrClose) {  // 여는 괄호를 삭제함
            blockLevel--
            if (bracketStack.isNotEmpty())
                bracketStack.pop()
        }

        else {
            blockLevel++  // 닫는 괄호를 삭제함
            bracketStack.push(tempBracketBuffer)
        }
    }

    fun addNewBlock(codeBlock: CodeBlock) {
        if (insertBlockPosition != -1) {
            if (codeBlock.type == 3) {
                Log.e("블록을 추가합니다", "${codeBlock.funcName}  ${codeBlock.type}  ${codeBlock.argument}")
                mCodeBlock.value!![insertBlockPosition].argument = codeBlock.argument
                insertedBlock = codeBlock.funcName
                insertBlockAt.postValue(insertBlockPosition)

                if (ignoreBlanks(mCodeBlock.value!![insertBlockPosition].funcName) == "if") {
                    coc[codeBlock.argument] = insertBlockPosition
                }
                insertBlockPosition = -1
                return
            }

            else
                return
        }

        else if (codeBlock.type == 3) {
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
                adding.address = bracketStack.peek() - 10000   // 자신을 연 if문의 주소
            }

            bracketStack.pop()
            Log.e("block level", "decreases")
            blockLevel--
        }

        var tap = ""
        for (i in 0 until blockLevel) {
            tap += "    "
        }

        adding.funcName = tap + adding.funcName

        if (adding.type == 1 || adding.type == 2) {
            tempBracketBuffer =
                if (ignoreBlanks(adding.funcName) == "if") {
                    bracketStack.push(IR + 10000)
                    IR + 10000
                }
                else {
                    bracketStack.push(IR)
                    IR
                }
            blockLevel++
        }

        IR++
        val block = mCodeBlock.value
        block!!.add(adding)
        mCodeBlock.postValue(block)
        insertBlockAt.postValue(-1)
        Log.e("${adding.funcName} ", "${insertBlockAt.value}에 추가됨")
    }

    fun run() {
        val run = RunThead()
        IR = 0
        run.start()
        iterator = 0
    }

    inner class RunThead : Thread() {
        lateinit var view: EditText
        override fun run() {
            try {
                if (!bracketStack.empty()) {
                    moveView.postValue(-5)
                    return
                }

                moveView.postValue(-2)
                sleep(speed)

                while (IR < mCodeBlock.value!!.size) {
                    if (metBoss.value == true && !isAttacking) {
                        if (mMonster!!.isAlive()) {
                            // 몬스터의 차례
                            Log.e("보스와의 배틀!", "!!!")
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
                            }

                            else {
                                Log.e("몬스터", "휴식!")
                            }
                        }
                    }

                    if (iterator > 30) {
                        // 이런 게임 깨는데 루프를 30번 넘게 돌진 않겠지?
                        moveView.postValue(-4)
                        return
                    }

                    nowProcessing.postValue(IR)
                    Log.e("실행 중 : ", mCodeBlock.value!![IR].funcName + " ${mCodeBlock.value!![IR].type}")

                    when (ignoreBlanks(mCodeBlock.value!![IR].funcName)) {
                        "move" -> {
                            movePrincess()
                            moveView.postValue(d)
                            collisionCheck()
                            Log.e("갑니다", "앞으로")
                            sleep(speed)
                        }

                        "turnLeft" -> {
                            //    moveView.value = 1
                            rotate(false)
                            moveView.postValue(4)
                            Log.e("돕니다", "왼쪽으로")
                            sleep(speed)
                        }

                        "turnRight" -> {
                            rotate(true)
                            //  moveView.value = 2
                            moveView.postValue(5)
                            Log.e("돕니다", "오른쪽으로")
                            sleep(speed)
                        }

                        "for" -> {
                            //iterator = mCodeBlock.value!![IR].argument
                            Log.e("반복", "${mCodeBlock.value!![IR].argument}")
                            sleep(speed)
                        }

                        "}" -> {
                            jumpTo = mCodeBlock.value!![IR].address
                            Log.e("jumpTo", "$jumpTo, $iterator to ${mCodeBlock.value!![jumpTo].argument} , ${ignoreBlanks(mCodeBlock.value!![jumpTo].funcName)}")

                            if (ignoreBlanks(mCodeBlock.value!![jumpTo].funcName) == "while") {
                                when (mCodeBlock.value!![jumpTo].argument) {
                                    7 -> {   // isAlive
                                        if (mMonster!!.isAlive()) {
                                            nowTerminated.postValue(IR)
                                            IR = jumpTo
                                            Log.e("아직 안 죽었네", "$jumpTo 로!")
                                            iterator++
                                        }

                                        else {
                                            iterator = 0
                                            metBoss.postValue(false)
                                        }
                                    }
                                }
                            }

                            else if (mMonster != null && ignoreBlanks(mCodeBlock.value!![jumpTo].funcName) == "if") {
                                if (isAttacking && mCodeBlock.value!![jumpTo].argument == mMonster!!.attackType) {
                                    princessAction.postValue(0)
                                    Log.e("몬스터", "공격 종료!")
                                    monsterAttack.postValue(-1)
                                    isAttacking = false
                                }
                            }

                            else if (mCodeBlock.value!![jumpTo].type == 1) {
                                Log.e("for 가 날 열었어", "${mCodeBlock.value!![jumpTo].argument}")
                                if (iterator + 1 < mCodeBlock.value!![jumpTo].argument) {
                                    nowTerminated.postValue(IR)
                                    IR = jumpTo
                                    Log.e("한 번 더!", "${mCodeBlock.value!![jumpTo].argument}   ${mCodeBlock.value!![IR].funcName}")
                                    iterator++
                                } else {
                                    iterator = 0
                                }
                            }
                            sleep(speed)
                        }

                        "if" -> {
                            when (mCodeBlock.value!![IR].argument) {
                                0 -> {
                                    if (mMonster != null) {
                                        if (isAttacking && mMonster!!.attackType == mCodeBlock.value!![IR].argument) {
                                            Log.e("막았다!", "${mCodeBlock.value!![jumpTo].argument} 공격")
                                            princessAction.postValue(9)
                                        }
                                        else {
                                            nowTerminated.postValue(IR)
                                            IR = mCodeBlock.value!![IR].address
                                        }
                                    }
                                }

                                1 -> {
                                    if (mMonster != null) {
                                        if (isAttacking && mMonster!!.attackType == mCodeBlock.value!![IR].argument) {
                                            Log.e("막았다!", "${mCodeBlock.value!![jumpTo].argument} 공격")
                                            princessAction.postValue(9)
                                        }
                                        else {
                                            nowTerminated.postValue(IR)
                                            IR = mCodeBlock.value!![IR].address
                                        }
                                    }
                                }

                                3 -> {  // 곡괭이
                                    if (!mPrincess.isPickAxe) {
                                        Log.e("분기", "${mCodeBlock.value!![IR].address}로!")
                                        nowTerminated.postValue(IR)
                                        IR = mCodeBlock.value!![IR].address
                                    }
                                }

                                4 -> { //버섯
                                    if(mPrincess.eatMushroom().toString() < "2"){
                                        Log.e("분기","${mCodeBlock.value!![IR].address}로!")
                                        nowTerminated.postValue(IR)
                                        IR = mCodeBlock.value!![IR].address
                                    }
                                }

                                5 -> { //책
                                    if(!mPrincess.isBook){
                                        Log.e("분기","${mCodeBlock.value!![IR].address}로!")
                                        nowProcessing.postValue(IR)
                                        IR = mCodeBlock.value!![IR].address
                                    }
                                }

                                6 -> { //나무
                                    if(mPrincess.pickBranch().toString() < "2"){
                                        Log.e("분기","${mCodeBlock.value!![IR].address}로!")
                                        nowTerminated.postValue(IR)
                                        IR = mCodeBlock.value!![IR].address
                                    }
                                }

                                else -> {

                                }
                            }
                            sleep(speed)
                        }

                        "else" -> {

                        }

                        "pickAxe" -> {
                            if (mMap.mapList!![y][x] == 3) {
                                mPrincess.pickAxe()
                                mMap.pickAxe(y, x)
                                changingView = "i$x$y"    // 아이템 뷰의 아이디는 i + x좌표 + y좌표 이다.
                                moveView.postValue(6)
                            }
                            else {
                                moveView.postValue(-3)
                                return
                            }
                            sleep(speed)
                        }

                        "attack" -> {
                            mMonster?.monsterAttacked(mPrincess.DPS)
                            monsterAttacked.postValue(true)
                            sleep(speed)
                            monsterAttacked.postValue(false)
                        }

                        "eatMushroom" -> {
                            if(mMap.mapList!![x][y] == 4){
                                mPrincess.eatMushroom()
                                mMap.eatMushroom(x,y)
                                changingView = "!$x$y"
                                moveView.postValue(6)
                            }else{
                                moveView.postValue(-3)
                                return
                            }
                            sleep(speed)
                        }

                        "pickBook" -> {
                            if(mMap.mapList!![y][x] == 5){
                                mPrincess.pickBook()
                                mMap.pickBook(x,y)
                                changingView = "i$x$y"
                                moveView.postValue(6)
                            }else{
                                moveView.postValue(-3)
                                return
                            }
                            sleep(speed)
                        }

                        "pickBranch" -> {
                            if(mMap.mapList!![y][x] == 6){
                                mPrincess.pickBranch()
                                mMap.pickBranch(x,y)
                                changingView = "i$x$y"
                                moveView.postValue(6)
                            }else{
                                moveView.postValue(-3)
                                return
                            }
                            sleep(speed)
                        }
                    }

                    nowTerminated.postValue(IR)
                    IR++  // PC
                }
                moveView.postValue(-3)
            } catch (e: IndexOutOfBoundsException) {
                return
            }
        }
    }
}