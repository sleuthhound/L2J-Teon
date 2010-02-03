--
-- Table structure for 'chat_filter_words'
--
CREATE TABLE IF NOT EXISTS `chat_filter_words` (
  `id` int(11) NOT NULL auto_increment,
  `word` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
);

--
-- Table structure for 'chat_filter_ignore'
--
CREATE TABLE IF NOT EXISTS `chat_filter_ignore` (
  `id` int(11) NOT NULL auto_increment,
  `word` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
);

--
-- Table structure for 'chat_filter_pretext'
--
CREATE TABLE IF NOT EXISTS `chat_filter_pretext` (
  `id` int(11) NOT NULL auto_increment,
  `word` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
);