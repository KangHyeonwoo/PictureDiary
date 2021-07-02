const toc = new Toc();
const map = {};
const picture = {};
const markerList = [];
let tempMarker;
picture.list = [];

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

	//2. pictures draw;
	picture.getList(pictureList => {
		pictureList.forEach(pictureObj => {
			
			const contents = toc.add(pictureObj);
			
			if(pictureObj.hasGeometry) {
				picture.addMarker(pictureObj, contents);
			}
		})
	})
	
	//3. button setting
	const extractButton = document.getElementById('extract-button');
	extractButton.addEventListener('click', picture.extract)
}

map.on = function() {
    const container = document.getElementById(this.options.divId);

    const options = {
        center: new kakao.maps.LatLng(this.options.center.lat, this.options.center.lng),
        level: this.options.level,
    };

    map.obj = new kakao.maps.Map(container, options);
}



picture.extract = function() {
    const url = '/picture/extract';
    const data = {};

    Async.post(url, data, function(pictureList){
        pictureList.forEach(pictureObj => {
            picture.list.push(pictureObj);
	
			const contents = toc.add(pictureObj);
			
			if(pictureObj.hasGeometry) {
				picture.addMarker(pictureObj, contents);
			}
		})
    })
}


picture.addMarker = function(pictureObj, contents) {
	const marker = new Marker(pictureObj, map.obj);
	marker.setMap();
	marker.leftClicked(responseMarker => {
		Marker.closeInfowindow(markerList);
		marker.openInfowindow();
	});
	markerList.push(marker);
	
	contents.addEventListener('click', function(event){
		map.obj.panTo(marker.position)
		
		Marker.closeInfowindow(markerList);
		marker.openInfowindow();
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
        result.forEach(pictureObj => picture.list.push(pictureObj));

        fnCallback(picture.list);
    })
}

picture.remove = function(pictureObj) {
	const data = {
		pictureId : pictureObj.pictureId,
	}
	const url = `/picture/${data.pictureId}/delete`;
	
	Async.post(url, data, function(result){
		//toc remove
		const contents = document.getElementById(pictureObj.tocId);
		contents.remove();
		
		//marker remove
		const marker = Marker.findByPictureId(pictureObj.pictureId, markerList);
		if(marker) {			
			marker.remove();
		}
		
		//markerlist pop
		const markerIndex = markerList.findIndex(e => e.pictureId === pictureObj.pictureId);
		markerList.splice(markerIndex, 1);
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
	kakao.maps.event.addListener(map.obj, 'click', picture.addTempMarker);
	
	kakao.maps.event.addListener(map.obj, 'add-geometry-ok', function(latlng){
		const data = {
            pictureId : pictureObj.pictureId,
            latitude : latlng.getLat(),
            longitude : latlng.getLng(),
        }

        const url = '/picture/addGeometry';
        Async.post(url, data, function(resultPictureObj){
			//toc 변경
			const contents = document.getElementById(resultPictureObj.tocId);
			
			toc.remove(pictureObj);
			toc.add(resultPictureObj);
			
			//marker추가
			picture.addMarker(resultPictureObj, contents);
			
			
			//picture.list에 기존 객체 삭제 후 새로 추가하기
			const idx = picture.list.findIndex(function(item){
				return item.pictureId === pictureObj.pictureId;
			});
			if (idx > -1) {
				picture.list.splice(idx, 1);
			}
			
			picture.list.push(resultPictureObj);
			
        })
	});
}

picture.addTempMarker = function(mouseEvent) {
	const latlng = mouseEvent.latLng;
    const latitude = latlng.getLat();
    const longitude = latlng.getLng();

	if(typeof tempMarker !== 'undefined') {
		tempMarker.remove();		
	}
	
	tempMarker = new TempMarker(latitude, longitude, map.obj);
	tempMarker.okButtonClick(function(){
		kakao.maps.event.trigger(map.obj, 'add-geometry-ok', latlng);
	})
	
	tempMarker.cancelButtonClick(function(){
		kakao.maps.event.removeListener(map.obj, 'click', picture.addTempMarker);
		tempMarker.remove();
	})
}

map.init();


