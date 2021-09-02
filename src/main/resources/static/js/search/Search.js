import SearchMarker from './SearchMarker.js';
import Address from '../map/Address.js';
import Toast from '../common/Toast.js';

export default class Search {
	
	#markerList = new Array();
	#map;
	
	constructor(map) {
		this.#map = map;
	}
	
	/** Public Methods */
	
	//장소 검색
	placeSearch(keyword, pageIndex) {
		const that = this;
		Address.searchKeyword(keyword, pageIndex)
			.then(searchResult => that.displayResult(searchResult))
			.then(searchResult => {
				that.setPagination(searchResult.pagination, function(index) {
					that.placeSearch(keyword, index);
				})
			})
			.catch(error => {
				Toast.show(error);
				that.#reset();
			});
		
		const menuSearchButton = document.getElementById('menu.search');
		menuSearchButton.click();
	}


	//검색 결과 TOC에 그리기
	displayResult(searchResult) {
		this.#reset();
		const listEl = document.getElementById('toc.search');
		const bounds = new kakao.maps.LatLngBounds();
		
		//TOC에 추가 및 마커 추가
		searchResult.addressList.forEach(place => {
			const searchItem = this.#rendererTocSearch(place);
			const searchMarker = new SearchMarker(place, this.#map);
			this.#markerList.push(searchMarker);
			
			listEl.appendChild(searchItem);
			
			const placePosition = new kakao.maps.LatLng(place.y, place.x);
			bounds.extend(placePosition);
		})
		
		//검색 결과 TOC 최상단으로 이동
		listEl.scrollTop = 0;
		//마커 위치로 bounds 조정
		this.#map.setBounds(bounds);
		
		return searchResult;
	}
	
	//페이지네이션 그리기
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
	
	//TOC에 검색 결과 item 객체 생성
	#rendererTocSearch(place) {
		
		const itemDiv = document.createElement('div');
			  itemDiv.classList.add('item');
		
		const itemInfoDiv = document.createElement('div');
			  itemInfoDiv.classList.add('item-info');
			  itemInfoDiv.classList.add('no-more');
		
		const picture = document.createElement('img');
			  picture.classList.add('picture');
			  picture.src = '/img/marker1-2.png';
		itemDiv.appendChild(picture);
		
		const placePTag = document.createElement('p');
			  placePTag.classList.add('title');
			  placePTag.innerText = place.place_name;
		itemInfoDiv.appendChild(placePTag);

		const addressPTag = document.createElement('p');
			  addressPTag.innerText = place.address_name;
		itemInfoDiv.appendChild(addressPTag);
		
		const categoryPTag = document.createElement('p');
			  categoryPTag.innerText = place.category_name;
		itemInfoDiv.appendChild(categoryPTag);
		
		itemDiv.appendChild(itemInfoDiv);
		
		return itemDiv;
	}
	
	//TOC 목록 / 마커 초기화하기
	#reset() {
		//마커 지우기
		this.#markerList.forEach(marker => marker.remove());
		this.#markerList = new Array();
		
		//TOC 목록 제거
		const listEl = document.getElementById('toc.search');
		const childrens = listEl.getElementsByClassName('item');
		Array.from(childrens).forEach(children => {
			listEl.removeChild(children);
		})
		
		//페이지네이션 제거
		const paginationEl = document.getElementById('pagination');
		
		while(paginationEl.hasChildNodes()) {
			paginationEl.removeChild(paginationEl.lastChild);
		}
	}
} 