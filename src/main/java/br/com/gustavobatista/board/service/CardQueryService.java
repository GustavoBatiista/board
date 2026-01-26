package br.com.gustavobatista.board.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import br.com.gustavobatista.board.persistence.dao.CardDao;
import br.com.gustavobatista.board.persistence.dto.CardDetailsDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CardQueryService {

    private final Connection connection;

    public Optional<CardDetailsDTO> findById(final Long id) throws SQLException {
        var dao = new CardDao(connection);
        return dao.findById(id);
    }
}
