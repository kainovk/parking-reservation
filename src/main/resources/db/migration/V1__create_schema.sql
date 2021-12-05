CREATE TABLE car
(
    id     UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    model  VARCHAR(256) NOT NULL,
    number VARCHAR(32)  NOT NULL,
    length SMALLINT     NOT NULL,
    width  SMALLINT     NOT NULL
);

CREATE TABLE parking_space
(
    id   UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    x    SMALLINT NOT NULL,
    y    SMALLINT NOT NULL,
    busy BIT      NOT NULL
);

CREATE TABLE booking
(
    car_id    UUID NOT NULL,
    ps_id     UUID NOT NULL,
    time_from TIME NOT NULL,
    time_to   TIME NOT NULL,
    FOREIGN KEY (car_id) REFERENCES car (id) ON DELETE CASCADE,
    FOREIGN KEY (ps_id) REFERENCES parking_space (id) ON DELETE CASCADE,
    CONSTRAINT FK_CAR_PS UNIQUE (car_id, ps_id)
);
