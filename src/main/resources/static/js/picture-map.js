import Marker from './Marker.js';
import TempMarker from './TempMarker.js';
import Address from './Address.js';
import HttpRequest from './HttpRequest.js';
import Toast from './Toast.js';

import Toc from './toc2.js'
import Map from './map.js'

const toc = new Toc();
const map = new Map();

window.onload = function() {
	init();
	
	const extractButton = document.getElementById('extract-button');
	extractButton.addEventListener('click', extract);
}

//데이터 및 맵 로드
function init() {
	HttpRequest.get('/pictures')
		.then(pictureList => toc.setContents(pictureList));
}

//파일 추출
function extract() {
	HttpRequest.post('/extract')
		.then(pictureList => pictureList.filter(pictureObj => pictureObj.hasGeometry))
		.then(getAddressList)
		.then(pictureList => HttpRequest.put('/pictures/addresses', pictureList))
		.then(result => console.log(result));
}

//사진리스트 주소 조회
async function getAddressList(pictureList) {
	for(let i = 0; i<pictureList.length; i++) {
		const pictureObj = pictureList[i];
		
		try {
			const location = await Address.searchDetailLocation(pictureObj.latitude, pictureObj.longitude);
			pictureObj.address = location.address.address_name;
		} catch (error) {}
	}
	
	return pictureList;
}