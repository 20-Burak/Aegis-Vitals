# Developing Aegis Vitals

## Toolchain

- JDK 21
- Gradle 9.2.1 via the wrapper (`gradlew` / `gradlew.bat`)
- NeoForge ModDevGradle 1.0.19, NeoForge 21.1.65, Parchment 2024.11.17 for Minecraft 1.21.1

No third-party jars are needed. The mod only uses NeoForge and Minecraft, and Gradle downloads both. There is no `libs/` folder to fill in.

## Gradle wrapper jar

This repository doesn't track `.jar` files, including `gradle/wrapper/gradle-wrapper.jar`. If your clone doesn't have that file, regenerate it once with a locally installed Gradle before you use `gradlew`:

```
gradle wrapper --gradle-version 9.2.1 --distribution-type bin
```

## Build

```
./gradlew build          # Linux/macOS
.\gradlew.bat build      # Windows
```

The mod jar is written to `build/libs/AegisVitals-neoforge-1.21.1-<version>.jar`.

## Run in a dev client

```
.\gradlew.bat runClient
```

The game runs in `run/`, which git ignores.
