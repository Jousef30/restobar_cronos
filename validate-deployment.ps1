# Script de Validación Pre-Despliegue
# Ejecutar: .\validate-deployment.ps1

Write-Host "Validando configuracion para despliegue en Render..." -ForegroundColor Cyan
Write-Host ""

$errors = @()
$warnings = @()
$success = @()

# Verificar que estamos en el directorio correcto
if (-Not (Test-Path ".\demo\pom.xml")) {
    Write-Host "Error: Ejecuta este script desde la raiz del proyecto" -ForegroundColor Red
    exit 1
}

Write-Host "Verificando archivos necesarios..." -ForegroundColor Yellow
Write-Host ""

# 1. Verificar system.properties
if (Test-Path ".\system.properties") {
    $content = Get-Content ".\system.properties" -Raw
    if ($content -match "java.runtime.version=21") {
        $success += "OK - system.properties configurado correctamente (Java 21)"
    } else {
        $errors += "ERROR - system.properties existe pero no especifica Java 21"
    }
} else {
    $errors += "ERROR - Falta archivo system.properties"
}

# 2. Verificar Dockerfile
if (Test-Path ".\demo\Dockerfile") {
    $content = Get-Content ".\demo\Dockerfile" -Raw
    if ($content -match "eclipse-temurin:21") {
        $success += "OK - Dockerfile usa Java 21"
    } else {
        $errors += "ERROR - Dockerfile no usa Java 21"
    }
} else {
    $errors += "ERROR - Falta archivo Dockerfile"
}

# 3. Verificar pom.xml
if (Test-Path ".\demo\pom.xml") {
    $content = Get-Content ".\demo\pom.xml" -Raw
    if ($content -match "<java.version>21</java.version>") {
        $success += "OK - pom.xml configurado para Java 21"
    } else {
        $errors += "ERROR - pom.xml no especifica Java 21"
    }
} else {
    $errors += "ERROR - Falta archivo pom.xml"
}

# 4. Verificar .dockerignore
if (Test-Path ".\demo\.dockerignore") {
    $success += "OK - .dockerignore presente"
} else {
    $warnings += "ADVERTENCIA - Falta .dockerignore (opcional pero recomendado)"
}

# 5. Verificar CorsConfig
if (Test-Path ".\demo\src\main\java\com\example\demo\config\CorsConfig.java") {
    $success += "OK - CorsConfig.java configurado"
} else {
    $warnings += "ADVERTENCIA - Falta CorsConfig.java (podrias tener problemas de CORS)"
}

# 6. Verificar render.yaml
if (Test-Path ".\render.yaml") {
    $success += "OK - render.yaml presente (Blueprint)"
} else {
    $warnings += "ADVERTENCIA - Falta render.yaml (puedes hacer despliegue manual)"
}

# 7. Verificar application.properties
if (Test-Path ".\demo\src\main\resources\application.properties") {
    $success += "OK - application.properties presente"
} else {
    $errors += "ERROR - Falta application.properties"
}

# 8. Verificar Maven Wrapper
if (Test-Path ".\demo\mvnw") {
    $success += "OK - Maven Wrapper presente"
} else {
    $errors += "ERROR - Falta Maven Wrapper (mvnw)"
}

# Resultados
Write-Host ""
Write-Host "================================================================" -ForegroundColor Cyan
Write-Host "                    RESULTADOS DE VALIDACION                    " -ForegroundColor Cyan
Write-Host "================================================================" -ForegroundColor Cyan
Write-Host ""

# Most exitosos
if ($success.Count -gt 0) {
    Write-Host "EXITOSOS ($($success.Count)):" -ForegroundColor Green
    foreach ($item in $success) {
        Write-Host "   $item" -ForegroundColor Green
    }
    Write-Host ""
}

# Mostrar advertencias
if ($warnings.Count -gt 0) {
    Write-Host "ADVERTENCIAS ($($warnings.Count)):" -ForegroundColor Yellow
    foreach ($item in $warnings) {
        Write-Host "   $item" -ForegroundColor Yellow
    }
    Write-Host ""
}

# Mostrar errores
if ($errors.Count -gt 0) {
    Write-Host "ERRORES ($($errors.Count)):" -ForegroundColor Red
    foreach ($item in $errors) {
        Write-Host "   $item" -ForegroundColor Red
    }
    Write-Host ""
}

# Conclusion
Write-Host "================================================================" -ForegroundColor Cyan
if ($errors.Count -eq 0) {
    Write-Host "Proyecto listo para desplegar en Render!" -ForegroundColor Green
    Write-Host ""
    Write-Host "Proximos pasos:" -ForegroundColor Cyan
    Write-Host "   1. Asegurate de tener las credenciales de Clever Cloud" -ForegroundColor White
    Write-Host "   2. Sube los cambios a GitHub" -ForegroundColor White
    Write-Host "   3. Ve a https://dashboard.render.com/" -ForegroundColor White
    Write-Host "   4. Crea un nuevo Web Service" -ForegroundColor White
    Write-Host "   5. Conecta tu repositorio: https://github.com/Jousef30/restobar_cronos" -ForegroundColor White
    Write-Host "   6. Configura las variables de entorno" -ForegroundColor White
    Write-Host ""
    Write-Host "Para mas detalles, consulta: RENDER_DEPLOY_GUIDE.md" -ForegroundColor Cyan
} else {
    Write-Host "Hay $($errors.Count) error(es) que debes corregir antes de desplegar" -ForegroundColor Red
    Write-Host "Por favor, revisa los errores listados arriba" -ForegroundColor Yellow
}
Write-Host "================================================================" -ForegroundColor Cyan
Write-Host ""
