package ca.mcgill.ecse321.eventregistration.dto;

import java.sql.Date;
import java.sql.Time;

public class ConcertDto extends EventDto {
	private String artist;
	private String name;
	private Date date;
	private Time startTime;
	private Time endTime;
	
	public ConcertDto() {
		
	}
	public ConcertDto(String name, Date date, Time startTime, Time endTime, String artist) {
		this.setName(name);
		this.setDate(date);
		this.setStartTime(startTime);
		this.setEndTime(endTime);
		this.setArtist(artist);
	}
	public String getArtist() {
		return this.artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Time getStartTime() {
		return startTime;
	}
	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}
	public Time getEndTime() {
		return endTime;
	}
	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}
}
