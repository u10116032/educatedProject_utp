-- phpMyAdmin SQL Dump
-- version 4.2.11
-- http://www.phpmyadmin.net
--
-- 主機: 127.0.0.1
-- 產生時間： 2016-07-13: 06:19:59
-- 伺服器版本: 5.6.21
-- PHP 版本： 5.6.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- 資料庫： `u10116032`
--

-- --------------------------------------------------------

--
-- 資料表結構 `homework`
--

CREATE TABLE IF NOT EXISTS `homework` (
`id` int(11) NOT NULL,
  `animal` int(11) NOT NULL,
  `plant` int(11) NOT NULL,
  `indicate` text COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 資料表的匯出資料 `homework`
--

INSERT INTO `homework` (`id`, `animal`, `plant`, `indicate`) VALUES
(1, 5, 3, '在台北市立大學找尋指定動物以及植物數目，各位加油=D');

-- --------------------------------------------------------

--
-- 資料表結構 `u10116032`
--

CREATE TABLE IF NOT EXISTS `u10116032` (
`_id` int(11) NOT NULL,
  `date` text COLLATE utf8_unicode_ci,
  `title` text COLLATE utf8_unicode_ci,
  `content` text COLLATE utf8_unicode_ci,
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `photo_dir` text COLLATE utf8_unicode_ci,
  `rec_dir` text COLLATE utf8_unicode_ci,
  `category` text COLLATE utf8_unicode_ci,
  `address` text COLLATE utf8_unicode_ci,
  `score` int(11) DEFAULT NULL,
  `comment` text COLLATE utf8_unicode_ci
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 資料表的匯出資料 `u10116032`
--

INSERT INTO `u10116032` (`_id`, `date`, `title`, `content`, `longitude`, `latitude`, `photo_dir`, `rec_dir`, `category`, `address`, `score`, `comment`) VALUES
(9, '2016-7-12', '麻雀', '好小隻!!', 121.514951, 25.0359787, 'http://163.21.245.192/u10116032/uploads/IMAGE_20160712_154119_-1334221130.jpg', 'http://163.21.245.192/u10116032/uploads/AUDIO_20160712_154122.mp3', '動物', '台灣台北市中正區公園路29', NULL, NULL),
(10, '2016-7-13', '哈士奇', '好可愛哦!', 121.5149503, 25.0359787, 'http://163.21.245.192/u10116032/uploads/IMAGE_20160712_154239_833686086.jpg', 'http://163.21.245.192/u10116032/uploads/AUDIO_20160712_154241.mp3', '動物', '台灣台北市中正區公園路29', NULL, NULL),
(11, '2016-7-12', '蟑螂', '好噁心!!', 121.5149664, 25.036007, 'http://163.21.245.192/u10116032/uploads/IMAGE_20160712_154334_-90040427.jpg', 'http://163.21.245.192/u10116032/uploads/AUDIO_20160712_154339.mp3', '動物', '台灣台北市中正區公園路29', NULL, NULL),
(12, '2016-7-12', '憤怒鳥', '好紅哦', 121.5149526, 25.0359496, 'http://163.21.245.192/u10116032/uploads/IMAGE_20160712_154435_-867391038.jpg', 'http://163.21.245.192/u10116032/uploads/AUDIO_20160712_154437.mp3', '動物', '台灣台北市中正區公園路29', NULL, NULL),
(13, '2016-7-12', '幼稚園小孩', '小孩好可愛哦', 121.513713, 25.036485, 'http://163.21.245.192/u10116032/uploads/IMAGE_20160712_154600_1745769427.jpg', 'http://163.21.245.192/u10116032/uploads/AUDIO_20160712_154604.mp3', '動物', '台灣台北市中正區重慶南路一段130-136', NULL, NULL),
(14, '2016-7-12', '葡萄', '葡萄', 121.51358921080828, 25.03686986632323, 'http://163.21.245.192/u10116032/uploads/IMAGE_20160712_155420_361176014.jpg', 'http://163.21.245.192/u10116032/uploads/AUDIO_20160712_155423.mp3', '植物', '台灣台北市中正區重慶南路一段128', NULL, NULL),
(15, '2016-7-12', '椰子樹', '好大', 121.51424999999999, 25.035548, 'http://163.21.245.192/u10116032/uploads/IMAGE_20160712_155808_1186049218.jpg', 'http://163.21.245.192/u10116032/uploads/AUDIO_20160712_155811.mp3', '植物', '台灣台北市中正區愛國西路1', NULL, NULL),
(16, '2016-7-12', '奇怪的植物', '不知道什麼奇怪植物', 121.5149594, 25.0359994, 'http://163.21.245.192/u10116032/uploads/IMAGE_20160712_160039_-685106683.jpg', 'http://163.21.245.192/u10116032/uploads/AUDIO_20160712_160042.mp3', '植物', '台灣台北市中正區公園路29', NULL, NULL);

-- --------------------------------------------------------

--
-- 資料表結構 `u10116033`
--

CREATE TABLE IF NOT EXISTS `u10116033` (
`_id` int(11) NOT NULL,
  `date` text COLLATE utf8_unicode_ci,
  `title` text COLLATE utf8_unicode_ci,
  `content` text COLLATE utf8_unicode_ci,
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `photo_dir` text COLLATE utf8_unicode_ci,
  `rec_dir` text COLLATE utf8_unicode_ci,
  `category` text COLLATE utf8_unicode_ci,
  `address` text COLLATE utf8_unicode_ci,
  `score` int(11) DEFAULT NULL,
  `comment` text COLLATE utf8_unicode_ci
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 資料表的匯出資料 `u10116033`
--

INSERT INTO `u10116033` (`_id`, `date`, `title`, `content`, `longitude`, `latitude`, `photo_dir`, `rec_dir`, `category`, `address`, `score`, `comment`) VALUES
(1, '2016-7-10', '金魚', '很愛吃東西', 121.4544749, 25.0124494, 'http://163.21.245.192/u10116032/uploads/IMAGE_20160710_133626_-1463012757.jpg', 'http://163.21.245.192/u10116032/uploads/AUDIO_20160710_133640.mp3', '動物', '台灣新北市板橋區中正路69巷87', NULL, NULL);

--
-- 已匯出資料表的索引
--

--
-- 資料表索引 `homework`
--
ALTER TABLE `homework`
 ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `u10116032`
--
ALTER TABLE `u10116032`
 ADD PRIMARY KEY (`_id`);

--
-- 資料表索引 `u10116033`
--
ALTER TABLE `u10116033`
 ADD PRIMARY KEY (`_id`);

--
-- 在匯出的資料表使用 AUTO_INCREMENT
--

--
-- 使用資料表 AUTO_INCREMENT `homework`
--
ALTER TABLE `homework`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- 使用資料表 AUTO_INCREMENT `u10116032`
--
ALTER TABLE `u10116032`
MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=17;
--
-- 使用資料表 AUTO_INCREMENT `u10116033`
--
ALTER TABLE `u10116033`
MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
