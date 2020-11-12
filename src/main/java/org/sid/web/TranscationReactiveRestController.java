package org.sid.web;

import org.sid.dao.SocieteRepository;
import org.sid.dao.TransactionRepositry;
import org.sid.entities.Societe;
import org.sid.entities.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;


@RestController
public class TranscationReactiveRestController {
    @Autowired
    private TransactionRepositry transactionRepositry;
    @Autowired
    private SocieteRepository societeRepository;

    private WebClient webClient;

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
    public Mono<Transaction> upDate(@RequestBody Transaction transaction, @PathVariable String id) {
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

    @GetMapping(value = "/streamTransactionsBySociete/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Transaction> stream(@PathVariable String id) {
        return societeRepository.findById(id).flatMapMany(soc -> {
            Flux<Long> interval = Flux.interval(Duration.ofMillis(1000));
            Flux<Transaction> transactionFlux = Flux.fromStream(Stream.generate(() -> {
                Transaction transaction = new Transaction();
                transaction.setInstant(Instant.now());
                transaction.setSociete(soc);
                transaction.setPrice(soc.getPrice() * (1 + (Math.random() * 12 - 6) / 100));
                return transaction;
            }));
            return Flux.zip(interval, transactionFlux).map(data -> {
                return data.getT2();
            }).share();
        });

    }

    @GetMapping(value = "/events/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Double> event(@PathVariable String id) {
        WebClient webClient = WebClient.create("http://localhost:8082");
        Flux<Double> eventFlux = webClient.get()
                .uri("/streamEvent/" + id)
                .retrieve().bodyToFlux(Event.class)
                .map(data -> data.getValue());
        return eventFlux;
    }

}
