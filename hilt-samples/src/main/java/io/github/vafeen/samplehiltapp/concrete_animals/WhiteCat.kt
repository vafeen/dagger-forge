package io.github.vafeen.samplehiltapp.concrete_animals

import io.github.vafeen.daggerfodge.annotations.BindsIn
import io.github.vafeen.samplehiltapp.animals.Cat
import io.github.vafeen.samplehiltapp.module.AnimalModule
import javax.inject.Inject

@BindsIn(parent = Cat::class, module = AnimalModule::class)
class WhiteCat @Inject constructor() : Cat {
	override val sound: String = Cat.MEOW
}
