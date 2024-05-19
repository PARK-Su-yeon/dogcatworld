DROP TABLE IF EXISTS pet_funeral;
CREATE TABLE pet_funeral (
                             pet_funeral_id INT AUTO_INCREMENT PRIMARY KEY,
                             funeral_name VARCHAR(50),
                             address VARCHAR(50),
                             phone_number VARCHAR(50),
                             homepage VARCHAR(1000),
                             deleted BIT(1) DEFAULT 0 NOT NULL,
                             created_at DATETIME,
                             updated_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
INSERT INTO pet_funeral (funeral_name, address, phone_number, homepage, deleted, created_at, updated_at)
VALUES ('펫포유', '세종특별자치시 장군면 금암리', '1533-4426', 'http://www.petforyou.kr', 0, NOW(), NOW());
INSERT INTO pet_funeral (funeral_name, address, phone_number, homepage, deleted, created_at, updated_at)
VALUES ('파트라슈', '부산 기장군 장안읍 좌동리', '051-723-2201', 'http://www.mypatrasche.co.kr', 0, NOW(), NOW());
INSERT INTO pet_funeral (funeral_name, address, phone_number, homepage, deleted, created_at, updated_at)
VALUES ('아이별', '부산광역시 기장군 장안읍 기룡길', '051-727-4499', 'http://www.aistar.co.kr', 0, NOW(), NOW());
INSERT INTO pet_funeral (funeral_name, address, phone_number, homepage, deleted, created_at, updated_at)
VALUES ('센트럴파크(주)', '부산광역시 기장군 일광면 차양길', '051-728-5411', 'http://www.startice.co.kr', 0, NOW(), NOW());
INSERT INTO pet_funeral (funeral_name, address, phone_number, homepage, deleted, created_at, updated_at)
VALUES ('대구러브펫', '대구 달서구 장동', '053-593-4900', 'http://dglovepet.kr', 0, NOW(), NOW());
