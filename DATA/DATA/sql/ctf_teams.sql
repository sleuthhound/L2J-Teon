--
-- Table structure for ctf_teams
-- Created by SqueezeD & Serpent for l2jfree
--
CREATE TABLE IF NOT EXISTS `ctf_teams` (
  `teamId` int(4) NOT NULL default '0',
  `teamName` varchar(255) NOT NULL default '',
  `teamX` int(11) NOT NULL default '0',
  `teamY` int(11) NOT NULL default '0',
  `teamZ` int(11) NOT NULL default '0',
  `teamColor` int(11) NOT NULL default '0',
  `flagX` int(11) NOT NULL default '0',
  `flagY` int(11) NOT NULL default '0',
  `flagZ` int(11) NOT NULL default '0',
  PRIMARY KEY (`teamId`)
) DEFAULT CHARSET=utf8;

INSERT INTO ctf_teams VALUES ('0', 'team1', '174235', '-86421', '-5108', '255', '174692', '-86443', '-5108');
INSERT INTO ctf_teams VALUES ('1', 'team2', '174240', '-89544', '-5107', '16763904', '174503', '-89470', '-5108');
