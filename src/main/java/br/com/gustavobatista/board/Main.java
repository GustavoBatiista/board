package br.com.gustavobatista.board;

import java.sql.SQLException;

import br.com.gustavobatista.board.persistence.config.ConnectionConfig;
import br.com.gustavobatista.board.persistence.migration.MigrationStrategy;

public class Main {

    public static void main(String[] args) throws SQLException{
        
        try(var connection = ConnectionConfig.getConnection()){
            new MigrationStrategy(connection).executeMigration();
    }
}
}