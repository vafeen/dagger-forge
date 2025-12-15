package io.vafeen.daggerfodge.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class SetComponent(vararg val value: KClass<*>)