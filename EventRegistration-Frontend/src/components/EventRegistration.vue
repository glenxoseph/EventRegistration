<template>
  <div id="eventregistration">

    <h2>Persons</h2>
    <table id="persons-table">
      <tr>
        <th>Name</th>
        <th>Events</th>
        <th>Payment ID</th>
        <th>Amount ($)</th>
      </tr>
      <tr v-for="(person, i) in persons" v-bind:key="`person-${i}`">
        <td>{{person.name}}</td>
        <td>
          <ul>
            <li v-for="(event, i) in person.eventsAttended" v-bind:key="`event-${i}`" style="list-style-type: disc;">
              <span class='registration-event-name'>{{event.name}}</span>
            </li>
          </ul>
        </td>
        <td>
          <ul>
            <li v-for="(event, i) in person.eventsAttended" v-bind:key="`event-${i}`" style="list-style-type: disc;">
              <span class='registration-event-name'>{{event.name}}</span>
            </li>
          </ul>
        </td>
        <td>
          <ul>
            <li v-for="(event, i) in person.eventsAttended" v-bind:key="`event-${i}`" style="list-style-type: disc;">
              <span class='registration-event-name'>{{event.name}}</span>
            </li>
          </ul>
        </td>
      </tr>
      <tr>
        <td>
          <input id="create-person-person-name" type="text" v-model="newPerson" placeholder="Person Name">
        </td>
        <td>
          <select name="role" v-model="personType">
            <option value="person">Person</option>
            <option value="performer">Performer</option>
          </select>
        </td>
        <td>
          <button id="create-person-button" v-bind:disabled="!newPerson" @click="createPerson(personType, newPerson)">Create Person</button>
        </td>
        <td></td>
      </tr>
    </table>
    <span v-if="errorPerson" style="color:red">Error: {{errorPerson}}</span>

    <hr>
    <h2>Events</h2>
    <table id='events-table'>
      <tr>
        <th>Name</th>
        <th>Date</th>
        <th>Start</th>
        <th>End</th>
        <th>Artist Name</th>
      </tr>
      <tr v-for="(event, i) in events" v-bind:id="event.name" v-bind:key="`event-${i}`">
        <td v-bind:id="`${event.name.replace(/\s/g, '_')}-name`">{{event.name}}</td>
        <td v-bind:id="`${event.name.replace(/\s/g, '_')}-date`">{{event.date}}</td>
        <td v-bind:id="`${event.name.replace(/\s/g, '_')}-starttime`">{{event.startTime}}</td>
        <td v-bind:id="`${event.name.replace(/\s/g, '_')}-endtime`">{{event.endTime}}</td>
        <td v-bind:id="`${event.name.replace(/\s/g, '_')}-artist`">{{event.artist}}</td>
      </tr>
      <tr v-for="(concert, i) in concerts" v-bind:id="concert.name" v-bind:key="`concert-${i}`">
        <td v-bind:id="`${concert.name.replace(/\s/g, '_')}-name`">{{concert.name}}</td>
        <td v-bind:id="`${concert.name.replace(/\s/g, '_')}-date`">{{concert.date}}</td>
        <td v-bind:id="`${concert.name.replace(/\s/g, '_')}-starttime`">{{concert.startTime}}</td>
        <td v-bind:id="`${concert.name.replace(/\s/g, '_')}-endtime`">{{concert.endTime}}</td>
        <td v-bind:id="`${concert.name.replace(/\s/g, '_')}-artist`">{{concert.artist}}</td>
      </tr>
      <tr>
        <td>
          <input id="event-name-input" type="text" v-model="newEvent.name" placeholder="Event Name">
        </td>
        <td>
          <input id="event-date-input" type="date" v-model="newEvent.date" placeholder="YYYY-MM-DD">
        </td>
        <td>
          <input id="event-starttime-input" type="time" v-model="newEvent.startTime" placeholder="HH:mm">
        </td>
        <td>
          <input id="event-endtime-input" type="time" v-model="newEvent.endTime" placeholder="HH:mm">
        </td>
        <td>
          <input id="event-artist-input" type="text" v-model="newEvent.artist" placeholder="Artist Name">
        </td>
        <td>
          <button id="event-create-button" v-bind:disabled="!newEvent.name" v-on:click="createEvent(newEvent)">Create</button>
        </td>
      </tr>
    </table>
    <span id="event-error" v-if="errorEvent" style="color:red">Error: {{errorEvent}}</span>
    <hr>

    <h2>Registrations</h2>
    <label>Person:
      <select id='registration-person-select' v-model="selectedPerson">
        <option disabled value="">Please select one</option>
        <option v-for="(person, i) in persons" v-bind:key="`person-${i}`">{{person.name}}</option>
      </select>
    </label>
    <label>Event:
      <select id='registration-event-select' v-model="selectedEvent">
        <option disabled value="">Please select one</option>
        <option v-for="(event, i) in events" v-bind:key="`event-${i}`">{{event.name}}</option>
        <option v-for="(concert, i) in concerts" v-bind:key="`concert-${i}`">{{concert.name}}</option>
      </select>
    </label>
    <button id='registration-button' v-bind:disabled="!selectedPerson || !selectedEvent" @click="registerEvent(selectedPerson, selectedEvent)">Register</button>
    <br/>
    <span v-if="errorRegistration" style="color:red">Error: {{errorRegistration}}</span>
    <hr>

    <h2>Assign Performer</h2>
    <label>Performer:
      <select id='assign-selected-performer-select' v-model="selectedPerformer">
        <option disabled value="">Please select one</option>
        <option v-for="(performer, i) in performers" v-bind:key="`performer-${i}`">{{performer.name}}</option>
      </select>
    </label>
    <label>Event:
      <select id='assign-selected-event-performer' v-model="selectedEvent">
        <option disabled value="">Please select one</option>
        <option v-for="(event, i) in events" v-bind:key="`event-${i}`">{{event.name}}</option>
      </select>
    </label>
    <button id='assign-button-performer' v-bind:disabled="!selectedPerformer || !selectedEvent" @click="performEvent(selectedPerformer, selectedEvent)">Assign</button>
    <br/>
    <span v-if="errorRegistration" style="color:red">Error: {{errorRegistration}}</span>
    <hr>

    <h2>Pay for Registration with Bitcoin</h2>
    <label>Person:
      <select id='bitcoin-person-select' v-model="newPayment.pName">
        <option disabled value="">Please select one</option>
        <option v-for="(person, i) in persons" v-bind:key="`person-${i}`">{{person.name}}</option>
      </select>
    </label>
    <label>Event:
      <select id='bitcoin-event-select' v-model="newPayment.eName">
        <option disabled value="">Please select one</option>
        <option v-for="(event, i) in events" v-bind:key="`event-${i}`">{{event.name}}</option>
        <option v-for="(concert, i) in concerts" v-bind:key="`concert-${i}`">{{concert.name}}</option>
      </select>
    </label>
    <br/>
    <label>User Id:
      <input id="bitcoin-id-input" v-model="newPayment.userID" type="text">
    </label>
    <label>Amount ($):
      <input id="bitcoin-amount-input" v-model="newPayment.amount" type="number">
    </label>
    <br/>
    <button id='bitcoin-button' v-bind:disabled="!newPayment.pName || !newPayment.eName || !newPayment.userID || !newPayment.amount" @click="pay(newPayment.pName, newPayment.eName, newPayment.userID, newPayment.amount)">Make Payment</button>
    <br/>
    <span id='bitcoin-error' v-if="errorPayment" style="color:red">Error: {{errorPayment}}</span>
  </div>
</template>

<script src="./registration.js"></script>

<style>
  #eventregistration {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    background: #f2ece8;
    margin-top: 60px;
  }
  .registration-event-name {
    display: inline-block;
    width: 25%;
  }
  .registration-event-name {
    display: inline-block;
  }
  h1, h2 {
    font-weight: normal;
  }
  ul {
    list-style-type: none;
    text-align: left;
  }
  a {
    color: #42b983;
  }
</style>
