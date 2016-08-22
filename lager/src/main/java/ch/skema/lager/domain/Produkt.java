package ch.skema.lager.domain;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Produkt {
	@Id
	@GeneratedValue
	@Column(name = "produkt_id")
	private Long id;
	private String name;
	private BigDecimal verkaufspreis;
	private BigDecimal einkaufspreisSl;
	private BigDecimal einkaufspreisBern;
	private BigDecimal abgaben;
	private boolean aktiv = true;

	@ManyToOne
	@JoinColumn(name = "kategorie_id")
	private Kategorie kategorie;

	@OneToMany(mappedBy = "produkt", fetch = FetchType.EAGER)
	private List<BestellPosition> bestellPosition;

	public boolean isAktiv() {
		return aktiv;
	}

	public void setAktiv(boolean aktiv) {
		this.aktiv = aktiv;
	}

	protected Produkt() {
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

	public BigDecimal getVerkaufspreis() {
		return verkaufspreis;
	}

	public void setVerkaufspreis(BigDecimal verkaufspreis) {
		this.verkaufspreis = verkaufspreis;
	}

	public BigDecimal getEinkaufspreisSl() {
		return einkaufspreisSl;
	}

	public void setEinkaufspreisSl(BigDecimal einkaufspreisSl) {
		this.einkaufspreisSl = einkaufspreisSl;
	}

	public BigDecimal getEinkaufspreisBern() {
		return einkaufspreisBern;
	}

	public void setEinkaufspreisBern(BigDecimal einkaufspreisBern) {
		this.einkaufspreisBern = einkaufspreisBern;
	}

	public BigDecimal getAbgaben() {
		return abgaben;
	}

	public void setAbgaben(BigDecimal abgaben) {
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
		return String.format("Kategorie[id=%d, name='%s']", id, name);
	}
}
