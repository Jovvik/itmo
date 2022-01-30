@echo off

@REM mkdir LAB6

ver > ver
wmic ComputerSystem get TotalPhysicalMemory > wmic_total
wmic OS get FreePhysicalMemory > wmic_free
wmic logicaldisk get name, size > wmic_disk

mkdir TEST
xcopy . TEST
cd TEST

type * > total

for /f "skip=1 delims=" %%F in ('dir /b /o-d *') do del "%%F"