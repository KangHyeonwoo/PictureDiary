const xhttp = new XMLHttpRequest();

map = {};
map.options = {
    divId : 'map',
    center : {
        lat : 33.50972,
        lng : 126.52194,
    },
    level : 9,
}

map.start = function() {
    const container = document.getElementById(this.options.divId);

    const options = {
        center: new kakao.maps.LatLng(this.options.center.lat, this.options.center.lng),
        level: this.options.level,
    };

    map = new kakao.maps.Map(container, options);
}

map.start();

xhttp.onreadystatechange = function(){
    //TODO SUCCESS, FAIL, ERROR 분기처리 및 비동기 함수 만들어서 공통 js로 빼기
    if(xhttp.readyState == 4 && xhttp.status == 200){
        const result = JSON.parse(xhttp.response);

        console.log(result);
    }
};
xhttp.open('GET', '/picture/list', true);
xhttp.send();

function extract() {
    const param = 'fname=Henry&lname=Ford';
    xhttp.open('POST', '/picture/extract', true);
    xhttp.setRequestHeader('Content-type', 'application/json');
    //xhttp.send(param);
    xhttp.send();
}