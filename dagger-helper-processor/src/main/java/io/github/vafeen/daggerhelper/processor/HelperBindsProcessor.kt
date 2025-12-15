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
import io.vafeen.daggerhelper.annotations.BindsIn
import io.vafeen.daggerhelper.annotations.SetComponent
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

	data class ModuleInfo(
		val moduleType: KSType,
		val installInComponents: List<String> = emptyList()
	)

	override fun process(resolver: Resolver): List<KSAnnotated> {
		val symbols = resolver.getSymbolsWithAnnotation(BindsIn::class.java.name)
		val classDataList = mutableListOf<ClassData>()

		// Собираем все аннотированные классы
		symbols.forEach { symbol ->
			if (symbol is KSClassDeclaration && symbol.validate()) {
				val annotation = symbol.annotations
					.firstOrNull {
						it.annotationType.resolve().declaration.qualifiedName?.asString() == BindsIn::class.java.name
					} ?: return@forEach

				val parentArg = annotation.arguments
					.firstOrNull { it.name?.asString() == BindsIn::parent.name }
				val moduleArg = annotation.arguments
					.firstOrNull { it.name?.asString() == BindsIn::module.name }

				if (parentArg == null || moduleArg == null) {
					logger?.error(
						"@${BindsIn::class.simpleName} must have both '${BindsIn::parent.name}' " +
								"and '${BindsIn::module.name}' arguments",
						symbol
					)
					return@forEach
				}

				val parent = parentArg.value as? KSType
				val module = moduleArg.value as? KSType

				if (parent == null || module == null) {
					logger?.error(
						"@${BindsIn::class.simpleName} arguments must be class types",
						symbol
					)
					return@forEach
				}

				val visibility = ProcessingVisibility.getClassAccessModifier(symbol)
				classDataList.add(ClassData(symbol, parent, module, visibility))
			}
		}

		// Группируем по модулю и получаем информацию о каждом модуле
		val groupedByModule = classDataList.groupBy { it.moduleClass }
		val modulesInfo = mutableMapOf<KSType, ModuleInfo>()

		// Собираем информацию о каждом модуле (есть ли @SetComponent)
		groupedByModule.keys.forEach { moduleType ->
			val moduleDeclaration = moduleType.declaration as? KSClassDeclaration
			val installInComponents = moduleDeclaration?.getInstallInComponents() ?: emptyList()
			modulesInfo[moduleType] = ModuleInfo(moduleType, installInComponents)
		}

		// Генерируем код для каждого модуля
		groupedByModule.forEach { (moduleType, classDataForModule) ->
			val moduleInfo = modulesInfo[moduleType] ?: ModuleInfo(moduleType)
			generateModuleForGroup(moduleInfo, classDataForModule)
		}

		return emptyList()
	}

	private fun KSClassDeclaration.getInstallInComponents(): List<String> {
		val setComponentAnnotation = this.annotations
			.firstOrNull {
				it.annotationType.resolve().declaration.qualifiedName?.asString() == SetComponent::class.java.name
			} ?: return emptyList()

		val componentArg = setComponentAnnotation.arguments
			.firstOrNull { it.name?.asString() == SetComponent::value.name }

		return if (componentArg?.value is List<*>) {
			val components = componentArg.value as List<*>
			components.mapNotNull { component ->
				if (component is KSType) {
					component.declaration.qualifiedName?.asString()
				} else {
					null
				}
			}
		} else {
			emptyList()
		}
	}

	private fun generateModuleForGroup(
		moduleInfo: ModuleInfo,
		classDataList: List<ClassData>
	) {
		if (classDataList.isEmpty()) return

		val firstData = classDataList.first()
		val moduleType = moduleInfo.moduleType
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

			// Добавляем @InstallIn если есть компоненты
			moduleInfo.installInComponents.ifNotEmpty { components ->
				writer.write("@dagger.hilt.InstallIn(\n")
				components.forEach {
					writer.write("${"${it.classs},".addIndent()}\n")
				}
				writer.write(")\n")
			}

			writer.write("${moduleVisibility}interface $moduleName : $moduleSimpleName {\n\n")

			classDataList.forEachIndexed { index, classData ->
				val annotatedClassName = classData.annotatedClass.simpleName.asString()
				val parentClassName = classData.parentClass.declaration.simpleName.asString()
				val methodName = "binds$annotatedClassName"

				writer.write("    @dagger.Binds\n")
				writer.write(
					"    fun $methodName(" +
							"${annotatedClassName.decapitalize()}: $annotatedClassName):" +
							" $parentClassName"
				)

				if (index < classDataList.size - 1) {
					writer.write("\n\n")
				} else {
					writer.write("\n")
				}
			}

			writer.write("\n}\n")
		}
	}

	private fun String.decapitalize() = replaceFirstChar { it.lowercase() }
	private fun <T> List<T>.ifNotEmpty(lambda: (List<T>) -> Unit) {
		if (this.isNotEmpty()) lambda(this)
	}

	val String.classs
		get() = "${this}::class"

	private fun String.addIndent(): String = this.prependIndent("    ")
}