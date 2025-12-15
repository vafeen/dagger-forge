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
import io.github.vafeen.daggerhelper.processor.processing.ProcessingVisibility
import io.vafeen.daggerhelper.annotations.HelperBinds
import java.io.OutputStreamWriter

internal var logger: KSPLogger? = null

internal class HelperBindsProcessor private constructor(private val codeGenerator: CodeGenerator) :
	SymbolProcessor {
	private val libName = "DaggerHelper"

	constructor(
		codeGenerator: CodeGenerator,
		kspLogger: KSPLogger
	) : this(codeGenerator) {
		logger = kspLogger
	}

	data class ClassData(
		val annotatedClass: KSClassDeclaration,
		val parentClass: KSType,
		val moduleClass: KSType,
		val visibility: ProcessingVisibility
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
					.firstOrNull { it.name?.asString() == HelperBinds::parent.name }
				val moduleArg = annotation.arguments
					.firstOrNull { it.name?.asString() == HelperBinds::module.name }

				if (parentArg == null || moduleArg == null) {
					logger?.error(
						"@${HelperBinds::class.simpleName} must have both '${HelperBinds::parent.name}' " +
								"and '${HelperBinds::module.name}' arguments",
						symbol
					)
					return@forEach
				}

				val parent = parentArg.value as? KSType
				val module = moduleArg.value as? KSType

				if (parent == null || module == null) {
					logger?.error(
						"@${HelperBinds::class.simpleName} arguments must be class types",
						symbol
					)
					return@forEach
				}

				val visibility = ProcessingVisibility.getClassAccessModifier(symbol)
				classDataList.add(ClassData(symbol, parent, module, visibility))
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
		val moduleName = "$libName${moduleType.declaration.simpleName.asString()}"
		val packageName = firstData.annotatedClass.packageName.asString()
		val moduleSimpleName = moduleType.declaration.simpleName.asString()

		// Определяем видимость всего модуля
		// Если есть хотя бы один internal класс, весь модуль будет internal
		val hasInternalClass = classDataList.any { it.visibility == ProcessingVisibility.INTERNAL }
		val moduleVisibility = if (hasInternalClass) "internal " else ""

		// Создаем новый файл
		val file = codeGenerator.createNewFile(
			dependencies = Dependencies(aggregating = false),
			packageName = packageName,
			fileName = moduleName
		)

		OutputStreamWriter(file, Charsets.UTF_8).use { writer ->
			writer.write("package $packageName\n\n")
			writer.write("@dagger.Module\n")
			writer.write("${moduleVisibility}interface $moduleName : $moduleSimpleName {\n\n")

			classDataList.forEachIndexed { index, classData ->
				val annotatedClassName = classData.annotatedClass.simpleName.asString()
				val parentClassName = classData.parentClass.declaration.simpleName.asString()
				val methodName = "binds$annotatedClassName"

				writer.write("    @dagger.Binds\n")
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