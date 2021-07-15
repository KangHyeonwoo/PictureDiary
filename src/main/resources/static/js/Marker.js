import Infowindow from './Infowindow.js';

export default class Marker {
	#marker;
	#position;
	#infowindow;
	#map;
	#pictureObj;
	
	static MarkerList = {
		list : [],
		add : function(marker) {
			this.list.push(marker);
		},
		remove : function(marker) {
			const index = this.list.findIndex(e => e.pictureId === marker.pictureId);
			this.list.splice(index, 1);
		}
	};
	
	constructor(pictureObj, map) {
		if(pictureObj.latitude == 0 || pictureObj.longitude == 0) {
			throw new this.#MarkerCreateException('geometry must not empty');
		}
		
		this.#position = new kakao.maps.LatLng(pictureObj.latitude, pictureObj.longitude);
		this.#map = map;
		
		const imageSrc = '/img/marker1-1.png';
    	const imageSize = new kakao.maps.Size(32, 32);
    	const imageOption = {offset: new kakao.maps.Point(32, 32)};
		const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);
		
        this.#marker = new kakao.maps.Marker({
             position: this.#position,
			 image: markerImage
        });
		this.#infowindow = new Infowindow('default', map, this, pictureObj);
		this.#pictureObj = pictureObj;
		
		const that = this;
		kakao.maps.event.addListener(this.#marker, 'click', function() {
			Marker.closeAllInfowindow();
			that.#infowindow.show();
		});
		
		this.#map = map;
		
		this.#add();
	}
	
	//add
	#add() {
		this.#marker.setMap(this.#map);
		Marker.MarkerList.add(this);
	}
	
	//remove
	remove() {
		this.#marker.setMap(null);
		Marker.MarkerList.remove(this);
		this.infowindow.close();
	}
	
	hide() {
		this.#marker.setMap(null);
		this.infowindow.close();
	}

	static closeAllInfowindow() {
		Marker.MarkerList.list.forEach(marker => {
			marker.infowindow.close();
		})
	}
	
	static findByPictureId(pictureId) {
        return Marker.MarkerList.list.find(e => e.pictureObj.pictureId == pictureId);
	}

	get infowindow() {
		return this.#infowindow;
	}

	get pictureObj() {
	    return this.#pictureObj;
	}
	
	get marker() {
		return this.#marker;
	}
	
	get position() {
		return this.#position;
	}
	
	#MarkerCreateException(message) {
		this.message = message;
		this.name = 'MarkerCreateException';
	}
}