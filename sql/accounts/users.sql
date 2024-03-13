create table users
(
    user_id       int auto_increment
        primary key,
    username      varchar(50)  not null,
    email         varchar(100) not null,
    password_hash varchar(64)  not null,
    constraint username_unique
        unique (username)
);

