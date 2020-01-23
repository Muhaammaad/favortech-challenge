package com.muhaammaad.challenge.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.muhaammaad.challenge.model.PictureDetail
import com.muhaammaad.challenge.ui.home.adapter.PictureListAdapter


@BindingAdapter("app:items")
fun setItems(recyclerView: RecyclerView, items: Map<String, PictureDetail>) {
    val adapter = recyclerView.adapter as? PictureListAdapter
    adapter?.addList(items.values)
}

@BindingAdapter("app:loadImage")
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