package com.datnguyen.movie_dev.ui.activity.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.bumptech.glide.Glide
import com.datnguyen.movie_dev.MyApplication
import com.datnguyen.movie_dev.R
import com.datnguyen.movie_dev.databinding.ActivityMainBinding
import com.datnguyen.movie_dev.databinding.FragmentHomeTabBinding
import com.datnguyen.movie_dev.extras.Constants
import com.datnguyen.movie_dev.ui.activity.secondary.SecondaryActivity
import com.datnguyen.movie_dev.ui.base.BaseActivity
import com.datnguyen.movie_dev.ui.base.BaseViewModel
import com.datnguyen.movie_dev.ui.fragment.search.SearchFragment
import com.datnguyen.movie_dev.ui.fragment.search.SearchViewModel
import com.datnguyen.movie_dev.ui.fragment.signin.SignInFragment
import com.datnguyen.movie_dev.utils.Utils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MainActivity : BaseActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mainActivityViewModel: MainActivityViewModel

    private val binding: ActivityMainBinding get() = (_binding as ActivityMainBinding?)!!

    var drawerLayout: DrawerLayout? = null
    var navView: NavigationView? = null
    var navController: NavController? = null

    //top nav
    var llProfile: View? = null
    var llSignIn: View? = null

    var imvAvatar: ImageView? = null
    var tvUsername: TextView? = null
    var tvName: TextView? = null

    override fun initUI() {
        // Handle the splash screen transition.
        installSplashScreen()

        //init drawer left menu
        setSupportActionBar(binding.appBarMain.toolbar)
        drawerLayout = binding.drawerLayout
        navView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_your_movies
            ), drawerLayout
        )
        navController?.let { navController ->
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView?.setupWithNavController(navController)
            //add after setupWithNavController
            //manual handle other nav controller
            navView?.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener {
                val handled = NavigationUI.onNavDestinationSelected(it, navController)
                if (!handled) {
                    // handle other navigation other than default
                    when (it.itemId) {
                        R.id.nav_sign_out -> {
                            signOut();
                        }
                    }
                }
                drawerLayout?.closeDrawer(GravityCompat.START)
                return@OnNavigationItemSelectedListener handled
            })
        }

        //map the header of navigation view
        val hView: View? = navView?.getHeaderView(0)
        llProfile = hView?.findViewById(R.id.ll_profile)
        llSignIn = hView?.findViewById(R.id.ll_sign_in)
        imvAvatar = hView?.findViewById(R.id.imv_avatar)
        tvUsername = hView?.findViewById(R.id.tv_username)
        tvName = hView?.findViewById(R.id.tv_name)

        updateProfile()

        hideSignOutMenuItem()
    }

    private fun signOut() {
        MaterialAlertDialogBuilder(this)
            .setMessage(getString(R.string.sign_out_confirm_msg))
            .setNegativeButton(
                getString(R.string.no)
            ) { dialog, which ->

            }
            .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                mainActivityViewModel?.logout()
            }
            .show()
    }

    //hide//show sign out menu item dependent on session login
    private fun hideSignOutMenuItem() {
        val navMenu: Menu? = navView?.menu
        navMenu?.findItem(R.id.nav_sign_out)?.isVisible = MyApplication.instance().session != null
        navMenu?.findItem(R.id.nav_your_movies)?.isVisible = MyApplication.instance().session != null
    }

    override fun initViewModel() {
        mainActivityViewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        binding.viewModel = mainActivityViewModel
        binding.lifecycleOwner = this

        mainActivityViewModel.logoutEvent.observe(this, Observer {
            updateProfile()
            hideSignOutMenuItem()
        })

        //handle loading layout
        mainActivityViewModel.loading.observe(this, Observer {
            if (it) {
                showProgress()
            } else {
                hideProgress()
            }
        })

        //handle error
        mainActivityViewModel.errorMessage.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun initData() {

    }

    override fun initListener() {
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        llSignIn?.setOnClickListener {
            //open sign in screen
            val screen = SignInFragment::class.java.name
            val intent = SecondaryActivity.createIntent(this)
            //pass the screen key to bundle
            intent.putExtra(SecondaryActivity.KEY_SCREEN, screen)
            //start activity
            startActivity(intent)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_search -> {
                //open search screen
                val screen = SearchFragment::class.java.name
                val intent = SecondaryActivity.createIntent(this)
                //pass the screen key to bundle
                intent.putExtra(SecondaryActivity.KEY_SCREEN, screen)
                //start activity
                startActivity(intent)
                return false
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return (navController?.navigateUp(appBarConfiguration) ?: false) || super.onSupportNavigateUp()
    }

    private fun closeNavLeftView(): Boolean {
        if (navView != null && drawerLayout?.isDrawerOpen(navView!!) == true) {
            drawerLayout?.closeDrawer(navView!!)
            return true
        }
        return false
    }

    /**
     * Back pressed
     */
    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (closeNavLeftView()) {       //check close left nav drawer
                return
            }
            val count = supportFragmentManager.backStackEntryCount ?: 0     //fragment
            try {
                if (count <= 1 && navController?.graph?.startDestinationId == navController?.currentDestination?.id) {
                    exitApp()
                } else {
                    onBackPressedDispatcher.onBackPressed()
                }
            } catch (ex: Exception) {
                exitApp()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun setLayoutXml(): Int {
        return R.layout.activity_main
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    //received event profile update -> update the profile info into header of the left drawer
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: String) {
        if (Constants.EVENT_BUS_PROFILE_UPDATE == event) {
            updateProfile()
            hideSignOutMenuItem()
        }
    }

    private fun updateProfile() {
        val session = MyApplication.instance().session
        session?.let {
            //display layout profile
            llProfile?.visibility = View.VISIBLE
            llSignIn?.visibility = View.GONE

            tvName?.text = session.name
            tvUsername?.text = session.username

            imvAvatar?.let {
                Glide.with(this)
                    .load(session.avatarUrl)
                    .override(Utils.convertDPtoPixel(48), Utils.convertDPtoPixel(48))
                    .circleCrop()
                    .into(it)
            }
        } ?: kotlin.run {
            //display layout sign in
            llProfile?.visibility = View.GONE
            llSignIn?.visibility = View.VISIBLE
        }
    }
}