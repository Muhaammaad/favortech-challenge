package com.muhaammaad.challenge.ui.home.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.muhaammaad.challenge.R
import com.muhaammaad.challenge.database.entity.UnsplashImages
import com.muhaammaad.challenge.database.repo.ImagesRepository
import com.muhaammaad.challenge.model.PictureDetail
import com.muhaammaad.challenge.ui.photoDetail.PictureDetailActivity
import com.muhaammaad.challenge.util.ApiConstants
import com.muhaammaad.challenge.util.Util
import com.muhaammaad.challenge.util.XAppExecutors
import java.util.*

class PictureListAdapter// Public Constructor
    (
    /**
     * Context and reference of the arrayList
     */
    private val mContext: Context, pictureDetails: Collection<PictureDetail>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mPictureDetailArrayList = ArrayList<PictureDetail>()

    init {
        mPictureDetailArrayList.addAll(pictureDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PictureListItemHolder(
            LayoutInflater
                .from(mContext).inflate(R.layout.picture_screen_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PictureListItemHolder).bindView(position)
    }

    override fun getItemCount(): Int {
        return mPictureDetailArrayList.size
    }

    fun updateList(values: Collection<PictureDetail>) {
        mPictureDetailArrayList.clear()
        mPictureDetailArrayList.addAll(values)
        this.notifyDataSetChanged()
    }

    fun addList(values: Collection<PictureDetail>) {
        val positionStart = mPictureDetailArrayList.size
        mPictureDetailArrayList.addAll(values)
        this.notifyItemRangeInserted(positionStart + 1, values.size);
    }

    fun addItem(singlePictureDetail: PictureDetail) {
        mPictureDetailArrayList.add(singlePictureDetail)
    }

    private inner class PictureListItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        //get the reference of the all views
        private val mPicture: ImageView = itemView.findViewById<View>(R.id.photo_id) as ImageView
        private var mPictureIndex: Int = 0

        init {
            mPicture.setOnClickListener(this)
        }

        //Bind the view
        fun bindView(position: Int) {
            //index of the picture position
            mPictureIndex = position

            val item = mPictureDetailArrayList.get(mPictureIndex)
            if (item.mPhotoBytes != null) {
                mPicture.setImageBitmap(Util.getBitmapWithByteArray(item.mPhotoBytes!!))
            } else {
                Glide.with(mContext).load(item.photoThumbnailUrl)
                    .asBitmap()
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                            mPicture.setImageBitmap(resource)
                            XAppExecutors.instance.diskIO().execute {
                                ImagesRepository.getRepositoryInstance(mContext).insert(
                                    UnsplashImages(
                                        item.photoId ?: "",
                                        Util.getBitmapBytes(resource),
                                        item.photoAuthorName ?: ""
                                    )
                                )
                            }
                        }
                    })
            }
        }

        override fun onClick(v: View) {
            //Intent for opening large image
            val pictureDetailIntent = Intent(mContext, PictureDetailActivity::class.java)
            pictureDetailIntent.putExtra(
                ApiConstants.PICTURE_DATA_KEY,
                mPictureDetailArrayList.get(mPictureIndex)
            )
            mContext.startActivity(pictureDetailIntent)
        }
    }
}

