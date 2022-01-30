@REM не запускать

@REM as admin
net share temp=C:\LAB6\share /GRANT:Jovvik,FULL

cp.bat

taskkill /PID pid /F

@REM cmd
fc /B share\boot.wim C:\Windows\RE_DRIVE\RECOVERYCD_ISO\sources\boot.wim

cp.bat