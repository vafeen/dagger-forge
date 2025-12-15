package com.example.samplehiltapp

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class MainActivityViewModel @Inject constructor(
	val cat: Cat,
	val dog: Dog
) : ViewModel()