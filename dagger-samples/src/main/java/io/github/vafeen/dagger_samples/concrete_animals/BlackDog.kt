package io.github.vafeen.dagger_samples.concrete_animals

import io.github.vafeen.dagger_samples.animals.Dog
import io.github.vafeen.dagger_samples.module.AnimalModule
import io.github.vafeen.daggerfodge.annotations.BindsIn
import javax.inject.Inject

@BindsIn(parent = Dog::class, module = AnimalModule::class)
internal class BlackDog @Inject constructor() : Dog {
	override val sound: String = Dog.WOOF
}