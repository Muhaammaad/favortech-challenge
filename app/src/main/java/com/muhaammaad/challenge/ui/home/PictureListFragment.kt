package  com.muhaammaad.challenge.ui.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.muhaammaad.challenge.R
import com.muhaammaad.challenge.databinding.FragmentPictureListBinding
import com.muhaammaad.challenge.ui.home.adapter.PictureListAdapter
import com.muhaammaad.challenge.ui.home.viewmodel.HomeViewModel

class PictureListFragment : Fragment() {

    private var sharedViewModel: HomeViewModel? = null
    private lateinit var mPictureListAdapter: PictureListAdapter
    private lateinit var binding: FragmentPictureListBinding
    private lateinit var mGridLayoutManager: GridLayoutManager

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

        mPictureListAdapter = PictureListAdapter(activity!!, sharedViewModel?.mPictureDetailsList?.values!!)
        mGridLayoutManager = GridLayoutManager(activity, 1)

        binding.recyclerView.layoutManager = mGridLayoutManager
        binding.recyclerView.adapter = mPictureListAdapter

        setScrollListnerToLoadMoreItems()
        initObservers()
        return binding.root
    }

    private fun initObservers() {
        sharedViewModel?.let {
            it.mImagesLoading.observe(this, Observer {
                Snackbar.make(
                    binding.recyclerView, it,
                    Snackbar.LENGTH_SHORT
                ).show()
            })
        }
    }

    private fun setScrollListnerToLoadMoreItems() {
        sharedViewModel?.let {
            val scrollListener = object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    it.visibleItemCounter = binding.recyclerView.childCount;
                    it.totalItemCounter = mGridLayoutManager.itemCount;
                    it.firstVisibleItem = mGridLayoutManager.findFirstVisibleItemPosition();

                    if (it.processingNewItem) {
                        if (it.totalItemCounter > it.previousTotalItem) {
                            it.processingNewItem = false;
                            it.previousTotalItem = it.totalItemCounter
                            it.pageCounter++
                        }
                    }

                    if (!it.processingNewItem &&
                        (it.totalItemCounter - it.visibleItemCounter) <= (it.firstVisibleItem + it.visibleThresholdItem)
                    ) {

                        it.getPictureListData()
                        it.processingNewItem = true
                    }
                }
            }
            binding.recyclerView.addOnScrollListener(scrollListener)
        }
    }
}