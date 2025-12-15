package com.example.samplehiltapp

import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import io.vafeen.daggerhelper.annotations.BindsIn
import io.vafeen.daggerhelper.annotations.SetComponent
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

@SetComponent(
	SingletonComponent::class,
	ViewModelComponent::class,
	FragmentComponent::class
)
interface AnimalModule

@BindsIn(parent = Cat::class, module = AnimalModule::class)
class WhiteCat @Inject constructor() : Cat {
	override val sound: String = Cat.MEOW
}

@BindsIn(parent = Dog::class, module = AnimalModule::class)
internal class BlackDog @Inject constructor() : Dog {
	override val sound: String = Dog.WOOF
}

//generated code

//@dagger.Module
//@dagger.hilt.InstallIn(
//	dagger.hilt.components.SingletonComponent::class,
//	dagger.hilt.android.components.ViewModelComponent::class,
//	dagger.hilt.android.components.FragmentComponent::class,
//)
//internal interface DaggerHelperAnimalModule : AnimalModule {
//
//	@dagger.Binds
//	fun bindsWhiteCat(whiteCat: WhiteCat): Cat
//
//	@dagger.Binds
//	fun bindsBlackDog(blackDog: BlackDog): Dog
//
//}
