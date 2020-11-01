package org.sid.web;

import org.sid.dao.SocieteRepository;
import org.sid.dao.TransactionRepositry;
import org.sid.entities.Societe;
import org.sid.entities.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TranscationReactiveRestController {
    @Autowired
    private TransactionRepositry transactionRepositry;

    @GetMapping(value = "/transactions")
    public Flux<Transaction> findAl() {
        return transactionRepositry.findAll();
    }

    @GetMapping(value = "/transaction/{id}")
    public Mono<Transaction> getOne(@PathVariable String id) {
        return transactionRepositry.findById(id);
    }

    @PostMapping("/transactions")
    public Mono<Transaction> save(@RequestBody Transaction transaction) {
        return transactionRepositry.save(transaction);
    }
    @DeleteMapping("/transaction/{id}")
    public Mono<Void> deleteOne(@PathVariable String id) {
        return transactionRepositry.deleteById(id);
    }
    @PutMapping("/transactions/{id}")
    public Mono<Transaction> upDate(@RequestBody Transaction transaction,@PathVariable String id) {
        transaction.setId(id);
        return transactionRepositry.save(transaction);
    }
    @GetMapping(value = "/streamTransactions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Transaction> streamransaction() {
        return transactionRepositry.findAll();
    }

    @GetMapping(value = "/transactionsBySociete/{id}")
    public Flux<Transaction> transactionsBySociete(@PathVariable String id) {
        Societe societe = new Societe();
        societe.setId(id);
        return transactionRepositry.findBySociete(societe);
    }
}
