package com.zytoune.dailygame.entity.games;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_collections")
public class Collections {

    @Id
    private int id;
    @Column(name = "as_child_relations")
    @JsonProperty("as_child_relations")
    private List<Integer> asChildRelations;
    @Column(name = "as_parent_relations")
    @JsonProperty("as_parent_relations")
    private List<Integer> asParentRelations;
    private List<Integer> games;
    private String name;
    private String slug;
}
