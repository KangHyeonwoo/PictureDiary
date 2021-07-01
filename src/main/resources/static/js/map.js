const toc = new Toc();
const map = {};
const picture = {};
const markerList = [];
picture.list = [];

map.options = {
    divId : 'map',
    center : {
        lat : 37.445009887950526,
        lng : 126.9529891719831,
    },
    level : 9,
}

map.init = function() {
    //1. map on
    this.on();

	//2. pictures draw;
	picture.getList(pictureList => {
		pictureList.forEach(pictureObj => {
			
			const contents = toc.add(pictureObj);
			
			if(pictureObj.hasGeometry) {
				const marker = new Marker(pictureObj, map.obj);
				marker.setMap();
				marker.leftClicked(responseMarker => {
					Marker.closeInfowindow(markerList);
					marker.openInfowindow();
				});
				markerList.push(marker);
				
				contents.addEventListener('click', function(event){
					map.obj.panTo(marker.position)
					
					Marker.closeInfowindow(markerList);
					marker.openInfowindow();
				})
			}
		})
	})
	
	//3. button setting
	const extractButton = document.getElementById('extract-button');
	extractButton.addEventListener('click', picture.extract)
}

map.on = function() {
    const container = document.getElementById(this.options.divId);

    const options = {
        center: new kakao.maps.LatLng(this.options.center.lat, this.options.center.lng),
        level: this.options.level,
    };

    map.obj = new kakao.maps.Map(container, options);
}



picture.extract = function() {
    const url = '/picture/extract';
    const data = {};

    Async.post(url, data, function(pictureList){
        pictureList.forEach(pictureObj => {
            picture.list.push(pictureObj);
	
			const contents = toc.add(pictureObj);
			
			if(pictureObj.hasGeometry) {
				const marker = new Marker(pictureObj, map.obj);
				marker.setMap();
				marker.leftClicked(responseMarker => {
					Marker.closeInfowindow(markerList);
					marker.openInfowindow();
				});
				markerList.push(marker);
				
				contents.addEventListener('click', function(event){
					map.obj.panTo(marker.position)
					
					Marker.closeInfowindow(markerList);
					marker.openInfowindow();
				})
			}
		})
    })
}


picture.findById = function(id) {
	
    return this.list.find(e => e.pictureId == id);
}

picture.getList = function(fnCallback) {
    const url = '/picture/list';
    const data = {};
    if(this.list.length > 0) {
        fnCallback(this.list);
    }

    Async.get(url, data, function(result) {
        result.forEach(pictureObj => picture.list.push(pictureObj));

        fnCallback(picture.list);
    })
}

picture.remove = function(pictureObj) {
	const data = {
		pictureId : pictureObj.pictureId,
	}
	const url = `/picture/${data.pictureId}/delete`;
	
	Async.post(url, data, function(result){
		//toc remove
		const contents = document.getElementById(pictureObj.tocId);
		contents.remove();
		
		//marker remove
		const marker = Marker.findByPictureId(pictureObj.pictureId, markerList);
		marker.remove();
		
		//markerlist pop
		const markerIndex = markerList.findIndex(e => e.pictureId === pictureObj.pictureId);
		markerList.splice(markerIndex, 1);
	})
}

picture.rename = function(pictureObj, name) {
	const data = {
		pictureId : pictureObj.pictureId,
		pictureName : name
	}
	const url = '/picture/rename';
	
	Async.post(url, data, function(result){
	    const pictureObj = picture.findById(result.pictureId);
	    pictureObj.pictureName = result.pictureName;

        Toc.closeRename(pictureObj);
	})
}

picture.addGeometry = function(pictureObj) {
    //alert(화면을 클릭해주세요.);
    kakao.maps.event.addListener(map.obj, 'click', function(mouseEvent) {
        const latlng = mouseEvent.latLng;

        pictureObj.latitude = latlng.getLat();
        pictureObj.longitude = latlng.getLng();

        const data = {
            pictureId : pictureObj.pictureId,
            latitude : pictureObj.latitude,
            longitude : pictureObj.longitude,
        }

        const url = '/picture/addGeometry';
        Async.post(url, data, function(resultPictureObj){
            //add marker
			const marker = new Marker(resultPictureObj, map.obj);
            marker.setMap();
            marker.leftClicked(responseMarker => {
                Marker.closeInfowindow(markerList);
                marker.openInfowindow();
            });
            markerList.push(marker);

			toc.remove(pictureObj);
			toc.add(resultPictureObj);
			
			const contents = document.getElementById(resultPictureObj.tocId);
            contents.addEventListener('click', function(event){
                map.obj.panTo(marker.position)

                Marker.closeInfowindow(markerList);
                marker.openInfowindow();
            })
			
			//picture.list에 기존 객체 삭제 후 새로 추가하기
			const idx = picture.list.findIndex(function(item){
				return item.pictureId === pictureObj.pictureId;
			});
			if (idx > -1) {
				picture.list.splice(idx, 1);
			}
			
			picture.list.push(resultPictureObj);
			
        })
    });
}

map.init();
