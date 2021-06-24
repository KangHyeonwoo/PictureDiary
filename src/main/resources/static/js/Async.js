class Async {
	static get = function(url, data, fnCallback) {
		const xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			if (xhttp.readyState == 4 && xhttp.status == 200) {
				const responseObj = JSON.parse(this.response);
				if (responseObj.status == 'OK') {
					fnCallback(responseObj.responseData);
				}
			}
		};
	
		let param = Async.#toParam(data);
		if (param != '') {
			param = '?' + param;
		}
	
		xhttp.open('GET', url, true);
		xhttp.send(param);
	}
	
	static syncHtml = function(url, data) {
		const xhttp = new XMLHttpRequest();
		
		let param = Async.#toParam(data);
		if (param != '') {
			param = '?' + param;
		}
	
		xhttp.open('GET', url, false);
		xhttp.send(null);
	
		if (xhttp.status != 200) {
			throw new Error("error");
		}
	
		return xhttp.responseText;
	}
	
	static post = function(url, data, fnCallback) {
		const xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			if (xhttp.readyState == 4 && xhttp.status == 200) {
				const responseObj = JSON.parse(this.response);
				if (responseObj.status == 'OK') {
					fnCallback(responseObj.responseData);
				}
			}
		};
	
		const param = Async.#toParam(data);
	
		xhttp.open('POST', url, true);
		xhttp.setRequestHeader('Content-type', 'application/json');
		xhttp.send(param);
	}

	static #toParam = function(data) {
		let param = '';
	
		if (typeof data != 'Object') {
			return param;
		}
	
		Object.keys(data).forEach(key => {
			param += (key + "=" + data[key] + '&');
		})
	
		return param.slice(0, -1);
	}
}

