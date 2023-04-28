DROP TABLE IF EXISTS hits;

CREATE TABLE hits
(
    id        integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    app       varchar(255),
    uri       varchar(255),
    ip        varchar(255),
    timestamp TIMESTAMP WITHOUT TIME ZONE
);