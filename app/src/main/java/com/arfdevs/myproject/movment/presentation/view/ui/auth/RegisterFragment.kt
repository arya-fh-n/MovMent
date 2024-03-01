package com.arfdevs.myproject.movment.presentation.view.ui.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Patterns
import android.view.View
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
import com.arfdevs.myproject.movment.databinding.FragmentRegisterBinding
import com.arfdevs.myproject.movment.presentation.view.component.CustomSnackbar
import com.arfdevs.myproject.movment.presentation.viewmodel.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {

    private val viewModel: AuthViewModel by viewModel()

    override fun initView() = with(binding) {
        btnRegister.isEnabled = false
        val locale = resources.configuration.locales[0].language

        ivLogo.load(R.drawable.splash_icon)
        tvRegisterTitle.text = getString(R.string.tv_register_title)
        tiEmail.hint = getString(R.string.ti_email_hint)
        tiPassword.hint = getString(R.string.ti_password_hint)
        btnRegister.text = getString(R.string.btn_register)

        tvTNC.text = view?.let {
            setupSpannable(locale, it.context)
        }
        tvTNC.movementMethod = LinkMovementMethod.getInstance()

        tvNotNewToMovment.text = getString(R.string.tv_not_new_to_movment)
        tvToLogin.text = getString(R.string.tv_to_login)

        emailValidation()
        passwordValidation()
    }

    override fun initListener() = with(binding) {
        btnRegister.setOnClickListener {
            val user = User(
                email = etEmail.text.toString().trim(),
                password = etPassword.text.toString().trim()
            )
            collectRegister(user)
        }

        tvToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    override fun initObserver() {}

    private fun collectRegister(user: User) = with(binding) {
        viewModel.registerUser(user).launchAndCollectIn(viewLifecycleOwner) { state ->
            state.onSuccess {
                loadingOverlay.visible(false)
                loadingAnim.visible(false)
                if (it) {
                    findNavController().navigate(R.id.action_registerFragment_to_profileFragment)
                }
            }.onError { e ->
                loadingOverlay.visible(false)
                loadingAnim.visible(false)
                context?.let {
                    CustomSnackbar.show(
                        it, binding.root,
                        getString(R.string.err_title_login_failed),
                        e.cause.toString()
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
                    btnRegister.isEnabled = false
                }

                !isEmailValid -> {
                    tiEmail.apply {
                        isHelperTextEnabled = false
                        error = context.getString(R.string.err_email_invalid)
                        isErrorEnabled = true
                    }
                    btnRegister.isEnabled = false
                }

                isPasswordBlank -> {
                    tiPassword.apply {
                        isHelperTextEnabled = false
                        error = context.getString(R.string.err_password_empty)
                        isErrorEnabled = true
                    }
                    btnRegister.isEnabled = false
                }

                !isPasswordValid -> {
                    tiPassword.apply {
                        isHelperTextEnabled = false
                        error = context.getString(R.string.err_password_invalid)
                        isErrorEnabled = true
                    }
                    btnRegister.isEnabled = false
                }

                else -> {
                    tiEmail.isErrorEnabled = false
                    tiPassword.isErrorEnabled = false
                    btnRegister.isEnabled = true
                }
            }
        }
    }

    private fun setupSpannable(
        locale: String,
        context: Context
    ): SpannableString {
        val rawString = resources.getString(R.string.tv_tnc)
        val spannableString = SpannableStringBuilder(rawString)

        val tncText = if (locale == "en") "Terms and Conditions" else "Syarat dan Ketentuan"
        val privacyText = if (locale == "en") "Privacy Policy" else "Kebijakan Privasi"

        val startTnc = rawString.indexOf(tncText)
        val endTnc = startTnc + tncText.length

        val startPrivacy = rawString.indexOf(privacyText)
        val endPrivacy = startPrivacy + privacyText.length

        val tncClickable = object : ClickableSpan() {
            override fun onClick(view: View) {
                openWebPage(context, getString(R.string.tv_tnc_link))
            }
        }

        val privacyClickable = object : ClickableSpan() {
            override fun onClick(view: View) {
                openWebPage(context, getString(R.string.tv_pp_link))
            }
        }

        spannableString.setSpan(
            tncClickable,
            startTnc,
            endTnc,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableString.setSpan(
            privacyClickable,
            startPrivacy,
            endPrivacy,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return SpannableString(spannableString)
    }

    private fun openWebPage(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }
}