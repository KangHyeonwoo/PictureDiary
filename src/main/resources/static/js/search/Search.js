import SearchMarker from './SearchMarker.js';

export default class Search {
	
	#markerList = new Array();
	#map;
	
	constructor(map) {
		this.#map = map;
	}
	
	/** Public Methods */
	
	displayResult(searchResult) {
		this.#reset();
		const listEl = document.getElementById('toc.search');
		const bounds = new kakao.maps.LatLngBounds();
		
		//3. TOC에 추가 및 마커 추가
		searchResult.addressList.forEach(place => {
			const searchItem = this.#rendererTocSearch(place);
			const searchMarker = new SearchMarker(place, this.#map);
			this.#markerList.push(searchMarker);
			
			searchItem.onmouseover = function() {
				
			}
			
			searchItem.onmouseout = function() {
				
			}
			
			listEl.appendChild(searchItem);
			
			const placePosition = new kakao.maps.LatLng(place.y, place.x);
			bounds.extend(placePosition);
		})
		
		this.#map.setBounds(bounds);
		
		return searchResult;
	}
	
	setPagination(pagination, pageClickedFunc) {
		const paginationEl = document.getElementById('pagination');
		const fragment = document.createDocumentFragment();
		
		while(paginationEl.hasChildNodes()) {
			paginationEl.removeChild(paginationEl.lastChild);
		}
		
		for(let i = 1; i <= pagination.last; i++) {
			const el = document.createElement('a');
			el.href = '#';
			el.innerHTML = i;
			
			if(i === pagination.current) {
				el.className = 'on';
			} else {
				el.onclick = (function(i){
					return function() {
						pageClickedFunc(i);
					}
				})(i);
			}
			
			fragment.appendChild(el);
		}
		
		paginationEl.appendChild(fragment);
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