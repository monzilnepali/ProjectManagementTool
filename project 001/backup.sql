/*
SQLyog Professional v12.09 (64 bit)
MySQL - 10.1.31-MariaDB : Database - projectmanagement
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`projectmanagement` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `projectmanagement`;

/*Table structure for table `project` */

DROP TABLE IF EXISTS `project`;

CREATE TABLE `project` (
  `project_id` int(11) NOT NULL AUTO_INCREMENT,
  `project_name` varchar(50) NOT NULL,
  `project_desc` varchar(100) NOT NULL,
  `project_categories` varchar(20) DEFAULT NULL,
  `project_creation` varchar(30) NOT NULL,
  `project_profile_imagepath` varchar(120) DEFAULT NULL,
  PRIMARY KEY (`project_id`)
) ENGINE=InnoDB AUTO_INCREMENT=131 DEFAULT CHARSET=latin1;

/*Data for the table `project` */

insert  into `project`(`project_id`,`project_name`,`project_desc`,`project_categories`,`project_creation`,`project_profile_imagepath`) values (128,'project managment tools','project','project','Thu 2018-07-12 at 08:49','D:\\server\\project managment tools\\project managment tools.jpg'),(130,'manjil new projec','website developement based upon java servelet','web developmenet','Fri 2018-08-10 at 07:47','D:\\server\\manjil new projec\\manjil new projec.png');

/*Table structure for table `projectfile` */

DROP TABLE IF EXISTS `projectfile`;

CREATE TABLE `projectfile` (
  `project_id` int(11) DEFAULT NULL,
  `project_filePath` varchar(100) DEFAULT NULL,
  `project_fileName` varchar(50) DEFAULT NULL,
  `project_fileSize` varchar(20) DEFAULT NULL,
  KEY `project_id` (`project_id`),
  CONSTRAINT `projectfile_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `project` (`project_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `projectfile` */

insert  into `projectfile`(`project_id`,`project_filePath`,`project_fileName`,`project_fileSize`) values (128,'D:\\Server\\project managment tools\\docs\\Screenshot (2).png','Screenshot (2).png','2MB'),(128,'D:\\Server\\project managment tools\\docs\\Screenshot (3).png','Screenshot (3).png','789KB'),(130,'D:\\Server\\manjil new projec\\docs\\Screenshot (38).png','Screenshot (38).png','3MB'),(130,'D:\\Server\\manjil new projec\\docs\\Screenshot (39).png','Screenshot (39).png','2MB');

/*Table structure for table `role` */

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_job` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `role` */

insert  into `role`(`role_id`,`role_job`) values (1,'manager'),(2,'member');

/*Table structure for table `task` */

DROP TABLE IF EXISTS `task`;

CREATE TABLE `task` (
  `task_id` int(11) NOT NULL AUTO_INCREMENT,
  `task_name` varchar(50) NOT NULL,
  `user_id` int(11) NOT NULL,
  `task_desc` varchar(200) DEFAULT NULL,
  `task_deadline` varchar(50) NOT NULL,
  `task_assignDate` varchar(50) DEFAULT NULL,
  `task_priority` varchar(15) NOT NULL,
  `project_id` int(11) NOT NULL,
  `task_status` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`task_id`,`user_id`,`project_id`),
  KEY `user_id` (`user_id`),
  KEY `task_ibfk_1` (`project_id`),
  CONSTRAINT `task_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `project` (`project_id`) ON DELETE CASCADE,
  CONSTRAINT `task_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

/*Data for the table `task` */

insert  into `task`(`task_id`,`task_name`,`user_id`,`task_desc`,`task_deadline`,`task_assignDate`,`task_priority`,`project_id`,`task_status`) values (3,'dfdf',3,NULL,'2018-08-10','Thu 2018-08-09 at 08:32 PM','Normal',128,2),(7,'raju task #1',3,NULL,'2018-08-18','Fri 2018-08-10 at 05:19 PM','High',128,2);

/*Table structure for table `taskcompletefile` */

DROP TABLE IF EXISTS `taskcompletefile`;

CREATE TABLE `taskcompletefile` (
  `task_id` int(11) DEFAULT NULL,
  `task_filePath` varchar(100) DEFAULT NULL,
  `task_fileName` varchar(50) DEFAULT NULL,
  `task_fileSize` varchar(20) DEFAULT NULL,
  KEY `task_id` (`task_id`),
  CONSTRAINT `taskcompletefile_ibfk_1` FOREIGN KEY (`task_id`) REFERENCES `task` (`task_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `taskcompletefile` */

/*Table structure for table `taskfile` */

DROP TABLE IF EXISTS `taskfile`;

CREATE TABLE `taskfile` (
  `task_id` int(11) DEFAULT NULL,
  `task_filePath` varchar(100) DEFAULT NULL,
  `file_name` varchar(50) DEFAULT NULL,
  `file_size` varchar(20) DEFAULT NULL,
  KEY `task_id` (`task_id`),
  CONSTRAINT `taskfile_ibfk_1` FOREIGN KEY (`task_id`) REFERENCES `task` (`task_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `taskfile` */

insert  into `taskfile`(`task_id`,`task_filePath`,`file_name`,`file_size`) values (7,'D:\\server\\128\\task\\Screenshot (44).png','Screenshot (44).png','200KB'),(7,'D:\\server\\128\\task\\Screenshot (45).png','Screenshot (45).png','231KB');

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(25) NOT NULL,
  `user_email` varchar(25) NOT NULL,
  `user_password` varchar(256) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;

/*Data for the table `user` */

insert  into `user`(`user_id`,`user_name`,`user_email`,`user_password`) values (1,'lapzap','lapzap98@gmail.com','8cb2237d0679ca88db6464eac60da96345513964'),(2,'aakash','aakash@gmail.com','f4c948593ed3703a9c9b42235e150d85c8a1e156'),(3,'raju','raju@gmail.com','55fd64d3d40f601a78f7917de5a10e6044a5e74d'),(11,'pikesh','pikesh@gmail.com','f21c8798822be024e374c1045e4d25adeb05b527');

/*Table structure for table `userproject` */

DROP TABLE IF EXISTS `userproject`;

CREATE TABLE `userproject` (
  `project_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '0',
  KEY `userproject_ibfk_1` (`project_id`),
  KEY `userproject_ibfk_2` (`user_id`),
  KEY `userproject_ibfk_3` (`role_id`),
  CONSTRAINT `userproject_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `project` (`project_id`) ON DELETE CASCADE,
  CONSTRAINT `userproject_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `userproject_ibfk_3` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `userproject` */

insert  into `userproject`(`project_id`,`user_id`,`role_id`,`status`) values (128,1,1,1),(128,2,2,0),(128,11,2,0),(128,3,2,0),(130,1,1,1),(130,11,2,0),(130,3,2,0);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
