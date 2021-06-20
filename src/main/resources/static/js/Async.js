function Async() {
    this.xhttp = new XMLHttpRequest();
}

Async.prototype.get = function(url, data, fnCallback) {
    this.xhttp.onreadystatechange = function(){
        if(this.readyState == 4 && this.status == 200){
            const responseObj = JSON.parse(this.response);
            if(responseObj.resultCode == 'SUCCESS') {
                fnCallback(responseObj.resultList);
            }
        }
    };

    let param = this.toParam(data);
    if(param != '') {
        param = '?' + param;
    }

    this.xhttp.open('GET', url, true);
    this.xhttp.send(param);
}

Async.prototype.post = function(url, data, fnCallback) {
    this.xhttp.onreadystatechange = function(){
        if(this.readyState == 4 && this.status == 200){
            const responseObj = JSON.parse(this.response);
            if(responseObj.resultCode == 'SUCCESS') {
                fnCallback(responseObj.resultList);
            }
        }
    };

    const param = this.toParam(data);

    this.xhttp.open('POST', url, true);
    this.xhttp.setRequestHeader('Content-type', 'application/json');
    this.xhttp.send(param);
}

Async.prototype.toParam = function(data) {
    let param = '';

    if(typeof data != 'Object') {
        return param;
    }

    Object.keys(data).forEach(key => {
        param += (key + "=" + data[key] + '&');
    })

    return param.slice(0, -1);
}