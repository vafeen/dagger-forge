plugins {
	id("java-library")
	alias(libs.plugins.jetbrains.kotlin.jvm)
	alias(libs.plugins.com.vanniktech.maven.publish)
}

group = project.findProperty("group").toString()
version = project.findProperty("version").toString()

java {
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}
kotlin {
	jvmToolchain(17)
}


mavenPublishing {
	publishToMavenCentral(automaticRelease = true)
	signAllPublications()

	coordinates(
		group as String?,
		"dagger-forge-annotations",
		version as String?
	)

	pom {
		name.set("Dagger Forge Annotations")
		description.set("Dagger Forge Annotations")
		inceptionYear.set("2025")
		url.set("https://github.com/vafeen/dagger-forge")
		licenses {
			license {
				name.set("The Apache License, Version 2.0")
				url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
				distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
			}
		}
		developers {
			developer {
				id.set("vafeen")
				name.set("A")
				url.set("https://github.com/vafeen/")
			}
		}
		scm {
			url.set("https://github.com/vafeen/dagger-forge")
			connection.set("scm:git:git://github.com/vafeen/dagger-forge.git")
			developerConnection.set("scm:git:ssh://git@github.com/vafeen/dagger-forge.git")
		}
	}
}