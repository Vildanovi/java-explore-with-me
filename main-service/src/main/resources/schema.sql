CREATE TABLE IF NOT EXISTS users
(
    id    INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    name  VARCHAR(250)                                     NOT NULL,
    email VARCHAR(254)                                     NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    pinned BOOLEAN                                          NOT NULL,
    title  VARCHAR(50)                                      NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS compilation_events
(
    compilation_id INTEGER REFERENCES compilations (id),
    event_id       INTEGER REFERENCES events (id),
    PRIMARY KEY (compilation_id, event_id)
);

CREATE TABLE IF NOT EXISTS events
(
    id                 INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    annotation         VARCHAR(2000)                                    NOT NULL,
    category           INTEGER REFERENCES categories (id)               NOT NULL,
    created            TIMESTAMP WITHOUT TIME ZONE,
    description        VARCHAR(7000),
    event_date         TIMESTAMP WITHOUT TIME ZONE,
    initiator          BIGINT REFERENCES users (id) ON DELETE CASCADE,
    lat                FLOAT                                            NOT NULL,
    lon                FLOAT                                            NOT NULL,
    paid               BOOLEAN                                          NOT NULL,
    participant_limit  INTEGER                                          NOT NULL,
    published          TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN                                          NOT NULL,
    title              VARCHAR(120)                                     NOT NULL,
    state              VARCHAR(50)                                      NOT NULL,
    views              INTEGER,
    confirmedRequest   INTEGER
);

CREATE TABLE IF NOT EXISTS categories
(
    id   INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    name VARCHAR(50)                                      NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS requests
(
    id        INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event     INTEGER REFERENCES events (id) ON DELETE CASCADE,
    requester INTEGER REFERENCES users (id) ON DELETE CASCADE,
    created   TIMESTAMP WITHOUT TIME ZONE,
    status    VARCHAR(50) NOT NULL,
    CONSTRAINT uniqueRequest UNIQUE (event, requester)
);