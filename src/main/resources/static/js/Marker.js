import Infowindow from './Infowindow.js';

export default class Marker {
	#marker = {};
	#position;
	#infowindow;
	#map;
	#pictureId;
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
        this.#marker = new kakao.maps.Marker({
             position: this.#position
        });
		this.#infowindow = new Infowindow('default', map, this.#marker, pictureObj);
		this.#pictureId = pictureObj.pictureId;
		
		const that = this;
		kakao.maps.event.addListener(this.#marker, 'click', function() {
			Marker.closeAllInfowindow();
			that.#infowindow.show();
		});
		
		this.#pictureId = pictureObj.pictureId;
		this.#map = map;
	}
	
	//add
	add() {
		this.#marker.setMap(this.#map);
		Marker.MarkerList.add(this);
	}
	
	//delete
	remove() {
		this.#marker.setMap(null);
		Marker.MarkerList.remove(this);
	}

	static closeAllInfowindow() {
		Marker.MarkerList.list.forEach(marker => {
			marker.infowindow.close();
		})
	}
	
	static findByPictureId(pictureId) {
        return Marker.MarkerList.list.find(e => e.pictureId == pictureId);
	}
	
	moveMarker() {
		console.log('move marker')
	}
	
	closeMoveMarkerAction() {
		
	}

	get infowindow() {
		return this.#infowindow;
	}

	get pictureId() {
	    return this.#pictureId;
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