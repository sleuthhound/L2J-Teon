@echo off
title L2JTeon:  Login Server Console
:start
echo Starting L2JTeon Login Server.
echo.
java -Dfile.encoding=UTF-8 -Xmx64m -cp javolution.jar;mmocore.jar;c3p0-0.9.1.2.jar;mysql-connector-java-5.1.6-bin.jar;l2jteon.jar net.sf.l2j.loginserver.L2LoginServer
if ERRORLEVEL 2 goto restart
if ERRORLEVEL 1 goto error
goto end
:restart
echo.
echo Admin Restarted ...
echo.
goto start
:error
echo.
echo LoginServer terminated abnormaly
echo.
:end
echo.
echo LoginServer terminated
echo.
pause
