package io.vafeen.samples

import dagger.Component
import io.vafeen.daggerhelper.annotations.HelperBinds
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

@HelperBinds(parent = Cat::class, module = AnimalModule::class)
class WhiteCat @Inject constructor() : Cat {
	override val sound: String = Cat.MEOW
}

@HelperBinds(parent = Dog::class, module = AnimalModule::class)
internal class BlackDog @Inject constructor() : Dog {
	override val sound: String = Dog.WOOF
}

@Component(modules = [DaggerHelperAnimalModule::class])
interface AnimalComponent {
	fun getDog(): Dog
	fun getCat(): Cat
}

// Сгенерированный код:
//@Module
//interface DaggerHelperAnimalModule : AnimalModule {
//	@Binds
//	fun bindsBlackDog(blackDog: BlackDog): Dog
//
//	@Binds
//	fun bindsWhiteCat(whiteCat: WhiteCat): Cat
//}
