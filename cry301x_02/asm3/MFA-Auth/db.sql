CREATE DATABASE mfa_sys;

USE mfa_sys;

CREATE TABLE users (
    userid VARCHAR(64) PRIMARY KEY,
    firstname VARCHAR(64),
    lastname VARCHAR(64),
    salt VARCHAR(64) NOT NULL,
    password VARCHAR(64) NOT NULL,
    isLocked TINYINT,
    faillogin TINYINT NOT NULL,
    otpsecret VARCHAR(64) NOT NULL,
    label TINYINT NOT NULL,
    CONSTRAINT chk_label CHECK (label IN (1, 2, 3, 4))
);

CREATE TABLE news (
    id INT PRIMARY KEY AUTO_INCREMENT,
    userid VARCHAR(64),
    content TEXT,
    date DATETIME NOT NULL,
    label TINYINT NOT NULL,
    CONSTRAINT chk_label_news CHECK (label IN (1, 2, 3, 4)),
    FOREIGN KEY (userid) REFERENCES users(userid)
);
