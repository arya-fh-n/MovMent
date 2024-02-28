package com.arfdevs.myproject.movment.presentation.view.ui.dashboard

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.arfdevs.myproject.core.base.BaseFragment
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.FragmentDashboardBinding

class DashboardFragment :
    BaseFragment<FragmentDashboardBinding>(FragmentDashboardBinding::inflate) {

    private lateinit var navController: NavController

    override fun initView() = with(binding) {
        toolbarDashboard.title = getString(R.string.app_name)

        val navHost = childFragmentManager.findFragmentById(R.id.fcv_dashboard) as NavHostFragment
        navController = navHost.navController
        bottomNavigation.setupWithNavController(navController)

        toolbarDashboard.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_cart -> {
                    true
                }

                R.id.menu_settings -> {
                    true
                }

                R.id.menu_sign_out -> {
                    true
                }

                else -> false
            }

        }
    }

    override fun initListener() {

    }

    override fun initObserver() {

    }

}