CREATE TABLE IF NOT EXISTS users
(
    id    INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    name  VARCHAR(250)                                     NOT NULL,
    email VARCHAR(254)                                     NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS category
(
    id   INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    name VARCHAR(50)                                      NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS events
(
    id                 INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    annotation         VARCHAR(2000),
    category           INTEGER                                          NOT NULL,
    created            TIMESTAMP WITHOUT TIME ZONE,
    description        VARCHAR(7000)                                    NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE                      NOT NULL,
    initiator          INTEGER                                          NOT NULL,
    lat                FLOAT                                            NOT NULL,
    lon                FLOAT                                            NOT NULL,
    paid               BOOLEAN                                          NOT NULL,
    participant_limit  INTEGER                                          NOT NULL,
    published          TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN                                          NOT NULL,
    title              VARCHAR(120)                                     NOT NULL,
    state              VARCHAR(50)                                      NOT NULL,
--     views              INTEGER,
--     confirmedRequest   INTEGER,
    CONSTRAINT FK_EVENT_CATEGORY FOREIGN KEY (category) REFERENCES category (id),
    CONSTRAINT FK_EVENT_INITIATOR FOREIGN KEY (initiator) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id        INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event     INTEGER,
    requester INTEGER,
    created   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    status    VARCHAR(50)                 NOT NULL,
    CONSTRAINT UNIQUE_REQUEST UNIQUE (event, requester),
    CONSTRAINT FK_REQUEST_EVENT FOREIGN KEY (event) REFERENCES events (id),
    CONSTRAINT FK_REQUEST_REQUESTER FOREIGN KEY (requester) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    pinned BOOLEAN                                          NOT NULL,
    title  VARCHAR(50) UNIQUE
);

CREATE TABLE IF NOT EXISTS compilation_events
(
    compilation_id INTEGER NOT NULL,
    event_id       INTEGER NOT NULL,
    CONSTRAINT PK_COMPILATION_EVENT PRIMARY KEY (compilation_id, event_id),
    CONSTRAINT FK_COMPILATION_EVENTS_COMPILATION FOREIGN KEY (compilation_id) REFERENCES compilations (id),
    CONSTRAINT FK__COMPILATION_EVENTS_EVENT FOREIGN KEY (event_id) REFERENCES events (id)
);