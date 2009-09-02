/*
MySQL Backup
Source Host:           localhost
Source Server Version: 5.0.51a-community-nt
Source Database:       l2jdb
Date:                  2008.10.11 19:37:14
*/

SET FOREIGN_KEY_CHECKS=0;
use l2jdb;
#----------------------------
# Table structure for auto_announcements
#----------------------------
CREATE TABLE `auto_announcements` (
  `id` int(11) NOT NULL auto_increment,
  `announcement` varchar(255) collate latin1_general_ci NOT NULL,
  `delay` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;
#----------------------------
# No records for table auto_announcements
#----------------------------


