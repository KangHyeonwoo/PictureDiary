import Marker from './Marker.js';
import TempMarker from './TempMarker.js';

export default class Map {
	#map;
	#options = {
		divId : 'map',
	    center : {
	        lat : 37.445009887950526,
	        lng : 126.9529891719831,
	    },
	    level : 9,
	}
	
	constructor() {
		this.#on();
	}
	
	/* Public Methods */
	
	//마커 추가
	addMarker(pictureObj) {
		const marker = new Marker(pictureObj, this.#map);
		
		return marker;
	}
	
	//마커 삭제
	removeMarker() {
		
	}
	
	//지도 이동 이벤트(콜백함수)
	addIdleEvent(fnCallback) {
		kakao.maps.event.addListener(this.#map, 'idle', () => {
			fnCallback(this.#map);
		});
	}
	
	//맵 객체 getter
	get obj() {
		return this.#map;
	}
	
	/* Private Methods */
	
	#on() {
		const container = document.getElementById(this.#options.divId);

	    const options = {
	        center: new kakao.maps.LatLng(this.#options.center.lat, this.#options.center.lng),
	        level: this.#options.level,
	    };
		
	    const map = new kakao.maps.Map(container, options);
/*
		kakao.maps.event.addListener(map, 'click', mouseEvent => {
			const latlng = mouseEvent.latLng;
			map.geometry.latitude = latlng.getLat();
			map.geometry.longitude = latlng.getLng();
		});
		*/
		this.#map = map;
	}
	
	
}