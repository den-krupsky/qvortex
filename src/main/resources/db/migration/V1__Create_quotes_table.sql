CREATE TABLE quote (
    time timestamp WITH time zone NOT NULL UNIQUE,
    value numeric CHECK (value > 0)
);

