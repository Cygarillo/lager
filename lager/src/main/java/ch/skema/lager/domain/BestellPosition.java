package ch.skema.lager.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class BestellPosition {
	@Id
	@GeneratedValue
	@Column(name = "bestell_pos_id")
	private Long id;
	private BigDecimal rabatt;
	private BigDecimal stueckpreis;
	private Long anzahl;

	@ManyToOne
	@JoinColumn(name = "produkt_id")
	private Produkt produkt;

	@ManyToOne
	@JoinColumn(name = "bestellung_id")
	private Bestellung bestellung;

	public Produkt getProdukt() {
		return produkt;
	}

	public void setProdukt(Produkt produkt) {
		this.produkt = produkt;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return String.format("BestellPosition[id=%d]", id);
	}

	public BigDecimal getRabatt() {
		return rabatt;
	}

	public void setRabatt(BigDecimal rabatt) {
		this.rabatt = rabatt;
	}

	public BigDecimal getStueckpreis() {
		return stueckpreis;
	}

	public void setStueckpreis(BigDecimal stueckpreis) {
		this.stueckpreis = stueckpreis;
	}

	public Long getAnzahl() {
		return anzahl;
	}

	public void setAnzahl(Long anzahl) {
		this.anzahl = anzahl;
	}

	public void setBestellung(Bestellung bestellung) {
		this.bestellung = bestellung;
	}

	public Bestellung getBestellung() {
		return bestellung;
	}

}
