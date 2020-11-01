package org.sid;

import org.sid.dao.SocieteRepository;
import org.sid.dao.TransactionRepositry;
import org.sid.entities.Societe;
import org.sid.entities.Transaction;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.util.stream.Stream;

@SpringBootApplication
public class ReactiveProgApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveProgApplication.class, args);
    }

    @Bean
    CommandLineRunner start(SocieteRepository societeRepository, TransactionRepositry transactionRepositry) {
        return args -> {
            societeRepository.deleteAll().subscribe(null, null, () -> {
                Stream.of("SG", "AWB", "BMCE", "AXA", "MAIF").forEach(s -> {
                    societeRepository.save(new Societe(s, s, 100 + Math.random() * 900)).subscribe(
                            soc -> {
                                System.out.println(soc.toString());
                            }
                    );
                });
            });
            transactionRepositry.deleteAll().subscribe(null, null, () -> {
				Stream.of("SG", "AWB", "BMCE", "AXA", "MAIF").forEach(s -> {
					societeRepository.findById(s).subscribe(soc -> {
						for(int i=0; i<10; i++) {
							Transaction transaction= new Transaction();
							transaction.setInstant(Instant.now());
							transaction.setSociete(soc);
							transaction.setPrice(soc.getPrice()*(1+(Math.random()*12-6)/100));
							transactionRepositry.save(transaction).subscribe(t-> {
										System.out.println(t.toString());
									}

							);

						}


					});
				});
					});


        };

    }

}
