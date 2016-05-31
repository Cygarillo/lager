package ch.skema.lager.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Kategorie {
	@Id
	@GeneratedValue
	private Long id;
	private String name;

	protected Kategorie() {
	}

	public Kategorie(String name) {
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

	@Override
	public String toString() {
		return String.format("Kategorie[id=%d, name='%s']", id, name);
	}
}
