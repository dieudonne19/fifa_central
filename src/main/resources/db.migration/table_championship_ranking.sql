CREATE TABLE championship_ranking(
    id varchar primary key ,
    championship_id varchar,
    difference_goals_median int,
    CONSTRAINT ch_fk FOREIGN KEY (championship_id) REFERENCES championship(id)
)