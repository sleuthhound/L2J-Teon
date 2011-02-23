#!/bin/sh
java -Djava.util.logging.config.file=console.cfg -cp c3p0-0.9.1.2.jar:l2jteon.jar:mysql-connector-java-5.1.6-bin.jar net.sf.l2j.accountmanager.SQLAccountManager
