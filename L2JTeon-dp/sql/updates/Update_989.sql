ALTER TABLE `characters` DROP tvt_ganados;
ALTER TABLE `characters` DROP participaciones_tvt;
ALTER TABLE `characters` ADD `event_points` DECIMAL( 11,0 )default NULL AFTER clan_create_expiry_time;
