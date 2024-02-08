package com.datnguyen.movie_dev.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation

abstract class BaseFragment : Fragment() {
    var rootView: View? = null
    var _binding: ViewDataBinding? = null

    /**
     * Get base activity if it is exits.
     *
     * @return BaseAppCompatActivity
     */
    val baseActivity: BaseActivity?
        get() = if (activity is BaseActivity) activity as BaseActivity? else null


    /**
     * Show progress layout
     */
    fun showProgress() {
        baseActivity?.showProgress()
    }

    /**
     * Hide progress layout
     */
    fun hideProgress() {
        baseActivity?.hideProgress()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            loadPassedParamsIfNeeded(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //to avoid recreate fragment
        if (_binding == null) {
            _binding = DataBindingUtil.inflate(inflater, setLayoutXml(), container, false)
            initUI()
            initViewModel()
        }

        initData()
        initListener()

        rootView = _binding?.root
        return rootView
    }

    /**
     * load passed params
     */
    protected open fun loadPassedParamsIfNeeded(extras: Bundle) {

    }


    /**
     * Set xml
     */
    abstract fun setLayoutXml(): Int

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

    //nav controller to open fragment
    val navController: NavController?
        get() = view?.let { Navigation.findNavController(it) }

    fun navControllerTo(parentId: Int, destId: Int, bundle: Bundle?) {
        view?.let {
            val navController = Navigation.findNavController(it)
            if (navController.currentDestination?.id == parentId) {
                navController.navigate(destId, bundle)
            }
        }
    }

    /**
     * Back pressed
     */
    fun onBackPressed() {
        baseActivity?.onBackPressedDispatcher?.onBackPressed()
    }

}