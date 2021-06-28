const toc = new Toc();
const map = {};
const picture = {};
const markerList = [];

map.options = {
    divId : 'map',
    center : {
        lat : 37.445009887950526,
        lng : 126.9529891719831,
    },
    level : 9,
}

map.init = function() {
    //1. map on
    this.on();

	picture.getList(pictureList => {
		pictureList.forEach(pictureObj => {
			
			const contents = toc.add(pictureObj);
			
			if(pictureObj.hasGeometry) {
				const marker = new Marker(pictureObj, map.obj);
				marker.setMap();
				marker.leftClicked(responseMarker => {
					Marker.closeInfowindow(markerList);
					//responseMarker.infowindow.open(map.obj, responseMarker.marker);
					responseMarker.openInfowindow();
				});
				markerList.push(marker);
				
				contents.addEventListener('click', function(event){
					map.obj.panTo(marker.position)
					
					Marker.closeInfowindow(markerList);
					//marker.infowindow.open(map.obj, marker.marker);
					responseMarker.openInfowindow();
				})
			}
		})
	})
}

map.on = function() {
    const container = document.getElementById(this.options.divId);

    const options = {
        center: new kakao.maps.LatLng(this.options.center.lat, this.options.center.lng),
        level: this.options.level,
    };

    map.obj = new kakao.maps.Map(container, options);
}

picture.list = [];

picture.extract = function() {
    const url = '/picture/extract';
    const data = {};

    Async.post(url, data, function(result){
        debugger;
    })
}


picture.findById = function(id) {
	
    return this.list.find(e => e.pictureId == id);
}

picture.getList = function(fnCallback) {
    const url = '/picture/list';
    const data = {};
    if(this.list.length > 0) {
        fnCallback(this.list);
    }

    Async.get(url, data, function(result) {
        result.forEach(pictureObj => {
			pictureObj.hasGeometry = (pictureObj.latitude != 0 && pictureObj.longitude != 0);
			pictureObj.tocId = (pictureObj.hasGeometry ? 'temp-group_' + pictureObj.pictureId
							 : 'data-group_' + pictureObj.pictureId);
            picture.list.push(pictureObj);
        });

        fnCallback(picture.list);
    })
}

picture.delete = function() {
	const data = {
		pictureId : pictureObj.pictureId,
	}
	const url = `/picture/${data.pictureId}/delete`;
	
	Async.post(url, data, function(result){
		debugger;
	})
}

picture.rename = function(pictureObj, name) {
	const data = {
		pictureId : pictureObj.pictureId,
		pictureName : name
	}
	const url = '/picture/rename';
	
	Async.post(url, data, function(result){
	    const pictureObj = picture.findById(result.pictureId);
	    pictureObj.pictureName = result.pictureName;

        Toc.closeRename(pictureObj);
	})
}

picture.addGeometry = function(pictureObj) {
    //alert(화면을 클릭해주세요.);

    kakao.maps.event.addListener(map.obj, 'click', function(mouseEvent) {
        const latlng = mouseEvent.latLng;

        pictureObj.latitude = latlng.getLat();
        pictureObj.longitude = latlng.getLng();

        console.log(`${pictureObj.latitude}, ${pictureObj.longitude}`);

        const data = {
            pictureId : pictureObj.pictureId,
            latitude : pictureObj.latitude,
            longitude : pictureObj.longitude,
        }

        const url = '/picture/addGeometry';
        Async.post(url, data, function(result){
            console.log(result);
            debugger;

        })
    });


}

map.init();
