@echo off

pushd %~dp0\..\..\UtilsManager\UtilsManager
call gradlew fatJar sourcesJar --console=plain
popd
