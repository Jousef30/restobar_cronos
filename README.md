# 🍽️ Restobar Cronos - Sistema de Gestión de Restaurante

Sistema completo de gestión para restaurantes con backend Spring Boot y frontend Angular.

## 📋 Estado del Proyecto

- ✅ Backend configurado con Java 21
- ✅ Base de datos MySQL en Clever Cloud
- ✅ Listo para desplegar en Render
- 🔄 Frontend Angular (desplegar por separado)

## 🚀 Despliegue Rápido en Render

### Opción 1: Usando Render Blueprint (Recomendado)

1. Haz clic en este botón [![Deploy to Render](https://render.com/images/deploy-to-render-button.svg)](https://render.com/deploy)
2. Conecta tu repositorio de GitHub
3. Render detectará automáticamente el archivo `render.yaml`
4. Configura las variables de entorno obligatorias (ver abajo)

### Opción 2: Configuración Manual

Consulta la [**Guía Completa de Despliegue**](./RENDER_DEPLOY_GUIDE.md) para instrucciones detalladas paso a paso.

## 🔑 Variables de Entorno Requeridas

Configura estas variables en el Dashboard de Render:

```bash
# Base de datos Clever Cloud
DATABASE_URL=jdbc:mysql://TU_HOST:3306/TU_DATABASE?useSSL=false&serverTimezone=UTC
DB_USERNAME=tu_usuario
DB_PASSWORD=tu_password

# Autenticación JWT
JWT_SECRET=TuClaveSecretaMuySeguryLarga2024!

# Email (opcional)
MAIL_USERNAME=tu_email@gmail.com
MAIL_PASSWORD=tu_app_password

# MercadoPago (opcional)
MERCADOPAGO_ACCESS_TOKEN=tu_access_token
```

## 📦 Estructura del Proyecto

```
proyecto/
├── demo/                          # 🔴 Backend Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/demo/
│   │   │   │       ├── config/
│   │   │   │       │   ├── SecurityConfig.java
│   │   │   │       │   └── CorsConfig.java   ✅ Nuevo
│   │   │   │       ├── controller/
│   │   │   │       ├── model/
│   │   │   │       ├── repository/
│   │   │   │       └── service/
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   ├── Dockerfile                 ✅ Actualizado a Java 21
│   ├── .dockerignore              ✅ Nuevo
│   └── pom.xml                    (Java 21)
│
├── restobar-frontend/             # 🔵 Frontend Angular
│   └── src/
│
├── system.properties              ✅ Nuevo (Java 21 para Render)
├── render.yaml                    ✅ Nuevo (Blueprint de Render)
├── RENDER_DEPLOY_GUIDE.md        ✅ Guía detallada
└── README.md                      ✅ Este archivo
```

## 🛠️ Tecnologías Utilizadas

### Backend
- **Java 21**
- **Spring Boot 3.5.6**
- **Spring Security** + **JWT**
- **Spring Data JPA**
- **MySQL** (Clever Cloud)
- **Maven**

### Frontend
- **Angular**
- **TypeScript**
- **Bootstrap/TailwindCSS**

## 🔧 Desarrollo Local

### Prerrequisitos
- Java 21
- Maven 3.8+
- MySQL 8.0+
- Node.js 18+ (para el frontend)

### Backend
```bash
cd demo
./mvnw spring-boot:run
```

### Frontend
```bash
cd restobar-frontend
npm install
ng serve
```

## 📚 Documentación

- [Guía de Despliegue en Render](./RENDER_DEPLOY_GUIDE.md)
- [Configuración de CORS](./demo/src/main/java/com/example/demo/config/CorsConfig.java)

## 🐛 Solución de Problemas

### Error: "release version 21 not supported"
✅ **SOLUCIONADO**: Hemos actualizado el Dockerfile y creado `system.properties` con Java 21.

### Error de conexión a la base de datos
1. Verifica las credenciales de Clever Cloud
2. Asegúrate de que la URL tenga el formato correcto
3. Confirma que las variables de entorno estén configuradas en Render

### Problemas de CORS
1. Actualiza `CorsConfig.java` con tu dominio de frontend
2. Verifica que el frontend esté usando la URL correcta del backend

## 👥 Contribuidores

- **Jousef** - Desarrollo principal

## 📄 Licencia

Este proyecto es privado y está bajo desarrollo académico.

---

## ✅ Checklist Pre-Despliegue

Antes de desplegar a Render, asegúrate de:

- [ ] Tener la base de datos MySQL activa en Clever Cloud
- [ ] Tener las credenciales de conexión a mano
- [ ] Haber probado el proyecto localmente
- [ ] Haber subido todos los cambios a GitHub
- [ ] Actualizar CorsConfig.java con el dominio del frontend
- [ ] Generar un JWT_SECRET seguro

## 🚀 Próximos Pasos

Después de desplegar el backend:

1. Desplegar el frontend en Vercel/Netlify
2. Actualizar CORS con el dominio del frontend
3. Configurar un dominio personalizado (opcional)
4. Configurar monitoreo y logs

---

**¿Listo para desplegar?** 👉 [Sigue la Guía de Despliegue](./RENDER_DEPLOY_GUIDE.md)
