export default class Address {
	static #ps = new kakao.maps.services.Places();
	static #geocoder = new kakao.maps.services.Geocoder();
	
	static #errorMessage = {
		ZERO_RESULT : '검색 결과가 존재하지 않습니다.',
		ERROR : '검색 결과 중 오류가 발생했습니다.'
	}
	
	static searchKeyword(keyword) {
		return new Promise(
			function(resolve, reject) {
				if (!keyword.replace(/^\s+|\s+$/g, '')) {
			        reject('키워드를 입력해주세요');
					return;
			    }
				
				Address.#ps.keywordSearch(keyword, function(data, status, pagination) {
					if(status === kakao.maps.services.Status.OK) {
						resolve({
							addressList : data,
							pagination : pagination
						});
					} else if (status === kakao.maps.services.Status.ZERO_RESULT) {
						reject(Address.#errorMessage.ZERO_RESULT);
					} else if (status === kakao.maps.services.Status.ERROR) {
						reject(Address.#errorMessage.ERROR);
					}
				});
			}
		);
	}
	
	static searchLocation(lat, lng) {
		return new Promise(
			function(resolve, reject) {
				Address.#geocoder.coord2RegionCode(lng, lat, function(result, status){
					if (status === kakao.maps.services.Status.OK) {
						resolve(result.find(region => region.region_type === 'H'));
						
				    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
						reject(Address.#errorMessage.ZERO_RESULT);
					} else if (status === kakao.maps.services.Status.ERROR) {
						reject(Address.#errorMessage.ERROR);
					}
				});
			}
		);
	}
	
	static searchCenterLocation(map) {
		const center = map.getCenter();
		const latitude = center.getLat();
		const longitude = center.getLng();
		
		return Address.searchLocation(latitude, longitude)
			.then(result => `${result.region_1depth_name} ${result.region_2depth_name} ${result.region_3depth_name}`)
			.catch(error => {
				console.log(error);
			})
	}
	
	static displayPlaces(places) {
		const listEl = document.getElementById('placesList');
		const menuEl = document.getElementById('menu_wrap');
		const fragment = document.createDocumentFragment();
		
		Address.removeAllChildNodes(listEl);
		
		for(let i=0; i< places.length; i++) {
			const itemEl = Address.getItem(places[i]);
			
			(place => {
			    //TODO css 적용하기
				itemEl.onclick = () => {
					console.log(place);
				}
			})(places[i]);
			
			fragment.appendChild(itemEl);
		}
		
		listEl.appendChild(fragment);
		menuEl.scrollTop = 0;
	}
	
	static getItem(places) {
		const el = document.createElement('li');
		let itemStr = 
	                '<div class="info">' +
	                '   <h5>' + places.place_name + '</h5>';
	
	    if (places.road_address_name) {
	        itemStr += '    <span>' + places.road_address_name + '</span>' +
	                    '   <span class="jibun gray">' +  places.address_name  + '</span>';
	    } else {
	        itemStr += '    <span>' +  places.address_name  + '</span>'; 
	    }
		itemStr += '		<span class="hidden location">' + places.x + ' ' + places.y +  '</span>'
	    el.innerHTML = itemStr;
	    el.className = 'item';

	    return el;
	}
	
	static displayPagination(pagination) {
		const paginationEl = document.getElementById('pagination');
		const fragment = document.createDocumentFragment();
		let i;
		
		while(paginationEl.hasChildNodes()) {
			paginationEl.removeChild(paginationEl.lastChild);
		}
		
		for(i=1; i<=pagination.last; i++) {
			const el = document.createElement('a');
			el.href = '#';
			el.innerHTML = i;
			
			if(i === pagination.current) {
				el.className = 'on';
			} else {
				el.onclick = (function(i){
					return function() {
						pagination.gotoPage(i);
					}
				})(i);
			}
			
			fragment.appendChild(el);
		}
		
		paginationEl.appendChild(fragment);
	}
	
	static removeAllChildNodes(el) {
		while (el.hasChildNodes()) {
			el.removeChild(el.lastChild);
		}
	}
}



