CREATE TABLE IF NOT EXISTS championship (
    id varchar PRIMARY KEY,
    name varchar unique,
    country varchar,
    api_url varchar
);
