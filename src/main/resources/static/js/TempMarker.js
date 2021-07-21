import Infowindow from './Infowindow.js';

/*
 *	지도에서 마커 위치 변경 및 새롭게 마커를 추가하는 경우 위치 확인용 임시 마커 생성 js 파일
 */
export default class TempMarker {
	#marker;
	#infowindow;
	
	constructor(pictureObj, latitude, longitude, map) {
		const position = new kakao.maps.LatLng(latitude, longitude);
		const imageSrc = '/img/marker1-2.png';
    	const imageSize = new kakao.maps.Size(32, 32);
    	const imageOption = {offset: new kakao.maps.Point(16, 32)};
		const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);
		
		this.#marker = new kakao.maps.Marker({
			position: position,
			image: markerImage
		})
		
		pictureObj.latitude = latitude;
		pictureObj.longitude = longitude;
		
		this.#infowindow = new Infowindow('temp', map, this, pictureObj);
		
		this.#marker.setMap(map);
		this.#infowindow.show();
	}
	
	remove() {
		this.#marker.setMap(null);
		this.#infowindow.close();
	}
	
	get infowindow() {
		return this.#infowindow;
	}
	
	get marker() {
		return this.#marker;
	}
}