package ca.mcgill.ecse321.eventregistration.model;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Performer extends Person{
	private Set<Event> events;
	
	@OneToMany(cascade = { CascadeType.ALL })
	public Set<Event> getPerforms() {
		return this.events;
	}

	public void setPerforms(Set<Event> eventss) {
		this.events = eventss;
	}
}
