/*
 *	지도에서 마커 위치 변경 및 새롭게 마커를 추가하는 경우 위치 확인용 임시 마커 생성 js 파일
 */
class TempMarker {
	
	constructor(pictureObj, map) {
		console.log(this);
		const position = new kakao.maps.LatLng(pictureObj.latitude, pictureObj.longitude);
		
		const infowindowContent = this.#infowindowContent(pictureObj);
        const infowindow = new kakao.maps.InfoWindow({
            position : position,
            content : infowindowContent
        });

	}
	
	#infowindowContent() {
		const div = document.createElement('div');
		const okButton = document.createElement('button');
		const cancelButton = document.createElement('button');
		
		div.appendChild(okButton);
		div.appendChild(cancelButton);
	}
}