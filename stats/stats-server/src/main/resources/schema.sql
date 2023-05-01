DROP TABLE IF EXISTS hits;

CREATE TABLE hits
(
    id        integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    app       varchar(255) not null,
    uri       varchar(8000),
    ip        varchar(15) not null,
    timestamp TIMESTAMP WITHOUT TIME ZONE
);