package com.muhaammaad.challenge.ui.home.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableArrayMap
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.muhaammaad.challenge.database.repo.ImagesRepository
import com.muhaammaad.challenge.model.PictureDetail
import com.muhaammaad.challenge.model.unsplashResponse
import com.muhaammaad.challenge.network.BackEndApi
import com.muhaammaad.challenge.network.WebServiceClient
import com.muhaammaad.challenge.ui.home.PictureListFragment
import com.muhaammaad.challenge.util.ApiConstants
import com.muhaammaad.challenge.util.SingleLiveEvent
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.HashMap


class HomeViewModel(application: Application) : AndroidViewModel(application), Callback<List<unsplashResponse>> {

    var mPictureDetailsList = ObservableArrayList<PictureDetail>()
    var visibleItemCounter: Int = 0
    val pageOffset: Int = 30
    var totalItemCounter: Int = 0
    var firstVisibleItem: Int = 0
    var pageCounter = 1
    var previousTotalItem = 0
    val visibleThresholdItem = 4
    var processingNewItem = true
    var progressDialog: ObservableBoolean? = null

    init {
        getPictureListDataFromDB()
    }

    fun getPictureListDataFromDB() {
        ImagesRepository.getRepositoryInstance(getApplication()).getimages().forEach {
            val singlePictureDetail = PictureDetail(
                it.photoId,
                it.thumbEncodedBytes,
                it.userName
            )
            mPictureDetailsList.add( singlePictureDetail)
        }
        getPictureListData()
    }

    fun getPictureListData() {
        val url = ApiConstants.PHOTO_TAG + "?" +
                ApiConstants.API_KEY_TAG + "=" + ApiConstants.API_KEY + "&" +
                ApiConstants.PAGE_OFFSET + "=" + pageCounter +
                "&" + ApiConstants.PER_PAGE_ITEM_OFFSET + "=" + pageOffset;
        progressDialog?.set(true)
        WebServiceClient.client.create(BackEndApi::class.java).getPhotos(url).enqueue(this)
    }

    override fun onFailure(call: Call<List<unsplashResponse>>?, t: Throwable?) {
        progressDialog?.set(false)
//        processingNewItem = false
//        Snackbar.make(
//            mRecyclerView!!, "Network error...",
//            Snackbar.LENGTH_SHORT
//        ).show()
    }

    override fun onResponse(call: Call<List<unsplashResponse>>?, response: Response<List<unsplashResponse>>?) {
        progressDialog?.set(false)
        val positionStart = mPictureDetailsList.size + 1
        try {
            response?.body()!!.forEach {
//                if (!mPictureDetailsList.containsKey(it.id)) {

                    val singlePictureDetail = PictureDetail(
                        it.id,
                        it.likes,
                        it.links?.download,
                        it.urls?.regular,
                        it.user?.name,
                        it.user?.username,
                        it.color,
                        it.user?.profileImage?.small
                    )
                    mPictureDetailsList.add( singlePictureDetail)
//                    mPictureListAdapter?.addItem(singlePictureDetail);
//                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
//        mPictureListAdapter?.notifyItemRangeInserted(positionStart, mPictureDetailsList.size - positionStart);

    }


}