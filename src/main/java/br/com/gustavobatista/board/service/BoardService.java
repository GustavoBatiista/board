package br.com.gustavobatista.board.service;

import java.sql.Connection;
import java.sql.SQLException;

import br.com.gustavobatista.board.persistence.dao.BoardDao;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BoardService {

    private final Connection connection;

    public boolean delete(final Long id) throws SQLException {
        var dao =  new BoardDao(connection);
        try{
            if(!dao.exists(id)){
                return false;
            }
            dao.delete(id);
            connection.commit();
            return true;
        }
        catch(SQLException e){
            connection.rollback();
            throw e;
        }
    }

}

