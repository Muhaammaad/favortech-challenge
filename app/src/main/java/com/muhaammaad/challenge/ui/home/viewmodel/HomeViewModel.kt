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
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import javax.inject.Inject

/**
 * ViewModel to handle Home view and pictureListFragment
 */
class HomeViewModel(application: Application) : BaseViewModel(application) {

    @Inject
    lateinit var BackEndApi: NetworkApi
    //region Member properties
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
    //endregion
    //region intialiazer
    init {
        getPictureListDataFromDB()
    }
    //endregion
    //region Image data Fetching
    /***
     * fetch images from database
     */
    fun getPictureListDataFromDB() {
        subscription = Observable.fromCallable { getImages() }
            .concatMap { dbList ->
                Observable.just(dbList)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result -> onRetrieveDBPhotosListSuccess(result) }
    }

    /***
     * get images from webservice
     */
    fun getPictureListData() {
        subscription = BackEndApi.getPhotos(getApiUrl())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrievePhotosListStart() }
            .subscribe(
                { result -> onRetrievePhotosListSuccess(result) },
                { onRetrievePhotosListError() }
            )
    }

    /***
     * get unsplash Api url accordingly
     */
    private fun getApiUrl(): String {
        return ApiConstants.PHOTO_TAG + "?" +
                ApiConstants.API_KEY_TAG + "=" + ApiConstants.API_KEY + "&" +
                ApiConstants.PAGE_OFFSET + "=" + pageCounter +
                "&" + ApiConstants.PER_PAGE_ITEM_OFFSET + "=" + pageOffset

    }

    /***
     * Database photos retrieval
     */
    private fun getImages(): List<PictureDetail> {
        val list = ArrayList<PictureDetail>()
        ImagesRepository.getRepositoryInstance(getApplication()).getimages().forEach {
            val singlePictureDetail = PictureDetail(
                it.photoId,
                it.thumbEncodedBytes,
                it.userName
            )
            list.add(singlePictureDetail)
        }
        return list
    }
    //endregion
    //region Callbacks
    /***
     * On Start of unsplash Api photos retrieval
     */
    private fun onRetrievePhotosListStart() {
        setProgressDialog(true)
        mImagesLoading.value = "Loading More Pictures..."
    }

    /***
     * On Failure of unsplash Api photos retrieval
     */
    private fun onRetrievePhotosListError() {
        setProgressDialog(false)
        processingNewItem = false
        mImagesLoading.value = "Network Error..."
    }

    /***
     * On Success of unsplash Api photos retrieval
     */
    private fun onRetrievePhotosListSuccess(result: List<unsplashResponse>) {
        setProgressDialog(false)
        try {
            val tempMap =
                HashMap<String, PictureDetail>() //using temp map so that can putall items and the binding adapter calls once for better ux
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

    /***
     * On Success of photos retrieval from Database
     */
    private fun onRetrieveDBPhotosListSuccess(result: List<PictureDetail>) {
        val tempMap =
            HashMap<String, PictureDetail>()//using temp map so that can putall items and the binding adapter calls once for better ux
        result.forEach {
            tempMap.put(it.photoId!!, it)
        }
        mPictureDetailsList.putAll(tempMap)
        getPictureListData()
    }
    //endregion
    //region general functions
    private fun setProgressDialog(show: Boolean) {
        progressDialog?.set(show)
    }

    /***
     *  when ViewModel is no longer used and will be destroyed
     */
    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }
    //endregion
}