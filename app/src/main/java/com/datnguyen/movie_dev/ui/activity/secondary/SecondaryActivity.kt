package com.datnguyen.movie_dev.ui.activity.secondary

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.datnguyen.movie_dev.R
import com.datnguyen.movie_dev.databinding.ActivitySecondaryBinding
import com.datnguyen.movie_dev.ui.base.BaseActivity
import com.datnguyen.movie_dev.ui.base.BaseViewModel
import com.datnguyen.movie_dev.ui.fragment.details.DetailsFragment
import com.datnguyen.movie_dev.ui.fragment.search.SearchFragment
import com.datnguyen.movie_dev.ui.fragment.signin.SignInFragment

class SecondaryActivity : BaseActivity() {
    companion object {
        val KEY_BUNDLE = "KEY_BUNDLE"
        val KEY_SCREEN = "KEY_SCREEN"

        fun createIntent(context: Context): Intent {
            return Intent(context, SecondaryActivity::class.java)
        }
    }

    private val binding: ActivitySecondaryBinding get() = (_binding as ActivitySecondaryBinding?)!!

    private var bundle: Bundle? = null
    private var screen: String? = null

    override fun loadPassedParamsIfNeeded(extras: Bundle) {
        super.loadPassedParamsIfNeeded(extras)
        bundle = extras.getBundle(KEY_BUNDLE)
        screen = extras.getString(KEY_SCREEN)
    }

    override fun setLayoutXml(): Int {
        return R.layout.activity_secondary
    }

    override fun initUI() {
        //create the intent follow screen key
        val intent = when (screen) {
            SearchFragment::class.java.name -> SearchFragment.createIntent()
            DetailsFragment::class.java.name -> DetailsFragment.createIntent()
            SignInFragment::class.java.name -> SignInFragment.createIntent()
            else -> null
        }

        //set fragment into the FrameLayout
        bundle?.let {
            intent?.arguments = it
        }
        intent?.let {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.slide_from_right,
                R.anim.slide_to_left,
                R.anim.slide_from_left,
                R.anim.slide_to_right
            )
            transaction.replace(R.id.content_fragment, intent, screen)
            transaction.addToBackStack(screen)
            transaction.commit()
        }
    }

    override fun initViewModel(){

    }

    override fun initData() {

    }

    override fun initListener() {
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    /**
     * Back pressed
     */
    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val count = supportFragmentManager.backStackEntryCount
            if (count <= 1) {
                finish()
            } else {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }
}