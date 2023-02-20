-- liquibase formatted sql

-- changeset liquibase:1
CREATE TABLE hello_wolrd (
    label VARCHAR(256),
    PRIMARY KEY (label)
);

-- changeset liquibase:2

CREATE TABLE auth_user (
    auth_user_id SERIAL,
    email VARCHAR(256) not null unique,
    password VARCHAR(256) not null,
    salt VARCHAR(256) not null,
    first_name VARCHAR(256) not null,
    last_name VARCHAR(256) not null,
    PRIMARY KEY (auth_user_id)
);

CREATE TABLE doc (
    doc_id SERIAL,
    name VARCHAR(256) not null,
    mime_type VARCHAR(256) not null,
    created_by_user_id int not null,
    updated_by_user_id int not null,
    created_datetime TIMESTAMPTZ not null,
    updated_datetime TIMESTAMPTZ not null,
    PRIMARY KEY (doc_id),
    FOREIGN KEY (created_by_user_id) REFERENCES auth_user (auth_user_id) ON UPDATE CASCADE,
    FOREIGN KEY (updated_by_user_id) REFERENCES auth_user (auth_user_id) ON UPDATE CASCADE
);

CREATE TABLE doc_version (
    doc_version_id SERIAL,
    version_num int not null,
    doc_id int not null,
    data bytea not null,
    created_datetime TIMESTAMPTZ not null,
    created_by_user_id int not null,
    PRIMARY KEY (doc_version_id),
    FOREIGN KEY (doc_id) REFERENCES doc (doc_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (created_by_user_id) REFERENCES auth_user (auth_user_id) ON UPDATE CASCADE
);

CREATE TABLE doc_task (
    doc_task_id SERIAL,
    doc_id int not null,
    name VARCHAR(256) not null,
    status VARCHAR(256) not null,
    content TEXT,
    created_by_user_id int not null,
    updated_by_user_id int not null,
    created_datetime TIMESTAMPTZ not null,
    updated_datetime TIMESTAMPTZ not null,
    PRIMARY KEY (doc_task_id),
    FOREIGN KEY (doc_id) REFERENCES doc (doc_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (created_by_user_id) REFERENCES auth_user (auth_user_id) ON UPDATE CASCADE
);

-- changeset liquibase:3

CREATE TABLE SPRING_SESSION (
	PRIMARY_ID CHAR(36) NOT NULL,
	SESSION_ID CHAR(36) NOT NULL,
	CREATION_TIME BIGINT NOT NULL,
	LAST_ACCESS_TIME BIGINT NOT NULL,
	MAX_INACTIVE_INTERVAL INT NOT NULL,
	EXPIRY_TIME BIGINT NOT NULL,
	PRINCIPAL_NAME VARCHAR(100),
	CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
);

CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);

CREATE TABLE SPRING_SESSION_ATTRIBUTES (
	SESSION_PRIMARY_ID CHAR(36) NOT NULL,
	ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
	ATTRIBUTE_BYTES BYTEA NOT NULL,
	CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
	CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE
);

