package ca.mcgill.ecse321.eventregistration.controller;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ca.mcgill.ecse321.eventregistration.model.*;
import ca.mcgill.ecse321.eventregistration.dto.*;
import ca.mcgill.ecse321.eventregistration.service.EventRegistrationService;

@CrossOrigin(origins = "*")
@RestController
public class EventRegistrationRestController {

	@Autowired
	private EventRegistrationService service;

	// POST Mappings

	// @formatter:off
	// Turning off formatter here to ease comprehension of the sample code by
	// keeping the linebreaks
	// Example REST call:
	// http://localhost:8088/persons/John
	@PostMapping(value = { "/persons/{name}", "/persons/{name}/" })
	public PersonDto createPerson(@PathVariable("name") String name) throws IllegalArgumentException {
		// @formatter:on
		Person person = service.createPerson(name);
		return convertToDto(person);
	}

	// http://localhost:8088/performers/John
	@PostMapping(value = { "/performers/{name}", "/performers/{name}/" })
	public PerformerDto createPerformer(@PathVariable("name") String name) throws IllegalArgumentException {
		// @formatter:on
		Performer performer = service.createPerformer(name);
		return convertToDto(performer);
	}

	// @formatter:off
	// Example REST call:
	// http://localhost:8080/events/testevent?date=2013-10-23&startTime=00:00&endTime=23:59
	@PostMapping(value = { "/events/{name}", "/events/{name}/" })
	public EventDto createEvent(@PathVariable("name") String name, @RequestParam Date date,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime startTime,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime endTime)
					throws IllegalArgumentException {
		// @formatter:on
		Event event = service.createEvent(name, date, Time.valueOf(startTime), Time.valueOf(endTime));
		return convertToDto(event);
	}

	@PostMapping(value = { "/bitcoin", "/bitcoin/" })
	public RegistrationDto pay(@RequestParam("userID") String userID, @RequestParam("amount") int amount, @RequestParam("pName") String pName, @RequestParam("eName") String eName)
			throws IllegalArgumentException {
		Person p = service.getPerson(pName);
		Event e = service.getEvent(eName);
		Registration r = service.getRegistrationByPersonAndEvent(p, e);
		Bitcoin bitcoin = service.createBitcoin(userID, amount);
		service.pay(r, bitcoin);
		return convertToDto(r, p, e, bitcoin);
	}

	// @formatter:off
	// Example REST call:
	// http://localhost:8080/events/testevent?date=2013-10-23&startTime=00:00&endTime=23:59
	@PostMapping(value = { "/concert/{name}", "/concert/{name}/" })
	public ConcertDto createConcert(@PathVariable("name") String name, @RequestParam Date date,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime startTime,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime endTime, @RequestParam String artist)
					throws IllegalArgumentException {
		// @formatter:on
		Concert concert = service.createConcert(name, date, Time.valueOf(startTime), Time.valueOf(endTime), artist);
		return convertToDto(concert);
	}

	// @formatter:off
	@PostMapping(value = { "/register", "/register/" })
	public RegistrationDto registerPersonForEvent(@RequestParam(name = "person") PersonDto pDto,
			@RequestParam(name = "event") EventDto eDto) throws IllegalArgumentException {
		// @formatter:on

		// Both the person and the event are identified by their names
		Person p = service.getPerson(pDto.getName());
		Event e = service.getEvent(eDto.getName());

		Registration r = service.register(p, e);
		return convertToDto(r, p, e);
	}
	
	// @formatter:off
	@PostMapping(value = { "/perform", "/perform/" })
	public boolean performerPerformsEvent(@RequestParam(name = "performer") PerformerDto pDto,
			@RequestParam(name = "event") EventDto eDto) throws IllegalArgumentException {
		// @formatter:on

		// Both the person and the event are identified by their names
		Performer p = service.getPerformer(pDto.getName());
		Event e = service.getEvent(eDto.getName());

		service.performsEvent(p, e);
		return true;
	}

	// GET Mappings

	@GetMapping(value = { "/events", "/events/" })
	public List<EventDto> getAllEvents() {
		List<EventDto> eventDtos = new ArrayList<>();
		for (Event event : service.getAllEvents()) {
			eventDtos.add(convertToDto(event));
		}
		return eventDtos;
	}
	
	@GetMapping(value = { "/concerts", "/concerts/" })
	public List<ConcertDto> getAllConcerts() {
		List<ConcertDto> concertDtos = new ArrayList<>();
		for (Concert concert : service.getAllConcerts()) {
			concertDtos.add(convertToDto(concert));
		}
		return concertDtos;
	}

	// Example REST call:
	// http://localhost:8088/events/person/JohnDoe
	@GetMapping(value = { "/events/person/{name}", "/events/person/{name}/" })
	public List<EventDto> getEventsOfPerson(@PathVariable("name") PersonDto pDto) {
		Person p = convertToDomainObject(pDto);
		return createAttendedEventDtosForPerson(p);
	}

	@GetMapping(value = { "/persons/{name}", "/persons/{name}/" })
	public PersonDto getPersonByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getPerson(name));
	}
	
	@GetMapping(value = { "/events/performer/{name}", "/events/performer/{name}/" })
	public List<EventDto> getEventsOfPerformer(@PathVariable("name") PerformerDto pDto) {
		Performer p = convertToDomainObject(pDto);
		return createAttendedEventDtosForPerformer(p);
	}

	@GetMapping(value = { "/registrations", "/registrations/" })
	public RegistrationDto getRegistration(@RequestParam(name = "person") PersonDto pDto,
			@RequestParam(name = "event") EventDto eDto) throws IllegalArgumentException {
		// Both the person and the event are identified by their names
		Person p = service.getPerson(pDto.getName());
		Event e = service.getEvent(eDto.getName());

		Registration r = service.getRegistrationByPersonAndEvent(p, e);
		return convertToDtoWithoutPerson(r);
	}

	@GetMapping(value = { "/registrations/person/{name}", "/registrations/person/{name}/" })
	public List<RegistrationDto> getRegistrationsForPerson(@PathVariable("name") PersonDto pDto)
			throws IllegalArgumentException {
		// Both the person and the event are identified by their names
		Person p = service.getPerson(pDto.getName());

		return createRegistrationDtosForPerson(p);
	}

	@GetMapping(value = { "/persons", "/persons/" })
	public List<PersonDto> getAllPersons() {
		List<PersonDto> persons = new ArrayList<>();
		for (Person person : service.getAllPersons()) {
			persons.add(convertToDto(person));
		}
		return persons;
	}
	
	@GetMapping(value = { "/performers", "/performers/" })
	public List<PerformerDto> getAllPerformers() {
		List<PerformerDto> performers = new ArrayList<>();
		for (Performer performer : service.getAllPerformers()) {
			performers.add(convertToDto(performer));
		}
		return performers;
	}

	@GetMapping(value = { "/events/{name}", "/events/{name}/" })
	public EventDto getEventByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getEvent(name));
	}

	// Model - DTO conversion methods (not part of the API)

	private EventDto convertToDto(Event e) {
		if (e == null) {
			throw new IllegalArgumentException("There is no such Event!");
		}
		EventDto eventDto = new EventDto(e.getName(), e.getDate(), e.getStartTime(), e.getEndTime());
		return eventDto;
	}
	
	private ConcertDto convertToDto(Concert c) {
		if (c == null) {
			throw new IllegalArgumentException("There is no such Concert!");
		}
		ConcertDto concert = new ConcertDto(c.getName(), c.getDate(), c.getStartTime(), c.getEndTime(), c.getArtist());
		return concert;
	}

	private BitcoinDto convertToDto(Bitcoin b) {
		if (b == null) {
			throw new IllegalArgumentException("There is no such Bitcoin!");
		}
		BitcoinDto bitcoin = new BitcoinDto(b.getUserID(), b.getAmount());
		return bitcoin;
	}

	private PersonDto convertToDto(Person p) {
		if (p == null) {
			throw new IllegalArgumentException("There is no such Person!");
		}
		PersonDto personDto = new PersonDto(p.getName());
		personDto.setEventsAttended(createAttendedEventDtosForPerson(p));
		return personDto;
	}

	private PerformerDto convertToDto(Performer p) {
		if (p == null) {
			throw new IllegalArgumentException("There is no such Performer!");
		}
		PerformerDto performerDto = new PerformerDto(p.getName());
		performerDto.setEventsAttended(createAttendedEventDtosForPerformer(p));
		performerDto.setEvents(createPerformedEventDtosForPerformer(p));

		return performerDto;
	}

	// DTOs for registrations
	private RegistrationDto convertToDto(Registration r, Person p, Event e, Bitcoin b) {
		System.out.println("RegistrationDto");
		EventDto eDto = convertToDto(e);
		PersonDto pDto = convertToDto(p);
		BitcoinDto bDto = convertToDto(b);
		return new RegistrationDto(pDto, eDto, bDto);
	}

	private RegistrationDto convertToDto(Registration r, Person p, Event e) {
		EventDto eDto = convertToDto(e);
		PersonDto pDto = convertToDto(p);
		return new RegistrationDto(pDto, eDto);
	}

	private RegistrationDto convertToDto(Registration r) {
		EventDto eDto = convertToDto(r.getEvent());
		PersonDto pDto = convertToDto(r.getPerson());
		RegistrationDto rDto = new RegistrationDto(pDto, eDto);
		return rDto;
	}

	// return registration dto without peron object so that we are not repeating
	// data
	private RegistrationDto convertToDtoWithoutPerson(Registration r) {
		RegistrationDto rDto = convertToDto(r);
		rDto.setPerson(null);
		return rDto;
	}

	private Person convertToDomainObject(PersonDto pDto) {
		List<Person> allPersons = service.getAllPersons();
		for (Person person : allPersons) {
			if (person.getName().equals(pDto.getName())) {
				return person;
			}
		}
		return null;
	}
	
	private Performer convertToDomainObject(PerformerDto pDto) {
		List<Performer> allPerformers = service.getAllPerformers();
		for (Performer performer : allPerformers) {
			if (performer.getName().equals(pDto.getName())) {
				return performer;
			}
		}
		return null;
	}
	
	private Bitcoin convertToDomainObject(BitcoinDto bDto) {
		if (service.getBitcoin(bDto.getUserID()) != null) {
			return service.getBitcoin(bDto.getUserID());
		}
		return null;
	}
	
	private Registration convertToDomainObject(RegistrationDto rDto) {
		List<Registration> allReg = service.getAllRegistrations();
		for (Registration reg : allReg) {
			if (reg.getId() == rDto.getId()) {
				return reg;
			}
		}
		return null;
	}

	// Other extracted methods (not part of the API)

	private List<EventDto> createAttendedEventDtosForPerson(Person p) {
		List<Event> eventsForPerson = service.getEventsAttendedByPerson(p);
		List<EventDto> events = new ArrayList<>();
		for (Event event : eventsForPerson) {
			events.add(convertToDto(event));
		}
		return events;
	}

	private List<RegistrationDto> createRegistrationDtosForPerson(Person p) {
		List<Registration> registrationsForPerson = service.getRegistrationsForPerson(p);
		List<RegistrationDto> registrations = new ArrayList<RegistrationDto>();
		for (Registration r : registrationsForPerson) {
			registrations.add(convertToDtoWithoutPerson(r));
		}
		return registrations;
	}
	
	private List<EventDto> createAttendedEventDtosForPerformer(Performer p) {
		List<Event> eventsForPerson = service.getEventsAttendedByPerformer(p);
		List<EventDto> events = new ArrayList<>();
		for (Event event : eventsForPerson) {
			events.add(convertToDto(event));
		}
		return events;
	}

	private List<EventDto> createPerformedEventDtosForPerformer(Performer p) {
		List<Event> eventsPerformed = service.getEventsPerformedByPerformer(p);
		List<EventDto> events = new ArrayList<>();
		for (Event event : eventsPerformed) {
			events.add(convertToDto(event));
		}
		return events;
	}
	
}
