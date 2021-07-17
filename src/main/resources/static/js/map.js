import Marker from './Marker.js';
import TempMarker from './TempMarker.js';
import Address from './Address.js';
import HttpRequest from './HttpRequest.js';

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
	HttpRequest.get('/pictures')
		.then(pictureList => pictureList.forEach(picture.add))
	
	//3. extract button setting
	const extractButton = document.getElementById('extract-button');
	extractButton.addEventListener('click', picture.extract)
	
	
	//ContextMenu
	kakao.maps.event.addListener(map.obj, 'toc-contextmenu-picture-remove', picture.tocContextMenuRemoveHandler);
	kakao.maps.event.addListener(map.obj, 'toc-contextmenu-picture-addGeometry', picture.tocContextMenuAddGeometryHandler)
	kakao.maps.event.addListener(map.obj, 'toc-contextmenu-picture-rename', picture.tocContextMenuRenameHandler)
	
	//Marker > Infowindow
	kakao.maps.event.addListener(map.obj, 'marker-infowindow-move-button', picture.markerInfowindowMoveGeometryHandler);
	
	//TempMarker > Move Geometry > Infowindow
	
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
	HttpRequest.post('pictures/extract')
		.then(pictureList => pictureList.forEach(picture.add))
}

picture.add = function(pictureObj) {
	const contents = toc.add(pictureObj);

	if(pictureObj.hasGeometry) {
		picture.addMarker(pictureObj, contents);
	}
}

picture.addMarker = function(pictureObj, contents) {
	const marker = new Marker(pictureObj, map.obj);
	
	marker.infowindow.setButton({
		name : '위치 변경',
		location : 'left',
		onclickEvent : () => picture.markerInfowindowMoveGeometryHandler(marker)
	})
	
	marker.infowindow.setButton({
		name : '삭제',
		location : 'right',
		onclickEvent : function(onclickParam) {
			console.log('left button clicked : ' + onclickParam)
		}
	});
	
	//Marker Click Event
	kakao.maps.event.addListener(marker.marker, 'click', function(e) {
	    const param = {};
		param.currentTarget = contents;
		
		toc.contentClickEventHandler(param);
	})
	
	contents.addEventListener('click', function(event){
		event.preventDefault();
		
		map.obj.panTo(marker.position)
		
		Marker.closeAllInfowindow();
		marker.infowindow.show();
	})
}


picture.moveGeomtry = function() {
	
}

/** EVENT HANDLER **/

picture.tocContextMenuRemoveHandler = function(pictureObj) {
	
	HttpRequest.delete(`/pictures/${pictureObj.pictureId}`)
		.then(result => {
			//toc remove
			toc.remove(pictureObj);
			
			//marker remove
			const marker = Marker.findByPictureId(pictureObj.pictureId);
			if(marker) {			
				marker.remove();
			}
		})
		.catch(error => console.error(error.message));
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
		
		const tempMarkerOkButtonParam = {
			pictureObj : pictureObj,
			tempMarker : tempMarker
		}
		
		tempMarker.infowindow.setButton({
			name : '확인',
			location : 'left',
			onclickEvent : () => picture.tempMarkerAddGeometryOkButtonHandler(tempMarkerOkButtonParam)
		});
		
		tempMarker.infowindow.setButton({
			name : '취소',
			location : 'right',
			onclickEvent : () => picture.tempMarkerAddGeometryCancelButtonHandler(tempMarker)
		});
	}
	
	kakao.maps.event.addListener(map.obj, 'click', addTempMarkerEventHandler);
}

picture.tocContextMenuRenameHandler= function(paramObj) {
	const url = `/pictures/${paramObj.pictureObj.pictureId}/pictureName`;
	const data = {
		pictureName : paramObj.pictureName
	}
	HttpRequest.patch(url, data)
		.then(result => {
			Toc.closeRename(paramObj.pictureObj);
			
			const tocElement = document.getElementById(result.tocId);
			tocElement.innerText = result.pictureName
		})
		.catch(error => {
			console.log(error)
		});
}

picture.markerInfowindowMoveGeometryHandler = function(obj) {
	const pictureObj = obj.pictureObj;
	const marker = Marker.findByPictureId(pictureObj.pictureId);
	
	/**
	1. marker off
	2. mouse click
	3. temp marker 생성(확인, 이동 취소)
	4. 확인 -> server save / tempmarker remove /marker remove /new marker insert
	5. 이동 취소 -> tempmarker remove / marker on 
	*/
	marker.hide();
	
	const addTempMarkerEventHandler = function(mouseEvent) {
		const latlng = mouseEvent.latLng;
	    const latitude = latlng.getLat();
	    const longitude = latlng.getLng();
	
		if(typeof tempMarker !== 'undefined') {
			tempMarker.remove();		
		}
		
		tempMarker = new TempMarker(pictureObj, latitude, longitude, map.obj);
		
		tempMarker.infowindow.setButton({
			name : '확인',
			location : 'left',
			onclickEvent : () => picture.tempMarkerMarkerMoveOkButtonHandler(marker, tempMarker)
		});
		
		tempMarker.infowindow.setButton({
			name : '취소',
			location : 'right',
			onclickEvent : () => picture.markerMoveCancelButtonHandler(marker, tempMarker)
		});
	}
	
	kakao.maps.event.addListener(map.obj, 'click', addTempMarkerEventHandler);
}

picture.tempMarkerMarkerMoveOkButtonHandler = function(marker, tempMarker) {
	const pictureObj = marker.pictureObj;
	const position = tempMarker.marker.getPosition();
	const geometry = {
		latitude : position.getLat(),
		longitude : position.getLng(),
	}
	
	//1. server save
	HttpRequest.patch(`/pictures/${pictureObj.pictureId}/geometry`, geometry)
		.then(resultPictureObj => {
			//2. tempmarker remove
			tempMarker.remove();
			//3. marker remove
			marker.remove();
			//4. new marker insert
			const contents = document.getElementById(resultPictureObj.tocId);
			picture.addMarker(resultPictureObj, contents);
		})
		.catch(error => {
			console.error(error.message)
		});
	
	
}

picture.markerMoveCancelButtonHandler = function(marker, tempMarker) {
	//1. tempmarker remove
	tempMarker.remove();
	
	//2. marker on
	marker.show();
}

picture.tempMarkerAddGeometryOkButtonHandler = function(obj) {
	const pictureObj = obj.pictureObj;
	const tempMarker = obj.tempMarker;
	const geometry = {
		latitude : pictureObj.latitude,
        longitude : pictureObj.longitude,
	}
	
	HttpRequest.patch(`/pictures/${pictureObj.pictureId}/geometry`, geometry)
		.then(resultPictureObj => {
			tempMarker.remove();
			
			//toc 변경
			const contents = document.getElementById(resultPictureObj.tocId);
			toc.remove(pictureObj);
			toc.add(resultPictureObj);
			
			//marker추가
			picture.addMarker(resultPictureObj, contents);
		})
		.catch(error => {
			console.error(error.message)
		});
}

//Temp Marker 인포윈도우 [취소] 버튼 클릭 이벤트
picture.tempMarkerAddGeometryCancelButtonHandler = function(tempMarker) {
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


