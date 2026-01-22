package br.com.gustavobatista.board.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import br.com.gustavobatista.board.persistence.dao.BoardColumnDao;
import br.com.gustavobatista.board.persistence.dao.BoardDao;
import br.com.gustavobatista.board.persistence.dto.BoardDetailsDTO;
import br.com.gustavobatista.board.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BoardQueryService {

    private final Connection connection;

    public Optional<BoardEntity> findById(final Long id) throws SQLException {
        var dao = new BoardDao(connection);
        var boardColumnDao = new BoardColumnDao(connection);
        var Optional = dao.findById(id);
        if (Optional.isPresent()) {
            var entity = Optional.get();
            entity.setBoardColumns(boardColumnDao.findByBoardId(entity.getId()));
            return Optional.of(entity);
        }
        return Optional.empty();

    }

    public Optional<BoardDetailsDTO> showBoardDetails(final Long id) throws SQLException {
        var dao = new BoardDao(connection);
        var boardColumnDao = new BoardColumnDao(connection);
        var Optional = dao.findById(id);
        if (Optional.isPresent()) {
            var entity = Optional.get();
            var columns = boardColumnDao.findByBoardIdWithDetails(entity.getId());
            var dto = new BoardDetailsDTO(entity.getId(), entity.getName(), columns);
            return Optional.of(dto);
        }
        return Optional.empty();
    }
}