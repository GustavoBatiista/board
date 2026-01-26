package br.com.gustavobatista.board.persistence.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

import com.mysql.cj.jdbc.StatementImpl;

import br.com.gustavobatista.board.persistence.dto.CardDetailsDTO;
import br.com.gustavobatista.board.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;
import static br.com.gustavobatista.board.persistence.converter.OffSetDateTimeConverter.toOffsetDateTime; // Adicione esta linha

@AllArgsConstructor
public class CardDao {

    private final Connection connection;

    public CardEntity insert(final CardEntity entity) throws SQLException {
        var sql = "INSERT INTO CARDS (title, description, boards_column_id) VALUES (?, ?, ?)";
        try (var statement = connection.prepareStatement(sql)) {
            var i = 1;
            statement.setString(i++, entity.getTitle());
            statement.setString(i++, entity.getDescription());
            statement.setLong(i, entity.getBoardColumn().getId());
            statement.executeUpdate();
            if(statement instanceof StatementImpl impl) {
                entity.setId(impl.getLastInsertID());
            }
        }
        return entity;
    }

    public void moveToColumn(final Long columnId, final Long cardId) throws SQLException {
        var sql = "UPDATE CARDS SET boards_column_id = ? WHERE id = ?";
        try (var statement = connection.prepareStatement(sql)) {
            var i = 1;
            statement.setLong(i++, columnId);
            statement.setLong(i++, cardId);
            statement.executeUpdate();
        }
    }

    public Optional<CardDetailsDTO> findById(final Long id) throws SQLException {
        var sql = """
                    SELECT c.id,
                    c.title,
                    c.description,
                    b.blocked_at,
                    b.block_reason,
                    c.boards_column_id,
                    bc.name,
                    (SELECT COUNT(sub_b.id)
                            FROM BLOCKS sub_b
                            WHERE sub_b.card_id = c.id) AS block_amount
                    FROM CARDS c
                    LEFT JOIN BLOCKS b
                      ON c.id = b.card_id
                     AND b.unblocked_at IS NULL
                    INNER JOIN BOARDS_COLUMNS bc
                      ON bc.id = c.column_id
                    WHERE c.id = ?;
                """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeQuery();
            var resultSet = statement.getResultSet();
            if (resultSet.next()) {
                var dto = new CardDetailsDTO(
                        resultSet.getLong("c.id"),
                        resultSet.getString("c.title"),
                        resultSet.getString("c.description"),
                        Objects.nonNull(resultSet.getString("b.block_reason")),
                        toOffsetDateTime(resultSet.getTimestamp("b.blocked_at")),
                        resultSet.getString("b.block_reason"),
                        resultSet.getInt("block_amount"),
                        resultSet.getLong("c.boards_column_id"),
                        resultSet.getString("bc.name"));
                return Optional.of(dto);
            }
        }
        return Optional.empty();
    }
}