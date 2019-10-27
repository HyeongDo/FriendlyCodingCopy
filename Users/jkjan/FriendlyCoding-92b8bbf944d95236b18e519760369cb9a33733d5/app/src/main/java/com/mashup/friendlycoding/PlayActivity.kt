package com.mashup.friendlycoding

import android.media.Image
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.mashup.friendlycoding.adapter.CodeBlockAdapter
import com.mashup.friendlycoding.adapter.MonsterHPBarAdapter
import com.mashup.friendlycoding.databinding.ActivityPlayBinding
import com.mashup.friendlycoding.viewmodel.BattleViewModel
import com.mashup.friendlycoding.viewmodel.CodeBlockViewModel
import kotlinx.android.synthetic.main.activity_play.*
import java.util.*

class PlayActivity : AppCompatActivity() {
    private var mBattleViewModel = BattleViewModel()
    private var mCodeBlockViewModel = CodeBlockViewModel()
    private lateinit var mAdapter: CodeBlockAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO : 지훈 형의 MainActivity, CodeBlockViewModel 과 합침
        val binding = DataBindingUtil.setContentView<ActivityPlayBinding>(this, R.layout.activity_play)
        binding.lifecycleOwner = this
        binding.battleVM = mBattleViewModel
        binding.codeBlockVM = mCodeBlockViewModel

        mBattleViewModel.init()

        //recycler view connects
        mAdapter = CodeBlockAdapter(this, mBattleViewModel.getCodeBlock()?.value!!)  // 실제 코드에선 mBattleViewModel.getCodeBlock() 이 아닌 mCodeBlockViewModel.getCodeBlock() 이 되어야 한다.
        val linearLayoutManager = LinearLayoutManager(this)
        rc_code_block_list.layoutManager = linearLayoutManager
        rc_code_block_list.adapter = mAdapter

        //원래는 maxHP를 100으로 두고 hp를 10씩 깎으면서 현재 hp / 10 만큼 View를 생성하려 했으나
        //그러면 한 번 공격 당할 때마다 n번 버튼을 생성하게 되므로
        //RecyclerView를 구현하여 공격당할 때마다 HP의 리스트의 요소를 제거해 나가는 것이 더 효율적이라고 생각했음
        val hpBar = LinearLayoutManager(this)
        hp_bar.layoutManager = hpBar
        hp_bar.adapter = MonsterHPBarAdapter(this, mBattleViewModel.getHP()?.value!!)
        hpBar.orientation = LinearLayoutManager.HORIZONTAL
    }
}