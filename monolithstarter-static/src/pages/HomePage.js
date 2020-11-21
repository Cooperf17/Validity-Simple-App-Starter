import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import {getRecords} from "../actions/homeActions";
import {getDuplicates} from "../actions/homeActions";

class HomePage extends Component {
  constructor(props){
    super(props);
    this.state = {
      records : [],
      duplicates : []
    };
  }

  componentDidMount(){
    getRecords().then(records => {
      this.setState({records});
    });

    getDuplicates()
      .then(results => results.json())
      .then(json => {
        this.setState({
          duplicates: json
        });
      });
  }

  render() {
    const{records, duplicates} = this.state;
    return (
      <div className="App">

        <h1>Duplicate Records</h1>
        <ul>
          {duplicates.map(duplicate => (
            <li key={duplicate.id}>
              Name: {duplicate.firstName} {duplicate.lastName}
              Company: {duplicate.company}, Email: {duplicate.email}
              Address: {duplicate.address1}, {duplicate.address2}, Zip: {duplicate.zip}
              City: {duplicate.city}, State: {duplicate.state}
              Phone: {duplicate.phone}
            </li>
          ))}
        </ul>

        <h1>Record List with Duplicates Removed</h1>
        <ul>
          {records.map(record => (
              <li key={record.id}>
                Name: {record.firstName} {record.lastName}
              </li>
            ))}
        </ul>


      </div>
      );
   }
  }


export default withRouter(HomePage);
