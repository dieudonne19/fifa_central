DROP TABLE IF EXISTS club_stats;

CREATE TABLE club_stats(
    club_id varchar references club(id),
    season_id varchar references season(id),
    points int,
    scored_goals int,
    conceded_goals int,
    clean_sheets int,
    difference_goals int,
    sync_date timestamp,
    CONSTRAINT season_fk FOREIGN KEY (season_id) REFERENCES season(id),
    PRIMARY KEY (club_id,season_id)
)