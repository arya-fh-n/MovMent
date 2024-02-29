package com.arfdevs.myproject.core.helper

sealed class SplashState<out R> {

    data object Onboarding : SplashState<Nothing>()
    data object Login : SplashState<Nothing>()
    data object Main : SplashState<Nothing>()
    data object Profile : SplashState<Nothing>()

}