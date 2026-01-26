package br.com.gustavobatista.board.ui;

import java.sql.SQLException;
import java.util.Scanner;

import br.com.gustavobatista.board.persistence.config.ConnectionConfig;
import br.com.gustavobatista.board.persistence.dto.BoardColumnIdInfoDTO;
import br.com.gustavobatista.board.persistence.entity.BoardColumnEntity;
import br.com.gustavobatista.board.persistence.entity.BoardEntity;
import br.com.gustavobatista.board.persistence.entity.CardEntity;
import br.com.gustavobatista.board.service.BoardColumnQueryService;
import br.com.gustavobatista.board.service.BoardQueryService;
import br.com.gustavobatista.board.service.CardQueryService;
import br.com.gustavobatista.board.service.CardService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BoardMenu {

    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    private final BoardEntity entity;

    public void execute() {
        try {
            System.out.printf("Bem vindo ao board %s, selecione a operação desejada:\n", entity.getId());
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
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void createCard() throws SQLException {
        var card = new CardEntity();
        System.out.println("Informe o titulo do card: ");
        card.setTitle(scanner.next());
        System.out.println("Informe a descrição do card: ");
        card.setDescription(scanner.next());
        card.setBoardColumn(entity.getInitialColumn());
        try (var connection = ConnectionConfig.getConnection()) {
            new CardService(connection).insert(card);
        }
    }

    private void moveCardToNextColumn() throws SQLException {
        System.out.println("Informe o id do card a ser movido para a próxima coluna: ");
        var CardId = scanner.nextLong();
        var boardColumnsInfo = entity.getBoardColumns().stream()
                .map(bc -> new BoardColumnIdInfoDTO(bc.getId(), bc.getOrder(), bc.getKind()))
                .toList();
        try (var connection = ConnectionConfig.getConnection()) {
            new CardService(connection).moveToNextColumn(CardId, boardColumnsInfo);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void blockCard() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'blockCard'");
    }

    private void unblockCard() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'unblockCard'");
    }

    private void cancelCard() throws SQLException {
        System.out.println("Informe o id do card a ser movido para a coluna de cancelamento: ");
        var CardId = scanner.nextLong();
        var cancelColumn = entity.getCancelColumn();
        var boardColumnsInfo = entity.getBoardColumns().stream()
                .map(bc -> new BoardColumnIdInfoDTO(bc.getId(), bc.getOrder(), bc.getKind()))
                .toList();
        try (var connection = ConnectionConfig.getConnection()) {
            new CardService(connection).cancel(CardId, cancelColumn.getId(), boardColumnsInfo);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

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

    private void showColumn() throws SQLException {
        System.out.printf("Escolha uma coluna do board %s\n", entity.getName());
        var columnsIds = entity.getBoardColumns()
                .stream()
                .map(BoardColumnEntity::getId)
                .toList();
        var selectedColumn = -1L;
        while (!columnsIds.contains(selectedColumn)) {
            entity.getBoardColumns()
                    .forEach(c -> System.out.printf("%s - %s [%s]\n", c.getId(), c.getName(), c.getKind()));
            selectedColumn = scanner.nextLong();
        }
        try (var connection = ConnectionConfig.getConnection()) {
            var column = new BoardColumnQueryService(connection).findById(selectedColumn);
            column.ifPresent(co -> {
                System.out.printf("Coluna: [%s] tipo: [%s] \n", co.getName(), co.getKind());
                co.getCards().forEach(ca -> System.out.printf("Card: [%s] - %s.\nDescrição: %s\n",
                        ca.getId(), ca.getTitle(), ca.getDescription()));
            });
        }
    }

    private void showCard() throws SQLException {
        System.out.println("Informe o id do card a ser visualizado: ");
        var selectedCardId = scanner.nextLong();
        try (var connection = ConnectionConfig.getConnection()) {
            new CardQueryService(connection).findById(selectedCardId)
                    .ifPresentOrElse(c -> {
                        System.out.printf("Card: %s - %S.\n", c.id(), c.title());
                        System.out.printf("Descrição: %s\n", c.description());
                        System.out.printf(
                                c.blocked() ? "Está bloqueado. Motivo: %s" + c.blockReason() : "Não está bloqueado.");
                        System.out.printf("Já foi bloqueado %s vezes.\n", c.blockAmount());
                        System.out.printf("Está no momento na Coluna: %s - %s.\n", c.columnId(), c.columnName());
                    },
                            () -> System.out.printf("Não existe um card com id %s\n", selectedCardId));
        }
    }

}
