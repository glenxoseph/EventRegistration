package ca.mcgill.ecse321.eventregistration.model;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import java.sql.Date;
import java.sql.Time;

@Entity
public class Concert extends Event {
	private String artist;

	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public String getArtist() {
		return this.artist;
	}

}
