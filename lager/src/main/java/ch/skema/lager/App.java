package ch.skema.lager;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ch.skema.lager.domain.BestellPosition;
import ch.skema.lager.domain.Bestellung;
import ch.skema.lager.domain.Kategorie;
import ch.skema.lager.domain.Kunde;
import ch.skema.lager.domain.Produkt;
import ch.skema.lager.repository.BestellPositionRepository;
import ch.skema.lager.repository.BestellungRepository;
import ch.skema.lager.repository.KategorieRepository;
import ch.skema.lager.repository.KundeRepository;
import ch.skema.lager.repository.ProduktRepository;

@SpringBootApplication
public class App {
	private static final Logger log = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		SpringApplication.run(App.class);
//		new BrowserLauncher().launch();
	}

	@Bean
	public CommandLineRunner loadData(KundeRepository repository, KategorieRepository katRepo, ProduktRepository produktRepo, BestellungRepository bestellRepo, BestellPositionRepository bestellPosRepo) {
		return (args) -> {
			// save a couple of customers
			repository.save(new Kunde("Jack Bauer"));
			repository.save(new Kunde("Chloe O'Brian"));
			Kunde kunde = new Kunde("Kim Bauer");
			repository.save(kunde);
			Kunde kunde2 = new Kunde("David Palmer");
			repository.save(kunde2);
			repository.save(new Kunde("Michelle Dessler"));

			Kategorie salzKategorie = new Kategorie("Salz");
			katRepo.save(salzKategorie);
			katRepo.save(new Kategorie("Seifen"));
			katRepo.save(new Kategorie("Nahrung"));

			Produkt produkt = new Produkt("Basensalz 1kg");
			produkt.setAbgaben(BigDecimal.ONE);
			produkt.setAktiv(true);
			produkt.setEinkaufspreisBern(BigDecimal.TEN);
			produkt.setEinkaufspreisSl(BigDecimal.TEN);
			produkt.setVerkaufspreis(BigDecimal.ONE);
			produkt.setKategorie(salzKategorie);
			produktRepo.save(produkt);

			Bestellung bestellung = new Bestellung(new Date());
			bestellung.setKunde(kunde);
			bestellRepo.save(bestellung);
			Bestellung bestellung2 = new Bestellung(new Date());
			bestellung2.setKunde(kunde2);
			bestellung2.setErledigt(true);
			bestellRepo.save(bestellung2);

			BestellPosition position = new BestellPosition();
			position.setAnzahl(42L);
			position.setProdukt(produkt);
			position.setRabatt(BigDecimal.ZERO);
			position.setStueckpreis(new BigDecimal(3));
			position.setBestellung(bestellung);
			bestellPosRepo.save(position);

			// fetch all customers
			log.info("Customers found with findAll():");
			log.info("-------------------------------");
			for (Kunde customer : repository.findAll()) {
				log.info(customer.toString());
			}
			log.info("");

			// fetch an individual customer by ID
			Kunde customer = repository.findOne(1L);
			log.info("Customer found with findOne(1L):");
			log.info("--------------------------------");
			log.info(customer.toString());
			log.info("");

			// fetch customers by last name
			log.info("Customer found with findByLastNameStartsWithIgnoreCase('Bauer'):");
			log.info("--------------------------------------------");
			for (Kunde bauer : repository.findByNameStartsWithIgnoreCase("Bauer")) {
				log.info(bauer.toString());
			}
			log.info("");
		};
	}
}
