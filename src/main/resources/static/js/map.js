import Marker from './Marker.js';
import TempMarker from './TempMarker.js';
import Address from './Address.js';

const map = {};
const picture = {};
let tempMarker;
const toc = new Toc();

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
	Toc.Map = map.obj;
	
	//2. pictures draw;
	picture.getList(pictureList => {
		pictureList.forEach(pictureObj => {
			
			const contents = toc.add(pictureObj);
			
			if(pictureObj.hasGeometry) {
				picture.addMarker(pictureObj, contents);
			}
		})
	})
	
	//3. extract button setting
	const extractButton = document.getElementById('extract-button');
	extractButton.addEventListener('click', picture.extract)
	
	
	//ContextMenu
	kakao.maps.event.addListener(map.obj, 'toc-contextmenu-picture-remove', picture.tocContextMenuRemoveHandler);
	kakao.maps.event.addListener(map.obj, 'toc-contextmenu-picture-addGeometry', picture.tocContextMenuAddGeometryHandler)
	
	//TempMarker > Infowindow
	kakao.maps.event.addListener(map.obj, 'tempMarker-infowindow-ok', picture.tempMarkerInfowindowOkButtonHandler);
	kakao.maps.event.addListener(map.obj, 'tempMarker-infowindow-cancel', picture.tempMarkerInfowindowCloseButtonHandler);
	
	const addressSearchButton = document.getElementById('address-search-button');
	addressSearchButton.addEventListener('click', picture.searchAddress)
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
			const contents = toc.add(pictureObj);
			
			if(pictureObj.hasGeometry) {
				picture.addMarker(pictureObj, contents);
			}
		})
    })
}


picture.addMarker = function(pictureObj, contents) {
	const marker = new Marker(pictureObj, map.obj);
	
	kakao.maps.event.addListener(marker.marker, 'click', function(e) {
	    const param = {};
		param.currentTarget = contents;
		toc.contentClickEventHandler(param);
	})
	
	contents.addEventListener('click', function(event){
		map.obj.panTo(marker.position)
		
		Marker.closeAllInfowindow();
		marker.infowindow.show();
		
	})
}

picture.getList = function(fnCallback) {
    const url = '/picture/list';
    const data = {};

    Async.get(url, data, function(result) {

        fnCallback(result);
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

/** EVENT HANDLER **/

picture.tocContextMenuRemoveHandler = function(pictureObj) {
	const data = {
		pictureId : pictureObj.pictureId,
	}
	const url = `/picture/${data.pictureId}/delete`;
	
	Async.post(url, data, function(result){
		//toc remove
		toc.remove(pictureObj);
		
		//marker remove
		const marker = Marker.findByPictureId(pictureObj.pictureId);
		if(marker) {			
			marker.remove();
		}
	})
}

picture.tocContextMenuAddGeometryHandler = function(pictureObj) {
	Marker.closeAllInfowindow();
	
	const addTempMarkerEventHandler = function(mouseEvent) {
		const latlng = mouseEvent.latLng;
	    const latitude = latlng.getLat();
	    const longitude = latlng.getLng();
	
		if(typeof tempMarker !== 'undefined') {
			tempMarker.remove();		
		}
		
		tempMarker = new TempMarker(pictureObj, latitude, longitude, map.obj);
	}
	
	kakao.maps.event.addListener(map.obj, 'click', addTempMarkerEventHandler);
}

picture.tempMarkerInfowindowOkButtonHandler = function(obj) {
	const pictureObj = obj.pictureObj;
	const tempMarker = obj.tempMarker;
	const url = '/picture/addGeometry';
	const data = {
        pictureId : pictureObj.pictureId,
        latitude : pictureObj.latitude,
        longitude : pictureObj.longitude,
    }

    Async.post(url, data, function(resultPictureObj){
		//toc 변경
		const contents = document.getElementById(resultPictureObj.tocId);
		toc.remove(pictureObj);
		toc.add(resultPictureObj);
		
		//marker추가
		picture.addMarker(resultPictureObj, contents);
    })
	
	tempMarker.remove();
}

//Temp Marker 인포윈도우 [취소] 버튼 클릭 이벤트
picture.tempMarkerInfowindowCloseButtonHandler = function(tempMarker) {
	tempMarker.remove();
	kakao.maps.event.removeListener(map.obj, 'click');
}

picture.searchAddress = function() {
    const keyword = document.getElementById('address-search-text').value;

	Address.search(keyword)
		.then(addressList => {
			console.log(addressList);
		})
		.catch(error => {
			console.log(error);
		})
}

map.init();


