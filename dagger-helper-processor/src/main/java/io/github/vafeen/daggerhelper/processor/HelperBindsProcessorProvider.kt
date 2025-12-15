package io.github.vafeen.daggerhelper.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

internal class HelperBindsProcessorProvider : SymbolProcessorProvider {
	override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
		return HelperBindsProcessor(
			codeGenerator = environment.codeGenerator,
			kspLogger = environment.logger,
		)
	}
}