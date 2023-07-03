@echo off

pushd %~dp0\..\..\..\..\Projects\Personal\UtilsManager\UtilsManager
call gradlew fatJar sourcesJar --console=plain
popd
