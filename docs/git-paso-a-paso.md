# Ramas de GitHub, merges y tag (Git Bash en VS Code)

> La rúbrica pide commits **reales** de cada recurso en su propia rama. Haz este versionado al desarrollar e integrar funcionalidad por funcionalidad; no basta renombrar una rama al final. Puedes partir del ZIP y **agregar los archivos por etapas** con `git add <rutas>`.

## 1. Repositorio (una sola vez)

En la terminal Git Bash, dentro de `ControlSaludIMC`:

```bash
git init -b master
git config user.name "TU_NOMBRE"
git config user.email "TU_CORREO_GITHUB"
git add README.md .gitignore .env.example pom.xml sql docs .vscode src/main/resources/application.properties src/main/java/com/tecmilenio/saludimc/ControlSaludImcApplication.java
git commit -m "chore: iniciar proyecto Maven y documentacion"
git branch develop
git switch develop
```

Aquí **no** uses `git add .` todavía: los recursos se van integrando en sus propias ramas.

## 2. Crear ramas por recurso

Para cada funcionalidad usa este mismo ciclo desde `develop`:

```bash
git switch develop
git switch -c feature/user
# Agrega SOLO los archivos correspondientes a Usuario, login, registro, seguridad y DTO compartidos.
git add src/main/java/com/tecmilenio/saludimc/domain/Usuario.java src/main/java/com/tecmilenio/saludimc/repo/UsuarioRepository.java src/main/java/com/tecmilenio/saludimc/config src/main/java/com/tecmilenio/saludimc/web src/main/java/com/tecmilenio/saludimc/dto src/main/java/com/tecmilenio/saludimc/service/UsuarioService.java src/main/java/com/tecmilenio/saludimc/api/UsuarioApi.java src/main/java/com/tecmilenio/saludimc/api/ApiSupport.java src/main/java/com/tecmilenio/saludimc/api/ErroresApi.java src/main/resources/templates/login.html src/main/resources/templates/registro.html src/main/resources/static/css/app.css
git commit -m "feat(user): registro y autenticacion segura"
git switch develop
git merge --no-ff feature/user -m "merge: feature/user en develop"
```

**Nota:** en la rama `feature/user`, algunas referencias del código a los recursos restantes estarán incompletas hasta fusionar el resto; al finalizar todas las ramas es cuando se ejecutan las pruebas completas.

Repite para **IMC**:

```bash
git switch -c feature/imc
git add src/main/java/com/tecmilenio/saludimc/domain/Medicion.java src/main/java/com/tecmilenio/saludimc/repo/MedicionRepository.java src/main/java/com/tecmilenio/saludimc/service/ImcService.java src/main/java/com/tecmilenio/saludimc/service/BmiCalculator.java src/main/java/com/tecmilenio/saludimc/api/ImcApi.java src/main/resources/templates/imc.html src/main/resources/static/js/dashboard.js src/test
git commit -m "feat(imc): calculo, historial REST y dashboard"
git switch develop
git merge --no-ff feature/imc -m "merge: feature/imc en develop"
```

Para las restantes **directory, file, notify**:

```bash
git switch -c feature/directory
git add src/main/java/com/tecmilenio/saludimc/domain/Directorio.java src/main/java/com/tecmilenio/saludimc/repo/DirectorioRepository.java src/main/java/com/tecmilenio/saludimc/api/DirectorioApi.java
git commit -m "feat(directory): gestion de carpetas"
git switch develop
git merge --no-ff feature/directory -m "merge: directory"

git switch -c feature/file
git add src/main/java/com/tecmilenio/saludimc/domain/Archivo.java src/main/java/com/tecmilenio/saludimc/repo/ArchivoRepository.java src/main/java/com/tecmilenio/saludimc/api/ArchivoApi.java
git commit -m "feat(file): reportes CSV persistidos"
git switch develop
git merge --no-ff feature/file -m "merge: file"

git switch -c feature/notify
git add src/main/java/com/tecmilenio/saludimc/domain/Notificacion.java src/main/java/com/tecmilenio/saludimc/repo/NotificacionRepository.java src/main/java/com/tecmilenio/saludimc/api/NotificacionApi.java
git commit -m "feat(notify): avisos por medicion"
git switch develop
git merge --no-ff feature/notify -m "merge: notify"
```

Revisa si hay archivos sin versionar:

```bash
git status
git log --graph --all --oneline --decorate
```

## 3. Copiar a master y crear versión estable

```bash
git switch master
git merge --no-ff develop -m "release: version estable 1.0.0"
git tag -a v1.0.0 -m "Version estable de Control de Salud e IMC"
```

## 4. Subir a GitHub

Crea en GitHub un repositorio **vacío**, sin README automático. Cambia la URL de ejemplo:

```bash
git remote add origin https://github.com/TU_USUARIO/control-salud-imc.git
git push -u origin master
git push -u origin develop
git push origin feature/user feature/imc feature/directory feature/file feature/notify
git push origin v1.0.0
```

No borres ninguna rama: la rúbrica pide preservar las ramas de funcionalidad. Puedes elegir `develop` como rama predeterminada en Settings > Branches si tu profesor espera observar avances. Comprueba en GitHub el grafo de commits y la etiqueta **v1.0.0**.
