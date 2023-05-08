DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS compilation_events CASCADE;

CREATE TABLE users
(
    id    INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    email VARCHAR(800) NOT NULL
);

CREATE TABLE categories
(
    id   INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE locations
(
    id  INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    lat DOUBLE PRECISION NOT NULL,
    lon DOUBLE PRECISION NOT NULL
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
    event          INTEGER                     NOT NULL,
    requester      INTEGER                     NOT NULL,
    request_status VARCHAR(9)

);

CREATE TABLE compilations
(
    id     INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title  VARCHAR(255) NOT NULL UNIQUE,
    pinned BOOLEAN      NOT NULL
);

CREATE TABLE compilation_events
(
    id          INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    compilation INTEGER NOT NULL,
    event       INTEGER NOT NULL
);

ALTER TABLE compilation_events
    ADD FOREIGN KEY (compilation) REFERENCES compilations (id) ON DELETE CASCADE;
ALTER TABLE compilation_events
    ADD FOREIGN KEY (event) REFERENCES events (id) ON DELETE CASCADE;
ALTER TABLE events
    ADD FOREIGN KEY (initiator) REFERENCES users (id) ON DELETE CASCADE;
ALTER TABLE events
    ADD FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE;
ALTER TABLE events
    ADD FOREIGN KEY (location_id) REFERENCES locations (id) ON DELETE CASCADE;
ALTER TABLE requests
    ADD FOREIGN KEY (requester) REFERENCES users (id) ON DELETE CASCADE;
ALTER TABLE requests
    ADD FOREIGN KEY (event) REFERENCES events (id) ON DELETE CASCADE;
