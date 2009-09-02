ALTER TABLE `characters` ADD `chatban_timer` decimal(20,0) default '0';
ALTER TABLE `characters` ADD `chatban_reason` varchar(255) NOT NULL default '';