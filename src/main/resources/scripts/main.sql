
 --liquibase formatted sql

--changeset yuraskaz:1

CREATE TABLE notification_task (
    ID INT ,
    ID_CHAT INT,
    NOTIFICATION TEXT,
    DATE TIMESTAMP,
    PRIMARY KEY (ID)

);


