--liquibase formatted sql
--changeset gustavo:202601201526
--comment: blocks table create

CREATE TABLE BLOCKS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    blocked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    block_reason VARCHAR(255) NOT NULL,
    unblocked_at TIMESTAMP DEFAULT NULL,
    unblock_reason VARCHAR(255) DEFAULT NULL,
    card_id BIGINT NOT NULL,
    CONSTRAINT cards__block_fk FOREIGN KEY (card_id) REFERENCES CARDS(id) ON DELETE CASCADE
) ENGINE=InnoDB;

--rollback DROP TABLE BLOCKS