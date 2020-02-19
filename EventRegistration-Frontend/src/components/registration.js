import _ from 'lodash';
import axios from 'axios';
let config = require('../../config');

let backendConfigurer = function () {
	switch (process.env.NODE_ENV) {
	case 'testing':
	case 'development':
		return 'http://' + config.dev.backendHost + ':' + config.dev.backendPort;
	case 'production':
		return 'https://' + config.build.backendHost + ':' + config.build.backendPort;
	}
}

let backendUrl = backendConfigurer();

let AXIOS = axios.create({
	baseURL: backendUrl
	// headers: {'Access-Control-Allow-Origin': frontendUrl}
});

export default {
	name: 'eventregistration',
	data() {
		return {
			persons: [],
			performers: [],
			events: [],
			concerts: [],
			registrions: [],
			newPerson: '',
			personType: 'Person',
			newEvent: {
				name: '',
				date: '2017-12-08',
				startTime: '09:00',
				endTime: '11:00',
				artist: ''
			},
			newPayment: {
				eName: '',
				pName: '',
				userID: '',
				amount: '',
			},
			selectedPerson: '',
			selectedPerformer: '',
			selectedEvent: '',
			errorPerson: '',
			errorEvent: '',
			errorRegistration: '',
			errorPayment: '',
			errorPerform: '',
			response: [],
		}
	},
	created: function () {
		// Initializing persons
		AXIOS.get('/persons')
		.then(response => {
			this.persons = response.data;
			this.persons.forEach(person => this.getPersonRegistrations(person.name))
		})
		.catch(e => {this.errorPerson = e});

		//initializing performers
		AXIOS.get('/performers')
		.then(response => {
			this.performers = response.data;
			this.performers.forEach(performer => this.getPerformerRegistrations(performer.name))
		})
		.catch(e => {this.errorPerson = e});

		AXIOS.get('/events').then(response => {this.events = response.data}).catch(e => {this.errorEvent = e});
		AXIOS.get('/concerts').then(response => {this.concerts = response.data}).catch(e => {this.errorEvent = e});

	},

	methods: {

		createPerson: function (personType, personName) {
			if (personType == 'person') {
				AXIOS.post('/persons/'.concat(personName), {}, {})
				.then(response => {
					this.persons.push(response.data);
					this.errorPerson = '';
					this.newPerson = '';
				})
				.catch(e => {
					e = e.response.data.message ? e.response.data.message : e;
					this.errorPerson = e;
					console.log(e);
				});
			}
			else {
				AXIOS.post('/performers/'.concat(personName), {}, {})
				.then(response => {
					this.persons.push(response.data);
					this.performers.push(response.data);
					this.errorPerson = '';
					this.newPerson = '';
				})
				.catch(e => {
					e = e.response.data.message ? e.response.data.message : e;
					this.errorPerson = e;
					console.log(e);
				});
			}
		},

		createEvent: function (newEvent) {

			let url = '';
			if (newEvent.artist == '') {
				AXIOS.post('/events/'.concat(newEvent.name), {}, {params: newEvent})
				.then(response => {
					this.events.push(response.data);
					this.errorEvent = '';
					this.newEvent.name = this.newEvent.make = this.newEvent.movie = this.newEvent.company = this.newEvent.artist = this.newEvent.title = '';
				})
				.catch(e => {
					e = e.response.data.message ? e.response.data.message : e;
					this.errorEvent = e;
					console.log(e);
				});
			}
			else {
				AXIOS.post('/concert/'.concat(newEvent.name), {}, {params: newEvent})
				.then(response => {
					this.events.push(response.data);
					this.errorEvent = '';
					this.newEvent.name = this.newEvent.make = this.newEvent.movie = this.newEvent.company = this.newEvent.artist = this.newEvent.title = '';
				})
				.catch(e => {
					e = e.response.data.message ? e.response.data.message : e;
					this.errorEvent = e;
					console.log(e);
				});
			}
		},


		pay: function (personName, eventName, userID, amount) {
			let params = {
					userID: this.newPayment.userID,
					amount: this.newPayment.amount,
					eName: this.newPayment.eName,
					pName: this.newPayment.pName
			};
			AXIOS.post('/bitcoin', {}, {params: params})
			.then(response => {
				this.registrions.push(response.data);
				this.newPayment.userID = '';
				this.newPayment.amount = '';
				this.newPayment.eName = '';
				this.newPayment.pName = '';
			})
			.catch(e => {
				e = e.response.data.message ? e.response.data.message : e;
				this.errorPayment = e;
				console.log(e);
			});

		},

		registerEvent: function (personName, eventName) {
			let event = this.events.find(x => x.name === eventName);
			if (event == null) {
				event = this.concerts.find(x => x.name === eventName);
			}
			let person = this.persons.find(x => x.name === personName);
			let params = {
					person: person.name,
					event: event.name
			};

			AXIOS.post('/register', {}, {params: params})
			.then(response => {
				person.eventsAttended.push(event)
				this.selectedPerson = '';
				this.selectedEvent = '';
				this.errorRegistration = '';
			})
			.catch(e => {
				e = e.response.data.message ? e.response.data.message : e;
				this.errorRegistration = e;
				console.log(e);
			});
		},

		performEvent: function (performerName, eventName) {
			let event = this.events.find(x => x.name === eventName);
			let performer = this.performers.find(x => x.name === performerName);
			let params = {
					performer: performer.name,
					event: event.name
			};
			AXIOS.post('/perform', {}, {params: params})
			.then(response => {
				performer.eventsPerformed.push(event)
				this.selectedPerson = '';
				this.selectedEvent = '';
				this.errorPerform = '';
			})
			.catch(e => {
				e = e.response.data.message ? e.response.data.message : e;
				this.errorPerformed = e;
				console.log(e);
			});
		},

		getPersonRegistrations: function (personName) {
			AXIOS.get('/events/person/'.concat(personName))
			.then(response => {
				if (!response.data || response.data.length <= 0) return;

				let indexPart = this.persons.map(x => x.name).indexOf(personName);
				this.persons[indexPart].eventsAttended = [];
				response.data.forEach(event => {
					this.persons[indexPart].eventsAttended.push(event);
				});
			})
			.catch(e => {
				e = e.response.data.message ? e.response.data.message : e;
				console.log(e);
			});
		},

		getPerformerRegistrations: function (personName) {
			AXIOS.get('/events/performer/'.concat(personName))
			.then(response => {
				if (!response.data || response.data.length <= 0) return;

				let indexPart = this.performers.map(x => x.name).indexOf(personName);
				this.performers[indexPart].eventsAttended = [];
				response.data.forEach(event => {
					this.performers[indexPart].eventsAttended.push(event);
				});
			})
			.catch(e => {
				e = e.response.data.message ? e.response.data.message : e;
				console.log(e);
			});
		},
	}
}
