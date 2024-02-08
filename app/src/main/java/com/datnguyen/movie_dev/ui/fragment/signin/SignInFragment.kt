package com.datnguyen.movie_dev.ui.fragment.signin

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.datnguyen.movie_dev.R
import com.datnguyen.movie_dev.databinding.FragmentSignInBinding
import com.datnguyen.movie_dev.extras.Constants
import com.datnguyen.movie_dev.ui.base.BaseFragment
import com.datnguyen.movie_dev.ui.base.BaseViewModel
import com.datnguyen.movie_dev.utils.Utils
import org.greenrobot.eventbus.EventBus

class SignInFragment : BaseFragment() {

    private lateinit var signInViewModel: SignInViewModel

    private val binding: FragmentSignInBinding get() = (_binding as FragmentSignInBinding?)!!

    companion object {
        fun createIntent(): SignInFragment {
            return SignInFragment()
        }
    }

    override fun setLayoutXml(): Int {
        return R.layout.fragment_sign_in
    }

    override fun initUI() {

    }

    override fun initViewModel() {
        signInViewModel = ViewModelProvider(this)[SignInViewModel::class.java]

        binding.viewModel = signInViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //handle loading layout
        signInViewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it) {
                showProgress()
            } else {
                hideProgress()
            }
        })

        //handle error
        signInViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        //handle success
        signInViewModel.loginSuccess.observe(viewLifecycleOwner) {
            //post event to update profile info on screens
            EventBus.getDefault().post(Constants.EVENT_BUS_PROFILE_UPDATE)

            //go back
            activity?.finish()
        }

        //handle username, password
        signInViewModel.username.observe(viewLifecycleOwner) {
            checkDisplaySignInButton()
        }
        signInViewModel.password.observe(viewLifecycleOwner) {
            checkDisplaySignInButton()
        }
    }

    override fun initData() {

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initListener() {
        //hide keyboard when touch outside or on recycle view
        binding.parent.setOnTouchListener { v, event ->
            activity?.let { Utils.hideSoftKeyboard(it) }
            false
        }

        //handle sign in button
        binding.btnSignIn.setOnClickListener {
            signInViewModel.login()
            Utils.hideSoftKeyboard(activity)
        }

        //handle sign up button
        binding.btnSignUp.setOnClickListener {
            //open website
            Utils.openWebPage(Constants.signUpUrl, context)
            Utils.hideSoftKeyboard(activity)
        }

        //handle forgot pw button
        binding.btnForgotPw.setOnClickListener {
            //open website
            Utils.openWebPage(Constants.resetPasswordUrl, context)
            Utils.hideSoftKeyboard(activity)
        }
    }

    private fun checkDisplaySignInButton() {
        if (signInViewModel.username.value?.isNotEmpty() == true && signInViewModel.password.value?.isNotEmpty() == true) {
            binding.btnSignIn.alpha = 1.0f
            binding.btnSignIn.isEnabled = true
        } else {
            binding.btnSignIn.alpha = 0.5f
            binding.btnSignIn.isEnabled = false
        }
    }
}