package com.arfdevs.myproject.movment.presentation.view.ui.dashboard

import android.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.arfdevs.myproject.core.base.BaseFragment
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.FragmentDashboardBinding
import com.arfdevs.myproject.movment.presentation.viewmodel.AuthViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardFragment :
    BaseFragment<FragmentDashboardBinding>(FragmentDashboardBinding::inflate) {

    private val viewModel: AuthViewModel by viewModel()

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
                    logOut()
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

    private fun logOut() {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setMessage(getString(R.string.ad_msg))
                .setNegativeButton(getString(R.string.option_negative)) { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton(getString(R.string.option_positive)) { dialog, which ->
                    viewModel.logoutUser()
                    activity?.supportFragmentManager?.findFragmentById(R.id.main_navigation_container)
                        ?.findNavController()
                        ?.navigate(R.id.action_dashboardFragment_to_loginFragment)
                }
                .show()
                .getButton(AlertDialog.BUTTON_POSITIVE)
                ?.setTextColor(ContextCompat.getColor(it, R.color.red))
        }
    }

}