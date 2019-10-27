package com.mashup.friendlycoding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mashup.friendlycoding.adapter.CodeBlockAdapter
import com.mashup.friendlycoding.databinding.ActivityMainBinding
import com.mashup.friendlycoding.repository.CodeBlock
import com.mashup.friendlycoding.viewmodel.CodeBlockViewModel
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.databinding.Observable
import com.mashup.friendlycoding.model.Run
import com.mashup.friendlycoding.viewmodel.MovingViewModel

class MainActivity : AppCompatActivity() {
    private val mCodeBlockViewModel = CodeBlockViewModel()
    private val mMovingViewModel = MovingViewModel()
    private val mRun = mCodeBlockViewModel.getRunModel()

    lateinit var mAdapter : CodeBlockAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        // bind Code Block View Model
        binding.codeBlockVM = mCodeBlockViewModel
        binding.codeBlock = mCodeBlockViewModel.getBlockButton()

        mCodeBlockViewModel.init()

        // bind Moving View Model
        binding.movingVM = mMovingViewModel
        mMovingViewModel.setPrincessImage(binding.ivPrincess)

        //recycler view connects
        mAdapter = CodeBlockAdapter(this, mCodeBlockViewModel.getCodeBlock().value!!)
        val linearLayoutManager = LinearLayoutManager(this)
        rc_code_block_list.layoutManager = linearLayoutManager
        rc_code_block_list.adapter = mAdapter

        mCodeBlockViewModel.getCodeBlock().observe(this,
            Observer<List<CodeBlock>> { mAdapter.notifyDataSetChanged() })

        mRun.getMoving().observe(this, Observer<Int> { t ->
            when (t) {
                0 -> {
                    mMovingViewModel.go()
                    Thread.sleep(100)
                }

                1 -> {
                    mMovingViewModel.rotation()
                    Thread.sleep(100)
                }
            }
        })
    }
}