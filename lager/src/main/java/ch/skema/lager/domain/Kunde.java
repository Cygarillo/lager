package ch.skema.lager.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Kunde {
	@Id
	@GeneratedValue
	@Column(name = "kunde_id")
	private Long id;
	private String name;

	protected Kunde() {
	}

	public Kunde(String name) {
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
		return String.format("Customer[id=%d, name='%s']", id, name);
	}
}
