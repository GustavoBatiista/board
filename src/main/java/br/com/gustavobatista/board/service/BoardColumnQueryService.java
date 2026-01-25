package br.com.gustavobatista.board.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import br.com.gustavobatista.board.persistence.dao.BoardColumnDao;
import br.com.gustavobatista.board.persistence.entity.BoardColumnEntity;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BoardColumnQueryService {

    private final Connection connection;

    public Optional<BoardColumnEntity> findById(final Long id) throws SQLException {
        var dao = new BoardColumnDao(connection);
        return dao.findById(id);
    }
}