package com.arfdevs.myproject.core.data.remote.responses

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.errorprone.annotations.Keep
import com.google.gson.annotations.SerializedName

@Keep
@Parcelize
data class PaymentMethodsResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: List<PaymentType>,

	@field:SerializedName("message")
	val message: String
) : Parcelable

@Keep
@Parcelize
data class PaymentType(

	@field:SerializedName("item")
	val item: List<PaymentMethodItem>,

	@field:SerializedName("title")
	val title: String
) : Parcelable

@Keep
@Parcelize
data class PaymentMethodItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("label")
	val label: String,

	@field:SerializedName("status")
	val status: Boolean
) : Parcelable
