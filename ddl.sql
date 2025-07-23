CREATE TYPE social_login_enum AS ENUM ('KAKAO', 'APPLE', 'PASSWORD');
CREATE TYPE gender_enum AS ENUM ('FEMALE', 'MALE');

CREATE TABLE "users" (
    "id"	uuid		NOT NULL,
    "name"	varchar(10)		NULL,
    "nickname"	varchar(16)		NULL,
    "profile_image"	varchar(255)		NULL,
    "type"	social_login_enum		NULL,
    "email"	varchar(255)		NULL,
    "gender"	gender_enum		NULL,
    "birth"	date		NULL,
    "password"	varchar(255)		NULL
);