package io.github.vafeen.daggerhelper.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.validate
import java.io.OutputStreamWriter

class HelperBindsProcessor(
	private val codeGenerator: CodeGenerator,
	private val logger: KSPLogger,
	private val options: Map<String, String>
) : SymbolProcessor {

	data class ClassData(
		val annotatedClass: KSClassDeclaration,
		val parentClass: KSType,
		val moduleClass: KSType
	)

	private val helperBindsAnnotationName = "io.vafeen.daggerhelper.annotations.HelperBinds"

	override fun process(resolver: Resolver): List<KSAnnotated> {
		val symbols = resolver.getSymbolsWithAnnotation(helperBindsAnnotationName)
		val classDataList = mutableListOf<ClassData>()

		// Собираем все аннотированные классы
		symbols.forEach { symbol ->
			if (symbol is KSClassDeclaration && symbol.validate()) {
				val annotation = symbol.annotations
					.firstOrNull {
						it.annotationType.resolve().declaration.qualifiedName?.asString() == helperBindsAnnotationName
					} ?: return@forEach

				val parentArg = annotation.arguments
					.firstOrNull { it.name?.asString() == "parent" }
				val moduleArg = annotation.arguments
					.firstOrNull { it.name?.asString() == "module" }

				if (parentArg == null || moduleArg == null) {
					return@forEach
				}

				val parent = parentArg.value as? KSType
				val module = moduleArg.value as? KSType

				if (parent == null || module == null) {
					return@forEach
				}

				classDataList.add(ClassData(symbol, parent, module))
			}
		}

		// Группируем по модулю и генерируем код
		val groupedByModule = classDataList.groupBy { it.moduleClass }

		groupedByModule.forEach { (moduleType, classDataForModule) ->
			generateModuleForGroup(moduleType, classDataForModule)
		}

		return emptyList()
	}

	private fun generateModuleForGroup(
		moduleType: KSType,
		classDataList: List<ClassData>
	) {
		if (classDataList.isEmpty()) return

		val firstData = classDataList.first()
		val moduleName = "DaggerHelper${moduleType.declaration.simpleName.asString()}"
		val packageName = firstData.annotatedClass.packageName.asString()
		val moduleSimpleName = moduleType.declaration.simpleName.asString()

		// Создаем новый файл
		val file = codeGenerator.createNewFile(
			dependencies = Dependencies(aggregating = false),
			packageName = packageName,
			fileName = moduleName
		)

		OutputStreamWriter(file, Charsets.UTF_8).use { writer ->
			writer.write("package $packageName\n\n")
			writer.write("import dagger.Module\n")
			writer.write("import dagger.Binds\n\n")
			writer.write("@Module\n")
			writer.write("interface $moduleName : $moduleSimpleName {\n\n")

			classDataList.forEachIndexed { index, classData ->
				val annotatedClassName = classData.annotatedClass.simpleName.asString()
				val parentClassName = classData.parentClass.declaration.simpleName.asString()
				val methodName = "binds$annotatedClassName"

				writer.write("    @Binds\n")
				writer.write("    fun $methodName(impl: $annotatedClassName): $parentClassName")

				if (index < classDataList.size - 1) {
					writer.write("\n\n")
				} else {
					writer.write("\n")
				}
			}

			writer.write("\n}\n")
		}
	}
}