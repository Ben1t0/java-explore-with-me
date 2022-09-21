CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255)                            NOT NULL,
    email VARCHAR(512)                            NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id),
    CONSTRAINT UQ_CATEGORY_NAME UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS locations
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name      VARCHAR(255)                            NOT NULL,
    radius    DOUBLE PRECISION                        NOT NULL,
    latitude  DOUBLE PRECISION                        NOT NULL,
    longitude DOUBLE PRECISION                        NOT NULL,
    CONSTRAINT pk_location PRIMARY KEY (id),
    CONSTRAINT UQ_LOCATION_NAME UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    initiator_id       BIGINT                                  NOT NULL,
    annotation         VARCHAR(255),
    description        VARCHAR(255),
    title              VARCHAR(255)                            NOT NULL,
    paid               BOOLEAN                                 NOT NULL,
    request_moderation BOOLEAN                                 NOT NULL,
    created            TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    published          TIMESTAMP WITHOUT TIME ZONE,
    category_id        BIGINT                                  NOT NULL,
    participant_limit  INT                                     NOT NULL,
    location_id        BIGINT,
    latitude           REAL                                    NOT NULL,
    longitude          REAL                                    NOT NULL,
    state              VARCHAR(20)                             NOT NULL,
    CONSTRAINT pk_event PRIMARY KEY (id),
    CONSTRAINT FK_EVENT_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT FK_EVENT_ON_INITIATOR FOREIGN KEY (initiator_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT FK_EVENT_ON_LOCATION FOREIGN KEY (location_id) REFERENCES locations (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    event_id     BIGINT                                  NOT NULL,
    requester_id BIGINT                                  NOT NULL,
    status       VARCHAR(20)                             NOT NULL,
    CONSTRAINT pk_request PRIMARY KEY (id),
    CONSTRAINT FK_REQUEST_ON_EVENT FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT FK_REQUEST_ON_REQUESTER FOREIGN KEY (requester_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT UQ_EVENT_AND_REQUESTER_ID UNIQUE (event_id, requester_id)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned BOOLEAN                                 NOT NULL,
    title  VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_compilation PRIMARY KEY (id),
    CONSTRAINT UQ_COMPILATION_TITLE UNIQUE (title)
);

CREATE TABLE IF NOT EXISTS events_compilations
(
    event_id  BIGINT NOT NULL,
    compil_id BIGINT NOT NULL,
    CONSTRAINT pk_event_compil PRIMARY KEY (event_id, compil_id),
    CONSTRAINT FK_EVENT_COMPIL_ON_EVENT FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT FK_EVENT_COMPIL_ON_COMPIL FOREIGN KEY (compil_id) REFERENCES compilations (id) ON DELETE CASCADE
);

CREATE OR REPLACE FUNCTION distance(lat1 float, lon1 float, lat2 float, lon2 float)
    RETURNS float
AS
'
    declare
        dist float = 0;
        rad_lat1 float;
        rad_lat2 float;
        theta float;
        rad_theta float;
    BEGIN
        IF lat1 = lat2 AND lon1 = lon2
        THEN
            RETURN dist;
        ELSE
            -- переводим градусы широты в радианы
            rad_lat1 = pi() * lat1 / 180;
            -- переводим градусы долготы в радианы
            rad_lat2 = pi() * lat2 / 180;
            -- находим разность долгот
            theta = lon1 - lon2;
            -- переводим градусы в радианы
            rad_theta = pi() * theta / 180;
            -- находим длину ортодромии
            dist = sin(rad_lat1) * sin(rad_lat2) + cos(rad_lat1) * cos(rad_lat2) * cos(rad_theta);

            IF dist > 1
            THEN dist = 1;
            END IF;

            dist = acos(dist);
            -- переводим радианы в градусы
            dist = dist * 180 / pi();
            -- переводим градусы в километры
            dist = dist * 60 * 1.8524;

            RETURN dist;
        END IF;
    END;
'
    LANGUAGE PLPGSQL;