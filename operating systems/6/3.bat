@REM windows is retarded about file encoding
chcp 65001 >NUL
net start > before

net stop "DNS-клиент"
timeout 3
net start > after

3_.bat
net start "DNS-клиент"

pause
