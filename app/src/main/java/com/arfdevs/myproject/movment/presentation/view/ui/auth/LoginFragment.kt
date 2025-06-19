package com.arfdevs.myproject.movment.presentation.view.ui.auth

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.navigation.fragment.findNavController
import coil.load
import com.arfdevs.myproject.core.base.BaseFragment
import com.arfdevs.myproject.core.domain.model.User
import com.arfdevs.myproject.core.helper.UiState
import com.arfdevs.myproject.core.helper.enabled
import com.arfdevs.myproject.core.helper.visible
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.FragmentLoginBinding
import com.arfdevs.myproject.movment.presentation.helper.Constants.PASSWORD_MAX_LENGTH
import com.arfdevs.myproject.movment.presentation.view.component.CustomSnackbar
import com.arfdevs.myproject.movment.presentation.viewmodel.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel: AuthViewModel by viewModel()

    override fun initView() = with(binding) {
        btnLogin.enabled(false)

        ivLogo.load(R.drawable.splash_icon)
        tvLoginTitle.text = getString(R.string.tv_login_title)
        tiEmail.hint = getString(R.string.ti_email_hint)
        tiPassword.hint = getString(R.string.ti_password_hint)
        btnLogin.text = getString(R.string.btn_login)

        tvNewToMovment.text = getString(R.string.tv_new_to_movment)
        tvToRegister.text = getString(R.string.tv_to_register)

        emailValidation()
        passwordValidation()
    }

    override fun initListener() = with(binding) {
        btnLogin.setOnClickListener {
            val user = User(
                email = etEmail.text.toString().trim(),
                password = etPassword.text.toString().trim()
            )
            viewModel.loginUser(user)
        }

        tvToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    override fun initObserver() = with(viewModel) {
        loginState.observe(viewLifecycleOwner) { state ->
            when (state) {
                UiState.Loading -> handleLoading(state is UiState.Loading)
                is UiState.Success -> {
                    if (state.data) findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
                    else handleError(getString(R.string.err_failed))
                }

                UiState.Empty -> handleError(getString(R.string.err_empty))
                is UiState.Error -> handleError(state.message)
                UiState.ErrorConnection -> handleError(getString(R.string.err_connection))
            }
        }
    }

    private fun handleLoading(isLoading: Boolean) = with(binding) {
        loadingOverlay.visible(isLoading)
        loadingAnim.visible(isLoading)
    }

    private fun handleError(message: String) = with(binding) {
        context?.let {
            CustomSnackbar.show(
                it, root,
                getString(R.string.err_title_login_failed),
                message
            )
        }
    }

    private fun emailValidation() {
        binding.apply {
            etEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    s?.let {
                        validateFields()
                    }
                }

            })
        }
    }

    private fun passwordValidation() {
        binding.apply {
            etPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    s?.let {
                        validateFields()
                    }
                }

            })
        }
    }

    private fun validateFields() {
        binding.apply {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            val isEmailBlank = email.isBlank()
            val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotBlank()

            val isPasswordBlank = password.isBlank()
            val isPasswordValid = password.length >= PASSWORD_MAX_LENGTH && password.isNotBlank()

            when {
                isEmailBlank -> {
                    tiEmail.apply {
                        isHelperTextEnabled = false
                        error = context.getString(R.string.err_email_empty)
                        isErrorEnabled = true
                    }
                    btnLogin.enabled(false)
                }

                !isEmailValid -> {
                    tiEmail.apply {
                        isHelperTextEnabled = false
                        error = context.getString(R.string.err_email_invalid)
                        isErrorEnabled = true
                    }
                    btnLogin.enabled(false)
                }

                isPasswordBlank -> {
                    tiPassword.apply {
                        isHelperTextEnabled = false
                        error = context.getString(R.string.err_password_empty)
                        isErrorEnabled = true
                    }
                    btnLogin.enabled(false)
                }

                !isPasswordValid -> {
                    tiPassword.apply {
                        isHelperTextEnabled = false
                        error = context.getString(R.string.err_password_invalid)
                        isErrorEnabled = true
                    }
                    btnLogin.enabled(false)
                }

                else -> {
                    tiEmail.isErrorEnabled = false
                    tiPassword.isErrorEnabled = false
                    btnLogin.enabled(true)
                }
            }
        }
    }

}
