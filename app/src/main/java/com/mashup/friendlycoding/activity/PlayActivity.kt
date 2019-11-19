package com.mashup.friendlycoding.activity


import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mashup.friendlycoding.R
import com.mashup.friendlycoding.adapter.CodeBlockAdapter
import com.mashup.friendlycoding.adapter.InputCodeBlockAdapter
import com.mashup.friendlycoding.databinding.ActivityPlayBinding
import com.mashup.friendlycoding.model.CodeBlock
import com.mashup.friendlycoding.viewmodel.*
import kotlinx.android.synthetic.main.activity_play.*
import java.lang.Thread.sleep

class PlayActivity : BaseActivity() {
    private var mPrincessViewModel = PrincessViewModel()
    private val mCodeBlockViewModel = CodeBlockViewModel()
    private val mMapSettingViewModel = MapSettingViewModel()
    private var mBattleViewModel: BattleViewModel? = null
    private val mRun = mCodeBlockViewModel.mRun
    private lateinit var layoutMainView: View
    private var itemNumber: Int = 0
    private var mp: MediaPlayer? = null
    private val mStageViewModel = StageViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityPlayBinding>(
            this,
            R.layout.activity_play
        )

        binding.lifecycleOwner = this
        mp = MediaPlayer.create(this, R.raw.stage2)
        mp!!.isLooping = true
        mp!!.start()

        layoutMainView = this.findViewById(R.id.constraintLayout)

        // Princess View Model과 bind
        binding.princessVM = mPrincessViewModel

        // Code Block View Model과 bind
        binding.codeBlockVM = mCodeBlockViewModel
        mRun.init()

        // 현재 몇 스테이지인지?
        val stageNum = intent.getIntExtra("stageNum", 0)

        if (stageNum == 11) {

            val intent = Intent(this, StoryActivity::class.java)
            startActivity(intent)

        }

        // Map Setting View Model과 bind 후 stageInfo 얻어오기
        binding.mapSettingVM = mMapSettingViewModel

        val stageInfo = mMapSettingViewModel.mMapSettingModel.getStageInfo(stageNum)
        val princessInfo = mPrincessViewModel.mMapSettingModel.getStageInfo(stageNum)
        val pcodeInfo = mCodeBlockViewModel.mMapSettingModel.getStageInfo(stageNum)
        mMapSettingViewModel.setStage(stageInfo, this)
        mPrincessViewModel.setPrincessImage(princessInfo, this)
        mCodeBlockViewModel.setSettingModel(pcodeInfo)

        mRun.mMap = stageInfo.map
        mRun.mPrincess = stageInfo.princess

        // stageNum 20 넘을 때 visible로 변경
        if (stageNum / 10 > 1) {
            binding.tvCount.isVisible = true
            binding.tvCountSet.isVisible = true
            binding.tvState.isVisible = true
            binding.tvStateSet.isVisible = true
        }

        when (stageNum) {
            21 -> {
                binding.tvCount.text = "Count = "
                binding.tvCountSet.text = "${mRun.mPrincess.branchCnt}"
                binding.tvState.text = "Book = "
                binding.tvStateSet.text = "${mRun.mPrincess.isBook}"
            }
            22 -> {
                binding.tvCount.text = "Count = "
                binding.tvCountSet.text = "${mRun.mPrincess.mushroomCnt}"
                binding.tvState.text = "Mushroom = "
                binding.tvStateSet.text = "${mRun.mPrincess.isMushroom}"
            }
            23 -> {
                binding.tvCount.text = "Count = "
                binding.tvCountSet.text = "${mRun.mPrincess.branchCnt}"
                binding.tvState.text = "Branch = "
                binding.tvStateSet.text = "${mRun.mPrincess.isBranch}"
            }
        }

        // 코드 블록의 리사이클러 뷰 연결
        val mAdapter = CodeBlockAdapter(this, mRun.mCodeBlock.value!!, mCodeBlockViewModel)
        val linearLayoutManager = LinearLayoutManager(this)
        rc_code_block_list.adapter = mAdapter
        rc_code_block_list.layoutManager = linearLayoutManager

        // 입력될 블록의 리사이클러 뷰 연결
        val mInputdapter =
            InputCodeBlockAdapter(mCodeBlockViewModel, mMapSettingViewModel.offeredBlock)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rc_input_code.adapter = mInputdapter
        rc_input_code.layoutManager = layoutManager

        bossField.isVisible = false

        // 코드 블록의 추가
        mRun.mCodeBlock.observe(this, Observer<List<CodeBlock>> {
            Log.e("추가됨", " ")
            if (mRun.mCodeBlock.value!!.size > 0) {
                rc_code_block_list.smoothScrollToPosition(mRun.mCodeBlock.value!!.size - 1)
                mAdapter.notifyItemChanged(mRun.mCodeBlock.value!!.size - 1)
            } else {
                mAdapter.notifyDataSetChanged()
            }
        })

        // 코드 실행 - 객체의 움직임 - View Model 호출
        mRun.moveView.observe(this, Observer<Int> { t ->
            when (t) {
                -2 -> {
                    rc_code_block_list.smoothScrollToPosition(0)
                    Log.e("실행 중", "위로")
                    // 실행 중에는 코드를 수정할 수 없다
                    clickableControl(false, mAdapter, mInputdapter)
                }
                -3 -> {
                    Log.e("실행 끝", "위로")
                    // 실행이 끝났으니 코드를 다시 수정가능하게 하자
                    clickableControl(true, mAdapter, mInputdapter)
                    mCodeBlockViewModel.coloringNowTerminated(
                        linearLayoutManager.findViewByPosition(
                            mRun.nowProcessing.value!!
                        )
                    )
                }

                -4 -> {
                    Toast.makeText(this, "무한 루프", Toast.LENGTH_SHORT).show()
                    Log.e("실행 끝", "위로")
                    clickableControl(true, mAdapter, mInputdapter)
                    mCodeBlockViewModel.coloringNowTerminated(
                        linearLayoutManager.findViewByPosition(
                            mRun.nowProcessing.value!!
                        )
                    )
                }

                -5 -> {
                    Log.e("compile", "error")
                    Toast.makeText(this, "컴파일 에러", Toast.LENGTH_SHORT).show()
                }

                -6 -> {
                    Toast.makeText(this, "보스에게 패배하였습니다.", Toast.LENGTH_SHORT).show()
                    boss.text = "보스"
                    constraintLayout.isVisible = true
                    bossField.isVisible = false
                    mCodeBlockViewModel.clearBlock()
                    mPrincessViewModel.clear()
                    clickableControl(true, mAdapter, mInputdapter)
                }

                6 -> {  // 곡괭이의 습득
                    itemNumber = resources.getIdentifier(
                        "item_" + mRun.changingView.toString(),
                        "id",
                        packageName
                    )
                    Log.e("습득된 아이템", "item_" + mRun.changingView.toString())
                    findViewById<ImageView>(itemNumber).isVisible = false
                    Log.e("좌표를알아보자", "${mRun.changingViewAll}")
                    if ((mRun.changingViewAll) % 10 == 4) {
                        binding.tvCountSet.setText("${mRun.mPrincess.mushroomCnt}")
                        binding.tvStateSet.setText("${mRun.mPrincess.isMushroom}")
                        if (mRun.mPrincess.mushroomCnt == 2) {
                            mPrincessViewModel.clear()
                            mRun.moveView.postValue(7)
                            binding.tvWin.isVisible = true
                        }
                    } else if ((mRun.changingViewAll) % 10 == 5) {
                        Log.e("책", "먹음")
                        binding.tvCountSet.setText("${mRun.mPrincess.bookCnt}")
                        binding.tvStateSet.setText("${mRun.mPrincess.isBook}")
                        mPrincessViewModel.clear()
                        mRun.moveView.postValue(7)
                        binding.tvWin.isVisible = true
                    } else if ((mRun.changingViewAll) % 10 == 6) {
                        Log.e("브랜치", "먹음")
                        binding.tvCountSet.setText("${mRun.mPrincess.branchCnt}")
                        binding.tvStateSet.setText("${mRun.mPrincess.isBranch}")
                        if (mRun.mPrincess.branchCnt == 2) {
                            mPrincessViewModel.clear()
                            mRun.moveView.postValue(7)
                            binding.tvWin.isVisible = true
                            if(mStageViewModel.check == 1){
                                mStageViewModel.check = 2
                            }
                        }
                    }
                }

                7 -> {  // 패배
                    mCodeBlockViewModel.clearBlock()
                    mPrincessViewModel.clear()
                    mAdapter.notifyDataSetChanged()
                }

                8 -> {  // 승리
                    binding.tvWin.isVisible = true
                }
                9 -> {  // 종료
                    finish()
                }

                else -> {
                    // 안 먹힘!
//                    if (t == -1) {
//                        Log.e("블록", "캐시 초기화")
//                        rc_code_block_list.recycledViewPool.clear()
//                    }
                    mPrincessViewModel.move(t)
                }
            }
        })
        // 코드 실행 - 현재 실행 중인 블록의 배경 색칠하기
        mRun.nowProcessing.observe(this, Observer<Int> { t ->
            mCodeBlockViewModel.coloringNowProcessing(linearLayoutManager.findViewByPosition(t))
            //if (t > 8)
            //rc_code_block_list.smoothScrollToPosition(t + 3)
        })

        // 코드 실행 - 현재 실행이 끝난 블록의 배경 끄기
        mRun.nowTerminated.observe(this, Observer<Int> { t ->
            mCodeBlockViewModel.coloringNowTerminated(linearLayoutManager.findViewByPosition(t))
        })

        // 코드 추가 - 블록이 삽입될 시
        mRun.insertBlockAt.observe(this, Observer<Int> { t ->
            if (t != -1) {
                Log.e("블록 삽입됨", "$t 에 ${mRun.insertedBlock}")
                //mCodeBlockViewModel.insertBlock(linearLayoutManager.findViewByPosition(t), mRun.insertedBlock!!)
                mAdapter.notifyItemChanged(t)
            }
        })

        mRun.princessAction.observe(this, Observer<Int> { t ->
            if (t == -1) {
                binding.shield.isVisible = false
            } else {
                when (t) {
                    0 -> {
                        binding.shield.setImageResource(
                            resources.getIdentifier(
                                "fire_shield",
                                "drawable",
                                packageName
                            )
                        )
                    }
                    1 -> {
                        binding.shield.setImageResource(
                            resources.getIdentifier(
                                "ice_shield",
                                "drawable",
                                packageName
                            )
                        )
                    }
                }
                binding.shield.isVisible = true
            }
        })

        mRun.monsterAttack.observe(this, Observer<Int> { t ->
            when (t) {
                0 -> {
                    binding.attackFire.isVisible = true
                }
                1 -> {
                    binding.attackIce.isVisible = true
                }
                -1 -> {
                    Log.e("몬스터", "공격 끌게요!")
                    binding.attackFire.isVisible = false
                    binding.attackIce.isVisible = false
                }
            }
        })

        // 공주가 보스를 만남
        mRun.metBoss.observe(this, Observer<Boolean> { t ->
            // 보스를 만났거나 만나지 않았을 때 뷰의 전환
            // 보스의 의미 : 함수의 호출이므로 사실 실제 앱에서는 clearBlock과 clear를 하면 안 된다.
            // 함수가 정상적으로 처리되면 (보스를 이기면) 원래의 맵으로 돌아오고, 그 다음 코드를 실행해야 한다.
            boss.text = if (t) "OFF" else "보스"
            constraintLayout.isVisible = !t
            bossField.isVisible = t
            mCodeBlockViewModel.clearBlock()
            mPrincessViewModel.clear()
            clickableControl(true, mAdapter, mInputdapter)

            if (t) {
                mBattleViewModel = BattleViewModel(
                    binding.hpBar,
                    binding.princess,
                    binding.monster,
                    binding.princessAttackMotion
                )
                mBattleViewModel!!.mRun = mRun
                mBattleViewModel!!.init()
                mRun.mMonster = stageInfo.monster
                binding.battleVM = mBattleViewModel
                //Toast.makeText(this, "보스를 만났어요", Toast.LENGTH_SHORT).show()
                rc_input_code.adapter = InputCodeBlockAdapter(
                    mCodeBlockViewModel,
                    mMapSettingViewModel.bossBattleBlock!!
                )
                rc_input_code.layoutManager = layoutManager
            } else {
                mBattleViewModel = null
                binding.battleVM = null
                //Toast.makeText(this, "보스를 물리쳤어요", Toast.LENGTH_SHORT).show()
                rc_input_code.adapter =
                    InputCodeBlockAdapter(mCodeBlockViewModel, mMapSettingViewModel.offeredBlock)
            }
            rc_input_code.layoutManager = layoutManager
        })

        mRun.monsterAttacked.observe(this, Observer<Boolean> { t ->
            if (t) {
                Log.e("공주의 공격!", "현재 HP : ${mRun.mMonster!!.getHP()}")
                mBattleViewModel!!.monsterAttacked()
            } else {
                princess_attack_motion.isVisible = false
            }
        })
    }



//    override fun onPause() {
//        super.onPause()
//        mp!!.stop()
//    }
//    override fun onStop() {
//        super.onStop()
//        mp!!.stop()
//
//    }

    override fun onDestroy() {
        super.onDestroy()
        mp!!.stop()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        Log.e("Layout Width - ", "Width" + (layoutMainView.width))
        mPrincessViewModel.setViewSize(layoutMainView.width)
        mMapSettingViewModel.setViewSize(layoutMainView.width)
    }

    private fun clickableControl(
        able: Boolean,
        codeBlockAdapter: CodeBlockAdapter,
        mInputCodeBlockAdapter: InputCodeBlockAdapter
    ) {
        codeBlockAdapter.clickable = able
        mInputCodeBlockAdapter.clickable = able
    }
}