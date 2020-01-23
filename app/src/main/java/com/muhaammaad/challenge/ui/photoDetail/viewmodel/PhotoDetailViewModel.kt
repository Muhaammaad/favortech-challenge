package com.muhaammaad.challenge.ui.photoDetail.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.muhaammaad.challenge.model.PictureDetail

class PhotoDetailViewModel(application: Application) : AndroidViewModel(application) {
    var mCurrentPictureDetail: PictureDetail? = null
}