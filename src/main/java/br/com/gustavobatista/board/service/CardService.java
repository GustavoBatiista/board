package br.com.gustavobatista.board.service;

import static br.com.gustavobatista.board.persistence.entity.BoardColumnKindEnum.CANCEL;
import static br.com.gustavobatista.board.persistence.entity.BoardColumnKindEnum.FINAL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import br.com.gustavobatista.board.exception.CardBlockedException;
import br.com.gustavobatista.board.persistence.dao.BlockDao;
import br.com.gustavobatista.board.persistence.dao.CardDao;
import br.com.gustavobatista.board.persistence.dto.BoardColumnIdInfoDTO;
import br.com.gustavobatista.board.persistence.entity.CardEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CardService {

    private final Connection connection;

    public CardEntity insert(final CardEntity entity) throws SQLException {
        try {
            var dao = new CardDao(connection);
            dao.insert(entity);
            connection.commit();
            return entity;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public void moveToNextColumn(final Long cardId, final List<BoardColumnIdInfoDTO> boardColumnsInfo)
            throws SQLException {
        try {
            var dao = new CardDao(connection);
            var optional = dao.findById(cardId);
            var dto = optional.orElseThrow(
                    () -> new EntityNotFoundException("O card de id %s não foi encontrado".formatted(cardId)));
            if (dto.blocked()) {
                var message = "O card %s está bloqueado, é necessário desbloquea-lo para mover".formatted(cardId);
                throw new CardBlockedException(message);
            }
            var currentColumn = boardColumnsInfo.stream()
                    .filter(bc -> bc.id().equals(dto.columnId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("O card informado pertence a outro board"));
            if (currentColumn.kind().equals(FINAL)) {
                throw new CardBlockedException("O card já foi finalizado");
            }
            var nextColumn = boardColumnsInfo.stream()
                    .filter(bc -> bc.order() == currentColumn.order() + 1)
                    .findFirst().orElseThrow(() -> new IllegalStateException("O card está cancelado"));
            dao.moveToColumn(nextColumn.id(), cardId);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }

    }

    public void cancel(final Long cardId, final long cancelColumnId, final List<BoardColumnIdInfoDTO> boardColumnsInfo)
            throws SQLException {
        try {
            var dao = new CardDao(connection);
            var optional = dao.findById(cardId);
            var dto = optional.orElseThrow(
                    () -> new EntityNotFoundException("O card de id %s não foi encontrado".formatted(cardId)));
            if (dto.blocked()) {
                var message = "O card %s está bloqueado, é necessário desbloquea-lo para mover".formatted(cardId);
                throw new CardBlockedException(message);
            }
            var currentColumn = boardColumnsInfo.stream()
                    .filter(bc -> bc.id().equals(dto.columnId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("O card informado pertence a outro board"));
            if (currentColumn.kind().equals(FINAL)) {
                throw new CardBlockedException("O card já foi finalizado");
            }
            boardColumnsInfo.stream()
                    .filter(bc -> bc.order() == currentColumn.order() + 1)
                    .findFirst().orElseThrow(() -> new IllegalStateException("O card está cancelado"));
            dao.moveToColumn(cancelColumnId, cardId);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public void block(final Long id, final String reason, final List<BoardColumnIdInfoDTO> boardColumnsInfo)
            throws SQLException {
        try {
            var dao = new CardDao(connection);
            var optional = dao.findById(id);
            var dto = optional.orElseThrow(
                    () -> new EntityNotFoundException("O card de id %s não foi encontrado".formatted(id)));
            if (dto.blocked()) {
                var message = "O card %s já está bloqueado".formatted(id);
                throw new CardBlockedException(message);
            }
            var currentColumn = boardColumnsInfo.stream().filter(bc -> bc.id().equals(dto.columnId()))
                    .findFirst()
                    .orElseThrow();
            if (currentColumn.kind().equals(FINAL) || currentColumn.kind().equals(CANCEL)) {
                var message = "O card está em um coluna do tipo %s e  não pode ser bloqueado"
                        .formatted(currentColumn.kind());
                throw new IllegalStateException(message);
            }
            var blockDao = new BlockDao(connection);
            blockDao.block(reason, id);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public void unblock(final Long id, final String reason) throws SQLException {
        try {
            var dao = new CardDao(connection);
            var optional = dao.findById(id);
            var dto = optional.orElseThrow(
                    () -> new EntityNotFoundException("O card de id %s não foi encontrado".formatted(id)));
            if (!dto.blocked()) {
                var message = "O card %s não está bloqueado".formatted(id);
                throw new CardBlockedException(message);
            }
            var blockDao = new BlockDao(connection);
            blockDao.unblock(reason, id);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }

    }
}
