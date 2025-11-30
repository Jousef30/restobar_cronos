# Guía de Despliegue en Render

## 🎯 Resumen
Este proyecto es un backend de Spring Boot que requiere Java 21 y MySQL.

## 📋 Cambios Realizados

### ✅ Archivos Actualizados/Creados:
1. **`Dockerfile`**: Actualizado de Java 17 a Java 21
2. **`system.properties`**: Creado para especificar Java 21 a Render
3. **Este archivo**: Documentación de despliegue

---

## 🚀 Pasos para Desplegar en Render

### 1. Preparar el Repositorio Git

Primero, necesitas subir los cambios a tu repositorio de GitHub:

```bash
cd c:\Users\Jousef\Documents\UTP\CICLO6\APLICATIVO_WEB_INTEGRADO\proyecto
git add .
git commit -m "Actualizar configuración para Render con Java 21"
git push origin main
```

### 2. Configurar el Web Service en Render

1. Ve a [Render Dashboard](https://dashboard.render.com/)
2. Haz clic en **"New +"** → **"Web Service"**
3. Conecta tu repositorio: `https://github.com/Jousef30/restobar_cronos`
4. Configura los siguientes campos:

#### Configuración Básica:
- **Name**: `restobar-cronos-backend` (o el nombre que prefieras)
- **Region**: Selecciona la más cercana a tus usuarios
- **Branch**: `main` (o la rama que uses)
- **Root Directory**: `demo`
- **Runtime**: `Docker`
- **Instance Type**: `Free` (para empezar)

#### Build Configuration:
- **Dockerfile Path**: `demo/Dockerfile`

### 3. Configurar Variables de Entorno

En Render Dashboard, ve a **Environment** y agrega las siguientes variables:

#### ⚠️ IMPORTANTE - Variables Obligatorias:

```
# Base de datos (obtenidas de Clever Cloud)
DATABASE_URL=jdbc:mysql://<tu-host-clever-cloud>:<puerto>/<nombre-db>?useSSL=false&serverTimezone=UTC
DB_USERNAME=<tu-usuario-clever-cloud>
DB_PASSWORD=<tu-password-clever-cloud>

# Puerto (Render lo asigna automáticamente)
PORT=8080

# JWT Secret (genera una clave segura)
JWT_SECRET=TuClaveSecretaSuperSeguraYLargaParaJWT2024!

# MercadoPago (opcional si usas pagos)
MERCADOPAGO_ACCESS_TOKEN=<tu-access-token-de-mercadopago>

# Email (opcional si usas correos)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=<tu-correo@gmail.com>
MAIL_PASSWORD=<tu-contraseña-de-aplicación>
```

#### 📝 Cómo obtener el DATABASE_URL de Clever Cloud:

1. Ve a tu dashboard de Clever Cloud
2. Selecciona tu base de datos MySQL
3. Busca la información de conexión:
   - **Host**: Algo como `xxxxx.mysql.db.clever-cloud.com`
   - **Port**: Normalmente `3306`
   - **Database**: Nombre de tu base de datos
   - **Username**: Tu usuario
   - **Password**: Tu contraseña

4. Construye el URL así:
   ```
   jdbc:mysql://HOST:PORT/DATABASE?useSSL=false&serverTimezone=UTC
   ```

### 4. Verificar el archivo application.properties

Tu aplicación usa `application.properties` que está en `.gitignore`. Asegúrate de que en tu repositorio existe el archivo `application.properties.example` como referencia.

Para producción, la aplicación tomará las variables de entorno que configuraste en Render.

### 5. Desplegar

1. Haz clic en **"Create Web Service"**
2. Render comenzará a:
   - Clonar tu repositorio
   - Construir la imagen Docker con Java 21
   - Ejecutar tu aplicación

3. Monitorea los logs en tiempo real para ver el progreso

### 6. Verificar el Despliegue

Una vez que el despliegue termine:

1. Render te dará una URL como: `https://restobar-cronos-backend.onrender.com`
2. Prueba la API:
   ```
   https://tu-app.onrender.com/api/health
   ```
   (si tienes un endpoint de health check)

---

## 🔧 Solución de Problemas

### Error: "release version 21 not supported"
✅ **SOLUCIONADO**: Actualizamos el Dockerfile a Java 21 y creamos `system.properties`

### Error: "Could not connect to database"
- Verifica que las variables de entorno estén correctamente configuradas
- Asegúrate de que la URL de Clever Cloud sea accesible desde Render
- Verifica que el usuario y contraseña sean correctos

### Error: "Application failed to start"
- Revisa los logs en Render Dashboard
- Verifica que todas las variables de entorno obligatorias estén configuradas
- Asegúrate de que tu código compile correctamente localmente primero

### La aplicación se duerme (Free tier)
- En el plan gratuito, Render apaga las aplicaciones después de 15 minutos de inactividad
- La primera petición después de dormir puede tardar 30-60 segundos

---

## 📦 Estructura del Proyecto

```
proyecto/
├── demo/                          # Backend Spring Boot
│   ├── src/
│   ├── pom.xml                   # Java 21 configurado
│   ├── Dockerfile                # ✅ Actualizado a Java 21
│   └── application.properties.example
├── restobar-frontend/            # Frontend Angular (desplegar separado)
├── system.properties             # ✅ Creado (Java 21)
└── RENDER_DEPLOY_GUIDE.md       # Este archivo
```

---

## 🌐 Configurar CORS (si es necesario)

Si tu frontend está en un dominio diferente, asegúrate de tener configurado CORS en tu `SecurityConfig.java`:

```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList(
        "https://tu-frontend.vercel.app",
        "https://tu-frontend.render.com",
        "http://localhost:4200"  // Para desarrollo
    ));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true);
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
}
```

---

## 📱 Desplegar el Frontend

El frontend Angular debe desplegarse por separado. Opciones recomendadas:
- **Vercel** (recomendado para Angular)
- **Netlify**
- **Render** (también soporta sitios estáticos)

---

## 📞 Contacto y Soporte

Si tienes problemas:
1. Revisa los logs en Render Dashboard
2. Verifica que Clever Cloud esté funcionando
3. Asegúrate de que el código compile localmente
4. Revisa que todas las variables de entorno estén configuradas

---

## ✅ Checklist Final

Antes de desplegar, verifica:

- [ ] Código subido a GitHub
- [ ] Dockerfile actualizado a Java 21
- [ ] system.properties creado
- [ ] Variables de entorno de Clever Cloud a mano
- [ ] JWT_SECRET generado
- [ ] CORS configurado correctamente
- [ ] Endpoint de prueba funcional

¡Buena suerte con el despliegue! 🚀
