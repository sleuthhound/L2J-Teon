-- --------------------------------
-- Table structure for `tvt_teams`
-- Created by SqueezeD from l2jfree
-- --------------------------------
CREATE TABLE IF NOT EXISTS `tvt_teams` (
  `teamId` int(4) NOT NULL default '0',
  `teamName` varchar(255) NOT NULL default '',
  `teamX` int(11) NOT NULL default '0',
  `teamY` int(11) NOT NULL default '0',
  `teamZ` int(11) NOT NULL default '0',
  `teamColor` int(11) NOT NULL default '0',
  PRIMARY KEY (`teamId`)
) DEFAULT CHARSET=utf8;

INSERT INTO tvt_teams VALUES ('0', 'team1', '148434', '46722', '-3411', '255');
INSERT INTO tvt_teams VALUES ('1', 'team2', '150497', '46722', '-3411', '16711680');
