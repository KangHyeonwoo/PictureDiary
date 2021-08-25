import PlaceMarker from '../marker/PlaceMarker.js';

export default class TocSearch {

	constructor() {}
	
	/** Public Methods */
	
	displayAddressList(addressList, map) {
		//place_name
		//address_name
		//category_name
		const listEl = document.getElementById('toc.search');
		const bounds = new kakao.maps.LatLngBounds();
		//1. 기존에 검색 목록 삭제
		
		//2. 기존에 검색 마커 삭제
		
		//3. TOC에 추가 및 마커 추가
		addressList.forEach(place => {
			const searchItem = this.#rendererTocSearch(place);
			const placeMarker = new PlaceMarker(place, map);
			
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
		
		const addressEl = document.createElement('p');
		const placeEl = document.createElement('p');
		
		addressEl.innerText = place.address_name;
		placeEl.innerText = place.place_name;
		
		searchItem.appendChild(addressEl);
		searchItem.appendChild(placeEl);
		
		return searchItem;
	}
	
	
} 