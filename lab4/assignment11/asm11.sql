DROP DATABASE IF EXISTS lab4asm11;
CREATE DATABASE lab4asm11;
USE lab4asm11;

CREATE TABLE entries (
	id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	title VARCHAR(255) charset utf8,
    publish_date DATE,
    image varchar(255),
    content LONGTEXT charset utf8,
    author VARCHAR(100) charset utf8
);

INSERT INTO entries (title, content, image, publish_date) VALUES (
	'Essential skills for a happy life!'
    ,'<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip exea commodo consequat.</p> <p>At eam doctus oportere, eam feugait delectus ne. Quo cu vulputate persecuti. Eum ut natum possim comprehensam, habeo dicta scaevola eu nec. Ea adhuc reformidans eam. Pri dolore epicuri eu, ne cum tantas fierent instructior. Pro                                     ridens intellegam ut, sea at graecis scriptorem eloquentiam.</p> <p>Per an labitur lucilius ullamcorper, esse erat ponderum ad vim. Possim laoreet suscipit ex pri, vix numquam eruditi feugait in. Nec maluisset complectitur te, at sea decore semper. Falli numquam                                     perpetua sea et, tibique repudiandae an pro. Ut his solum persius postulant. Soluta nemore debitis ne eos, cum an scripta pericula partiendo.</p>'
	,'i283445314544979646._szw480h1280_.jpg'
    ,'2023-11-16'
);

INSERT INTO entries (title, content, image, publish_date) VALUES (
	'You''ve gotta dance'
    ,'<blockquote class="pull-right"><p>"?You''ve gotta dance like there''s nobody watching,<br>Love like you''ll never be hurt,<br>Sing like there''s nobody listening,<br>And live like it''s heaven on earth.? "</p></blockquote>'
    ,''
    ,'2022-02-03' 
);

INSERT INTO entries (title, content, image, publish_date) VALUES (
	'Photo'
    ,''
    ,'i283445314544979644._szw1280h1280_.jpg'
    ,'2020-01-17'
);

INSERT INTO entries (title, content, publish_date, author) VALUES (
	'About Me'
    ,'<p><span>Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip
	 ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue
	 duis dolore te feugait nulla facilisi. Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer possim assum. </span></p> <p><span>Typi non habent claritatem insitam; est usus legentis in iis qui facit eorum
	 claritatem. Investigationes demonstraverunt lectores legere me lius quod ii legunt saepius. Claritas est etiam processus dynamicus, qui sequitur mutationem consuetudium lectorum. Mirum est notare quam littera gothica, quam nunc putamus parum claram, anteposuerit
	 litterarum formas humanitatis per seacula quarta decima et quinta decima. Eodem modo typi, qui nunc nobis videntur parum clari, fiant sollemnes in futurum.</span></p>'
    ,'2020-01-17'
    ,'Vivianne Russell'
);

CREATE TABLE advertisements (
	id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) charset utf8,
    content LONGTEXT charset utf8, 
)

CREATE TABLE configurations (
	id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `type` VARCHAR(20) CHARSET UTF8
)

