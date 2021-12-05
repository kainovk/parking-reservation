CREATE TABLE parking_space
(
    id   UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    x    SMALLINT NOT NULL,
    y    SMALLINT NOT NULL,
    busy BIT      NOT NULL
);

CREATE TABLE car
(
    id     UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    model  VARCHAR(256) NOT NULL,
    number VARCHAR(32)  NOT NULL,
    length SMALLINT     NOT NULL,
    width  SMALLINT     NOT NULL
);

