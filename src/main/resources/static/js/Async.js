function Async() {
    this.xhttp = new XMLHttpRequest();
}

Async.prototype.get = function(url, data, fnCallback) {
    xhttp.open('GET', url, true);
    xhttp.send();
    xhttp.onreadystatechange = function(){
        //TODO SUCCESS, FAIL, ERROR 분기처리 및 비동기 함수 만들어서 공통 js로 빼기
        if(xhttp.readyState == 4 && xhttp.status == 200 && xhttp.response.resultCode == SUCCESS){
            fnCallback(response);
        } else {
            console.error('Error : ' + response.message);
        }
    };
}

Async.prototype.post = function(url, data, fnCallback) {
    const param = 'fname=Henry&lname=Ford';
    xhttp.open('POST', '/picture/extract', true);
    xhttp.setRequestHeader('Content-type', 'application/json');
    //xhttp.send(param);
    xhttp.send();
}


xhttp.open('GET', '/picture/list', true);
xhttp.send();

