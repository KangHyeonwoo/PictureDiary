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
			    }
				
				Address.#ps.keywordSearch(keyword, function(data, status, pagination) {
					if(status === kakao.maps.services.Status.OK) {
						console.log(pagination);
						
						resolve(data);
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
	
	
}



