package com.muhaammaad.challenge.ui.photoDetail.viewmodel

import android.app.Application
import com.muhaammaad.challenge.base.BaseViewModel
import com.muhaammaad.challenge.model.PictureDetail

/**
 * ViewModel to handle PhotoDetail view
 */
class PhotoDetailViewModel(application: Application) : BaseViewModel(application) {
    var mCurrentPictureDetail: PictureDetail? = null
}