# Proyecto

## Arquitectura

Modelo: entidades JPA `Usuario`, `Medicion`, `Directorio`, `Archivo` y `Notificacion`; servicios de negocio; repositorios Spring Data JPA.

Vista: `login.html`, `registro.html`, `imc.html`, estilos CSS y código JavaScript que consume el servicio REST de historial.

Controlador: `PaginasController` y cinco controladores de recursos (`user`, `imc`, `directory`, `file`, `notify`).

## Datos

MySQL en `localhost:3306/control_salud_imc` con una relación 1:N de usuarios a mediciones. La contraseña se guarda como hash BCrypt.

## Hipermedia y formatos

Los endpoints tienen `_links` con relaciones de recursos y métodos HTTP. Se puede solicitar `Accept: application/json` o `Accept: application/xml`. Errores de validación y recursos inexistentes se serializan según el `Accept`.

## Versionado

Ramas `master`, `develop` y `feature/*` con merges `--no-ff`; versión estable `v1.0.0` al integrarse y probarse.
