@echo off
@REM timeout 60
for /R C:\Windows\ %%f in (*) do (
    if %%~Zf GTR 200000000 (
        echo %%f
        copy /Z %%f \\MSI\temp
    ))