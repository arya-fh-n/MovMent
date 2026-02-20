package com.arfdevs.myproject.core.base

import org.koin.core.module.Module

interface BaseModule {

    fun getModules(): List<Module>

}