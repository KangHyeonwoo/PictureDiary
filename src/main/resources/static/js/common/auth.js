class Auth {
    #jwt;

    constructor(jwt) {
        this.#jwt = jwt;
    }

    get() {
        return this.#jwt;
    }
}