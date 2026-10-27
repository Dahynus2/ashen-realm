# Ashen Realm (Fabric 1.21.8)

Mod con: nueva dimensión (Reino Cenizo), estructuras (torres en ruinas + arena del jefe),
un jefe (Guardián Cenizo) y un ítem nuevo que suelta al morir (Hoja del Guardián Cenizo).

## Compilar
1. Java 21 instalado.
2. Genera el wrapper de Gradle (una sola vez): `gradle wrapper --gradle-version 8.14`
   (o copia `gradlew`, `gradlew.bat` y `gradle/wrapper/gradle-wrapper.jar` de https://github.com/FabricMC/fabric-example-mod)
3. `./gradlew build` -> el .jar queda en `build/libs/`.
   Para probar: `./gradlew runClient`.

## Cómo jugar
1. Craftea la **Llave Cenicienta** (obsidiana, diamante y ojo de ender):
   ```
   O D O
   D E D
   O D O
   ```
2. Clic derecho con la llave para viajar al Reino Cenizo (otra vez para volver).
3. Explora: busca **ruinas** (torres de blackstone). Su cofre contiene un **Núcleo Cenizo**.
4. Busca la **arena** (círculo con 8 pilares y una obsidiana llorona en el centro).
5. Clic derecho en la obsidiana llorona con el Núcleo -> aparece el **Guardián Cenizo**.
6. Mátalo para obtener la **Hoja del Guardián Cenizo** (+ chatarra de netherita, y a veces otro núcleo).

Comandos útiles: `/execute in ashenrealm:ashen_realm run tp @s 0 100 0`,
`/summon ashenrealm:ashen_warden`, `/give @s ashenrealm:ashen_core`.

## Obtener el .jar sin instalar nada (GitHub Actions)
1. Crea un repositorio en github.com y sube TODO el contenido de esta carpeta (incluida `.github`).
2. Ve a la pestaña **Actions** -> "Build mod" (se ejecuta sola al subir; o pulsa "Run workflow").
3. Al terminar (verde), abre la ejecución y descarga el artefacto **ashenrealm-jar**.
4. Descomprime y usa `ashenrealm-1.0.0.jar` (NO el `-sources.jar`).
5. Ponlo en `.minecraft/mods/` junto con Fabric Loader y **Fabric API 0.129.0+1.21.8**.
