package com.datnguyen.movie_dev.ui.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.datnguyen.movie_dev.R
import java.util.LinkedHashSet

abstract class BaseActivity : AppCompatActivity() {
    var _binding: ViewDataBinding? = null

    private var backButtonCount = 0

    //dialog to display the loading progress
    private var setOfDialogs: MutableCollection<Dialog> = LinkedHashSet()

    //show loading progress
    private fun createProgressDialog() {
        val progressDialog = Dialog(this, R.style.DialogFullScreen)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_progress, null)
        //show view
        val view = dialogView.findViewById<View>(R.id.progressBar)
        view.visibility = View.VISIBLE

        progressDialog.setContentView(dialogView)
        progressDialog.setCancelable(false)

        val dialogWindow = progressDialog.window
        dialogWindow?.let {
            dialogWindow.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            dialogWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
        progressDialog.show()

        setOfDialogs.add(progressDialog)
    }

    private fun destroyProgressDialog() {
        for (dialog in setOfDialogs) {
            dialog.dismiss()
        }
    }

    /**
     * Show progress layout
     */
    fun showProgress() {
        createProgressDialog()
    }

    /**
     * Hide progress layout
     */
    fun hideProgress() {
        destroyProgressDialog()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //check and load intent params
        intent?.extras?.let {
            loadPassedParamsIfNeeded(it)
        }

        if (_binding == null) {
            _binding = DataBindingUtil.setContentView(this, setLayoutXml())
        }

        initUI()
        initViewModel()
        initData()
        initListener()
    }

    /**
     * load passed params
     */
    protected open fun loadPassedParamsIfNeeded(extras: Bundle) {

    }

    /**
     * Set xml
     */
    abstract fun setLayoutXml() : Int

    /**
     * Set handler + execute view binding
     */
    abstract fun initUI()

    /**
     * Init view model
     */
    abstract fun initViewModel()

    /**
     * Binding and initialize data into layout
     */
    abstract fun initData()

    /**
     * initialize the listener event
     */
    abstract fun initListener()

    //call on root activity
    fun exitApp() {
        //check exit app
        if (backButtonCount >= 1) {
            finish()
        } else {
            Toast.makeText(this, getString(R.string.exit_app_msg), Toast.LENGTH_SHORT).show()
            backButtonCount++
            Handler(Looper.getMainLooper()).postDelayed({ backButtonCount = 0 }, (3 * 1000).toLong())
        }
    }


}