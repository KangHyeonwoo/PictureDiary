import Address from './map/Address.js';
import HttpRequest from './common/HttpRequest.js';
import Toast from './common/Toast.js';
import Toc from './toc/Toc.js'
import Map from './map/Map.js'
import Search from './search/Search.js';

const toc = new Toc();					//TOC 객체
const map = new Map();					//Map 객체
const search = new Search(map.obj);		//검색 모듈 (TOC > 검색하는 부분)

//검색어 입력 텍스트
const addressSearchText = document.getElementById('address-search-text');

window.onload = function() {
	init();
	
	//추출버튼 클릭 이벤트
	const extractButton = document.getElementById('extract-button');
	extractButton.addEventListener('click', extract);
	
	//검색 이벤트 추가
	setAddressSearchEvent();
	
	//지도 이벤트 추가
	setMapEvent();
}

//데이터 및 맵 로드
function init() {
	/**
	(메모) 마커먼저 추가한 이유.
		TOC에 Contents 추가 후 Contents를 클릭할 때
		지도의 중심이 해당 Content에 매핑되는 마커로 이동해야 하기 때문에, 
		1.마커를 먼저 만들고 
		2.Contents 를 만들고
		3.Contents에 이벤트를 부여하는 로직으로 결정.
	 */
	HttpRequest.get('/pictures')
		//마커 추가하기
		.then(pictureList => {
			pictureList
				.filter(pictureObj => pictureObj.hasGeometry)
				.forEach(pictureObj => {
					const marker = map.addMarker(pictureObj);
					pictureObj.marker = marker;
				})
			
			return pictureList
		})
		//TOC 추가하기
		.then(pictureList => {
			pictureList.forEach(pictureObj => toc.addContent(pictureObj));
		});
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

//검색 이벤트 부여
function setAddressSearchEvent() {
	
	//장소 검색 텍스트에 이벤트 부여
	addressSearchText.addEventListener('keypress', event => {
		if(event.code === 'Enter') {
			event.preventDefault();
			search.placeSearch(event.currentTarget.value);
		}
		else if(event.code === 'Escape') {
			event.preventDefault();
			console.log('ESC Button Clicked');
		}
	})
	
	//장소 검색 버튼에 이벤트 부여
	const searchButton = document.getElementById('address-search-button');
	searchButton.addEventListener('click', event => {
		event.preventDefault();
		search.placeSearch(addressSearchText.value);
	})
}

function setMapEvent() {
	
	//처음 지도 로드 시 지도 중심좌표 주소 조회
	Address.searchCenterLocation(map.obj)
		.then(address => addressSearchText.placeholder = address);
	
	//지도 드래그 시 지도 중심좌표 주소 조회
	map.addIdleEvent(mapObj => {
		Address.searchCenterLocation(mapObj)
			.then(address => addressSearchText.placeholder = address);
	})
}

