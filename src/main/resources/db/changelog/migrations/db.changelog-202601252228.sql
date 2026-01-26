--liquibase formatted sql
--changeset gustavo:202601201526
--comment: set unblocked_reason nullable

ALTER TABLE BLOCKS MODIFY COLUMN unblocked_reason VARCHAR(255) NULL;

--rollback ALTER TABLE BLOCKS MODIFY COLUMN unblocked_reason VARCHAR(255) NOT NULL;
