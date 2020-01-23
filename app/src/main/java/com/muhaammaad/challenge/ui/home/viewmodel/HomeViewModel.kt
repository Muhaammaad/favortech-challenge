package com.muhaammaad.challenge.ui.home.viewmodel

import android.app.Application
import androidx.databinding.ObservableArrayMap
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel
import com.muhaammaad.challenge.database.repo.ImagesRepository
import com.muhaammaad.challenge.model.PictureDetail
import com.muhaammaad.challenge.model.unsplashResponse
import com.muhaammaad.challenge.network.BackEndApi
import com.muhaammaad.challenge.network.WebServiceClient
import com.muhaammaad.challenge.util.ApiConstants
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeViewModel(application: Application) : AndroidViewModel(application), Callback<List<unsplashResponse>> {

    var mPictureDetailsList = ObservableArrayMap<String, PictureDetail>()
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
        val tempMap = HashMap<String, PictureDetail>()
        ImagesRepository.getRepositoryInstance(getApplication()).getimages().forEach {
            val singlePictureDetail = PictureDetail(
                it.photoId,
                it.thumbEncodedBytes,
                it.userName
            )
            tempMap.put(it.photoId!!, singlePictureDetail)
        }
        mPictureDetailsList.putAll(tempMap)
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
        try {
            val tempMap = HashMap<String, PictureDetail>()
            response?.body()!!.forEach {
                if (!mPictureDetailsList.containsKey(it.photoId)) {

                    val singlePictureDetail = PictureDetail(
                        it.photoId,
                        it.likes,
                        it.links?.download,
                        it.urls?.regular,
                        it.user?.name,
                        it.user?.username,
                        it.color,
                        it.user?.profileImage?.small
                    )
                    tempMap.put(it.photoId!!, singlePictureDetail)
                }
            }
            mPictureDetailsList.putAll(tempMap)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }


}