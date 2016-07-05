package ch.skema.lager.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Bestellung {
	@Id
	@GeneratedValue
	private Long id;
	private Date datum;
	private boolean erledigt;
	@ManyToOne(targetEntity = Kunde.class)
	private Kunde kunde;
	@OneToMany(targetEntity = BestellPosition.class)
	private List<BestellPosition> bestellPosition;

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
}
