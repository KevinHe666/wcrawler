CREATE DATABASE `summer` /*!40100 DEFAULT CHARACTER SET utf8 */;

CREATE TABLE `crawler_url` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `url` varchar(45) NOT NULL,
  `parent` int(11) DEFAULT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `url_UNIQUE` (`url`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `proxy`;
CREATE TABLE `proxy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(20) NOT NULL,
  `port` varchar(10) NOT NULL,
  `success_probability` double(255,3) DEFAULT NULL,
  `success_times` int(11) DEFAULT '0',
  `failure_times` int(11) DEFAULT NULL,
  `last_success_timestamp` bigint(20) DEFAULT NULL,
  `last_success_time_consume` bigint(20) DEFAULT NULL,
  `avg_success_time_consume` bigint(20) DEFAULT NULL,
  `store_status` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=112 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `zh_user`;
CREATE TABLE `zh_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` int(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `url_token` varchar(255) NOT NULL,
  `headline` varchar(1024) DEFAULT NULL,
  `business_name` varchar(255) DEFAULT NULL,
  `educations_school_name` varchar(255) DEFAULT NULL,
  `locations_name` varchar(255) DEFAULT NULL,
  `link_person` varchar(255) DEFAULT NULL,
  `follower_count` int(255) DEFAULT NULL,
  `following_count` int(255) DEFAULT NULL,
  `voteup_count` int(255) DEFAULT NULL,
  `thanked_count` int(255) DEFAULT NULL,
  `answer_count` int(255) DEFAULT NULL,
  `question_count` int(255) DEFAULT NULL,
  `articles_count` int(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11679 DEFAULT CHARSET=utf8;

-- Table structure for follow_relation
-- ----------------------------
DROP TABLE IF EXISTS `follow_relation`;
CREATE TABLE `follow_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `follower` varchar(1024) NOT NULL DEFAULT '',
  `followee` varchar(1024) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
