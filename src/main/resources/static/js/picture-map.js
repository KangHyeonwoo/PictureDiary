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
	
	//추출버튼 클릭 이벤트
	const extractButton = document.getElementById('extract-button');
	extractButton.addEventListener('click', extract);
	
	//검색 이벤트 추가
	setAddressSearchEvent();
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

function setAddressSearchEvent() {
	//장소 검색 텍스트
	const addressSearchText = document.getElementById('address-search-text');
	addressSearchText.addEventListener('keyup', event => {
		if(event.code === 'Enter') {
			event.preventDefault();
			console.log('Enter Button Clicked')
		}
		
		if(event.code === 'Escape') {
			event.preventDefault();
			console.log('ESC Button Clicked');
		}
	})
	
	//처음 지도 로드 시 지도 중심좌표 주소 조회
	Address.searchCenterLocation(map.obj)
		.then(address => addressSearchText.placeholder = address);
	
	//지도 드래그 시 지도 중심좌표 주소 조회
	map.addIdleEvent(mapObj => {
		Address.searchCenterLocation(mapObj)
			.then(address => addressSearchText.placeholder = address);
	})
}