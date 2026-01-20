package br.com.gustavobatista.board.persistence.entity;

import lombok.Data;

@Data
public class BoardColumnsEntity {

    private Long id;
    private String name;
    private int order;
    private String kind;
    private BoardColumsnKindEnum board;
    
}
