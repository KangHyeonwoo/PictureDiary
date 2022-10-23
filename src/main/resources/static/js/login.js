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
    /*
    HttpRequest.post('/login', data).then()
        .then(response => {
            debugger;
        });
    */

    const options = {
        method : 'POST',
        headers : {
            'Content-Type' : 'application/json'
        },
        body : JSON.stringify(data)
    };

    fetch('/login', options)
        .then(response => {
            if(response.ok) {
                return response.headers;
            }

            console.log(response.json())
        })
        .then(headers => {
            const auth = headers.get('auth');

            moveToMainPage(auth);
        })
}

function moveToMainPage(auth) {
    console.log(auth);
}