package io.github.vafeen.dagger_samples.concrete_animals

import io.github.vafeen.dagger_samples.animals.Cat
import io.github.vafeen.dagger_samples.module.AnimalModule
import io.github.vafeen.daggerfodge.annotations.BindsIn
import javax.inject.Inject

@BindsIn(parent = Cat::class, module = AnimalModule::class)
class WhiteCat @Inject constructor() : Cat {
	override val sound: String = Cat.MEOW
}