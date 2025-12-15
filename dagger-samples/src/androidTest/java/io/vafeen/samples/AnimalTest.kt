package io.vafeen.samples

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AnimalTest {
	private val animalComponent = DaggerAnimalComponent.create()

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