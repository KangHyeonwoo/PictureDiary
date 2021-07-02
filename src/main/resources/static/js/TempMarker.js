/*
 *	지도에서 마커 위치 변경 및 새롭게 마커를 추가하는 경우 위치 확인용 임시 마커 생성 js 파일
 */
class TempMarker {
	#marker;
	#infowindow;
		
	constructor(latitude, longitude, map) {
		const position = new kakao.maps.LatLng(latitude, longitude);
		this.#marker = new kakao.maps.Marker({
			position: position
		})
		
		const infowindowContent = this.#infowindowContent();
        this.#infowindow = new kakao.maps.InfoWindow({
            position : position,
            content : infowindowContent
        });
	
		this.#marker.setMap(map);
		this.#infowindow.open(map, this.#marker);
	}
	
	remove() {
		this.#marker.setMap(null);
		this.#infowindow.close();
	}
	
	okButtonClick(fnCallback) {
		document.getElementById('temp-marker-ok-button').addEventListener('click', event => {
			event.stopPropagation();
			fnCallback();
		})
	}
	
	cancelButtonClick(fnCallback) {
		document.getElementById('temp-marker-cancel-button').addEventListener('click', event => {
			event.stopPropagation();
			fnCallback();
		})
	}
	
	#infowindowContent() {
		const div = document.createElement('div');
		div.className = 'btn-group';
		const okButton = document.createElement('button');
		okButton.id = 'temp-marker-ok-button';
		okButton.className = 'basic';
		okButton.innerText = '확인';
		
		const cancelButton = document.createElement('button');
		cancelButton.id = 'temp-marker-cancel-button'
		cancelButton.className = 'cancel';
		cancelButton.innerText = '취소';
		
		div.appendChild(okButton);
		div.appendChild(cancelButton);
		
		return div;
	}
	
	get infowindow() {
		return this.#infowindow;
	}
}