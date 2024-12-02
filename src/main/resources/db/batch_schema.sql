DROP TABLE IF EXISTS pet_funeral;
ALTER TABLE pet_funeral MODIFY COLUMN image LONGTEXT;
CREATE TABLE pet_funeral (
                             pet_funeral_id INT AUTO_INCREMENT PRIMARY KEY,
                             funeral_name VARCHAR(50),
                             address VARCHAR(50),
                             phone_number VARCHAR(50),
                             homepage VARCHAR(1000),
                             region ENUM('서울', '인천', '경기', '세종', '강원', '부산', '충청', '전라', '경상', '울산', '광주', '대구'),
                             image VARCHAR(255),
                             deleted BIT(1) DEFAULT 0 NOT NULL,
                             created_at DATETIME,
                             updated_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
INSERT INTO pet_funeral (funeral_name, address, phone_number, homepage, region, image, deleted, created_at, updated_at) VALUES
