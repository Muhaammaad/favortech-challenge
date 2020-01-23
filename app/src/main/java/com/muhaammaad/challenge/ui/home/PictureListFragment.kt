package  com.muhaammaad.challenge.ui.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

import org.json.JSONException

import com.muhaammaad.challenge.R
import com.muhaammaad.challenge.database.repo.ImagesRepository
import com.muhaammaad.challenge.databinding.FragmentPictureListBinding
import com.muhaammaad.challenge.model.PictureDetail
import com.muhaammaad.challenge.model.unsplashResponse
import com.muhaammaad.challenge.network.BackEndApi
import com.muhaammaad.challenge.network.WebServiceClient
import com.muhaammaad.challenge.ui.home.adapter.PictureListAdapter
import com.muhaammaad.challenge.ui.home.viewmodel.HomeViewModel
import com.muhaammaad.challenge.util.ApiConstants
import kotlinx.android.synthetic.main.fragment_picture_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PictureListFragment : Fragment()/*, Callback<List<unsplashResponse>>*/ {


    private var sharedViewModel: HomeViewModel? = null

    //ArrayList of the all picture details

    private lateinit var mPictureListAdapter: PictureListAdapter
    private var mGridLayoutManager: GridLayoutManager? = null
    private lateinit var binding: FragmentPictureListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_picture_list, container, false)
        activity?.let {
            /**
             *  get view model in activity scope
             */
            sharedViewModel = ViewModelProviders.of(it).get(HomeViewModel::class.java)
        }
        binding.viewmodel = sharedViewModel

        mPictureListAdapter = PictureListAdapter(activity!!, sharedViewModel?.mPictureDetailsList!!)
        mGridLayoutManager = GridLayoutManager(activity, 1)

        binding.recyclerView.layoutManager = mGridLayoutManager
        binding.recyclerView.adapter = mPictureListAdapter

        sharedViewModel?.let {
            val scrollListener = object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    it.visibleItemCounter = binding.recyclerView.getChildCount();
                    it.totalItemCounter = mGridLayoutManager!!.getItemCount();
                    it.firstVisibleItem = mGridLayoutManager!!.findFirstVisibleItemPosition();

                    if (it.processingNewItem ?: false) {
                        if (it.totalItemCounter > it.previousTotalItem) {
                            it.processingNewItem = false;
                            it.previousTotalItem = it.totalItemCounter
                            it.pageCounter++
                        }
                    }

                    if (!it.processingNewItem &&
                        (it.totalItemCounter - it.visibleItemCounter) <= (it.firstVisibleItem + it.visibleThresholdItem)
                    ) {
                        Snackbar.make(
                            binding.recyclerView, "Loading More Pictures...",
                            Snackbar.LENGTH_SHORT
                        ).show();
                        it.getPictureListData()
                        it.processingNewItem = true
                    }
                }
            }
            binding.recyclerView.addOnScrollListener(scrollListener)
        }

        return binding.root
    }
}

@BindingAdapter("app:items")
fun setItems(recyclerView: RecyclerView, items: ArrayList<PictureDetail>?) {
    val adapter = recyclerView.adapter as PictureListAdapter
    if (adapter != null && items != null) {
        adapter.updateList(items)
        adapter.notifyDataSetChanged()
    }
}