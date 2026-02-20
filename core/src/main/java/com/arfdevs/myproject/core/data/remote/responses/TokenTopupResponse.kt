package com.arfdevs.myproject.core.data.remote.responses

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

class TokenTopupResponse: ArrayList<TokenTopupResponse.TokenTopupItem>(){
	@Keep
	@Parcelize
	data class TokenTopupItem(

		@field:SerializedName("price")
		val price: Int,

		@field:SerializedName("token")
		val token: Int

	) : Parcelable
}