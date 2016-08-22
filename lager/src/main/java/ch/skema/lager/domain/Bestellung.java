package ch.skema.lager.domain;

import java.util.ArrayList;
import java.util.Date;
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
public class Bestellung {
	@Id
	@GeneratedValue
	@Column(name = "bestellung_id")
	private Long id;
	private Date datum;
	private boolean erledigt;

	@ManyToOne
	@JoinColumn(name = "kunde_id")
	private Kunde kunde;

	@OneToMany(mappedBy = "bestellung", fetch = FetchType.EAGER)
	private List<BestellPosition> bestellPosition = new ArrayList<>();

	protected Bestellung() {
	}

	public Bestellung(Date datum) {
		this.datum = datum;
	}

	public Long getId() {
		return id;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public boolean isErledigt() {
		return erledigt;
	}

	public void setErledigt(boolean erledigt) {
		this.erledigt = erledigt;
	}

	public Kunde getKunde() {
		return kunde;
	}

	public void setKunde(Kunde kunde) {
		this.kunde = kunde;
	}

	@Override
	public String toString() {
		return String.format("Bestellung[id=%d]", id);
	}

	public List<BestellPosition> getBestellPosition() {
		return bestellPosition;
	}

	public void setBestellPosition(List<BestellPosition> bestellPosition) {
		this.bestellPosition = bestellPosition;
	}
}
