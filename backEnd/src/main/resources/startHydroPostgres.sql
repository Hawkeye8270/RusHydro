CREATE DATABASE rushydro;

CREATE TABLE IF NOT EXISTS data (
                                     id BIGSERIAL PRIMARY KEY,
                                     river VARCHAR(50),
                                     ges VARCHAR(50),
                                     date DATE,
                                     level NUMERIC(10,2)
);