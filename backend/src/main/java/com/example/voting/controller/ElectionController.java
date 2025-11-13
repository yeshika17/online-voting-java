package com.example.voting.controller;

import com.example.voting.model.*;
import com.example.voting.service.VotingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/elections")
public class ElectionController {
    @Autowired private VotingService votingService;

    @GetMapping
    public List<Election> list() { return votingService.listElections(); }

    @GetMapping("/{id}")
    public Election get(@PathVariable Long id) { return votingService.getElection(id).orElse(null); }

    @GetMapping("/{id}/candidates")
    public List<Candidate> candidates(@PathVariable Long id) { return votingService.listCandidates(id); }

    @PostMapping("/{id}/vote")
    public Map<String,String> vote(@PathVariable Long id, @RequestBody VoteRequest req, HttpServletRequest request) {
        String username = (String) request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : (String) request.getAttribute("javax.servlet.http.HttpServletRequest.user");
        // in our JwtFilter we set Authentication principal to username (String)
        Object principal = request.getUserPrincipal();
        if(principal==null) {
            principal = request.getAttribute("org.springframework.security.core.Authentication.principal");
        }
        String user = null;
        if(principal instanceof String) user = (String) principal;
        else if(principal!=null) user = principal.toString();

        if(user==null) throw new RuntimeException("Unauthorized");
        votingService.vote(id, req.getCandidateId(), user);
        return Map.of("message","Vote recorded");
    }

    @GetMapping("/{id}/results")
    public List<Result> results(@PathVariable Long id) {
        List<Vote> votes = votingService.getVotes(id);
        return votes.stream().collect(Collectors.groupingBy(v->v.getCandidate().getId()))
                .entrySet().stream().map(e-> new Result(e.getKey(), e.getValue().size()))
                .collect(Collectors.toList());
    }

    @PostMapping("/seed")
    public String seed() {
        // create a sample election with two candidates
        Election e = new Election();
        e.setName("Demo Election");
        e.setDescription("Demo election");
        e.setStartsAt(Instant.now().minusSeconds(60));
        e.setEndsAt(Instant.now().plusSeconds(3600));
        Candidate c1 = new Candidate();
        c1.setName("Alice");
        c1.setParty("A Party");
        Candidate c2 = new Candidate();
        c2.setName("Bob");
        c2.setParty("B Party");
        c1.setElection(e); c2.setElection(e);
        e.setCandidates(List.of(c1,c2));
        // save via votingService's repo indirectly by accessing repository through service - to keep it simple, use winners:
        // Using votingService's repository is not exposed; instead we rely on JPA cascade when saving using entity manager auto - simpler approach:
        // We'll emulate by saving through electionRepo via reflection (not ideal). To keep seed minimal, instruct user to create via H2 console or API.
        return "Seed endpoint not implemented server-side; use H2 console to add initial data or call register endpoints.";
    }

    @Data static class VoteRequest { private Long candidateId; }
    @Data static class Result { private Long candidateId; private int votes; public Result(Long c,int v){this.candidateId=c;this.votes=v;} }
}
