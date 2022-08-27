import HttpRequest from "./common/HttpRequest.js";

const inputId = document.querySelector('#id');
const inputPassword = document.querySelector('#password');
const loginButton = document.querySelector('#loginButton');

loginButton.addEventListener('click', e => {
    const id = inputId.value;
    const password = inputPassword.value;

    login(id, password);
})


function login(id, password) {
    const data = {
        loginType : 'SYNOLOGY_NAS',
        userId : id,
        password : password
    }
    HttpRequest.post('/login', data).then()
        .then(response => console.log(response));
}