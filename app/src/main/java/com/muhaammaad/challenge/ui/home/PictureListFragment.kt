package  com.muhaammaad.challenge.ui.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.muhaammaad.challenge.R
import com.muhaammaad.challenge.databinding.FragmentPictureListBinding
import com.muhaammaad.challenge.model.PictureDetail
import com.muhaammaad.challenge.ui.home.adapter.PictureListAdapter
import com.muhaammaad.challenge.ui.home.viewmodel.HomeViewModel

class PictureListFragment : Fragment()/*, Callback<List<unsplashResponse>>*/ {


    private var sharedViewModel: HomeViewModel? = null
    private lateinit var mPictureListAdapter: PictureListAdapter
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

        mPictureListAdapter = PictureListAdapter(activity!!, sharedViewModel?.mPictureDetailsList?.values!!)
        val gridLayoutManager = GridLayoutManager(activity, 1)

        binding.recyclerView.layoutManager = gridLayoutManager
        binding.recyclerView.adapter = mPictureListAdapter

        sharedViewModel?.let {
            val scrollListener = object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    it.visibleItemCounter = binding.recyclerView.getChildCount();
                    it.totalItemCounter = gridLayoutManager.getItemCount();
                    it.firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

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
fun setItems(recyclerView: RecyclerView, items: Map<String, PictureDetail>) {
    val adapter = recyclerView.adapter as? PictureListAdapter
    adapter?.addList(items.values)
}