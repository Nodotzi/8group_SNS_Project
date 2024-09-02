create table user
(
    id       bigint       primary key auto_increment,
    email varchar(100) not null,
    password varchar(100) not null,
    status varchar(100) not null,
    nickname varchar(100) not null,
    introduce varchar(100) not null

);

create table relationship
(
    id       bigint       primary key auto_increment,
    asking_id bigint not null,
    asked_id bigint not null,
    status varchar(100) not null

);

create table friends
(
    id bigint primary key auto_increment,
    friendA_id bigint not null,
    friendB_id bigint not null
);

create table posting
(
    id bigint primary key auto_increment,
    user_email varchar(100) not null,
    title varchar(100) not null,
    contents varchar(100) not null
);