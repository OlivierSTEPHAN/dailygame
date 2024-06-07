package com.zytoune.dailygame.entity.games;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_companies")
public class Companies {
   
    @Id
    private int id;

    @Column(name = "changed_company_id")
    @JsonProperty("changed_company_id")
    private Integer changedCompanyId;
    private Integer country;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    private String name;
    private Integer parent;
    private String slug;
    @Column(name = "start_date")
    @JsonProperty("start_date")
    private Timestamp startDate;
    @Column(name = "start_date_category")
    @JsonProperty("start_date_category")
    private Integer startDateCategory;
    private String url;
    private List<Integer> websites;
}
