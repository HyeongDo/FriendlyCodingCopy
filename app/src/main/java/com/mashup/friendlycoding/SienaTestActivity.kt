package com.mashup.friendlycoding

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mashup.friendlycoding.databinding.ActivitySienaTestBinding
import com.mashup.friendlycoding.viewmodel.SienaTestViewModel


class SienaTestActivity : AppCompatActivity() {

    private var mSienaTestViewModel = SienaTestViewModel()
    lateinit var layoutMainView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivitySienaTestBinding>(
            this,
            R.layout.activity_siena_test
        )
        layoutMainView = this.findViewById(R.id.mainLayout)
        binding.lifecycleOwner = this
        binding.testSiena = mSienaTestViewModel
        mSienaTestViewModel.setPrincessImage(binding.ivPrinsecess)


    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {

        Log.e("Layout Width - ", "Width" + (layoutMainView.width))
        Log.e("Layout Height - ", "height" + (layoutMainView.height))

        mSienaTestViewModel.setViewSize(layoutMainView.width, layoutMainView.height)

    }

}
