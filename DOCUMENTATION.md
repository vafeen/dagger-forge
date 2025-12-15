# Documentation


## Quick start with Hilt 

1. Define your module with SetComponent

```kt
@SetComponent(SingletonComponent::class)
interface AnimalModule
```

2. Annotate your classes with their parent classes and your module

```kt
@BindsIn(parent = Cat::class, module = AnimalModule::class)
class WhiteCat @Inject constructor() : Cat

@BindsIn(parent = Dog::class, module = AnimalModule::class) 
internal class BlackDog @Inject constructor() : Dog
```

3. Enjoy it without a boiler plate 

## Quick start with Dagger

1. Define your module

```kt
interface AnimalModule
```

2. Annotate your classes with their parent classes and your module

```kt
@BindsIn(parent = Cat::class, module = AnimalModule::class)
class WhiteCat @Inject constructor() : Cat

@BindsIn(parent = Dog::class, module = AnimalModule::class) 
internal class BlackDog @Inject constructor() : Dog
```

3. Define your component and add **Generated** module

```kt
@Component(modules = [DaggerForgeAnimalModule::class])
interface AnimalComponent {
	fun getDog(): Dog
	fun getCat(): Cat
}
```

4. Enjoy it without a boiler plate
