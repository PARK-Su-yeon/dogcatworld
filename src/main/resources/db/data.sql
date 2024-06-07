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
                                                                                                                            ('Heavenly Pets', '123 Pet St, Pet City', '123-456-7890', 'http://heavenlypets.com', '서울', 'heavenlypets.jpg', 0, NOW(), NOW()),
                                                                                                                            ('Peaceful Paws', '456 Peaceful Ln, Peace Town', '234-567-8901', 'http://peacefulpaws.com', '인천', 'peacefulpaws.jpg', 0, NOW(), NOW()),
                                                                                                                            ('Eternal Rest', '789 Eternal Blvd, Rest City', '345-678-9012', 'http://eternalrest.com', '경기', 'eternalrest.jpg', 0, NOW(), NOW()),
                                                                                                                            ('Rainbow Bridge', '321 Rainbow Rd, Bridge Town', '456-789-0123', 'http://rainbowbridge.com', '세종', 'rainbowbridge.jpg', 0, NOW(), NOW()),
                                                                                                                            ('Serenity Haven', '654 Serenity St, Haven City', '567-890-1234', 'http://serenityhaven.com', '강원', 'serenityhaven.jpg', 0, NOW(), NOW()),
                                                                                                                            ('Pet Peace', '987 Peaceful Dr, Pet Town', '678-901-2345', 'http://petpeace.com', '부산', 'petpeace.jpg', 0, NOW(), NOW()),
                                                                                                                            ('Final Journey', '213 Final Ave, Journey City', '789-012-3456', 'http://finaljourney.com', '충청', 'finaljourney.jpg', 0, NOW(), NOW()),
                                                                                                                            ('Lasting Memories', '435 Memory Ln, Lasting Town', '890-123-4567', 'http://lastingmemories.com', '전라', 'lastingmemories.jpg', 0, NOW(), NOW()),
                                                                                                                            ('Pet Paradise', '657 Paradise St, Pet City', '901-234-5678', 'http://petparadise.com', '경상', 'petparadise.jpg', 0, NOW(), NOW()),
                                                                                                                            ('Restful Paws', '879 Restful Rd, Paws Town', '012-345-6789', 'http://restfulpaws.com', '울산', 'restfulpaws.jpg', 0, NOW(), NOW());