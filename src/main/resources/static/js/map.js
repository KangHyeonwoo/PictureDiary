//const xhttp = new XMLHttpRequest();
const async = new Async();

const picture = {};

picture.data = {};
picture.data.dataList = [];
picture.data.tempList = [];

picture.map = {};
picture.map.options = {
    divId : 'map',
    center : {
        lat : 33.50972,
        lng : 126.52194,
    },
    level : 9,
}

picture.map.on = function() {
    const container = document.getElementById(this.options.divId);

    const options = {
        center: new kakao.maps.LatLng(this.options.center.lat, this.options.center.lng),
        level: this.options.level,
    };

    map = new kakao.maps.Map(container, options);
}

picture.map.pictureExtract = function() {
    const url = '/picture/extract';
    const data = {};

    async.post(url, data, function(result){
        debugger;
    })
}

picture.map.getList = function() {
    const url = '/picture/list';
    const data = {};

    async.get(url, data, function(result) {
        debugger;
    })
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
        picture.data.dataList.push(pictureObj);
    } else {
        tempGroup.appendChild(li);
        picture.data.tempList.push(pictureObj);
    }
}

picture.map.on();
picture.map.getList();