import PlaceMarker from '../marker/PlaceMarker.js';

export default class TocSearch {
	
	#markerList = new Array();
	#map;
	
	constructor() {}
	
	/** Public Methods */
	
	displayAddressList(addressList, map) {
		//TODO 이부분 맘에 안듦
		if(!this.#map) {
			this.#map = map;
		}
		
		this.#reset();
		const listEl = document.getElementById('toc.search');
		const bounds = new kakao.maps.LatLngBounds();
		//1. 기존에 검색 목록 삭제
		
		//2. 기존에 검색 마커 삭제
		
		//3. TOC에 추가 및 마커 추가
		addressList.forEach(place => {
			const searchItem = this.#rendererTocSearch(place);
			const placeMarker = new PlaceMarker(place, map);
			this.#markerList.push(placeMarker);
			searchItem.onmouseover = function() {
				
			}
			
			searchItem.onmouseout = function() {
				
			}
			
			listEl.appendChild(searchItem);
			
			const placePosition = new kakao.maps.LatLng(place.y, place.x);
			bounds.extend(placePosition);
		})
		
	}
	
	setPagination(pagination) {
		
	}
	
	/** Private Methods */
	
	#rendererTocSearch(place) {
		const searchItem = document.createElement('div');
		searchItem.classList.add('search-item');
		
		const addressEl = document.createElement('p');
		const placeEl = document.createElement('p');
		const categoryEl = document.createElement('p');
		
		addressEl.innerText = place.address_name;
		placeEl.innerText = place.place_name;
		categoryEl.innerText = place.category_name;
		
		searchItem.appendChild(placeEl);
		searchItem.appendChild(categoryEl);
		searchItem.appendChild(addressEl);
		
		return searchItem;
	}
	//TOC 목록 / 마커 초기화하기
	#reset() {
		//마커 지우기
		this.#markerList.forEach(marker => marker.remove());
		this.#markerList = new Array();
		
		//TOC 목록 제거
		const listEl = document.getElementById('toc.search');
		const childrens = listEl.getElementsByClassName('search-item');
		Array.from(childrens).forEach(children => {
			listEl.removeChild(children);
		})
	}
} 