do
$$
    begin
        if not exists(select from pg_type where typname = 'positions') then
            CREATE TYPE positions AS ENUM (
                'STRIKER', 'MIDFIELDER', 'DEFENSE', 'GOAL_KEEPER'
                );
        end if;
    end
$$;

CREATE TABLE IF NOT EXISTS player
(
    id               varchar primary key,
    name             varchar(200),
    number int ,
    position         positions,
    country          varchar,
    age              int,
    championship_id  varchar,
    club_id          varchar,
    sync_date        timestamp,
    unique (number,club_id)
);