package com.boostcampwm2023.snappoint.presentation.viewpost.post

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.boostcampwm2023.snappoint.R
import com.boostcampwm2023.snappoint.databinding.FragmentPostBinding
import com.boostcampwm2023.snappoint.presentation.base.BaseFragment
import com.boostcampwm2023.snappoint.presentation.viewpost.ViewPostViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostFragment : BaseFragment<FragmentPostBinding>(R.layout.fragment_post) {

    private val postViewModel: PostViewModel by viewModels()
    private val viewPostViewModel: ViewPostViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()

        collectViewModelData()
    }

    private fun initBinding() {
        with(binding) {
            vm = postViewModel
            activityVm = viewPostViewModel
        }
    }

    private fun collectViewModelData() {
        lifecycleScope.launch {
            postViewModel.event.collect {
                when (it) {
                    PostEvent.NavigatePrev -> {
                        if (!findNavController().popBackStack()) {
                            viewPostViewModel.finishPostView()
                        }
                    }

                    PostEvent.SavePost -> {
                        saveCurrentPost()
                    }
                }
            }
        }
    }

    private fun saveCurrentPost() {
        with(viewPostViewModel.post.value) {
            postViewModel.saveCurrentPost(
                title,
                author,
                timeStamp,
                postBlocks
            )
        }
    }
}