package br.com.gustavobatista.board.persistence.dto;

import br.com.gustavobatista.board.persistence.entity.BoardColumnKindEnum;

public record BoardColumnIdInfoDTO(Long id, int order,BoardColumnKindEnum kind) {


}
