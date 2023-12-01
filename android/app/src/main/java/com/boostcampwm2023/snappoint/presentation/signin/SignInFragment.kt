package com.boostcampwm2023.snappoint.presentation.signin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.boostcampwm2023.snappoint.R
import com.boostcampwm2023.snappoint.databinding.FragmentSigninBinding
import com.boostcampwm2023.snappoint.presentation.auth.AuthViewModel
import com.boostcampwm2023.snappoint.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSigninBinding>(R.layout.fragment_signin) {

    private val activityViewModel: AuthViewModel by activityViewModels()
    private val viewModel: SignInViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()

        collectViewModelData()
    }

    private fun initBinding() {
        with(binding) {
            vm = viewModel
        }
    }

    private fun collectViewModelData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.event.collect { event ->
                    when (event) {
                        is SignInEvent.Fail -> {
                            showToastMessage(event.error)
                        }

                        is SignInEvent.Success -> {
                            activityViewModel.sendSuccessResult()
                        }

                        is SignInEvent.Signup -> {
                            navigateToSignup()
                        }
                    }
                }
            }
        }
    }

    private fun navigateToSignup() {
        findNavController().run {
            if (currentDestination?.id != R.id.signupFragment) {
                navigate(R.id.action_loginFragment_to_signupFragment)
            }
        }
    }
}