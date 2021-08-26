export default class SearchMarker {
	
	#marker;
	#position;
	#map;
	#infowindow;
	
	constructor(place, map) {
		const imageSrc = '/img/marker1-2.png';
    	const imageSize = new kakao.maps.Size(32, 32);
    	const imageOption = {offset: new kakao.maps.Point(16, 32)};
		const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);
		
		this.#position = new kakao.maps.LatLng(place.y, place.x);
		this.#map = map;
		
		this.#marker = new kakao.maps.Marker({
			position : this.#position,
			image : markerImage
		})
		
		const title = place.place_name;
		this.#infowindow = new Infowindow(title);
		
		this.#add();
		this.#addMarkerEvent();
	}
	
	/** Public Methods */
	
	remove() {
		this.#marker.setMap(null);	
	}
	
	/** Private Methods */
	
	//마커 객체 지도에 추가하기
	#add() {
		this.#marker.setMap(this.#map);
	}
	
	//마커 객체에 이벤트 부여하기
	#addMarkerEvent() {
		const that = this;
		
		kakao.maps.event.addListener(this.#marker, 'mouseover', function() {
			that.#infowindow.show();
        });

        kakao.maps.event.addListener(this.#marker, 'mouseout', function() {
            that.#infowindow.hide();
        });
	}
}

class Infowindow {
	#infowindow;
	#marker;
	#map;
	#title;
	
	constructor(title, marker, map) {
		const content ='<div style="padding:5px;z-index:1;">' + title + '</div>';
		
		this.#infowindow = new kakao.maps.InfoWindow({zIndex:1})
	    this.#infowindow.setContent(content);
		
		this.#title = title;
		this.#marker = marker;
		this.#map = map;
	}
	
	show() {
		this.#infowindow.open(this.#map, this.#marker);
	}
	
	hide() {
		this.#infowindow.close();
	}
	
	get title() {
		return this.#title;
	}
}