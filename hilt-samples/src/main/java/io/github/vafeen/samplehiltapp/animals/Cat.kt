package io.github.vafeen.samplehiltapp.animals

import io.github.vafeen.samplehiltapp.Animal

interface Cat : Animal {
	companion object {
		const val MEOW = "Meow!"
	}
}