CREATE TABLE project
(
    id                 SERIAL PRIMARY KEY NOT NULL,
    name               VARCHAR            NOT NULL,
    description        VARCHAR,
    archived           BIT                NOT NULL,
    days_wr_upsertable INTEGER CHECK ( days_wr_upsertable >= 0 )
);

CREATE TABLE work_record
(
    id          SERIAL PRIMARY KEY NOT NULL,
    record_date DATE               NOT NULL,
    minutes     INTEGER CHECK ( minutes > 0 AND minutes <= 24 * 60 ),
    description VARCHAR,
    project_id  INTEGER            NOT NULL,
    user_id     VARCHAR            NOT NULL,
    FOREIGN KEY (project_id) REFERENCES Project (id)
);

CREATE TABLE project_user
(
    id          SERIAL PRIMARY KEY NOT NULL,
    project_id  INTEGER            NOT NULL,
    user_id     VARCHAR            NOT NULL,
    hourly_cost INTEGER CHECK ( hourly_cost >= 0 ),
    FOREIGN KEY (project_id) REFERENCES Project (id),
    UNIQUE (project_id, user_id)
);