package com.boostcampwm2023.snappoint.presentation.main.around

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boostcampwm2023.snappoint.databinding.ItemAroundPostBinding
import com.boostcampwm2023.snappoint.presentation.model.PostSummaryState

class PostListAdapter(
    private val onPreviewButtonClicked: (Int) -> Unit,
    private val onViewPostButtonClicked: (Int) -> Unit,
) : ListAdapter<PostSummaryState, PostItemViewHolder>(diffUtil) {

    private var expandedIndexSet: MutableSet<Int> = mutableSetOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PostItemViewHolder(
            binding = ItemAroundPostBinding.inflate(inflater, parent, false),
            onExpandButtonClicked = { index ->
                if (expandedIndexSet.contains(index)) expandedIndexSet.remove(index)
                else expandedIndexSet.add(index)
            },
            onPreviewButtonClicked = onPreviewButtonClicked,
            onViewPostButtonClicked = onViewPostButtonClicked
        )
    }

    override fun onBindViewHolder(holder: PostItemViewHolder, position: Int) {
        holder.bind(getItem(position), expandedIndexSet.contains(position))
    }

    override fun onCurrentListChanged(
        previousList: MutableList<PostSummaryState>, currentList: MutableList<PostSummaryState>
    ) {
        super.onCurrentListChanged(previousList, currentList)
        expandedIndexSet.clear()
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<PostSummaryState>() {
            override fun areItemsTheSame(oldItem: PostSummaryState, newItem: PostSummaryState): Boolean {
                return oldItem.uuid == newItem.uuid
            }

            override fun areContentsTheSame(oldItem: PostSummaryState, newItem: PostSummaryState): Boolean {
                return oldItem == newItem
            }

        }
    }
}

@BindingAdapter("posts", "onPreviewButtonClick", "onViewPostButtonClick")
fun RecyclerView.bindRecyclerViewAdapter(posts: List<PostSummaryState>, onPreviewButtonClicked: (Int) -> Unit, onViewPostButtonClicked: (Int) -> Unit) {
    if (adapter == null) adapter = PostListAdapter(onPreviewButtonClicked = onPreviewButtonClicked,
        onViewPostButtonClicked = onViewPostButtonClicked)
    (adapter as PostListAdapter).submitList(posts)
}