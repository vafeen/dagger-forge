package io.github.vafeen.dagger_samples.animals

import io.github.vafeen.dagger_samples.Animal

interface Dog : Animal {
	companion object {
		const val WOOF = "Woof!"
	}
}