@echo off
title L2JTeon: Game Server Console
:start
echo Starting L2JTeon Game Server.
echo Visit L2JTeon.servergame.com for updates.
echo.
REM ------------------------------------------------------------------------
REM #======================================================================#
REM # You need to set here your JDK/JRE params in case of x64 bits System. #
REM # Remove the "REM" after set PATH variable                             #
REM # If you're not a x64 system user just leave                           # 
REM #======================================================================#
REM set PATH="type here your path to java jdk/jre (including bin folder)"
REM ------------------------------------------------------------------------

REM -------------------------------------
REM Default parameters for a basic server.
java -Dfile.encoding=UTF-8 -Xmx512m -cp bsf.jar;bsh-engine.jar;bsh-2.0b5.jar;mmocore.jar;javolution.jar;c3p0-0.9.1.2.jar;mysql-connector-java-5.1.6-bin.jar;l2jteon.jar;jython.jar;jython-engine.jar;commons-logging-1.1.jar;java-engine.jar net.sf.l2j.gameserver.GameServer
REM
REM If you have a big server and lots of memory, you could experiment for example with
REM java -server -Xmx1536m -Xms1024m -Xmn512m -XX:PermSize=256m -XX:SurvivorRatio=8 -Xnoclassgc -XX:+AggressiveOpts
REM -------------------------------------
if ERRORLEVEL 2 goto restart
if ERRORLEVEL 1 goto error
goto end
:restart
echo.
echo Administrator Restarted ...
echo.
goto start
:error
echo.
echo GameServer Terminated Abnormaly, Please Verify Your Files.
echo.
:end
echo.
echo GameServer Terminated.
echo.
pause
