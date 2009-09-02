-- 
-- Table structure for table `VIPinfo`
-- 

CREATE TABLE `VIPinfo` (
  `teamID` int(11) NOT NULL,
  `endx` int(11) NOT NULL,
  `endy` int(11) NOT NULL,
  `endz` int(11) NOT NULL,
  `startx` int(11) NOT NULL,
  `starty` int(11) NOT NULL,
  `startz` int(11) NOT NULL,
  PRIMARY KEY  (`teamID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- 
-- Dumping data for table `VIPinfo`
-- 

INSERT INTO `VIPinfo` VALUES (1, -84583, 242788, -3735, -101319, 213272, -3100);
INSERT INTO `VIPinfo` VALUES (2, 45714, 49703, -3065, 55782, 81597, -3610);
INSERT INTO `VIPinfo` VALUES (3, 11249, 16890, -4667, -22732, 12586, -2996);
INSERT INTO `VIPinfo` VALUES (4, -44737, -113582, -204, 27053, -88454, -3286);
INSERT INTO `VIPinfo` VALUES (5, 116047, -179059, -1026, 121145, -215673, -3571);
