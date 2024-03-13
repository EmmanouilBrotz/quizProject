create table games
(
    game_id         int auto_increment
        primary key,
    user_id         int      null,
    score           int      null,
    completion_time datetime null,
    constraint games_ibfk_1
        foreign key (user_id) references users (user_id)
);

create index user_id
    on games (user_id);

