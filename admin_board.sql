-- 관리자 게시판 (관리자만 사용)
use eum12;

CREATE TABLE IF NOT EXISTS admin_article (
  articleNO     INT AUTO_INCREMENT PRIMARY KEY,
  parentNO      INT DEFAULT 0,
  id            VARCHAR(50) NOT NULL,
  title         VARCHAR(255) NOT NULL,
  content       MEDIUMTEXT,
  imageFileName VARCHAR(255),
  is_notice     TINYINT(1) DEFAULT 0,
  writeDate     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

select * from admin_article;