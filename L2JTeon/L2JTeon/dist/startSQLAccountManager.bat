@echo off
color 17
title Account Manager
REM #======================================================================#
REM # You need to set here your JDK/JRE params in case of x64 bits System. #
REM # Remove the "REM" after set PATH variable                             #
REM # If you're not a x64 system user just leave                           # 
REM #======================================================================#
REM set PATH="type here your path to java jdk/jre (including bin folder)"

@java -Djava.util.logging.config.file=console.cfg -cp c3p0-0.9.1.2.jar;l2jteon.jar;mysql-connector-java-5.1.5-bin.jar net.sf.l2j.accountmanager.SQLAccountManager
@pause
