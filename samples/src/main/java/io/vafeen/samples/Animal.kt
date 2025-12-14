package io.vafeen.samples

import dagger.Component
import io.vafeen.daggerhelper.annotations.HelperBinds

interface Animal {
	fun makeSound()
}

interface Cat : Animal

interface Dog : Animal

interface AnimalModule

@HelperBinds(parent = Cat::class, module = AnimalModule::class)
class WhiteCat : Cat {
	override fun makeSound() = println("Meow!")
}

@HelperBinds(parent = Dog::class, module = AnimalModule::class)
class BlackDog : Dog {
	override fun makeSound() = println("Woof!")

}

@Component(
	modules = [
//    DaggerHelperAnimalModule::class
	]
)
interface AnimalComponent {

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
