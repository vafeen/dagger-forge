package io.github.vafeen.samplehiltapp.animals

import io.github.vafeen.samplehiltapp.Animal

interface Dog : Animal {
	companion object {
		const val WOOF = "Woof!"
	}
}