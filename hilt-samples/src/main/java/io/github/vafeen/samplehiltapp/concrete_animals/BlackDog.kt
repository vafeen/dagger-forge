package io.github.vafeen.samplehiltapp.concrete_animals

import io.github.vafeen.daggerfodge.annotations.BindsIn
import io.github.vafeen.samplehiltapp.animals.Dog
import io.github.vafeen.samplehiltapp.module.AnimalModule
import javax.inject.Inject

@BindsIn(parent = Dog::class, module = AnimalModule::class)
internal class BlackDog @Inject constructor() : Dog {
	override val sound: String = Dog.WOOF
}