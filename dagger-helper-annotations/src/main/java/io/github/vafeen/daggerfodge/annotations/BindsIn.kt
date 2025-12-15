package io.github.vafeen.daggerfodge.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class BindsIn(
	val parent: KClass<*>,
	val module: KClass<*>
)