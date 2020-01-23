package com.muhaammaad.challenge.ui.photoDetail


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.muhaammaad.challenge.R
import com.muhaammaad.challenge.databinding.ActivityPictureDetailScreenBinding
import com.muhaammaad.challenge.model.PictureDetail
import com.muhaammaad.challenge.ui.photoDetail.viewmodel.PhotoDetailViewModel
import com.muhaammaad.challenge.util.ApiConstants

class PictureDetailActivity : AppCompatActivity() {

    private var binding: ActivityPictureDetailScreenBinding? = null
    var viewmodel: PhotoDetailViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_picture_detail_screen)
        viewmodel = ViewModelProviders.of(this).get(PhotoDetailViewModel::class.java)
        binding?.viewmodel = viewmodel
        viewmodel?.mCurrentPictureDetail = intent.getParcelableExtra<PictureDetail>(ApiConstants.PICTURE_DATA_KEY)
        binding?.executePendingBindings()
    }
}

