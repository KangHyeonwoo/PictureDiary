class Marker {
	#marker = {};
	#position;
	#infowindow;
	#map;
	#pictureId;
	
	constructor(pictureObj, map) {
		if(pictureObj.latitude == 0 || pictureObj.longitude == 0) {
			throw new this.#MarkerCreateException('geometry must not empty');
		}
		
		//position
		this.#position = new kakao.maps.LatLng(pictureObj.latitude, pictureObj.longitude);
		
		//infowindow
		/*
		const infowindowContent = this.#infowindowContent(pictureObj);
        this.#infowindow = new kakao.maps.InfoWindow({
            position : this.#position,
            content : infowindowContent,
            removable : true
        });
		*/
		
		//marker
        const marker = new kakao.maps.Marker({
             position: this.#position
        });
		const that = this;
		kakao.maps.event.addListener(marker, 'click', function() {
			Marker.closeInfowindow(markerList);
			that.openInfowindow();
		});
		
		this.#infowindow = new Infowindow('default', map, marker, pictureObj);
		
		this.#pictureId = pictureObj.pictureId;
		this.#map = map;
		this.#marker = marker;
	}
	
	//add
	setMap() {
		this.#marker.setMap(this.#map);
	}
	
	//delete
	remove() {
		this.#marker.setVisible(false);
	}
/*
	openInfowindow() {
	    const that = this;
		this.infowindow.open(this.#map, this.#marker);
		
		const markerMoveButton = document.getElementById('marker-move-button');
		const markerDeleteButton = document.getElementById('marker-delete-button');

		markerMoveButton.addEventListener('click', event => {
			event.preventDefault();
			that.moveMarker();
		})
		
		markerDeleteButton.addEventListener('click', event => {
			event.preventDefault();
			console.log(2);
		});
	}
	
	static closeInfowindow(markerList) {
		markerList.forEach(marker => {
			marker.infowindow.close();
		})
	}


	openInfowindow() {
		this.#infowindow.open();
	}
*/	
	static closeInfowindow(markerList) {
		markerList.forEach(marker => {
			marker.infowindow.close();
		})
	}
	
	static findByPictureId(pictureId, markerList) {
        return markerList.find(e => e.pictureId == pictureId);
	}
/*
	#infowindowContent(pictureObj) {
		const url = '/infowindow/'+pictureObj.pictureId;
		const data = {};
		
		const infowindowContent = Async.syncHtml(url, data);
		
		return infowindowContent;
	}
*/
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