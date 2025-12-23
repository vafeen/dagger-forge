package io.github.vafeen.dagger_samples.component

import dagger.Component
import io.github.vafeen.dagger_samples.animals.Cat
import io.github.vafeen.dagger_samples.animals.Dog
import io.github.vafeen.dagger_samples.module.DaggerForgeAnimalModule

@Component(modules = [DaggerForgeAnimalModule::class])
interface AnimalComponent {
	fun getDog(): Dog
	fun getCat(): Cat
}

