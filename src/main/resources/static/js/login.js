import HttpRequest from "./common/HttpRequest.js";

const inputUsername = document.querySelector('#username');
const inputPassword = document.querySelector('#password');
const loginButton = document.querySelector('#loginButton');

loginButton.addEventListener('click', e => {
    const username = inputUsername.value;
    const password = inputPassword.value;

    login(username, password);
})


function login(username, password) {
    const data = {
        loginType : 'SYNOLOGY_NAS',
        username : username,
        password : password
    }
    HttpRequest.post('/login', data).then()
        .then(response => console.log(response));
}