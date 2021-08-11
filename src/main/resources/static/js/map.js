
export default class Map {
	
	#options = {
		divId : 'map',
	    center : {
	        lat : 37.445009887950526,
	        lng : 126.9529891719831,
	    },
	    level : 9,
	}
	
	constructor() {
		this.#on();
	}
	
	/* Public Methods */
	
	
	/* Private Methods */
	
	#on() {
		const container = document.getElementById(this.#options.divId);

	    const options = {
	        center: new kakao.maps.LatLng(this.#options.center.lat, this.#options.center.lng),
	        level: this.#options.level,
	    };
		
	    map.obj = new kakao.maps.Map(container, options);
		kakao.maps.event.addListener(map.obj, 'click', mouseEvent => {
			const latlng = mouseEvent.latLng;
			map.geometry.latitude = latlng.getLat();
			map.geometry.longitude = latlng.getLng();
		});
	}
}