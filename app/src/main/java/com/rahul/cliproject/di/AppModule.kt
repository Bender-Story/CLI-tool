package com.rahul.cliproject.di

import com.rahul.cliproject.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule= module {
    viewModel { MainViewModel() }
}