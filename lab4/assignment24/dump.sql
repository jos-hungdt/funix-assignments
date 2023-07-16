DROP DATABASE IF EXISTS lab4asm24;
CREATE DATABASE lab4asm24;
USE lab4asm24;

CREATE TABLE product
(
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `Name` VARCHAR(255) CHARSET utf8  NOT NULL,
    Picture VARCHAR(255) CHARSET utf8  NOT NULL,
    Price DECIMAL(6, 2) NULL,
    `Description` VARCHAR(1000) CHARSET utf8 NULL,
    Delivery VARCHAR(255) CHARSET utf8 NULL
);

INSERT INTO product (id, `Name`, Picture, Price, `Description`, Delivery) VALUES (1, 'Homemade Jam', 'i281756464687106784._rsw480h480_szw480h480_.jpg', 5.00, 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit.Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Lorem ipsum dolor sit amet, consectetuer adipiscing elit.', '1-2 business days');
INSERT INTO product (id, `Name`, Picture, Price, `Description`, Delivery) VALUES (2, 'Berries', 'i281756464687107103._rsw480h480_szw480h480_.jpg', 3.00, 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit.', null);
INSERT INTO product (id, `Name`, Picture, Price, `Description`, Delivery) VALUES (3, 'Fresh Eggs Daily', 'i281756464687107233._rsw480h480_szw480h480_.jpg', 10.00, 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit.', null);
INSERT INTO product (id, `Name`, Picture, Price, `Description`, Delivery) VALUES (4, 'Potatoes', 'i281756464687107384._rsw480h480_szw480h480_.jpg', 3.00, 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit.', null);
INSERT INTO product (id, `Name`, Picture, Price, `Description`, Delivery) VALUES (5, 'Blossom Honey', 'i281756464687107465._rsw480h480_szw480h480_.jpg', 8.00, null, null);

CREATE TABLE blog (
	id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	title VARCHAR(255) charset utf8,
    publish_date DATE,
    image varchar(255),
    content LONGTEXT charset utf8
);

INSERT INTO blog (title, content, image, publish_date) VALUES (
	'Lorem ipsum dolor'
    ,'<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip exea commodo consequat.</p> <p>At eam doctus oportere, eam feugait delectus ne. Quo cu vulputate persecuti. Eum ut natum possim comprehensam, habeo dicta scaevola eu nec. Ea adhuc reformidans eam. Pri dolore epicuri eu, ne cum tantas fierent instructior. Pro ridens intellegam ut, sea at graecis scriptorem eloquentiam.</p> <p>Per an labitur lucilius ullamcorper, esse erat ponderum ad vim. Possim laoreet suscipit ex pri, vix numquam eruditi feugait in. Nec maluisset complectitur te, at sea decore semper. Falli numquam perpetua sea et, tibique repudiandae an pro. Ut his solum persius postulant. Soluta nemore debitis ne eos, cum an scripta pericula partiendo.</p>'
	,'i281756464684715224._szw480h1280_.jpg'
    ,'2023-11-16'
);

INSERT INTO blog (title, content, image, publish_date) VALUES (
	'Lorem ipsum dolor'
    ,'<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip exea commodo consequat.</p> <p>At eam doctus oportere, eam feugait delectus ne. Quo cu vulputate persecuti. Eum ut natum possim comprehensam, habeo dicta scaevola eu nec. Ea adhuc reformidans eam. Pri dolore epicuri eu, ne cum tantas fierent instructior. Pro ridens intellegam ut, sea at graecis scriptorem eloquentiam.</p> <p>Per an labitur lucilius ullamcorper, esse erat ponderum ad vim. Possim laoreet suscipit ex pri, vix numquam eruditi feugait in. Nec maluisset complectitur te, at sea decore semper. Falli numquam perpetua sea et, tibique repudiandae an pro. Ut his solum persius postulant. Soluta nemore debitis ne eos, cum an scripta pericula partiendo.</p>'
	,'i281756464684715223._szw480h1280_.jpg'
    ,'2023-05-16'
);

INSERT INTO blog (title, content, image, publish_date) VALUES (
	'Photo'
    ,''
    ,'i281756464684715221._szw1280h1280_.jpg'
    ,'2020-01-17'
);


CREATE TABLE contact
(
    id 			  INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `Name`          VARCHAR(255) CHARSET utf8  NOT NULL,
    Email         VARCHAR(255) CHARSET utf8  NULL,
    Address       VARCHAR(255) CHARSET utf8  NULL,
    City          VARCHAR(255) CHARSET utf8  NULL,
    Country       VARCHAR(255) CHARSET utf8  NULL,
    Tel           VARCHAR(20) CHARSET utf8  NULL,
    Opening_hours VARCHAR(255) CHARSET utf8  NULL
);
INSERT INTO contact (id, `Name`, Email, Address, City, Country, Tel, Opening_hours) VALUES (1, 'Melville Family Farm Store
', 'your-email@your-email.com', '123 Cach Mang Thang Tam', 'Ho Chi Minh City', 'Vietnam', '12345', 'Tuesday- Saturday: 10 am-6 pm<br>Wednesdays open until 7 pm<br><br>Our Farm Store is closed Sundays and Mondays.');

