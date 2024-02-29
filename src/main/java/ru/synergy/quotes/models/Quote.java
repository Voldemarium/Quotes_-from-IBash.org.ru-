package ru.synergy.quotes.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "quotes")
@ToString
public class Quote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "\"quoteId\"", nullable = false)
    private Integer quoteId;

//    @Lob //(Large Object - будут храниться только ссылки на текст)
//    columnDefinition = "text" - чтобы в таблице "quotes"
    @Column(columnDefinition = "text", name = "text", nullable = false)
    private String text;

}
