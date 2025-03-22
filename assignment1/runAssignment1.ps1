# Project directory
$PROJECT_DIR = Get-Location
$SRC_DIR = "$PROJECT_DIR\src"
$BUILD_DIR = "$PROJECT_DIR\build"

# Remove existing build directory if it exists, then create a new one
if (Test-Path $BUILD_DIR) {
    Remove-Item -Recurse -Force $BUILD_DIR
}
New-Item -ItemType Directory -Path $BUILD_DIR | Out-Null

Write-Host "Compiling all Java files..."

# Compile all .java files directly
$javaFiles = Get-ChildItem -Recurse -Path $SRC_DIR -Filter *.java | Select-Object -ExpandProperty FullName
javac -d $BUILD_DIR -cp $SRC_DIR $javaFiles

if ($LASTEXITCODE -eq 0) {
    Write-Host "Compilation successful!"
    Write-Host "Running the program..."
    java -cp $BUILD_DIR Main.Main
} else {
    Write-Host "Error during compilation!"
    exit 1
}