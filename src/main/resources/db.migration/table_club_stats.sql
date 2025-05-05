CREATE TABLE club_stats(
    id varchar primary key ,
    club_id varchar,
    points int,
    scored_goals int,
    conceded_goals int,
    clean_sheets int,
    season_id varchar,
    sync_date timestamp,
    CONSTRAINT club_fk FOREIGN KEY (club_id) REFERENCES club(id),
    CONSTRAINT season_fk FOREIGN KEY (season_id) REFERENCES season(id)

)