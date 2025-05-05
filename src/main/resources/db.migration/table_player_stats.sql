CREATE TABLE IF NOT EXISTS player_stats (
    id varchar primary key ,
    player_id varchar,
    season_id varchar unique,
    playing_time_id varchar,
    scored_goals bigint,
    sync_date timestamp
);

ALTER TABLE player_stats ADD CONSTRAINT player_id_fk FOREIGN KEY (player_id) REFERENCES player(id);
ALTER TABLE player_stats ADD CONSTRAINT playing_time_id_fk FOREIGN KEY (playing_time_id) REFERENCES playing_time(id);

/* miandry table season */
ALTER TABLE player_stats ADD CONSTRAINT season_id_fk FOREIGN KEY (season_id) REFERENCES season(id);
