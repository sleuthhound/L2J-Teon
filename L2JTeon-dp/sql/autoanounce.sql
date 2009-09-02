CREATE TABLE `auto_announcements` (
`id` int(11) NOT NULL auto_increment,
`announcement` varchar(255) collate latin1_general_ci NOT NULL,
`delay` int(11) NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci AUTO_INCREMENT=1 ;
