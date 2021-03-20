package com.rk.weather.ui.main.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rk.weather.R
import com.rk.weather.data.db.WeatherDatabase
import com.rk.weather.data.db.entity.BookmarkEntity
import com.rk.weather.databinding.BookmarkFragmentBinding
import com.rk.weather.ui.main.MainActivity
import com.rk.weather.utills.Constants

class BookmarkFragment : Fragment() {

    private lateinit var viewModel: BookmarkViewModel
    lateinit var binding: BookmarkFragmentBinding
    var weatherDatabase: WeatherDatabase? = null
    lateinit var recyclerView: RecyclerView
    lateinit var bookmarksList: MutableList<BookmarkEntity>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bookmark_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        recyclerView = binding.recyclerBookmark
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(BookmarkViewModel::class.java)
        weatherDatabase = context?.let { WeatherDatabase.getDatabase(it) }
        weatherDatabase?.let {
            viewModel.getBookmarkList(it).observe(viewLifecycleOwner, Observer {
                bookmarksList = it
                initBookmarkList(bookmarksList)
            })
        }
    }

    private fun initBookmarkList(list: MutableList<BookmarkEntity>?) {
        val adapter = list?.let {
            BookmarkAdapter(it) {
                val sharedPreferences = (activity as MainActivity).sharedPreferences
                sharedPreferences.edit().putString(Constants.Coords.LAT, it.lat.toString()).apply()
                sharedPreferences.edit().putString(Constants.Coords.LON, it.lon.toString()).apply()
//                findNavController().navigate(R.id.action_bookmarkFragment_to_dashBoardFragment)
                findNavController().popBackStack()
            }
        }
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        val swipeToHandle = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerView.adapter as BookmarkAdapter
                weatherDatabase?.let {
                    viewModel.deleteBookmark(
                        it,
                        bookmarksList[viewHolder.adapterPosition]
                    )
                }
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToHandle)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}