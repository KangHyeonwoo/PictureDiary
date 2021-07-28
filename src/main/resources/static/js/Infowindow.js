export default class Infowindow {
	#type;
	#map;
	#marker;
	#pictureObj;
	#infowindow;
	
	//infowindow body
	#title;
	#image;
	#date;
	#divButtonGroup;
	
	constructor(type, map, marker, pictureObj) {
		this.#type = type;
		this.#map = map;
		this.#marker = marker;
		this.#pictureObj = pictureObj;

		const position = new kakao.maps.LatLng(pictureObj.latitude, pictureObj.longitude);
        this.#infowindow = new kakao.maps.InfoWindow({
            position : position,
            content : this.#makeContentsHtml(pictureObj),
            removable : type != 'temp'
        });
	}
	
	show() {
		//set data in infowindow
		this.#title.innerText = (this.#pictureObj.pictureName ? this.#pictureObj.pictureName : this.#pictureObj.pictureOriginName);
		this.#image.src = `/picture/images/${this.#pictureObj.pictureOriginName}.${this.#pictureObj.extension}`;
		this.#date.innerText = this.#pictureObj.refinePictureDate;
		
		//show
		this.#infowindow.open(this.#map, this.#marker.marker);
	}
	
	close() {
		this.#infowindow.close();
	}
	
	setButton(buttonObj) {
		const buttonName = buttonObj.name;
		let className = (buttonObj.location === 'right' ? 'cancel' : 'basic');
			className += ' w80';
		
		const button = document.createElement('button');
		button.className = className;
		button.innerText = buttonName;
		button.addEventListener('click', buttonObj.onclickEvent)
		
		this.#divButtonGroup.appendChild(button);
	}
	
	changeTitle(title) {
		this.#title.innerText = title;
		this.#pictureObj.pictureName = title;
	}
	
	#makeContentsHtml() {
		const divInfowindow = document.createElement('div');
			  divInfowindow.className = 'infowindow';
		
		const table = document.createElement('table');
		const tbody = document.createElement('tbody');
		const trTitle = document.createElement('tr');
		const tdPictureName = document.createElement('td');
			  tdPictureName.className = "title";
		const trImg = document.createElement('tr');
		const tdImg = document.createElement('td');
		const img = document.createElement('img');
			  img.className = 'picture';
		const trDate = document.createElement('tr');
		const tdDate = document.createElement('td');
		
		this.#title = tdPictureName;
		this.#image = img;
		this.#date = tdDate;
		
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
		this.#divButtonGroup = divButtonGroup;

		tbody.appendChild(divButtonGroup);
		
		return divInfowindow;
	}
}