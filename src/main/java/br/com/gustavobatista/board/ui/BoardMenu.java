package br.com.gustavobatista.board.ui;

import java.sql.SQLException;
import java.util.Scanner;

import br.com.gustavobatista.board.persistence.config.ConnectionConfig;
import br.com.gustavobatista.board.persistence.entity.BoardEntity;
import br.com.gustavobatista.board.service.BoardQueryService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BoardMenu {

    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    private final BoardEntity entity;

    public void execute() {
        try {
            System.out.printf("Bem vindo ao board %s, selecione a operação desejada: %n", entity.getId());
            var option = -1;
            while (option != 9) {
                System.out.println("1 - Criar um card");
                System.out.println("2 - Mover um card");
                System.out.println("3 - Bloquear um card");
                System.out.println("4 - Desbloquear um card");
                System.out.println("5 - Cancelar um card");
                System.out.println("6 - Visualizar board");
                System.out.println("7 - Visualizar coluna com cards");
                System.out.println("8 - Visualizar card");
                System.out.println("9 - Voltar para o menu anterior");
                System.out.println("10 - Sair");
                option = scanner.nextInt();
                switch (option) {
                    case 1 -> createCard();
                    case 2 -> moveCardToNextColumn();
                    case 3 -> blockCard();
                    case 4 -> unblockCard();
                    case 5 -> cancelCard();
                    case 6 -> showBoard();
                    case 7 -> showColumn();
                    case 8 -> showCard();
                    case 9 -> System.out.println("Voltando para o menu anterior");
                    case 10 -> System.exit(0);
                    default -> System.out.println("Opção inválida, informe uma opção do menu");
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private Object createCard() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createCard'");
    }

    private Object moveCardToNextColumn() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'moveCardToNextColumn'");
    }

    private Object blockCard() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'blockCard'");
    }

    private Object unblockCard() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'unblockCard'");
    }

    private Object cancelCard() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cancelCard'");
    }

    private void showBoard() throws SQLException {
        try (var connection = ConnectionConfig.getConnection()) {
            var optional = new BoardQueryService(connection).showBoardDetails(entity.getId());
            optional.ifPresent(b -> {
                System.out.printf("Board: [%s%s]\n", b.id(), b.name());
                b.columns().forEach(c -> System.out.printf("Coluna: [%s] tipo: [%s] tem  %s cards\n", c.name(),
                        c.kind(), c.cardsAmount()));
            });
        }
    }

    private Object showColumn() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'showColumn'");
    }

    private Object showCard() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'showCard'");
    }

}
