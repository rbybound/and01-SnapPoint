package com.boostcampwm2023.snappoint.presentation.subscription

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.boostcampwm2023.snappoint.R
import com.boostcampwm2023.snappoint.databinding.FragmentSubscriptionBinding
import com.boostcampwm2023.snappoint.presentation.base.BaseFragment
import com.boostcampwm2023.snappoint.presentation.createpost.CreatePostActivity
import com.boostcampwm2023.snappoint.presentation.main.MainViewModel

class SubscriptionFragment : BaseFragment<FragmentSubscriptionBinding>(R.layout.fragment_subscription) {

    private val activityViewModel: MainViewModel by activityViewModels()
    private val viewModel: SubscriptionViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()

    }

    private fun initBinding() {
        binding.btnCreatePost.setOnClickListener {
            val intent = Intent(requireContext(), CreatePostActivity::class.java)
            startActivity(intent)
        }
    }
}