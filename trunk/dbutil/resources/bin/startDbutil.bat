@echo off
set CURRENT_DIR=%cd%
if not "%DBUTIL_HOME%" == "" goto gotHome
set DBUTIL_HOME=%CURRENT_DIR%
if exist "%DBUTIL_HOME%\bin\startDbutil.bat" goto okHome
cd ..
set DBUTIL_HOME=%cd%
cd %CURRENT_DIR%
:gotHome
if exist "%DBUTIL_HOME%\bin\startDbutil.bat" goto okHome
echo The DBUTIL_HOME environment variable is not defined correctly
echo This environment variable is needed to run this program
goto end
:okHome
rem ORACLE_HOME=
rem OPMN_LIB=c:\oracle\product\10.1.0\Companion\opmn\lib
set DBUTIL_CLASSPATH="%DBUTIL_HOME%\lib"
set dir="%DBUTIL_HOME%\lib"
set DBUTIL_CLASSPATH=%dir%
for %%i in ("%dir%\*.jar") do call :setone "%%i"
rem start javaw -DDBUTIL_HOME=%DBUTIL_HOME% -Doracle.ons.oraclehome=%ORACLE_HOME% -Xmx150M -Xms150M -cp %DBUTIL_CLASSPATH% components.db.StartMe

start javaw -DDBUTIL_HOME=%DBUTIL_HOME% -Xmx150M -Xms150M -cp %DBUTIL_CLASSPATH% org.wsm.database.tools.StartMe

:setone
set DBUTIL_CLASSPATH=%DBUTIL_CLASSPATH%;%1%
:end