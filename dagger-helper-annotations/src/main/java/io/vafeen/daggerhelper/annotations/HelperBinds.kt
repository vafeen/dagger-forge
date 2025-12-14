package io.vafeen.daggerhelper.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class HelperBinds(
	val parent: KClass<*>,
	val module: KClass<*>
)