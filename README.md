# Control de Salud e IMC

Proyecto de Evidencia Final de **Computación Avanzada en Java**. Implementa el Avance 1 (clases `Usuario` y `IMC`, dos tablas con relación 1:N y pantallas de login/registro/panel) mediante **Spring Boot + Maven + MySQL Workbench + MVC + REST**.

> **Aviso sobre el enunciado:** la guía oficial menciona **NetBeans y GlassFish**, mientras este proyecto cumple la preferencia de **VS Code + Spring Boot (Tomcat embebido)**. Si tu profesor considera GlassFish obligatorio, solicita validación o adapta el despliegue a WAR. **No afirmes que esto usa GlassFish**.

## 1. Requisitos

- Java JDK **17 o superior** (`java -version`). Se ha elegido Spring Boot **3.5.16**.
- **Maven** (`mvn -version`) o integración Maven en VS Code.
- **MySQL Server 8.x** iniciado y MySQL Workbench para ejecutar SQL.
- VS Code y extensiones **Extension Pack for Java** + **Spring Boot Extension Pack**.

## 2. Crear la base de datos (MySQL Workbench)

1. Abre MySQL Workbench y entra a tu conexión local (`localhost:3306`).
2. Abre `sql/crear_base_datos.sql` y ejecútalo con el icono de rayo:
   ```sql
   CREATE DATABASE IF NOT EXISTS control_salud_imc
     CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
3. Asegúrate de que MySQL Server está funcionando. El archivo `application.properties` apunta a esa base de datos.
4. **No hace falta crear tablas a mano**: `spring.jpa.hibernate.ddl-auto=update` las genera al iniciar (`usuarios`, `imc`, `directories`, `files`, `notifications`). Si tu profesor quiere el SQL explícito, también puedes ejecutar `sql/estructura_completa_opcional.sql` antes de arrancar (crea las cinco tablas). Se usa esta opción de JPA solo para prácticas académicas; en producción se recomiendan migraciones Flyway y restricciones explícitas.

## 3. Abrir y ejecutar en Visual Studio Code (Windows)

1. Descomprime el ZIP y en VS Code entra a **File > Open Folder > ControlSaludIMC**.
2. Acepta la importación Maven y espera a que descargue las dependencias la primera vez (se requiere internet).
3. Abre la **terminal PowerShell** en VS Code y configura las credenciales de tu MySQL local:
   ```powershell
   $env:DB_USER="root"
   $env:DB_PASSWORD="TU_CONTRASENA_REAL"
   ```
   Si root no tiene contraseña, usa `$env:DB_PASSWORD=""`. Las variables no deben quedar publicadas en GitHub. `application.properties` lee `DB_USER` y `DB_PASSWORD`.
4. Arranca la aplicación:
   ```powershell
   mvn spring-boot:run
   ```
   Alternativamente, abre `ControlSaludImcApplication.java` y usa **Run Java** (asegura antes que VS Code usa las mismas variables de entorno de tu conexión).
5. Abre **http://localhost:8080**.
6. Haz clic en **Regístrate aquí**, crea un usuario de 15 años o más, estatura de 1.00 a 2.50 m. Inicia sesión.
7. En el panel introduce peso positivo, revisa el IMC y el historial. Prueba crear una carpeta, generar un reporte CSV y descargarlo.

**Si el puerto 8080 está ocupado**, cambia `server.port` a `8081` en `application.properties` y abre `http://localhost:8081`.

**Si aparece `Access denied for user root`**, revisa el password en PowerShell. **Si aparece `Communications link failure`**, revisa que MySQL Server esté arrancado y el puerto sea 3306. **Si aparece `Unknown database`**, ejecuta otra vez el SQL de creación.

## 4. Funcionamiento y validaciones

- El registro solicita nombre completo, usuario único, contraseña (mínimo 8 caracteres), edad (>=15), sexo, estatura (1.0-2.5 metros) y peso inicial opcional.
- La contraseña se almacena con **BCrypt** (no en texto plano).
- Al iniciar sesión, Spring Security crea una sesión autenticada; sin sesión no se puede calcular ni ver el historial.
- Cada `POST /api/imc` valida masa corporal >0 y estatura de 1 a 2.5; calcula `IMC = kg / (m*m)` y lo guarda con fecha.
- `GET /api/imc` consulta exclusivamente las mediciones del usuario autenticado y las devuelve por REST. **JavaScript consume el servicio y dibuja la tabla** (CSR, renderizado del lado del cliente); Thymeleaf dibuja la estructura de la vista (MVC).
- Los indicadores de IMC usan categorías **generales de adultos**: <18.5 bajo peso; 18.5-24.99 saludable; 25-29.99 sobrepeso; >=30 obesidad. **No son interpretación clínica válida para adolescentes** y no sustituyen el diagnóstico profesional.
- Cada cálculo crea un aviso `notify`.
- Cada usuario puede crear `directory` (carpetas) y guardar `file` (instantáneas CSV de su histórico en MySQL). La ruta `/api/file/{id}/download` descarga el reporte.
- Los recursos REST aceptan `Accept: application/json` o `Accept: application/xml`, incluyen enlaces `_links` para descubrir relaciones, devuelven errores en ambas representaciones, y usan HTTP `GET/POST/PUT/PATCH/DELETE` según corresponda.

## 5. API REST y métodos

| Recurso | Endpoints | Métodos | Función |
|---|---|---|---|
| user | `/api/user`, `/api/user/me` | POST, GET, PUT, DELETE | Crear cuenta, consultar/actualizar/eliminar perfil |
| imc | `/api/imc`, `/api/imc/{id}` | GET, POST, DELETE | Consultar/guardar mediciones |
| directory | `/api/directory`, `/api/directory/{id}` | GET, POST, PUT, DELETE | Gestionar carpetas |
| file | `/api/file`, `/api/file/{id}`, `/api/file/{id}/download` | GET, POST, PUT, DELETE | Guardar y descargar CSV |
| notify | `/api/notify`, `/api/notify/{id}`, `/api/notify/{id}/read` | GET, PATCH, DELETE | Consultar y gestionar avisos |

**Todos los endpoints salvo POST `/api/user` requieren autenticación.** Los POST/PUT/PATCH/DELETE de una sesión web requieren protección CSRF; el JavaScript envía el token de la página `imc.html`. Para probar en Postman, primero inicia sesión y reutiliza cookies y token CSRF (o usa el navegador, como se indica abajo).

### Ejemplo JSON (en sesión iniciada)

Desde la consola del navegador abierta en `http://localhost:8080/imc`:

```javascript
fetch('/api/imc', {headers:{Accept:'application/json'}})
  .then(r=>r.json()).then(console.log);
```

### Ejemplo XML (en sesión iniciada)

```javascript
fetch('/api/imc', {headers:{Accept:'application/xml'}})
  .then(r=>r.text()).then(console.log);
```

### Error con XML y JSON

Puedes revisar un recurso inexistente después de iniciar sesión:

```javascript
fetch('/api/imc/999999', {headers:{Accept:'application/xml'}})
  .then(async r => console.log(r.status, await r.text()));
```

Cambia `application/xml` por `application/json`. Debe responder HTTP **404** con `status`, `error`, `mensaje`, `ruta` y `fecha`. También se devuelven **400** en validaciones **409** en registros duplicados o carpetas con reportes, y **401/403** en problemas de sesión/permisos.

### MySQL: ver datos guardados

```sql
USE control_salud_imc;
SHOW TABLES;
SELECT id, nombre_completo, usuario, edad, sexo, estatura FROM usuarios;
SELECT id, id_usuario, peso, estatura, valor_imc, estado, fecha FROM imc ORDER BY fecha DESC;
SELECT * FROM directories;
SELECT id, nombre, id_directorio FROM files;
SELECT id, mensaje, leida FROM notifications;
```

## 6. Pruebas Maven

```powershell
mvn test
mvn clean package
```

Las pruebas unitarias incluidas verifican el cálculo, sus límites y clasificación en `BmiCalculatorTest`. La aplicación integral depende de MySQL en ejecución; revisa el plan de pruebas manual en `docs/manual-pruebas.md`.

