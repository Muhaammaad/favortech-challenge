package com.muhaammaad.challenge.ui.photoDetail

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide


import com.muhaammaad.challenge.R
import com.muhaammaad.challenge.databinding.ActivityPictureDetailScreenBinding
import com.muhaammaad.challenge.model.PictureDetail
import com.muhaammaad.challenge.ui.photoDetail.viewmodel.PhotoDetailViewModel
import com.muhaammaad.challenge.util.ApiConstants
import com.muhaammaad.challenge.util.Util

class PictureDetailActivity : AppCompatActivity() {

    var binding: ActivityPictureDetailScreenBinding? = null
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


@BindingAdapter("fullImage")
fun loadImage(
    view: ImageView,
    mCurrentPictureDetail: PictureDetail?
) {
    if (mCurrentPictureDetail?.mPhotoBytes != null) {
        view.setImageBitmap(Util.getBitmapWithByteArray(mCurrentPictureDetail.mPhotoBytes!!))
    } else {
        Glide.with(view.context).load(mCurrentPictureDetail?.photoThumbnailUrl).into(view)
    }
}