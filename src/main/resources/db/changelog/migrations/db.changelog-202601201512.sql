--liquibase formatted sql
--changeset gustavo:202601201512
--comment: boards_columns table create

CREATE TABLE BOARDS_COLUMNS(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    `order` INT NOT NULL,
    kind VARCHAR(7) NOT NULL,
    boards_id BIGINT NOT NULL,
    CONSTRAINT boards__columns_board_fk FOREIGN KEY (boards_id) REFERENCES BOARDS(id) ON DELETE CASCADE,
    CONSTRAINT id_order_uk UNIQUE KEY unique_boards_id_order(boards_id, `order`)
) ENGINE=InnoDB;

--rollback DROP TABLE BOARDS_COLUMNS