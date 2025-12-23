package io.github.vafeen.dagger_samples

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.vafeen.dagger_samples.animals.Cat
import io.github.vafeen.dagger_samples.animals.Dog
import io.github.vafeen.dagger_samples.component.AnimalComponent
import io.github.vafeen.dagger_samples.component.DaggerAnimalComponent
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AnimalTest {
	private val animalComponent: AnimalComponent = DaggerAnimalComponent.create()

	@Test
	fun testDog() {
		val dog = animalComponent.getDog()
		assert(dog.sound == Dog.WOOF)
	}

	@Test
	fun testCat() {
		val cat = animalComponent.getCat()
		assert(cat.sound == Cat.MEOW)
	}
}