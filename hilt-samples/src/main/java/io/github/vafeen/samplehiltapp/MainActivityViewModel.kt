package io.github.vafeen.samplehiltapp

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.vafeen.samplehiltapp.animals.Cat
import io.github.vafeen.samplehiltapp.animals.Dog
import javax.inject.Inject

@HiltViewModel
internal class MainActivityViewModel @Inject constructor(
	val cat: Cat,
	val dog: Dog
) : ViewModel()