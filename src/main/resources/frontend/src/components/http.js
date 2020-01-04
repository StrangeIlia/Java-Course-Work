import axios from 'axios'

export const HTTP = axios.create({
    baseURL: "http://" + location.hostname + ":50001/api",
    headers: {
        'Content-Type': 'application/json',
    }
});

if(localStorage.token !== undefined)
    HTTP.defaults.headers['Authorization'] = 'Bearer ' + localStorage.token;