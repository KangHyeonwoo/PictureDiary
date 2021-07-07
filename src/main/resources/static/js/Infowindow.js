export default class Infowindow {
	#type;
	#map;
	#marker;
	#pictureObj;
	#infowindow;
	
	constructor(type, map, marker, pictureObj) {
		this.#type = type;
		this.#map = map;
		this.#marker = marker
		this.#pictureObj = pictureObj;

		const position = new kakao.maps.LatLng(pictureObj.latitude, pictureObj.longitude);
        this.#infowindow = new kakao.maps.InfoWindow({
            position : position,
            content : this.#makeContents(pictureObj),
            removable : true
        });
	}
	
	show() {
		this.#infowindow.open(this.#map, this.#marker);
	}
	
	close() {
		this.#infowindow.close();
	}
	
	#makeContents() {
		const divInfowindow = document.createElement('div');
			  divInfowindow.className = 'infowindow';
		
		const table = document.createElement('table');
		const tbody = document.createElement('tbody');
		const trTitle = document.createElement('tr');
		const tdPictureName = document.createElement('td');
			  tdPictureName.innerText = (this.#pictureObj.pictureName ? this.#pictureObj.pictureName : 
										this.#pictureObj.pictureOriginName);
			  tdPictureName.className = "title";
		const trImg = document.createElement('tr');
		const tdImg = document.createElement('td');
		const img = document.createElement('img');
			  //임시. 바꿔야함
			  img.src = `/picture/images/${this.#pictureObj.pictureOriginName}.${this.#pictureObj.extension}`;
			  img.className = 'picture';
		const trDate = document.createElement('tr');
		const tdDate = document.createElement('td');
			  tdDate.innerText = this.#pictureObj.pictureDate;
		
		divInfowindow.appendChild(table);
		table.appendChild(tbody);
		tbody.appendChild(trTitle);
		trTitle.appendChild(tdPictureName);
		tbody.appendChild(trImg);
		trImg.appendChild(tdImg);
		tdImg.appendChild(img);
		tbody.appendChild(trDate);
		trDate.appendChild(tdDate);
		
		const divButtonGroup = document.createElement('div');
		divButtonGroup.style.width = '180px';
		divButtonGroup.className = 'btn-group';
		
		if(this.#type === 'temp') {
			const addMarkerButton = document.createElement('button');
			addMarkerButton.className = 'basic w80';
			addMarkerButton.innerText = '확인';
			addMarkerButton.addEventListener('click', e => {
				console.log('addMarkerButton click')
			})
			const addMarkerCancelButton = document.createElement('button');
			addMarkerCancelButton.className = 'cancel w80';
			addMarkerCancelButton.innerText = '취소';
			addMarkerCancelButton.addEventListener('click', e => {
				console.log('addMarkerCancelButton click')
			})
			
			divButtonGroup.appendChild(addMarkerButton);
			divButtonGroup.appendChild(addMarkerCancelButton);
		//default
		} else {
			const markerMoveButton = document.createElement('button');
			markerMoveButton.className = 'basic w80';
			markerMoveButton.innerText = '이동';
			markerMoveButton.addEventListener('click', e => {
				console.log('markerMoveButton click')
			})
			
			const markerDeleteButton = document.createElement('button');
			markerDeleteButton.className = 'cancel w80';
			markerDeleteButton.innerText = '삭제';
			markerDeleteButton.addEventListener('click', e => {
				console.log('markerDeleteButton click')
			})
			
			divButtonGroup.appendChild(markerMoveButton);
			divButtonGroup.appendChild(markerDeleteButton);
		}
		
		tbody.appendChild(divButtonGroup);
		
		return divInfowindow;
	}
}