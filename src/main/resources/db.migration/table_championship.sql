do
$$
begin
        if not exists(select from pg_type where typname = 'championship_name') then
CREATE TYPE championship_name AS ENUM (
                'PREMIERE_LEAGUE', 'BUNDESLIGA', 'LA_LIGA', 'SERIA', 'LIGUE_1'
                );
end if;
end
$$;


CREATE TABLE IF NOT EXISTS championship (
    id varchar PRIMARY KEY,
    name championship_name unique,
    country varchar,
    api_url varchar
);
