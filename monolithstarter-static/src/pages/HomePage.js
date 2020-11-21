import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import {getRecords} from "../actions/homeActions";
import {getDuplicates} from "../actions/homeActions";

const style  =
  `
    table,th,td
    {
    border: 1px solid black
    }
    table
    {
    border-collapse: collapse
    }
  `;

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

    getDuplicates().then(duplicates => {
      this.setState({duplicates});
      });
  }

  render() {

    const{records, duplicates} = this.state;
    console.log(records);
    console.log(duplicates);

    return (
      <div className="App">
        <style>{style}</style>
        <h1>Duplicate Records</h1>
        <table className={style}>
          <tr style={{fontWeight:"bold"}}>
            <td>Name</td>
            <td>Company</td>
            <td>Email</td>
            <td>Address</td>
            <td>Zip</td>
            <td>City</td>
            <td>State</td>
            <td>Phone</td>
          </tr>
          {duplicates.map(duplicate => (
            <tr key={duplicate.id}>
              <td>{duplicate.firstName} {duplicate.lastName}</td>
              <td>{duplicate.company}</td>
              <td>{duplicate.email}</td>
              <td>{duplicate.address1}, {duplicate.address2}</td>
              <td>{duplicate.zip}</td>
              <td>{duplicate.city}</td>
              <td>State: {duplicate.state}</td>
              <td>{duplicate.phone}</td>
            </tr>
          ))}
        </table>

        <h1>Record List with Duplicates Removed</h1>
        <table style={
          {}
        }>
          <tr style={{fontWeight:"bold"}}>
            <td>Name</td>
            <td>Company</td>
            <td>Email</td>
            <td>Address</td>
            <td>Zip</td>
            <td>City</td>
            <td>State</td>
            <td>Phone</td>
          </tr>
          {records.map(record => (
            <tr key={record.id}>
              <td>{record.firstName} {record.lastName}</td>
              <td>{record.company}</td>
              <td>{record.email}</td>
              <td>{record.address1}, {record.address2}</td>
              <td>{record.zip}</td>
              <td>{record.city}</td>
              <td>{record.state}</td>
              <td>{record.phone}</td>
            </tr>
          ))}
        </table>


      </div>
      );
   }
  }

export default withRouter(HomePage);
