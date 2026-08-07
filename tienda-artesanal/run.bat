@echo off
echo ========================================================
echo   Iniciando Aplicacion Tienda Artesanal (JavaFX MVC)
echo ========================================================
cd /d "%~dp0"
mvn clean javafx:run
pause
