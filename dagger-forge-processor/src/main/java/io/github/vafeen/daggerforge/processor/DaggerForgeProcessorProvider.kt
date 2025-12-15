package io.github.vafeen.daggerforge.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

internal class DaggerForgeProcessorProvider : SymbolProcessorProvider {
	override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
		return DaggerForgeProcessor(
			codeGenerator = environment.codeGenerator,
			kspLogger = environment.logger,
		)
	}
}