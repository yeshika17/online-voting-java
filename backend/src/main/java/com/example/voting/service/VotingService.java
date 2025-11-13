package com.example.voting.service;

import com.example.voting.model.*;
import com.example.voting.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class VotingService {
    @Autowired private ElectionRepository electionRepo;
    @Autowired private CandidateRepository candidateRepo;
    @Autowired private VoteRepository voteRepo;
    @Autowired private UserRepository userRepo;

    public List<Election> listElections() { return electionRepo.findAll(); }

    public Optional<Election> getElection(Long id) { return electionRepo.findById(id); }

    public List<Candidate> listCandidates(Long electionId) { return candidateRepo.findByElectionId(electionId); }

    @Transactional
    public void vote(Long electionId, Long candidateId, String username) {
        Election election = electionRepo.findById(electionId).orElseThrow(() -> new RuntimeException("Election not found"));
        Instant now = Instant.now();
        if(election.getStartsAt()!=null && election.getEndsAt()!=null) {
            if(now.isBefore(election.getStartsAt()) || now.isAfter(election.getEndsAt())) throw new RuntimeException("Election not active");
        }
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Candidate candidate = candidateRepo.findById(candidateId).orElseThrow(() -> new RuntimeException("Candidate not found"));
        Vote vote = new Vote();
        vote.setElection(election);
        vote.setCandidate(candidate);
        vote.setUser(user);
        try {
            voteRepo.save(vote);
        } catch (DataIntegrityViolationException ex) {
            throw new RuntimeException("Already voted");
        }
    }

    public List<Vote> getVotes(Long electionId) { return voteRepo.findByElectionId(electionId); }
}
