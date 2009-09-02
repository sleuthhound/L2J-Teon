/*
MySQL Backup
Source Host:           localhost
Source Server Version: 5.0.51a-community-nt
Source Database:       l2jdb
Date:                  2008.10.11 19:45:40
*/

SET FOREIGN_KEY_CHECKS=0;
use l2jdb;
#----------------------------
# Table structure for raid_event_spawnlist
#----------------------------
CREATE TABLE `raid_event_spawnlist` (
  `id` int(11) NOT NULL auto_increment,
  `location` varchar(40) NOT NULL default '',
  `raid_locX` int(9) NOT NULL,
  `raid_locY` int(9) NOT NULL,
  `raid_locZ` int(9) NOT NULL,
  `player_locX` int(9) NOT NULL,
  `player_locY` int(9) NOT NULL,
  `player_locZ` int(9) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
#----------------------------
# Records for table raid_event_spawnlist
#----------------------------


insert  into raid_event_spawnlist values 
(1, 'Test', -93287, -251026, -3336, -94347, -251026, -3136), 
(2, 'Test', -87131, -257755, -3336, -88020, -257755, -3136), 
(3, 'Test', 174167, -75329, -5107, 174085, -76703, -5007), 
(4, 'Test', 174252, -88483, -5139, 174242, -86548, -5007), 
(5, 'Test', 174091, -82305, -5123, 174103, -80650, -5007);

