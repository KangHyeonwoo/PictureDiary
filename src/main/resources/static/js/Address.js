export default class Address {
	static #ps = new kakao.maps.services.Places();
	
	static search(keyword) {
		
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
						reject('검색 결과가 존재하지 않습니다.');
					} else if (status === kakao.maps.services.Status.ERROR) {
						reject('검색 결과 중 오류가 발생했습니다.');
					}
				});
			}
		);
	}
}



