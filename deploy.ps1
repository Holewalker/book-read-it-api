# Configuración
$projectId = "bookreadit-api"
$region = "europe-west1"
$repoName = "bookreadit-repo"
$imageName = "bookreadit-api"
$imagePath = "$region-docker.pkg.dev/$projectId/$repoName/$imageName"
$envFile = ".\aws.env"

# Cargar variables AWS desde archivo .env
Get-Content $envFile | ForEach-Object {
    if ($_ -match "^(?<key>[^=]+)=(?<value>.*)$") {
        $key = $matches['key'].Trim()
        $value = $matches['value'].Trim()
        Set-Variable -Name $key -Value $value
    }
}
# Mostrar las claves cargadas
Write-Host "AWS_ACCESS_KEY_ID: $AWS_ACCESS_KEY_ID"
Write-Host "AWS_SECRET_ACCESS_KEY: $AWS_SECRET_ACCESS_KEY"

$awsAccessKey = $AWS_ACCESS_KEY_ID
$awsSecretKey = $AWS_SECRET_ACCESS_KEY


Write-Host "1. Limpiando y compilando el proyecto..."
mvn clean package

if ($LASTEXITCODE -ne 0) {
    Write-Host "Error: Falló la compilación. Abortando." 
    exit 1
}

Write-Host "Compilación completada correctamente."

Write-Host "2. Construyendo la imagen Docker..."
docker build -t $imagePath .

if ($LASTEXITCODE -ne 0) {
    Write-Host "Error: Falló la construcción de la imagen. Abortando." 
    exit 1
}

Write-Host "Imagen Docker creada correctamente."

Write-Host "3. Subiendo la imagen a Artifact Registry..."
docker push $imagePath

if ($LASTEXITCODE -ne 0) {
    Write-Host "Error: Falló el envío de la imagen a Artifact Registry. Abortando." 
    exit 1
}

Write-Host "Imagen subida correctamente a Artifact Registry."

Write-Host "4. Desplegando en Cloud Run..."
gcloud run deploy $imageName `
  --image $imagePath `
  --region $region `
  --platform managed `
  --allow-unauthenticated `
  --port 8080 `
  --memory 512Mi `
  --set-env-vars "AWS_ACCESS_KEY_ID=$awsAccessKey,AWS_SECRET_ACCESS_KEY=$awsSecretKey"

if ($LASTEXITCODE -ne 0) {
    Write-Host "Error: Falló el despliegue en Cloud Run. Revisa los errores para más información." 
    exit 1
}

Write-Host "Despliegue completado con éxito." -ForegroundColor Green
