#!/bin/bash

err=1
until [ $err == 0 ]; 
do
	[ -d log/ ] || mkdir log/
	[ -d log/backup/ ] || mkdir log/backup
	[ -d log/java/ ] || mkdir log/java
	[ -d log/stdout/ ] || mkdir log/stdout
	[ -f log/java0.log.0 ] && mv log/java0.log.0 "log/backup/`date +%Y-%m-%d_%H-%M-%S`_java.log"
	[ -f log/stdout.log ] && mv log/stdout.log "log/backup/`date +%Y-%m-%d_%H-%M-%S`_stdout.log"
	java -Xms64m -Xmx128m -cp javolution.jar:mmocore.jar:c3p0-0.9.1.2.jar:mysql-connector-java-5.1.6-bin.jar:l2jteon.jar net.sf.l2j.loginserver.L2LoginServer > log/stdout.log 2>&1
 	err=$?
#	/etc/init.d/mysql restart
	sleep 10;
done
