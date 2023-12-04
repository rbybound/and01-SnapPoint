package com.boostcampwm2023.snappoint.presentation.main.subscription

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.boostcampwm2023.snappoint.R
import com.boostcampwm2023.snappoint.data.local.entity.LocalBlock
import com.boostcampwm2023.snappoint.data.local.entity.LocalPost
import com.boostcampwm2023.snappoint.databinding.FragmentSubscriptionBinding
import com.boostcampwm2023.snappoint.presentation.base.BaseFragment
import com.boostcampwm2023.snappoint.presentation.createpost.CreatePostActivity

class SubscriptionFragment : BaseFragment<FragmentSubscriptionBinding>(R.layout.fragment_subscription) {

    private val viewModel: SubscriptionViewModel by viewModels()

    private val post: LocalPost = LocalPost(
        title = "TITLE",
        author = "AUTHOR",
        timestamp = "TIMESTAMP",
        listOf(
            LocalBlock(
                typeOrdinal = 1,
                content = "CONTENT",
                description = "DESCRIPTION"
            )
        )
    )
    private var count: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()
    }

    private fun initBinding() {
        with(binding) {
            btnCreatePost.setOnClickListener {
                val intent = Intent(requireContext(), CreatePostActivity::class.java)
                startActivity(intent)
            }
            btnInsertLocalPost.setOnClickListener {
                viewModel.insertPost(
                    post.copy(title = "Title${count++}")
                )
            }
        }
    }
}