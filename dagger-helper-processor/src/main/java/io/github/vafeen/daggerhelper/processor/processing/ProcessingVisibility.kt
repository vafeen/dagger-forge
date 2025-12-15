package io.github.vafeen.daggerhelper.processor.processing

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.Modifier
import io.github.vafeen.daggerhelper.processor.logger
import io.vafeen.daggerhelper.annotations.BindsIn

internal enum class ProcessingVisibility {
	PUBLIC {
		override fun nameForFile(): String = "public"
	},
	INTERNAL {
		override fun nameForFile(): String = "internal"
	};

	abstract fun nameForFile(): String

	companion object {
		fun getClassAccessModifier(
			ksClassDeclaration: KSClassDeclaration,
		): ProcessingVisibility {
			val error =
				{ modifier: Modifier -> "Symbols annotated with ${BindsIn::class.simpleName} cannot be ${modifier.name}" }
			return when {
				ksClassDeclaration.modifiers.contains(Modifier.PRIVATE) -> INTERNAL
					.also {
						logger?.error(
							error(Modifier.PRIVATE),
							ksClassDeclaration
						)
					}

				ksClassDeclaration.modifiers.contains(Modifier.PROTECTED) -> INTERNAL
					.also {
						logger?.error(
							error(Modifier.PROTECTED),
							ksClassDeclaration
						)
					}

				ksClassDeclaration.modifiers.contains(Modifier.INTERNAL) -> INTERNAL
//                ksClassDeclaration.modifiers.contains(Modifier.PUBLIC) -> PUBLIC
				else -> PUBLIC
			}
		}
	}
}
