-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: 10.41.84.94    Database: kleannara
-- ------------------------------------------------------
-- Server version	11.4.4-MariaDB-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `id` varchar(45) DEFAULT NULL,
  `password` varchar(200) DEFAULT NULL,
  `reg_date` datetime NOT NULL DEFAULT current_timestamp(),
  `mod_date` datetime DEFAULT NULL,
  `auth_menu1` varchar(1) DEFAULT 'Y' COMMENT 'main관리',
  `auth_menu2` varchar(2) DEFAULT 'Y' COMMENT '사업소개 관리',
  `auth_menu3` varchar(3) DEFAULT 'Y' COMMENT 'ESG경영 관리',
  `auth_menu4` varchar(4) DEFAULT 'Y' COMMENT '투자정보 관리',
  `auth_menu5` varchar(5) DEFAULT 'Y' COMMENT '뉴스룸 관리',
  `auth_menu6` varchar(6) DEFAULT 'Y' COMMENT '고객센터 관리',
  `auth_menu7` varchar(7) DEFAULT 'Y' COMMENT '관리자 권한 관리',
  `del_yn` varchar(1) DEFAULT 'N' COMMENT '삭제여부',
  `passwordChangeFlg` varchar(10) NOT NULL DEFAULT 'N' COMMENT '패스워드 변경 플래그 (N이면 변경요청)',
  `passwordChangeDate` datetime DEFAULT current_timestamp() COMMENT '비밀번호 변경 날짜',
  `status` int(11) NOT NULL DEFAULT 0,
  `last_login_date` datetime DEFAULT NULL COMMENT '마지막 로그인 날짜',
  `login_allow` tinyint(4) NOT NULL DEFAULT 0,
  `auth_menu8` varchar(1) DEFAULT 'Y' COMMENT '부조리신고 센터 관리',
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci COMMENT='관리자 관리';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `adminLog`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `adminLog` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `id` varchar(45) DEFAULT NULL,
  `type` varchar(10) DEFAULT '' COMMENT 'C/R/U/D/I/O',
  `serviceName` varchar(100) DEFAULT '' COMMENT '서비스명',
  `serviceTarget` varchar(100) DEFAULT '' COMMENT '대상',
  `referer` varchar(500) DEFAULT '' COMMENT 'referer',
  `action` varchar(500) DEFAULT '' COMMENT 'action',
  `memo` longtext DEFAULT '' COMMENT '내용',
  `ip` varchar(100) DEFAULT '' COMMENT '요청IP',
  `reg_date` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=4216 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `banner`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `banner` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(1) DEFAULT '2' COMMENT '배너타입 1:영상 2:이미지',
  `title` varchar(500) DEFAULT NULL COMMENT '헤드라인',
  `file_path` varchar(100) DEFAULT NULL COMMENT '파일물리경로',
  `file_name` varchar(100) DEFAULT NULL COMMENT '파일이름',
  `video_url` varchar(100) DEFAULT NULL COMMENT '영상url',
  `link_title` varchar(100) DEFAULT NULL COMMENT '링크버튼명',
  `link_url` varchar(100) DEFAULT NULL COMMENT '링크 url',
  `show_yn` varchar(1) DEFAULT 'Y' COMMENT '노출여부',
  `reg_date` datetime DEFAULT current_timestamp() COMMENT '등록일',
  `mod_date` datetime DEFAULT NULL COMMENT '수정일',
  `show_order` int(11) DEFAULT 1 COMMENT '노출순서',
  `del_yn` varchar(1) DEFAULT 'N' COMMENT '삭제여부',
  `file_path_ta` varchar(100) DEFAULT NULL COMMENT '테블릿파일경로',
  `file_name_ta` varchar(100) DEFAULT NULL COMMENT '테블릿파일이름',
  `file_path_mo` varchar(100) DEFAULT NULL COMMENT '모바일파일경로',
  `file_name_mo` varchar(100) DEFAULT NULL COMMENT '모바일파일이름',
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='배너관리';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `brand`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `brand` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(1) DEFAULT NULL COMMENT '1: 용지 2:ps 3:hl',
  `title_ko` varchar(200) DEFAULT NULL COMMENT '제목',
  `title_en` varchar(200) DEFAULT NULL COMMENT '제목',
  `title_zh` varchar(200) DEFAULT NULL COMMENT '제목',
  `show_order` int(11) DEFAULT 1 COMMENT '노출순서',
  `image_path` varchar(100) DEFAULT NULL COMMENT '이미지 경로',
  `image_name` varchar(100) DEFAULT NULL COMMENT '이미지 이름',
  `insta_url` varchar(100) DEFAULT NULL COMMENT '인스타url',
  `shop_url` varchar(100) DEFAULT NULL COMMENT '직영몰url',
  `sub_copy_ko` varchar(1000) DEFAULT NULL COMMENT '서브카피',
  `sub_copy_en` varchar(1000) DEFAULT NULL COMMENT '서브카피',
  `sub_copy_zh` varchar(1000) DEFAULT NULL COMMENT '서브카피',
  `headline_ko` varchar(200) DEFAULT NULL COMMENT '헤드라인',
  `headline_en` varchar(200) DEFAULT NULL COMMENT '헤드라인',
  `headline_zh` varchar(200) DEFAULT NULL COMMENT '헤드라인',
  `show_yn` varchar(1) DEFAULT 'Y' COMMENT '노출여부',
  `reg_admin` int(11) DEFAULT NULL COMMENT '작성자',
  `reg_date` datetime DEFAULT current_timestamp() COMMENT '작성일',
  `mod_date` datetime DEFAULT NULL COMMENT '수정일',
  `del_yn` varchar(1) DEFAULT 'N' COMMENT '삭제여부',
  `story_ko` text DEFAULT NULL COMMENT '브랜드이야기',
  `story_en` text DEFAULT NULL COMMENT '브랜드이야기',
  `story_zh` text DEFAULT NULL COMMENT '브랜드이야기',
  `thumb_path` varchar(100) DEFAULT NULL COMMENT '썸네일 경로',
  `thumb_name` varchar(100) DEFAULT NULL COMMENT '썸네일 이름',
  `explain_ko` varchar(200) DEFAULT NULL COMMENT '브랜드설명',
  `explain_en` varchar(200) DEFAULT NULL COMMENT '브랜드설명',
  `explain_zh` varchar(200) DEFAULT NULL COMMENT '브랜드설명',
  `logo_path` varchar(100) DEFAULT NULL COMMENT '로고 경로',
  `logo_name` varchar(100) DEFAULT NULL COMMENT '로고 이름',
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='용지, 브랜드 관리';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `category`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `title_ko` varchar(200) DEFAULT NULL COMMENT '제목',
  `title_en` varchar(200) DEFAULT NULL COMMENT '제목',
  `title_zh` varchar(200) DEFAULT NULL COMMENT '제목',
  `content_ko` varchar(1000) DEFAULT NULL COMMENT '내용',
  `content_en` varchar(1000) DEFAULT NULL COMMENT '내용',
  `content_zh` varchar(1000) DEFAULT NULL COMMENT '내용',
  `show_yn` varchar(1) DEFAULT 'Y' COMMENT '노출여부',
  `reg_admin` int(11) DEFAULT NULL COMMENT '작성자',
  `image_path` varchar(100) DEFAULT NULL COMMENT '이미지경로',
  `image_name` varchar(100) DEFAULT NULL COMMENT '이미지이름',
  `show_order` int(11) DEFAULT 1 COMMENT '노출순서',
  `reg_date` datetime DEFAULT current_timestamp() COMMENT '작성일',
  `mod_date` datetime DEFAULT NULL COMMENT '수정일',
  `del_yn` varchar(1) DEFAULT 'N' COMMENT '삭제여부',
  `brand_idx` int(11) DEFAULT NULL COMMENT '브랜드 idx',
  `thumb_path` varchar(100) DEFAULT NULL COMMENT '섬네일 경로',
  `thumb_name` varchar(100) DEFAULT NULL COMMENT '섬네일 이름',
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='용지, 브랜드 카테고리 관리';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer_information_log`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer_information_log` (
  `idx` int(11) NOT NULL AUTO_INCREMENT COMMENT '인덱스',
  `category` varchar(50) DEFAULT NULL COMMENT '종류',
  `title` varchar(100) DEFAULT NULL COMMENT '제목',
  `version` varchar(50) DEFAULT NULL COMMENT '버전',
  `prev_version` varchar(50) DEFAULT NULL COMMENT '이전 버전',
  `contents` longtext DEFAULT NULL COMMENT '내용',
  `regist_date` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='고객님 안내 이력 로그';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `faq`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `faq` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(1) DEFAULT '1' COMMENT '타입 1:제품문의 2:이용문의 3:제안ㆍ건의 4:기타',
  `title` varchar(200) DEFAULT NULL COMMENT '제목',
  `content` varchar(2000) DEFAULT NULL COMMENT '내용',
  `top_yn` varchar(1) DEFAULT 'N' COMMENT '탑 노출',
  `red_member` int(11) DEFAULT NULL COMMENT '작성자',
  `reg_date` datetime DEFAULT NULL COMMENT '등록일',
  `mod_date` datetime DEFAULT NULL COMMENT '수정일',
  `show_order` int(11) DEFAULT NULL COMMENT '노출순서',
  `del_yn` varchar(1) DEFAULT 'N' COMMENT '삭제여부',
  `reg_admin_id` varchar(50) DEFAULT NULL,
  `reg_name` varchar(50) DEFAULT NULL,
  `reg_ip` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='faq';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `news`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `news` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(1) DEFAULT '1' COMMENT '타입 1:공지사항 2:언론보도 3:홍보자료',
  `sub_type` varchar(50) DEFAULT '1' COMMENT '1:안내  | 캠페인 2:매채 3:광고갤러리 | 공모전 | 아기모델 선발 | 홍보영상',
  `title` varchar(200) DEFAULT NULL COMMENT '제목',
  `open_date` varchar(100) DEFAULT NULL COMMENT '게재일',
  `content` text DEFAULT NULL COMMENT '내용',
  `top_yn` varchar(1) DEFAULT 'N' COMMENT '중요공지',
  `main_yn` varchar(1) DEFAULT 'N' COMMENT '메인화면 노출',
  `news_url` varchar(200) DEFAULT NULL COMMENT '매채 URL',
  `thumb_path` varchar(100) DEFAULT NULL COMMENT '섬네일경로',
  `thumb_name` varchar(100) DEFAULT NULL COMMENT '섬네일이름',
  `file_path` varchar(100) DEFAULT NULL COMMENT '파일경로',
  `file_name` varchar(100) DEFAULT NULL COMMENT '파일이름',
  `show_yn` varchar(1) DEFAULT NULL COMMENT '노출여부',
  `reg_member` int(11) DEFAULT NULL COMMENT '작성자',
  `youtube_url` varchar(200) DEFAULT NULL COMMENT '유투브 url',
  `attach_path` varchar(100) DEFAULT NULL COMMENT '첨부파일 경로',
  `attach_name` varchar(100) DEFAULT NULL COMMENT '첨부파일 이름',
  `tag` varchar(200) DEFAULT NULL COMMENT '태그',
  `reg_date` datetime DEFAULT current_timestamp() COMMENT '등록일',
  `mod_date` datetime DEFAULT NULL COMMENT '수정일\n',
  `del_yn` varchar(1) DEFAULT 'N' COMMENT '삭제여부',
  `read_cnt` int(11) DEFAULT 0 COMMENT '조회수',
  `show_order` int(11) DEFAULT 5 COMMENT '노출순서',
  `content_ta` text DEFAULT NULL COMMENT '테블릿 내용. 공모전, 아기모델선발시에만 사용',
  `content_mo` text DEFAULT NULL COMMENT '모바일 내용. 공모전, 아기모델선발시에만 사용',
  `noticeHead` varchar(50) DEFAULT NULL,
  `reg_admin_id` varchar(50) DEFAULT NULL,
  `reg_name` varchar(50) DEFAULT NULL,
  `reg_ip` varchar(50) DEFAULT NULL,
  `content_top` text DEFAULT NULL COMMENT '주요공지 내용',
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=845 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='게시판 관리';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `popup`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `popup` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(500) DEFAULT NULL COMMENT '헤드라인',
  `lang` varchar(1) DEFAULT '1' COMMENT '언어 1:국문 2:영문 3:중문',
  `pop_x` int(11) DEFAULT NULL COMMENT '좌표 x',
  `pop_y` int(11) DEFAULT NULL COMMENT '좌표 y',
  `pop_w` int(11) DEFAULT NULL COMMENT '가로사이즈',
  `pop_h` int(11) DEFAULT NULL COMMENT '높이',
  `content` text DEFAULT NULL COMMENT '내용',
  `reg_date` datetime DEFAULT current_timestamp() COMMENT '등록일',
  `mod_date` datetime DEFAULT NULL COMMENT '수정일',
  `del_yn` varchar(1) DEFAULT 'N' COMMENT '삭제여부',
  `start_date` datetime DEFAULT NULL COMMENT '시작일',
  `end_date` datetime DEFAULT NULL COMMENT '종료일',
  `show_pc` varchar(1) DEFAULT 'Y' COMMENT 'pc노출',
  `show_mo` varchar(1) DEFAULT 'Y' COMMENT '모바일노출',
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='팝업관리';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `title_ko` varchar(200) DEFAULT NULL COMMENT '제목',
  `title_en` varchar(200) DEFAULT NULL COMMENT '제목',
  `title_zh` varchar(200) DEFAULT NULL COMMENT '제목',
  `content_ko` varchar(1000) DEFAULT NULL COMMENT '내용',
  `content_en` varchar(1000) DEFAULT NULL COMMENT '내용',
  `content_zh` varchar(1000) DEFAULT NULL COMMENT '내용',
  `show_yn` varchar(1) DEFAULT 'Y' COMMENT '노출여부',
  `reg_admin` int(11) DEFAULT NULL COMMENT '작성자',
  `show_order` int(11) DEFAULT 1 COMMENT '노출순서',
  `size_ko` varchar(100) DEFAULT NULL COMMENT '평량',
  `size_en` varchar(100) DEFAULT NULL COMMENT '사이즈',
  `size_zh` varchar(100) DEFAULT NULL COMMENT '사이즈',
  `color_ko` varchar(100) DEFAULT NULL COMMENT '색상',
  `color_en` varchar(100) DEFAULT NULL COMMENT '색상',
  `color_zh` varchar(100) DEFAULT NULL COMMENT '색상',
  `purpose_ko` varchar(100) DEFAULT NULL COMMENT '목적',
  `purpose_en` varchar(200) DEFAULT NULL COMMENT '목적',
  `purpose_zh` varchar(200) DEFAULT NULL COMMENT '목적',
  `thumb_path` varchar(100) DEFAULT NULL COMMENT '섬네일 경로',
  `thumb_name` varchar(100) DEFAULT NULL COMMENT '섬네일 이름',
  `image_path` varchar(100) DEFAULT NULL COMMENT '이미지 경로',
  `image_name` varchar(100) DEFAULT NULL COMMENT '이미지 이름',
  `reg_date` datetime DEFAULT current_timestamp() COMMENT '작성일',
  `mod_date` datetime DEFAULT NULL COMMENT '수정일',
  `del_yn` varchar(1) DEFAULT 'N' COMMENT '삭제여부',
  `category_idx` int(11) DEFAULT NULL COMMENT '카테고리',
  `content_ko_s` varchar(1000) DEFAULT NULL COMMENT '내용_작은글씨',
  `content_en_s` varchar(1000) DEFAULT NULL COMMENT '내용_작은글씨',
  `content_zh_s` varchar(1000) DEFAULT NULL COMMENT '내용_작은글씨',
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=158 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='용지, 브랜드 관리';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `report` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(1) DEFAULT NULL COMMENT '1: esg 2:영업 3:감사 4:ir 5:전자공고',
  `title` varchar(200) NOT NULL COMMENT '제목',
  `file_path` varchar(100) DEFAULT NULL COMMENT '파일 경로',
  `file_name` varchar(100) DEFAULT NULL COMMENT '파일 이름',
  `show_yn` varchar(1) DEFAULT 'Y' COMMENT '노출여부',
  `reg_member` int(11) DEFAULT NULL COMMENT '등록자',
  `download_cnt` int(11) DEFAULT NULL COMMENT '다운로드수',
  `top_yn` varchar(1) DEFAULT 'N' COMMENT '중요공지',
  `content` text DEFAULT NULL COMMENT '내용',
  `sYear` varchar(10) DEFAULT NULL COMMENT '지속가능 경영보고서 > 연도 추가',
  `read_cnt` int(11) DEFAULT NULL COMMENT '조회수',
  `reg_date` datetime DEFAULT current_timestamp() COMMENT '등록일',
  `mod_date` datetime DEFAULT NULL COMMENT '수정일',
  `del_yn` varchar(1) DEFAULT 'N' COMMENT '삭제여부',
  `acpt_no` varchar(30) DEFAULT NULL COMMENT '공시지가 기본키',
  `notice_head` varchar(50) DEFAULT NULL,
  `reg_admin_id` varchar(50) DEFAULT NULL,
  `reg_name` varchar(50) DEFAULT NULL,
  `reg_ip` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=485802 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='리포트 관리';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `voc`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `voc` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(1) DEFAULT '1' COMMENT '1:부조리신고센터 2:제품관련 3:비지니스',
  `req_type` varchar(1) DEFAULT '1' COMMENT '신고타입 1:고객 2:협력업체 3:임직원',
  `status` varchar(1) DEFAULT '1' COMMENT '처리상태 1:접수 2:확인 3:처리중 4:처리완료',
  `name` varchar(20) DEFAULT NULL COMMENT '이름',
  `email` varchar(100) DEFAULT NULL COMMENT '이메일',
  `mobile` varchar(20) DEFAULT NULL COMMENT '전화번호',
  `title` varchar(200) DEFAULT NULL COMMENT '제목',
  `file_name` varchar(100) DEFAULT NULL COMMENT '파일이름',
  `file_path` varchar(100) DEFAULT NULL COMMENT '파일경로',
  `reg_ip` varchar(100) DEFAULT NULL,
  `reg_date` datetime DEFAULT current_timestamp() COMMENT '등록일',
  `mod_date` datetime DEFAULT NULL COMMENT '수정일',
  `dm_lang` varchar(1) DEFAULT '1' COMMENT 'dm언어 1:국문 2:영문 3:중문',
  `dm_status` varchar(1) DEFAULT '1' COMMENT 'dm처리상태 1:확인전 2:확인중 3:확인완료',
  `dm_text` varchar(1000) DEFAULT NULL COMMENT 'dm응답',
  `dm_id` varchar(100) DEFAULT NULL COMMENT 'dm id',
  `dm_name` varchar(100) DEFAULT NULL COMMENT 'dm name',
  `dm_date` datetime DEFAULT NULL COMMENT 'dm 응답시간',
  `del_yn` varchar(1) DEFAULT 'N' COMMENT '삭제여부',
  `content` text DEFAULT NULL COMMENT '컨텐츠',
  `password` varchar(100) DEFAULT NULL COMMENT '작성글 비밀번호',
  `address` varchar(500) DEFAULT NULL COMMENT '주소',
  `address_detail` varchar(500) DEFAULT NULL COMMENT '주소상세',
  `zipcode` varchar(10) DEFAULT NULL COMMENT '우편번호',
  `prod_type` varchar(1) DEFAULT NULL COMMENT '제품분류타입 1:기저귀 2:생리대 3:화장지 4:물티슈 5:마스크',
  `esg_type` varchar(1) DEFAULT '1' COMMENT '1:부조리신고 2:분쟁조정신청',
  `dm_ip` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=168 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='부조리 신고센터 관리';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `voc_answers`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `voc_answers` (
  `idx` int(11) NOT NULL AUTO_INCREMENT COMMENT '순번(Auto Increment)',
  `p_idx` int(11) NOT NULL COMMENT '부모 idx(=voc.idx)',
  `dm_lang` varchar(1) DEFAULT '1' COMMENT 'dm언어 1:국문 2:영문 3:중문',
  `dm_status` varchar(1) DEFAULT '1' COMMENT 'dm처리상태 1:확인전 2:확인중 3:확인완료',
  `dm_text` varchar(1000) DEFAULT NULL COMMENT 'dm응답',
  `dm_id` varchar(100) DEFAULT NULL COMMENT 'dm id',
  `dm_name` varchar(100) DEFAULT NULL COMMENT 'dm name',
  `dm_date` datetime DEFAULT NULL COMMENT 'dm 응답시간',
  `dm_ip` varchar(50) DEFAULT NULL COMMENT 'dm ip',
  `emailYN` varchar(1) DEFAULT 'N' COMMENT 'Email 발송여부(Y=발송 / N=미발송)',
  PRIMARY KEY (`idx`),
  KEY `voc_answers_idx_IDX` (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `voc_file`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `voc_file` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `p_idx` int(11) DEFAULT NULL COMMENT 'voc idx',
  `file_name` varchar(100) DEFAULT NULL COMMENT '파일 이름',
  `file_path` varchar(100) DEFAULT NULL COMMENT '파일 경로',
  `del_yn` varchar(1) DEFAULT 'N' COMMENT '삭제여부',
  `reg_date` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='부조리 신고센터 파일';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'kleannara'
--
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'IGNORE_SPACE,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`hpgadm`@`192.1.117.206` PROCEDURE `proc_update_admin_allow`()
BEGIN
    DECLARE v_ids TEXT DEFAULT '';
    DECLARE v_cnt INT DEFAULT 0;

    -- 1) 대상자 목록 (문자셋을 utf8mb4 로 통일)
    SELECT 
        CAST(GROUP_CONCAT(id ORDER BY id SEPARATOR ',') AS CHAR CHARACTER SET utf8mb4)
    INTO v_ids
    FROM admin
    WHERE last_login_date < NOW() - INTERVAL 90 DAY
      AND login_allow = 0;

    -- 2) 대상자 수
    SELECT COUNT(*) INTO v_cnt
    FROM admin
    WHERE last_login_date < NOW() - INTERVAL 90 DAY
      AND login_allow = 0;

    -- 3) 업데이트
    UPDATE admin
    SET login_allow = 1
    WHERE last_login_date < NOW() - INTERVAL 90 DAY
      AND login_allow = 0;

    -- 4) adminLog 기록 (CONCAT 내부 문자열을 모두 utf8mb4 로 통일)
    INSERT INTO adminLog
    (id, type, serviceName, memo, ip, reg_date)
    VALUES (
        CAST('시스템' AS CHAR CHARACTER SET utf8mb4),
        'U',
        CAST('로그인 특정 일수 경과 배치' AS CHAR CHARACTER SET utf8mb4),
        CASE 
            WHEN v_cnt > 0 THEN CONCAT(
                CAST('로그인 특정 일수 경과 배치 실행, 대상자 ' AS CHAR CHARACTER SET utf8mb4),
                v_cnt,
                CAST('명 (' AS CHAR CHARACTER SET utf8mb4),
                CAST(v_ids AS CHAR CHARACTER SET utf8mb4),
                CAST(')' AS CHAR CHARACTER SET utf8mb4)
            )
            ELSE CAST('로그인 특정 일수 경과 배치: 대상자 없음' AS CHAR CHARACTER SET utf8mb4)
        END,
        '127.0.0.1',
        NOW()
    );
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'IGNORE_SPACE,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`hpgadm`@`192.1.117.206` PROCEDURE `proc_update_admin_psw`()
BEGIN
    DECLARE v_cnt INT DEFAULT 0;

    -- psw 업데이트 대상
    SELECT COUNT(*) INTO v_cnt
    FROM admin
   WHERE passwordChangeFlg = 'Y'
     AND passwordChangeDate <= DATE_SUB(NOW(), INTERVAL 90 DAY);

    --  업데이트
    UPDATE admin
       SET passwordChangeFlg = 'N'
     WHERE passwordChangeFlg = 'Y'
       AND passwordChangeDate <= DATE_SUB(NOW(), INTERVAL 90 DAY);

    -- adminLog 기록
    INSERT INTO adminLog
    (id, type, serviceName, memo,ip, reg_date)
    SELECT
        '시스템',
        'U',
        '비밀번호 변경 배치',
        CASE WHEN v_cnt > 0 
             THEN CONCAT('비밀번호 변경 플래그 업데이트 실행, 결과: ', v_cnt, '개 변경')
             ELSE '비밀번호 변경 플래그 업데이트 실행, 결과 없음'
        END, '127.0.0.1',
        NOW();
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'IGNORE_SPACE,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`hpgadm`@`192.1.117.206` PROCEDURE `proc_update_voc_masking`()
BEGIN
    DECLARE v_cnt INT DEFAULT 0;
    DECLARE v_file_cnt INT DEFAULT 0;

    -- 3년 경과 대상 조회
    SELECT COUNT(*) INTO v_cnt
    FROM voc
    WHERE reg_date <= DATE_SUB(NOW(), INTERVAL 3 YEAR)
      AND status <> '5';

    -- 삭제 대상 첨부파일 수 조회
    SELECT COUNT(*) INTO v_file_cnt
    FROM voc_file vf
    INNER JOIN voc v
        ON vf.p_idx = v.idx
    WHERE v.reg_date <= DATE_SUB(NOW(), INTERVAL 3 YEAR);

    -- 첨부파일  삭제
    DELETE vf
    FROM voc_file vf
    INNER JOIN voc v
        ON vf.p_idx = v.idx
    WHERE v.reg_date <= DATE_SUB(NOW(), INTERVAL 3 YEAR);

    -- voc 개인정보 마스킹 처리
    UPDATE voc
    SET 
        name           = '*****',
        email          = '*****',
        mobile         = '*****',
        address        = '*****',
        address_detail = '*****',
        zipcode        = '*****',
        password       = '*****',
        status         = '5'
    WHERE reg_date <= DATE_SUB(NOW(), INTERVAL 3 YEAR)
      AND status <> '5';

    -- adminLog 기록
    INSERT INTO adminLog
    (id, type, serviceName, memo, ip, reg_date)
    SELECT
        '시스템',
        'U',
        'voc 3년 경과 변경 배치',
        CASE 
            WHEN v_cnt > 0
            THEN CONCAT(
                    'voc 3년 경과 변경 플래그 업데이트 실행, 결과: ',
                    v_cnt,
                    '개 변경 / 첨부파일 삭제: ',
                    v_file_cnt,
                    '개'
                 )
            ELSE 'voc 3년 경과 변경 플래그 업데이트 실행, 결과 없음'
        END,
        '127.0.0.1',
        NOW();

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-02 10:32:30
