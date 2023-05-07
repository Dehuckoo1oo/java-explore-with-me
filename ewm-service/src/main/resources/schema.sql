DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS compilationEvents CASCADE;

CREATE TABLE users
(
    id    integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name  varchar(255) not null,
    email varchar(800) not null
);

CREATE TABLE categories
(
    id   integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar(255) not null
);

CREATE TABLE locations
(
    id  integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    lat double precision not null,
    lon double precision not null
);

CREATE TABLE events
(
    id                 INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    annotation         VARCHAR(2000)               NOT NULL,
    category_id        INTEGER,
    created_on         TIMESTAMP WITHOUT TIME ZONE,
    description        VARCHAR(7000),
    event_date         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    initiator          INTEGER                     NOT NULL,
    location_id        INTEGER                     NOT NULL,
    paid               BOOLEAN                     NOT NULL,
    participant_limit  INTEGER                     NOT NULL,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN                     NOT NULL,
    state              VARCHAR(9)                  NOT NULL,
    title              varchar(255)                NOT NULL
);

CREATE TABLE requests
(
    id             INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    event_id       INTEGER                     NOT NULL,
    requester      INTEGER                     NOT NULL,
    request_status VARCHAR(9)

);

CREATE TABLE compilations
(
    id     INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title  VARCHAR(255) NOT NULL UNIQUE,
    pinned boolean      NOT NULL
);

CREATE TABLE compilationEvents
(
    id             integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    compilation_id INTEGER NOT NULL,
    event_id       INTEGER NOT NULL
);

ALTER TABLE compilationEvents
    ADD FOREIGN KEY (compilation_id) REFERENCES compilations (id) ON DELETE CASCADE;
ALTER TABLE compilationEvents
    ADD FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE;
ALTER TABLE events
    ADD FOREIGN KEY (initiator) REFERENCES users (id) ON DELETE CASCADE;
ALTER TABLE events
    ADD FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE;
ALTER TABLE events
    ADD FOREIGN KEY (location_id) REFERENCES locations (id) ON DELETE CASCADE;
ALTER TABLE requests
    ADD FOREIGN KEY (requester) REFERENCES users (id) ON DELETE CASCADE;
ALTER TABLE requests
    ADD FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE;
