package com.rk.weather.ui.main.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rk.weather.data.db.entity.BookmarkEntity
import com.rk.weather.databinding.ItemBookmarkBinding

class BookmarkAdapter(
    private val items: MutableList<BookmarkEntity>,
    private val callBack: (BookmarkEntity) -> Unit
) : RecyclerView.Adapter<BookmarkAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mBinding =
            ItemBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        mBinding.viewModel = BookmarkViewModel()
        mBinding.rootView.setOnClickListener {
            mBinding.viewModel?.item?.get().let {
                it?.let { it1 -> callBack.invoke(it1) }
            }
        }
        return ViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items.get(position))
    }

    override fun getItemCount(): Int = items.size


    fun removeAt(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    class ViewHolder(val binding: ItemBookmarkBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bookmarkEntity: BookmarkEntity) {
            binding.viewModel?.item?.set(bookmarkEntity)
            binding.executePendingBindings()
        }
    }
}