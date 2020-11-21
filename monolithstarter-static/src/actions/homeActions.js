import axios from 'axios';

export async function getRecords() {
  return (await axios.get('/api/records')).data;
}

export async function getDuplicates() {
  return (await axios.get('/api/duplicates')).data;
}
