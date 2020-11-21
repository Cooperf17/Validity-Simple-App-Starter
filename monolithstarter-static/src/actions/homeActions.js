import axios from 'axios';

export async function getRecords() {
  return (await axios.get('http://localhost:8080/api/records')).data;
}

export async function getDuplicates() {
  return (await axios.get('http://localhost:8080/api/duplicates')).data;
}
