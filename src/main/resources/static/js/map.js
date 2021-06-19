const xhttp = new XMLHttpRequest();
const dataObj = {};
dataObj.dataList = [];
dataObj.tempList = [];

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
        const responseObj = JSON.parse(xhttp.response);

        const resultList = responseObj.resultList;
        resultList.forEach(addToc);
    }
};
xhttp.open('GET', '/picture/list', true);
xhttp.send();

function pictureExtract() {
    const param = 'fname=Henry&lname=Ford';
    xhttp.open('POST', '/picture/extract', true);
    xhttp.setRequestHeader('Content-type', 'application/json');
    //xhttp.send(param);
    xhttp.send();
}

function addMarker(pictureObj) {
    const latitude = pictureObj.latitude;
    const longitude = pictureObj.longitude;
    const markerPosition  = new kakao.maps.LatLng(latitude, longitude);
    const marker = new kakao.maps.Marker({
        position: markerPosition
    });

    // 마커가 지도 위에 표시되도록 설정합니다
    marker.setMap(map);
}

function addToc(pictureObj) {
    const dataGroup = document.getElementById('data-group');
    const tempGroup = document.getElementById('temp-group');

    const li = document.createElement('li');
    li.innerText = ((pictureObj.pictureName == '' || pictureObj.pictureName == null)? pictureObj.pictureOriginName : pictureObj.pictureName);

    const hasGeometry = (pictureObj.latitude != 0 && pictureObj.longitude != 0);
    if(hasGeometry) {
        addMarker(pictureObj);
        dataGroup.appendChild(li);
        dataObj.dataList.push(pictureObj);
    } else {
        tempGroup.appendChild(li);
        dataObj.tempList.push(pictureObj);
    }
}