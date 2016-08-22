package ch.skema.lager.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Kategorie {
	@Id
	@GeneratedValue
	@Column(name = "kategorie_id")
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

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Kategorie) {
			return ((Kategorie) obj).getId().equals(id);
		}
		return super.equals(obj);
	}
}
