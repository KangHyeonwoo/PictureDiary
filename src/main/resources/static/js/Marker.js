class Marker {
	#marker = {};
	#position;
	#infowindow;
	#map;
	
	constructor(pictureObj, map) {
		if(pictureObj.latitude == 0 || pictureObj.longitude == 0) {
			throw new this.#MarkerCreateException('geometry must not empty');
		}
		
		//position
		this.#position = new kakao.maps.LatLng(pictureObj.latitude, pictureObj.longitude);
		
		//infowindow
		const infowindowContent = this.#infowindowContent(pictureObj);
        this.#infowindow = new kakao.maps.InfoWindow({
            position : this.#position,
            content : infowindowContent,
            removable : true
        });

		//marker
        this.#marker = new kakao.maps.Marker({
             position: this.#position
        });
		this.#marker.id = pictureObj.pictureId;
		
		this.#map = map;
	}
	
	//add
	setMap() {
		this.#marker.setMap(this.#map);
	}
	
	leftClicked(fnCallback) {
		const that = this;
		kakao.maps.event.addListener(that.#marker, 'click', function() {
			fnCallback(that)
		});
	}
	
	rightClicked(fnCallback) {
		const that = this;
		kakao.maps.event.addListener(that.#marker, 'rightclick', function() {
			fnCallback(that)
		});
	}
	
	//remove
	remove() {
		
	}
	
	static closeInfowindow(markerList) {
		markerList.forEach(marker => {
			marker.infowindow.close();
		})
	}
	
	#infowindowContent(pictureObj) {
		const url = '/infowindow/'+pictureObj.pictureId;
		const data = {};
		
		const infowindowContent = Async.syncHtml(url, data);
		
		return infowindowContent;
	}
	
	get infowindow() {
		return this.#infowindow;
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