const async = new Async();

const picture = {};
picture.toc = {
	dataGroupId : 'data-group',
	tempGroupId : 'temp-group'
};
picture.toc.list = [];

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
        result.forEach(picture.toc.add);
    })
}

function addMarker(pictureObj) {
    const latitude = pictureObj.latitude;
    const longitude = pictureObj.longitude;
    const markerPosition  = new kakao.maps.LatLng(latitude, longitude);
    const marker = new kakao.maps.Marker({
        position: markerPosition,
    });
	marker.id = pictureObj.pictureId;
	
    // 마커가 지도 위에 표시되도록 설정합니다
    marker.setMap(map);

	kakao.maps.event.addListener(marker, 'click', function() {
	    // 마커 위에 인포윈도우를 표시합니다
		const pictureInfo = picture.toc.findById(marker.id);
		
		var iwContent = '<div style="padding:5px;">' + pictureInfo.pictureOriginName + '</div>', // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
		    iwPosition = new kakao.maps.LatLng(pictureInfo.latitude, pictureInfo.longitude), //인포윈도우 표시 위치입니다
		    iwRemoveable = true; // removeable 속성을 ture 로 설정하면 인포윈도우를 닫을 수 있는 x버튼이 표시됩니다
		
		// 인포윈도우를 생성하고 지도에 표시합니다
		var infowindow = new kakao.maps.InfoWindow({
		    position : iwPosition, 
		    content : iwContent,
		    removable : iwRemoveable
		});
		
		infowindow.open(map, marker);
		//지도 클릭 -> 인포윈도우 닫기
		kakao.maps.event.addListener(map, 'click', function() {
			infowindow.close();
		});

	});
}

picture.toc.add = function(pictureObj) {
	const hasGeometry = (pictureObj.latitude != 0 && pictureObj.longitude != 0);
	
    const dataGroup = document.getElementById(picture.toc.dataGroupId);
    const tempGroup = document.getElementById(picture.toc.tempGroupId);
	
    const li = document.createElement('li');
    li.id = (hasGeometry ? picture.toc.dataGroupId + '_' + pictureObj.pictureId 
						 : picture.toc.tempGroupId + '_' + pictureObj.pictureId);
    li.innerText = ((pictureObj.pictureName == '' || pictureObj.pictureName == null)? pictureObj.pictureOriginName : pictureObj.pictureName);
    
    if(hasGeometry) {
    	pictureObj.type = 'data';
        addMarker(pictureObj);
        dataGroup.appendChild(li);
    } else {
    	pictureObj.type = 'temp';
        tempGroup.appendChild(li);
    }

	picture.toc.list.push(pictureObj);
}

picture.toc.findById = function(id) {
	return this.list.find(e => e.pictureId == id);
}



picture.map.on();
picture.map.getList();