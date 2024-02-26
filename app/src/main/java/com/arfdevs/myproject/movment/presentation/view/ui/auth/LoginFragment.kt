package com.arfdevs.myproject.movment.presentation.view.ui.auth

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.navigation.fragment.findNavController
import coil.load
import com.arfdevs.myproject.core.base.BaseFragment
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

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
            Snackbar.make(root, "Login click!", Snackbar.LENGTH_SHORT).show()
        }

        tvToRegister.setOnClickListener {
            Snackbar.make(root, "To register click!", Snackbar.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
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
                        error = "Email cannot be empty"
                        isErrorEnabled = true
                    }
                    btnLogin.isEnabled = false
                }

                !isEmailValid -> {
                    tiEmail.apply {
                        isHelperTextEnabled = false
                        error = "Email is invalid"
                        isErrorEnabled = true
                    }
                    btnLogin.isEnabled = false
                }

                isPasswordBlank -> {
                    tiPassword.apply {
                        isHelperTextEnabled = false
                        error = "Password cannot be empty"
                        isErrorEnabled = true
                    }
                    btnLogin.isEnabled = false
                }

                !isPasswordValid -> {
                    tiPassword.apply {
                        isHelperTextEnabled = false
                        error = "Password is invalid"
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