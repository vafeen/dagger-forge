package io.github.vafeen.samplehiltapp.module

import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import io.github.vafeen.daggerfodge.annotations.SetComponent

@SetComponent(
	SingletonComponent::class,
	ViewModelComponent::class,
	FragmentComponent::class
)
interface AnimalModule

//generated code

//@dagger.Module
//@dagger.hilt.InstallIn(
//	dagger.hilt.components.SingletonComponent::class,
//	dagger.hilt.android.components.ViewModelComponent::class,
//	dagger.hilt.android.components.FragmentComponent::class,
//)
//internal interface DaggerForgeAnimalModule : com.example.samplehiltapp.module.AnimalModule {
//
//	@dagger.Binds
//	fun bindsWhiteCat(whiteCat: com.example.samplehiltapp.WhiteCat): com.example.samplehiltapp.Cat
//
//	@dagger.Binds
//	fun bindsBlackDog(blackDog: com.example.samplehiltapp.BlackDog): com.example.samplehiltapp.Dog
//
//}
