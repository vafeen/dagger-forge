package io.vafeen.samples

import dagger.Component
import io.vafeen.daggerfodge.annotations.BindsIn
import javax.inject.Inject

interface Animal {
	val sound: String
}

interface Cat : Animal {
	companion object {
		const val MEOW = "Meow!"
	}
}

interface Dog : Animal {
	companion object {
		const val WOOF = "Woof!"
	}
}

interface AnimalModule

@BindsIn(parent = Cat::class, module = AnimalModule::class)
class WhiteCat @Inject constructor() : Cat {
	override val sound: String = Cat.MEOW
}

@BindsIn(parent = Dog::class, module = AnimalModule::class)
internal class BlackDog @Inject constructor() : Dog {
	override val sound: String = Dog.WOOF
}

@Component(modules = [DaggerForgeAnimalModule::class])
interface AnimalComponent {
	fun getDog(): Dog
	fun getCat(): Cat
}

// Generated code:
//@dagger.Module
//internal interface DaggerForgeAnimalModule : AnimalModule {
//	@dagger.Binds
//	fun bindsWhiteCat(impl: WhiteCat): Cat
//	@dagger.Binds
//	fun bindsBlackDog(impl: BlackDog): Dog
//}
