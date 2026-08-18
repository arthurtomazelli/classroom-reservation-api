CREATE TABLE classrooms (
    id UUID PRIMARY KEY,
    block VARCHAR(3) NOT NULL,
    number VARCHAR(5) NOT NULL,
    capacity INTEGER CHECK(capacity > 0),
    type VARCHAR(50) CHECK (type IN ('LABORATORY', 'REGULAR', 'AUDITORIUM')),
    available BOOLEAN,
    version INTEGER,

    UNIQUE(block, number)
);

CREATE TABLE reservations (
    id UUID PRIMARY KEY,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    status VARCHAR(20) CHECK (status IN ('CONFIRMED', 'CANCELLED')),
    classroom_id UUID REFERENCES classrooms(id),
    version INTEGER
);