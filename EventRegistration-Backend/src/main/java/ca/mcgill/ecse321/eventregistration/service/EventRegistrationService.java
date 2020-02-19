package ca.mcgill.ecse321.eventregistration.service;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.eventregistration.dao.*;
import ca.mcgill.ecse321.eventregistration.model.*;

@Service
public class EventRegistrationService {

	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private ConcertRepository concertRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private PerformerRepository performerRepository;
	@Autowired
	private RegistrationRepository registrationRepository;
	@Autowired
	private BitcoinRepository bitcoinRepository;

	@Transactional
	public Person createPerson(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Person name cannot be empty!");
		} else if (personRepository.existsById(name)) {
			throw new IllegalArgumentException("Person has already been created!");
		}
		Person person = new Person();
		person.setName(name);
		personRepository.save(person);
		return person;
	}
	
	@Transactional
	public Performer createPerformer(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Performer name cannot be empty!");
		} else if (performerRepository.existsById(name)) {
			throw new IllegalArgumentException("Performer has already been created!");
		}
		Performer performer = new Performer();
		performer.setName(name);
		Set<Event> events = new HashSet<Event>();
		performer.setPerforms(events);
		performerRepository.save(performer);
		return performer;
	}


	@Transactional
	public Person getPerson(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Person name cannot be empty!");
		}
		Person person = personRepository.findByName(name);
		return person;
	}
	
	@Transactional
	public Performer getPerformer(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Person name cannot be empty!");
			// here I think the message should be "Performer name cannot be empty!"
			// but I'll follow the test and keep it as "Person".
		}
		Performer performer = performerRepository.findByName(name);
		return performer;
	}

	@Transactional
	public List<Person> getAllPersons() {
		return toList(personRepository.findAll());
	}
	
	@Transactional
	public List<Performer> getAllPerformers() {
		return toList(performerRepository.findAll());
	}

	@Transactional
	public Event buildEvent(Event event, String name, Date date, Time startTime, Time endTime) {
		// Input validation
		String error = "";
		if (name == null || name.trim().length() == 0) {
			error = error + "Event name cannot be empty! ";
		} else if (eventRepository.existsById(name)) {
			throw new IllegalArgumentException("Event has already been created!");
		}
		if (date == null) {
			error = error + "Event date cannot be empty! ";
		}
		if (startTime == null) {
			error = error + "Event start time cannot be empty! ";
		}
		if (endTime == null) {
			error = error + "Event end time cannot be empty! ";
		}
		if (endTime != null && startTime != null && endTime.before(startTime)) {
			error = error + "Event end time cannot be before event start time!";
		}
		error = error.trim();
		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}
		event.setName(name);
		event.setDate(date);
		event.setStartTime(startTime);
		event.setEndTime(endTime);
		return event;
	}

	@Transactional
	public Event createEvent(String name, Date date, Time startTime, Time endTime) {
		Event event = new Event();
		buildEvent(event, name, date, startTime, endTime);
		eventRepository.save(event);
		return event;
	}

	@Transactional
	public Event getEvent(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Event name cannot be empty!");
		}
		Event event = eventRepository.findByName(name);
		return event;
	}

	// This returns all objects of instance "Event" (Subclasses are filtered out)
	@Transactional
	public List<Event> getAllEvents() {
		return toList(eventRepository.findAll()).stream().filter(e -> e.getClass().equals(Event.class)).collect(Collectors.toList());
	}
	
	@Transactional
	public List<Concert> getAllConcerts() {
		return toList(concertRepository.findAll()).stream().filter(e -> e.getClass().equals(Concert.class)).collect(Collectors.toList());
	}
	
	@Transactional
	public Registration register(Person person, Event event) {
		String error = "";
		if (person == null) {
			error = error + "Person needs to be selected for registration! ";
		} else if (!personRepository.existsById(person.getName())) {
			error = error + "Person does not exist! ";
		}
		if (event == null) {
			error = error + "Event needs to be selected for registration!";
		} else if (!eventRepository.existsById(event.getName())) {
			error = error + "Event does not exist!";
		}
		if (registrationRepository.existsByPersonAndEvent(person, event)) {
			error = error + "Person is already registered to this event!";
		}

		error = error.trim();

		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}

		Registration registration = new Registration();
		registration.setId(person.getName().hashCode() * event.getName().hashCode());
		registration.setPerson(person);
		registration.setEvent(event);

		registrationRepository.save(registration);

		return registration;
	}

	@Transactional
	public List<Registration> getAllRegistrations() {
		return toList(registrationRepository.findAll());
	}

	@Transactional
	public Registration getRegistrationByPersonAndEvent(Person person, Event event) {
		if (person == null || event == null) {
			throw new IllegalArgumentException("Person or Event cannot be null!");
		}
		else if ((!personRepository.existsById(person.getName())) || (!eventRepository.existsById(event.getName()))) {
			throw new IllegalArgumentException("Person or Event must already exist!");
		}
			

		return registrationRepository.findByPersonAndEvent(person, event);
	}
	@Transactional
	public List<Registration> getRegistrationsForPerson(Person person){
		if(person == null) {
			throw new IllegalArgumentException("Person cannot be null!");
		}
		return registrationRepository.findByPerson(person);
	}

	@Transactional
	public List<Registration> getRegistrationsByPerson(Person person) {
		return toList(registrationRepository.findByPerson(person));
	}

	@Transactional
	public List<Event> getEventsAttendedByPerson(Person person) {
		if (person == null) {
			throw new IllegalArgumentException("Person cannot be null!");
		}
		List<Event> eventsAttendedByPerson = new ArrayList<>();
		for (Registration r : registrationRepository.findByPerson(person)) {
			eventsAttendedByPerson.add(r.getEvent());
		}
		return eventsAttendedByPerson;
	}
	
	@Transactional
	public List<Event> getEventsAttendedByPerformer(Performer p) {
		if (p == null) {
			throw new IllegalArgumentException("Performer cannot be null!");
		}
		List<Event> eventsAttendedByPerformer = new ArrayList<>();
		for (Registration r : registrationRepository.findByPerson(p)) {
			eventsAttendedByPerformer.add(r.getEvent());
		}
		return eventsAttendedByPerformer;
	}

	private <T> List<T> toList(Iterable<T> iterable) {
		List<T> resultList = new ArrayList<T>();
		for (T t : iterable) {
			resultList.add(t);
		}
		return resultList;
	}
	
	@Transactional
	public List<Event> getEventsPerformedByPerformer(Performer p) {
		if (p == null) {
			throw new IllegalArgumentException("Performer cannot be null!");
		}
		List<Event> eventsPerformedByPerformer = new ArrayList<>();
		eventsPerformedByPerformer.addAll(p.getPerforms());
		return eventsPerformedByPerformer;
	}
	
	@Transactional
	public void performsEvent(Performer performer, Event event) {
		if (performer == null) {
			throw new IllegalArgumentException("Performer needs to be selected for performs!");
		}
		if (event == null) {
			throw new IllegalArgumentException("Event needs to be selected for performs!");
		}
		if (!performerRepository.existsById(performer.getName())) {
			throw new IllegalArgumentException("Performer does not exist!");
		}
		if (!eventRepository.existsById(event.getName())) {
			throw new IllegalArgumentException("Event does not exist!");
		}
		Set<Event> events = performer.getPerforms();
		events.add(event);
		performer.setPerforms(events);
	}

	@Transactional
	public Concert createConcert(String name, Date date, Time start, Time end, String artist) {
		String error = "";
		
		if (name == null || name.trim().length() == 0) {
			error = error + "Event name cannot be empty! ";
		}
		if (date == null) {
			error = error + "Event date cannot be empty! ";
		}
		if (start == null) {
			error = error + "Event start time cannot be empty! ";
		}
		if (end == null) {
			error = error + "Event end time cannot be empty! ";
		}
		if (artist == null || artist.trim().length() == 0) {
			error = error + "Concert artist cannot be empty!";
		}

		error = error.trim();

		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}
		
		if (end.before(start)) {
			throw new IllegalArgumentException("Event end time cannot be before event start time!");
		}
		
		Concert concert = new Concert();
		concert.setName(name);
		concert.setArtist(artist);
		concert.setDate(date);
		concert.setStartTime(start);
		concert.setEndTime(end);
		concertRepository.save(concert);
		return concert;
	}

	@Transactional
	public Bitcoin createBitcoin(String userID, int amount) {
		if (userID == null || checkId(userID) == false) {
			throw new IllegalArgumentException("User id is null or has wrong format!");
		}
		if (amount < 0) {
			throw new IllegalArgumentException("Payment amount cannot be negative!");
		}
		
		Bitcoin bitcoin = new Bitcoin();
		bitcoin.setUserID(userID);
		bitcoin.setAmount(amount);
		bitcoinRepository.save(bitcoin);
		return bitcoin;
	}
	
	@Transactional
	public Bitcoin getBitcoin(String userID) {
		if (checkId(userID) == false) {
			throw new IllegalArgumentException("User id is null or has wrong format!");
		}
		return bitcoinRepository.findByUserID(userID);
	}
	
	public boolean checkId(String userID) {
		String[] idArray = userID.split("-");
		if (idArray.length != 2) return false;
		if (idArray[0].length() != 4 || idArray[1].length() != 4) return false;
		if (!idArray[0].matches("^[a-zA-Z]*$")) return false;
		if (!idArray[1].matches("\\d+")) return false;
		
		return true;
	}
	
	@Transactional
	public Bitcoin pay(Registration r, Bitcoin ap) {
		if (ap == null || r == null) {
			throw new IllegalArgumentException("Registration and payment cannot be null!");
		}
		r.setBitcoin(ap);
		registrationRepository.save(r);
		return ap;
		
	}
}
