package  com.muhaammaad.challenge.ui.home


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

import org.json.JSONException

import com.muhaammaad.challenge.R
import com.muhaammaad.challenge.database.repo.ImagesRepository
import com.muhaammaad.challenge.model.PictureDetail
import com.muhaammaad.challenge.model.unsplashResponse
import com.muhaammaad.challenge.network.BackEndApi
import com.muhaammaad.challenge.network.WebServiceClient
import com.muhaammaad.challenge.ui.home.adapter.PictureListAdapter
import com.muhaammaad.challenge.util.ApiConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PictureListFragment : Fragment(), Callback<List<unsplashResponse>> {


    private var mPictureListAdapter: PictureListAdapter? = null
    private var mGridLayoutManager: GridLayoutManager? = null
    private var mRecyclerView: RecyclerView? = null
    private var mProgressBar: ProgressBar? = null

    //Counter Variable that count the visible item as well as the pageCounter
    var mPictureDetailsList = HashMap<String, PictureDetail>()
    var visibleItemCounter: Int = 0
    val pageOffset: Int = 30
    var totalItemCounter: Int = 0
    var firstVisibleItem: Int = 0
    var pageCounter = 1
    var previousTotalItem = 0
    val visibleThresholdItem = 4
    var processingNewItem = true


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_picture_list, container, false)
        mProgressBar = rootView.findViewById<View>(R.id.progress_bar) as ProgressBar
        mRecyclerView = rootView.findViewById<View>(R.id.recycler_view) as RecyclerView
        getPictureListDataFromDB()
        mPictureListAdapter = PictureListAdapter(activity!!, mPictureDetailsList.values)
        mGridLayoutManager = GridLayoutManager(activity, 1)

        mRecyclerView!!.layoutManager = mGridLayoutManager
        mRecyclerView!!.adapter = mPictureListAdapter

        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                visibleItemCounter = mRecyclerView!!.getChildCount();
                totalItemCounter = mGridLayoutManager!!.getItemCount();
                firstVisibleItem = mGridLayoutManager!!.findFirstVisibleItemPosition();

                if (processingNewItem ?: false) {
                    if (totalItemCounter > previousTotalItem) {
                        processingNewItem = false;
                        previousTotalItem = totalItemCounter
                        pageCounter++
                    }
                }

                if (!processingNewItem &&
                    (totalItemCounter - visibleItemCounter) <= (firstVisibleItem + visibleThresholdItem)
                ) {
                    Snackbar.make(
                        mRecyclerView!!, "Loading More Pictures...",
                        Snackbar.LENGTH_SHORT
                    ).show();
                    getPictureListData()
                    processingNewItem = true
                }
            }
        }
        mRecyclerView?.addOnScrollListener(scrollListener)
        return rootView
    }


    fun getPictureListDataFromDB() {
        ImagesRepository.getRepositoryInstance(context!!).getimages().forEach {
            val singlePictureDetail = PictureDetail(
                it.photoId,
                it.thumbEncodedBytes,
                it.userName
            )
            mPictureDetailsList.put(it.photoId ?: "", singlePictureDetail)
        }
        mPictureListAdapter?.notifyDataSetChanged();
        getPictureListData()
    }

    private fun getPictureListData() {
        val url = ApiConstants.PHOTO_TAG + "?" +
                ApiConstants.API_KEY_TAG + "=" + ApiConstants.API_KEY + "&" +
                ApiConstants.PAGE_OFFSET + "=" + pageCounter +
                "&" + ApiConstants.PER_PAGE_ITEM_OFFSET + "=" + pageOffset;
        mProgressBar!!.visibility = View.VISIBLE
        WebServiceClient.client.create(BackEndApi::class.java).getPhotos(url).enqueue(this)
    }

    override fun onFailure(call: Call<List<unsplashResponse>>?, t: Throwable?) {
        mProgressBar?.setVisibility(View.GONE)
//        processingNewItem = false
        Snackbar.make(
            mRecyclerView!!, "Network error...",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun onResponse(call: Call<List<unsplashResponse>>?, response: Response<List<unsplashResponse>>?) {
        mProgressBar?.setVisibility(View.GONE)
        val positionStart = mPictureDetailsList.size + 1
        try {
            Log.d(TAG, response.toString())
            response?.body()!!.forEach {
                if (!mPictureDetailsList.containsKey(it.id)) {
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
                    mPictureDetailsList.put(it.id ?: "", singlePictureDetail)
                    mPictureListAdapter?.addItem(singlePictureDetail);
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        mPictureListAdapter?.notifyItemRangeInserted(positionStart, mPictureDetailsList.size - positionStart);

    }

    companion object {

        //get the tag of the class
        val TAG = PictureListFragment::class.java.simpleName
    }

}
