package io.github.vafeen.dagger_samples.animals

import io.github.vafeen.dagger_samples.Animal


interface Cat : Animal {
	companion object {
		const val MEOW = "Meow!"
	}
}