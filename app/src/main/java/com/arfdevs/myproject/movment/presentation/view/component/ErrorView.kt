package com.arfdevs.myproject.movment.presentation.view.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.arfdevs.myproject.core.helper.visible
import com.arfdevs.myproject.movment.databinding.ErrorScreenBinding

class ErrorView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttribute: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttribute) {

    private var binding: ErrorScreenBinding

    init {
        binding = ErrorScreenBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setMessage(
        title: String,
        description: String,
        btnLabel: String = "",
        action: () -> (Unit) = {}
    ) = with(binding) {
        tvTitle.text = title
        tvDescription.text = description
        btnAction.visible(btnLabel.isNotEmpty())
        btnAction.text = btnLabel
        btnAction.setOnClickListener {
            action.invoke()
        }
    }
}