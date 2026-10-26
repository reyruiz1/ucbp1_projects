# Primer proyecto - E25-2

Responsable: Mateo Cossio.
Base: reyruiz1/ucbp1_projects, feature/grpc-logs, commit f693073.
Rama de entrega: mateocossio/E25-2. No hacer merge.
Esta entrega contiene solamente E25-2 (25 puntos), no los 100 puntos del grupo.

## Abrir

1. Extraer el ZIP en una carpeta nueva.
2. Abrir la carpeta que contiene settings.gradle.kts en Android Studio.
3. Esperar la sincronización y tener instalado el SDK indicado por el proyecto (compileSdk 37).
4. El google-services.json proporcionado por el grupo está incluido en androidApp.
   Está ignorado por Git. No se debe publicar manualmente.
5. Ejecutar androidApp en un dispositivo Android.

Se conservan las versiones de la base. No se incluyen .git, local.properties, cachés,
compilaciones ni APK. El JSON configura el proyecto base; este ejercicio no consulta Firebase.

## Qué cambia

- App.kt abre directamente ConfigLocalScreen para demostrar esta rama individual.
- MainApplication inicia Koin, pero ya no lanza el smoke test de Firebase ni programa logs.
  El resto de las funcionalidades y archivos del grupo se conserva, sin ejecutarse al inicio.
- ConfigItem tiene id autogenerado, clave, valor y timestamp real en milisegundos.
- ConfigItemDao expone insert, getAll con Flow, deleteById y deleteAll.
- ConfigSeed registra que los ejemplos fueron sembrados.
- La siembra y su indicador se guardan dentro de una transacción.
- AppDatabase pasa de 1 a 2 con AutoMigration y conserva la tabla dollars.
- El builder Android usa BundledSQLiteDriver, con la versión SQLite ya definida en la base.
- Capas data/domain/presentation, repositorio, State/Event/ViewModel y módulo Koin.
- El ViewModel observa Room; las escrituras no recargan manualmente la lista.
- Hay validación de campos vacíos, estado de carga y mensajes de error.
- La pantalla es una lista desplazable sencilla y muestra las fechas en UTC.

Los ejemplos son datos almacenados, no ajustes que cambien el tema o el idioma de la app.
El objetivo del ejercicio es guardar y mostrar configuraciones.

## Verificación obligatoria en Android Studio

En la terminal PowerShell, desde la raíz:

```powershell
.\gradlew.bat :androidApp:assembleDebug
```

Al compilar, KSP debe generar shared/schemas/edu.ucb.project.config.AppDatabase/2.json.
Inclúyelo en tu commit. No fue inventado ni generado manualmente.

1. Primera apertura de la función: aparecen tema=claro, idioma=es y max_items=10.
2. Agregar moneda=Bs: aparece inmediatamente, con ID y timestamp.
3. Borrar una fila: desaparece sin refrescar.
4. Borrar todo: queda vacía.
5. Cerrar y abrir después de borrar todo: sigue vacía; no reaparecen los ejemplos.
6. Agregar dos registros, cerrar y abrir en modo avión: permanecen.
7. En modo avión, agregar y borrar siguen funcionando.
8. Campos vacíos: aparece un mensaje y no se inserta.
9. Para comprobar la migración, instalar sobre la app base con datos dollars existentes;
   comprobar con App Inspection que se conservaron. No desinstalar para esta prueba.

Si la base ya tenía tablas de otros ejercicios o una versión distinta de 1,
no reemplazar Database.kt sin revisar la migración.
No usar fallbackToDestructiveMigration como arreglo automático.

## Subir a tu rama existente

No hacer git init sobre la carpeta del ZIP. Utilizar el clon que ya tiene tu .git.
Primero comprobar en el clon:

```bash
git status
git branch --show-current
```

Debe estar en mateocossio/E25-2. Si hay trabajo local pendiente, respaldarlo antes.
Copiar del ZIP solamente los archivos indicados abajo a sus mismas rutas del clon:

- shared/src/commonMain/kotlin/edu/ucb/project/configlocal/ (carpeta completa)
- shared/src/commonMain/kotlin/edu/ucb/project/di/ConfigLocalModule.kt
- shared/src/commonMain/kotlin/edu/ucb/project/di/SharedModules.kt
- shared/src/commonMain/kotlin/edu/ucb/project/config/Database.kt
- shared/src/commonMain/kotlin/edu/ucb/project/App.kt
- shared/src/androidMain/kotlin/edu/ucb/project/config/Database.android.kt
- shared/build.gradle.kts
- androidApp/src/main/kotlin/edu/ucb/project/MainApplication.kt
- LEEME_E25-2.md

Conservar .git, local.properties y los ajustes de tu Android Studio.
Si el líder actualizó esos archivos desde f693073, comparar antes de reemplazar.
Copiar google-services.json a androidApp solamente si todavía no lo tienes.
Si habías creado el paquete localconfig de instrucciones anteriores, no dejar
una segunda entidad ConfigItem: esta entrega usa configlocal.

Después de compilar y probar:

```bash
git add shared/src/commonMain/kotlin/edu/ucb/project/configlocal
git add shared/src/commonMain/kotlin/edu/ucb/project/di/ConfigLocalModule.kt
git add shared/src/commonMain/kotlin/edu/ucb/project/di/SharedModules.kt
git add shared/src/commonMain/kotlin/edu/ucb/project/config/Database.kt
git add shared/src/commonMain/kotlin/edu/ucb/project/App.kt
git add shared/src/androidMain/kotlin/edu/ucb/project/config/Database.android.kt
git add shared/build.gradle.kts
git add androidApp/src/main/kotlin/edu/ucb/project/MainApplication.kt
git add shared/schemas LEEME_E25-2.md
git diff --cached --stat
git diff --cached --check
git commit -m "feat(E25-2): configuracion local Room con Flow y siembra unica"
git push -u origin mateocossio/E25-2
```

No abrir PR ni hacer merge: la demostración se realiza en esta rama.

## Estado de verificación de esta entrega

- Revisión de estructura, imports y SQL; git diff --check.
- Comprobación SQLite de inserción, borrado, persistencia y siembra única (no sustituye Room).
- Compilación Android NO ejecutada: el entorno no pudo descargar Gradle 9.1.0
  por una restricción de red. No se dispone de prueba en dispositivo.
- El esquema 2.json se generará al compilar con KSP en Android Studio.
- No se afirma compatibilidad iOS: el ejercicio y la prueba de esta entrega son Android.
