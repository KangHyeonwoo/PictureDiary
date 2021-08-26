export default class Address {
	static #ps = new kakao.maps.services.Places();
	static #geocoder = new kakao.maps.services.Geocoder();
	
	static #errorMessage = {
		EMPTY_KEYWORD : '키워드를 입력해주세요',
		ZERO_RESULT : '검색 결과가 존재하지 않습니다.',
		ERROR : '검색 결과 중 오류가 발생했습니다.'
	}
	
	static searchKeyword(keyword, page) {
		const i = (typeof page === 'undefined' ? 1 : page);
		const searchOptions = {
			'page' : i
		};
		return new Promise(
			function(resolve, reject) {
				if (!keyword.replace(/^\s+|\s+$/g, '')) {
			        reject(Address.#errorMessage.EMPTY_KEYWORD);
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
				}, searchOptions);
			}
		);
	}
	
	//행정동 주소 조회
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
	
	//법정동 상세 주소 조회
	static searchDetailLocation(lat, lng) {
		return new Promise(
			function(resolve, reject) {
				Address.#geocoder.coord2Address(lng, lat, function(result, status){
					if (status === kakao.maps.services.Status.OK) {
						resolve(result[0]);
				    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
						reject(Address.#errorMessage.ZERO_RESULT);
					} else if (status === kakao.maps.services.Status.ERROR) {
						reject(Address.#errorMessage.ERROR);
					}
				});
			}
		)
	}
	
	//지도 중심위치의 주소정보 조회
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
}



