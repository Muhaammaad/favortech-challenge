package com.muhaammaad.challenge.ui.home.viewmodel

import android.app.Application
import androidx.databinding.ObservableArrayMap
import androidx.databinding.ObservableBoolean
import com.muhaammaad.challenge.base.BaseViewModel
import com.muhaammaad.challenge.database.repo.ImagesRepository
import com.muhaammaad.challenge.model.PictureDetail
import com.muhaammaad.challenge.model.unsplashResponse
import com.muhaammaad.challenge.network.NetworkApi
import com.muhaammaad.challenge.util.ApiConstants
import com.muhaammaad.challenge.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import javax.inject.Inject


class HomeViewModel(application: Application) : BaseViewModel(application) {
    @Inject
    lateinit var BackEndApi: NetworkApi
    private lateinit var subscription: Disposable

    val mImagesLoading: SingleLiveEvent<String> = SingleLiveEvent()
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
        subscription = BackEndApi.getPhotos(url)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrievePhotosListStart() }
            .subscribe(
                { result -> onRetrievePhotosListSuccess(result) },
                { onRetrievePhotosListError() }
            )
    }

    private fun onRetrievePhotosListStart() {
        progressDialog?.set(true)
        mImagesLoading.value = "Loading More Pictures..."
    }

    private fun onRetrievePhotosListError() {
        progressDialog?.set(false)
        processingNewItem = false
        mImagesLoading.value = "Network Error..."
    }

    private fun onRetrievePhotosListSuccess(result: List<unsplashResponse>) {
        progressDialog?.set(false)
        try {
            val tempMap = HashMap<String, PictureDetail>()
            result.forEach {
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

            if (tempMap.isEmpty()) {
                /***
                 * Means received data was cached in database and shown to user
                 */
                pageCounter++
                getPictureListData()
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }
}