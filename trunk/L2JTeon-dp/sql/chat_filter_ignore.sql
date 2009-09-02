/*
MySQL Backup
Source Host:           localhost
Source Server Version: 5.0.51a-community-nt
Source Database:       l2jdb
Date:                  2008.10.11 19:39:23
*/

SET FOREIGN_KEY_CHECKS=0;
use l2jdb;
#----------------------------
# Table structure for chat_filter_ignore
#----------------------------
CREATE TABLE `chat_filter_ignore` (
  `id` int(11) NOT NULL auto_increment,
  `word` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
#----------------------------
# No records for table chat_filter_ignore
#----------------------------


