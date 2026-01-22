package br.com.gustavobatista.board.persistence.dto;

import br.com.gustavobatista.board.persistence.entity.BoardColumnKindEnum;

public record BoardColumnDTO(Long id, String name, BoardColumnKindEnum kind, int cardsAmount) {


    
}
