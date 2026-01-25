package br.com.gustavobatista.board.persistence.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import br.com.gustavobatista.board.persistence.dto.CardDetailsDTO;
import lombok.AllArgsConstructor;
import static br.com.gustavobatista.board.persistence.converter.OffSetDateTimeConverter.toOffsetDateTime;  // Adicione esta linha

@AllArgsConstructor
public class CardDao {

    private final Connection connection;

    public Optional<CardDetailsDTO> findById(final Long id) throws SQLException {
        var sql = """
                    SELECT c.id,
                    c.title,
                    c.description,
                    b.blocked_at,
                    b.block_reason,
                    c.boards_column_id,
                    bc.name,
                    COUNT(SELECT sub_b.id
                            FROM BLOCKS sub_b
                            WHERE sub_b.card_id = c.id) AS block_amount
                    FROM CARDS c
                    LEFT JOIN BLOCKS b
                    ON c.id = b.card_id
                    AND b.unblocked_at IS NULL
                    INNER JOIN BOARDS_COLUMNS bc
                    ON bc.id = c.column_id
                    WHERE c.id = ?
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
                        resultSet.getString("b.block_reason").isEmpty(),
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