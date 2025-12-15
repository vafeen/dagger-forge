# Dagger Forge [![GitHub Tag](https://img.shields.io/github/v/tag/vafeen/dagger-forge)](https://github.com/vafeen/dagger-forge/releases/latest/)

<div align="center">
  <img src="pictures/ico.png" alt="Dagger Forge Logo" width="600">
</div>

**DaggerForge** is a lightweight Kotlin library for generating Dagger modules using **KSP (Kotlin Symbol Processing)**.
It automatically creates `@Module` interfaces with `@Binds` methods based on the `@BindsIn` annotation, with Hilt integration via `@SetComponent`.

## Implementation

[![GitHub Tag](https://img.shields.io/github/v/tag/vafeen/dagger-forge)](https://github.com/vafeen/dagger-forge/releases/latest/)

Gradle:
```kotlin
implementation("io.github.vafeen:dagger-forge-annotations:VERSION")
ksp("io.github.vafeen:dagger-forge-processor:VERSION")
```

Other:

https://central.sonatype.com/artifact/io.github.vafeen/dagger-forge-annotations
https://central.sonatype.com/artifact/io.github.vafeen/dagger-forge-processor

## Docs

[DOCUMENTATION](DOCUMENTATION.md)

[SAMPLES](SAMPLES.md)
