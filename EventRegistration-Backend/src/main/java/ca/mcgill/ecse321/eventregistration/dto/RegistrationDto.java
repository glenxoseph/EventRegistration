package ca.mcgill.ecse321.eventregistration.dto;

public class RegistrationDto {

	private int id;
	private PersonDto person;
	private EventDto event;
	private BitcoinDto bitcoin;

	public RegistrationDto() {
	}

	public RegistrationDto(PersonDto person, EventDto event) {
		this.person = person;
		this.event = event;
	}
	
	public RegistrationDto(PersonDto person, EventDto event, BitcoinDto bitcoin) {
		this.person = person;
		this.event = event;
		this.bitcoin = bitcoin;
	}
	
	public BitcoinDto getBitcoin() {
		return this.bitcoin;
	}
	
	public void setApplePay(BitcoinDto bitcoin) {
		this.bitcoin = bitcoin;
	}

	public EventDto getEvent() {
		return event;
	}

	public void setEvent(EventDto event) {
		this.event = event;
	}

	public PersonDto getPerson() {
		return person;
	}

	public void setPerson(PersonDto person) {
		this.person = person;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
