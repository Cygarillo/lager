package ch.skema.lager.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Produkt {
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private Long verkaufspreis;
	private Long einkaufspreisSl;
	private Long einkaufspreisBern;
	private Long abgaben;
	private boolean aktiv;

	@ManyToOne(targetEntity = Kategorie.class)
	private Kategorie kategorie;

	public boolean isAktiv() {
		return aktiv;
	}

	public void setAktiv(boolean aktiv) {
		this.aktiv = aktiv;
	}

	protected Produkt() {
		aktiv = true;
	}

	public Produkt(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getVerkaufspreis() {
		return verkaufspreis;
	}

	public void setVerkaufspreis(Long verkaufspreis) {
		this.verkaufspreis = verkaufspreis;
	}

	public Long getEinkaufspreisSl() {
		return einkaufspreisSl;
	}

	public void setEinkaufspreisSl(Long einkaufspreisSl) {
		this.einkaufspreisSl = einkaufspreisSl;
	}

	public Long getEinkaufspreisBern() {
		return einkaufspreisBern;
	}

	public void setEinkaufspreisBern(Long einkaufspreisBern) {
		this.einkaufspreisBern = einkaufspreisBern;
	}

	public Long getAbgaben() {
		return abgaben;
	}

	public void setAbgaben(Long abgaben) {
		this.abgaben = abgaben;
	}

	public Kategorie getKategorie() {
		return kategorie;
	}

	public void setKategorie(Kategorie kategorie) {
		this.kategorie = kategorie;
	}

	@Override
	public String toString() {
		return String.format("Kategorie[id=%d, name='%s', verkaufspreis='%d', einkaufspreisSl='%d', einkaufspreisBern='%d', abgaben='%d']", id, name, verkaufspreis, einkaufspreisSl, einkaufspreisBern, abgaben);
	}
}
