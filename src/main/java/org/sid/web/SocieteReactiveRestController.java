package org.sid.web;

import org.sid.dao.SocieteRepository;
import org.sid.entities.Societe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class SocieteReactiveRestController {
    @Autowired
    private SocieteRepository societeRepository;

    @GetMapping(value = "societes")
    public Flux<Societe> findAl() {
        return societeRepository.findAll();
    }

    @GetMapping(value = "societe/{id}")
    public Mono<Societe> getOne(@PathVariable String id) {
        return societeRepository.findById(id);
    }

    @PostMapping("/societes")
    public Mono<Societe> save(@RequestBody Societe societe) {
        return societeRepository.save(societe);
    }
    @DeleteMapping("societe/{id}")
    public Mono<Void> deleteOne(@PathVariable String id) {
        return societeRepository.deleteById(id);
    }
    @PutMapping("/societes/{id}")
    public Mono<Societe> upDate(@RequestBody Societe societe,@PathVariable String id) {
        societe.setId(id);
        return societeRepository.save(societe);
    }

}
