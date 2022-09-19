CREATE TABLE IF NOT EXISTS stat
(
    id             SERIAL,
    last_update    TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    count_requests BIGINT,
    start_date     TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT stat_pkey PRIMARY KEY (id)
);


