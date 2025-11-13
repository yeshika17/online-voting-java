package com.example.voting.repository;

import com.example.voting.model.Vote;
import com.example.voting.model.Election;
import com.example.voting.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByElectionAndUser(Election election, User user);
    List<Vote> findByElectionId(Long electionId);
}
