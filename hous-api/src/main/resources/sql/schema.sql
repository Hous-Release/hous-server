DROP TABLE IF EXISTS badge;
DROP TABLE IF EXISTS deploy;
DROP TABLE IF EXISTS feedback;
DROP TABLE IF EXISTS personality;
DROP TABLE IF EXISTS personality_test;
DROP TABLE IF EXISTS setting;
DROP TABLE IF EXISTS test_score;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS onboarding;
DROP TABLE IF EXISTS acquire;
DROP TABLE IF EXISTS represent;
DROP TABLE IF EXISTS room;
DROP TABLE IF EXISTS participate;
DROP TABLE IF EXISTS rule;
DROP TABLE IF EXISTS rule_image;
DROP TABLE IF EXISTS todo;
DROP TABLE IF EXISTS done;
DROP TABLE IF EXISTS take;
DROP TABLE IF EXISTS redo;


CREATE TABLE badge
(
    id         bigint auto_increment primary key,
    created_at datetime(6) null,
    updated_at datetime(6) null,
    image_url  varchar(300) not null,
    info       varchar(30)  not null
);

CREATE TABLE deploy
(
    id         bigint auto_increment primary key,
    created_at datetime(6) null,
    updated_at datetime(6) null,
    market_url varchar(300) not null,
    os         varchar(30)  not null,
    version    varchar(30)  not null
);

CREATE TABLE feedback
(
    id         bigint auto_increment primary key,
    created_at datetime(6) null,
    updated_at datetime(6) null,
    comment    varchar(300) null
);

CREATE TABLE personality
(
    id                         bigint auto_increment primary key,
    created_at                 datetime(6) null,
    updated_at                 datetime(6) null,
    bad_personality_image_url  varchar(300) not null,
    bad_personality_name       varchar(300) not null,
    color                      varchar(30)  not null,
    description                varchar(500) not null,
    good_personality_image_url varchar(300) not null,
    good_personality_name      varchar(300) not null,
    image_url                  varchar(300) not null,
    name                       varchar(30)  not null,
    recommend_title            varchar(300) not null,
    recommend_todo             varchar(300) not null,
    title                      varchar(300) not null,
    first_download_image_url   varchar(300) not null,
    second_download_image_url  varchar(300) not null
);

CREATE TABLE personality_test
(
    id            bigint auto_increment primary key,
    created_at    datetime(6) null,
    updated_at    datetime(6) null,
    answers       varchar(300) not null,
    idx           int          not null,
    image_url     varchar(300) not null,
    question      varchar(300) not null,
    question_type varchar(30)  not null
);

CREATE TABLE setting
(
    id                      bigint auto_increment primary key,
    created_at              datetime(6) null,
    updated_at              datetime(6) null,
    badge_push_status       varchar(30) not null,
    is_push_notification    bit         not null,
    new_todo_push_status    varchar(30) not null,
    remind_todo_push_status varchar(30) not null,
    rules_push_status       varchar(30) not null,
    today_todo_push_status  varchar(30) not null
);

CREATE TABLE test_score
(
    id           bigint auto_increment primary key,
    created_at   datetime(6) null,
    updated_at   datetime(6) null,
    clean        int not null,
    introversion int not null,
    light        int not null,
    noise        int not null,
    smell        int not null
);

CREATE TABLE user
(
    id          bigint auto_increment primary key,
    created_at  datetime(6) null,
    updated_at  datetime(6) null,
    fcm_token   varchar(300) null,
    social_id   varchar(300) not null,
    social_type varchar(30)  not null,
    status      varchar(30)  not null,
    setting_id  bigint       not null,
    constraint UK_3i3vte4rqus8rvevykm874whj unique (fcm_token)
);

CREATE TABLE onboarding
(
    id             bigint auto_increment primary key,
    created_at     datetime(6) null,
    updated_at     datetime(6) null,
    birthday       varchar(255) not null,
    introduction   varchar(100) null,
    is_public      bit          not null,
    job            varchar(30) null,
    mbti           varchar(30) null,
    nickname       varchar(30)  not null,
    personality_id bigint       not null,
    test_score_id  bigint null,
    user_id        bigint       not null
);

CREATE TABLE acquire
(
    id            bigint auto_increment primary key,
    created_at    datetime(6) null,
    updated_at    datetime(6) null,
    is_read       bit not null,
    badge_id      bigint null,
    onboarding_id bigint null
);

CREATE TABLE represent
(
    id            bigint auto_increment primary key,
    created_at    datetime(6) null,
    updated_at    datetime(6) null,
    badge_id      bigint null,
    onboarding_id bigint null
);

CREATE TABLE room
(
    id               bigint auto_increment primary key,
    created_at       datetime(6) null,
    updated_at       datetime(6) null,
    code             varchar(30) not null,
    name             varchar(30) not null,
    participants_cnt int         not null,
    onboarding_id    bigint      not null
);

CREATE TABLE participate
(
    id            bigint auto_increment primary key,
    created_at    datetime(6) null,
    updated_at    datetime(6) null,
    onboarding_id bigint null,
    room_id       bigint null
);

CREATE TABLE rule
(
    id           bigint auto_increment primary key,
    created_at   datetime(6) null,
    updated_at   datetime(6) null,
    idx          int          not null,
    name         varchar(100) not null,
    description  varchar(100),
    is_represent bit          not null default false,
    room_id      bigint null
);

CREATE TABLE rule_image
(
    id            bigint auto_increment primary key,
    created_at    datetime(6) null,
    updated_at    datetime(6) null,
    original_name varchar(255) null,
    image_s3_url  varchar(255) null,
    rule_id       bigint null
);

CREATE TABLE todo
(
    id                   bigint auto_increment primary key,
    created_at           datetime(6) null,
    updated_at           datetime(6) null,
    is_push_notification bit          not null,
    name                 varchar(100) not null,
    room_id              bigint null
);

CREATE TABLE done
(
    id            bigint auto_increment primary key,
    created_at    datetime(6) null,
    updated_at    datetime(6) null,
    onboarding_id bigint null,
    todo_id       bigint null
);

CREATE TABLE take
(
    id            bigint auto_increment primary key,
    created_at    datetime(6) null,
    updated_at    datetime(6) null,
    onboarding_id bigint null,
    todo_id       bigint not null
);

CREATE TABLE redo
(
    id          bigint auto_increment primary key,
    created_at  datetime(6) null,
    updated_at  datetime(6) null,
    day_of_week varchar(30) not null,
    take_id     bigint null
);
