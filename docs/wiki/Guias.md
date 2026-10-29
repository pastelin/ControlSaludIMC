# Guías

## Instalación

1. JDK 17+, Maven y MySQL Server.
2. Crear BD con `sql/crear_base_datos.sql` en MySQL Workbench.
3. Configurar las variables de entorno `DB_USER` y `DB_PASSWORD`.
4. En VS Code abrir la carpeta y ejecutar `mvn spring-boot:run`.
5. Navegar a `http://localhost:8080`.

## Uso

Regístrate con edad >=15 y estatura 1-2.5 m. Inicia sesión. Introduce peso >0 y estatura, calcula el IMC y comprueba el historial. Crea carpetas y descarga reportes CSV desde el panel.

## Probar XML

Después de iniciar sesión, en la consola del navegador:

```javascript
fetch('/api/imc', {headers:{Accept:'application/xml'}})
  .then(r=>r.text()).then(console.log);
```

## Errores

- `Access denied`: revisar usuario/contraseña de MySQL.
- `Unknown database`: ejecutar el SQL de creación.
- `Communications link failure`: verificar que MySQL Server está iniciado.
- Error 401 de API: iniciar sesión.
- Error 400 al calcular: revisar peso y estatura.

Más detalles: README principal y `docs/manual-pruebas.md`.
