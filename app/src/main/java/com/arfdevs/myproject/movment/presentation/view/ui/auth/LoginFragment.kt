package com.arfdevs.myproject.movment.presentation.view.ui.auth

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.navigation.fragment.findNavController
import coil.load
import com.arfdevs.myproject.core.base.BaseFragment
import com.arfdevs.myproject.core.domain.model.User
import com.arfdevs.myproject.core.helper.launchAndCollectIn
import com.arfdevs.myproject.core.helper.onError
import com.arfdevs.myproject.core.helper.onLoading
import com.arfdevs.myproject.core.helper.onSuccess
import com.arfdevs.myproject.core.helper.visible
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.FragmentLoginBinding
import com.arfdevs.myproject.movment.presentation.view.component.CustomSnackbar
import com.arfdevs.myproject.movment.presentation.viewmodel.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel: AuthViewModel by viewModel()

    override fun initView() = with(binding) {
        btnLogin.isEnabled = false

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
            collectLogin(user)
        }

        tvToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    override fun initObserver() {}

    private fun collectLogin(user: User) = with(binding) {
        viewModel.loginUser(user).launchAndCollectIn(viewLifecycleOwner) { state ->
            state.onSuccess { success ->
                loadingOverlay.visible(false)
                loadingAnim.visible(false)
                if (success) {
                    findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
                }
            }.onError { e ->
                loadingOverlay.visible(false)
                loadingAnim.visible(false)
                context?.let {
                    CustomSnackbar.show(
                        it, binding.root,
                        getString(R.string.err_title_login_failed),
                        e.localizedMessage?.toString() ?: e.message.toString()
                    )
                }
            }.onLoading {
                loadingOverlay.visible(true)
                loadingAnim.visible(true)
            }
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
            val isPasswordValid = password.length >= 8 && password.isNotBlank()

            when {
                isEmailBlank -> {
                    tiEmail.apply {
                        isHelperTextEnabled = false
                        error = context.getString(R.string.err_email_empty)
                        isErrorEnabled = true
                    }
                    btnLogin.isEnabled = false
                }

                !isEmailValid -> {
                    tiEmail.apply {
                        isHelperTextEnabled = false
                        error = context.getString(R.string.err_email_invalid)
                        isErrorEnabled = true
                    }
                    btnLogin.isEnabled = false
                }

                isPasswordBlank -> {
                    tiPassword.apply {
                        isHelperTextEnabled = false
                        error = context.getString(R.string.err_password_empty)
                        isErrorEnabled = true
                    }
                    btnLogin.isEnabled = false
                }

                !isPasswordValid -> {
                    tiPassword.apply {
                        isHelperTextEnabled = false
                        error = context.getString(R.string.err_password_invalid)
                        isErrorEnabled = true
                    }
                    btnLogin.isEnabled = false
                }

                else -> {
                    tiEmail.isErrorEnabled = false
                    tiPassword.isErrorEnabled = false
                    btnLogin.isEnabled = true
                }
            }
        }
    }


}