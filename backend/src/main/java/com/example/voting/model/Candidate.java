package com.example.voting.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Candidate {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String party;

    @ManyToOne
    @JoinColumn(name = "election_id")
    private Election election;
}
