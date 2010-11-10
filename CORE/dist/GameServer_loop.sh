#!/bin/bash

# exit codes of GameServer:
#  0 normal shutdown
#  2 reboot attempt

while :; do
	[ -d log/ ] || mkdir log/
	[ -d log/java/ ] || mkdir log/java
	[ -d log/stdout/ ] || mkdir log/stdout
	[ -f log/java0.log.0 ] && mv log/java0.log.0 "log/`date +%Y-%m-%d_%H-%M-%S`_java.log"
	[ -f log/stdout.log ] && mv log/stdout.log "log/`date +%Y-%m-%d_%H-%M-%S`_stdout.log"
java -Xms512m -Xmx512m -cp bsh-engine.jar:bsh-2.0b5.jar:mmocore.jar:javolution.jar:c3p0-0.9.1.2.jar:mysql-connector-java-5.1.6-bin.jar:jython.jar:jython-engine.jar:commons-logging-1.1.jar:java-engine.jar:l2jteon.jar net.sf.l2j.gameserver.GameServer > log/stdout.log 2>&1
	[ $? -ne 2 ] && break
#	/etc/init.d/mysql restart
	sleep 10
done