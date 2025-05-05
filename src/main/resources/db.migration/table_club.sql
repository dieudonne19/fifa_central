DROP TABLE IF EXISTS club;

CREATE TABLE club
(
    id            varchar      not null primary key,
    name          varchar(255) not null unique,
    year_creation varchar check ( length(year_creation) = 4 ),
    acronym       varchar(10),
    stadium       varchar(100) not null,
    championship_id varchar ,
    coach_name varchar,
    sync_date timestamp,
    CONSTRAINT ch_fk FOREIGN KEY  (championship_id) REFERENCES championship(id),
    CONSTRAINT coach_fk FOREIGN KEY (coach_name) REFERENCES coach(coach_name)
);