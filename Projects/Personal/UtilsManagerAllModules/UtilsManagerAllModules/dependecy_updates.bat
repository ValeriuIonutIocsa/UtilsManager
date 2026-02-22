@echo off

pushd %~dp0
call gradlew dependencyUpdates --no-parallel --console=plain
popd
