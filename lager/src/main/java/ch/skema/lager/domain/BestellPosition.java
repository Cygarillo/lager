package ch.skema.lager.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class BestellPosition {
	@Id
	@GeneratedValue
	private Long id;
	private Long rabatt;
	private Long preis;

	@ManyToOne(targetEntity = Produkt.class)
	private Produkt produkt;

	public Produkt getProdukt() {
		return produkt;
	}

	public void setProdukt(Produkt produkt) {
		this.produkt = produkt;
	}

	protected BestellPosition() {
	}

	public Long getId() {
		return id;
	}

	public Long getPreis() {
		return preis;
	}

	public void setPreis(Long preis) {
		this.preis = preis;
	}

	public Long getRabatt() {
		return rabatt;
	}

	public void setRabatt(Long rabatt) {
		this.rabatt = rabatt;
	}

	@Override
	public String toString() {
		return String.format("BestellPosition[id=%d]", id);
	}
}
