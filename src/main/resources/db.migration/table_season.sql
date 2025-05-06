CREATE TABLE IF NOT EXISTS season(
    id varchar primary key ,
    year bigint,
    championship_id varchar,
    alias varchar,
    CONSTRAINT championship_fk FOREIGN KEY (championship_id) REFERENCES championship(id)
);