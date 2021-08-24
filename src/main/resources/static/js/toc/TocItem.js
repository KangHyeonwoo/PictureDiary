export default class TocItem {
	#itemDiv;

	constructor(pictureObj) {
		const item = this.#rendererItem(pictureObj);
		this.#itemDiv = item;
	}
	
	get itemDiv() {
		return this.#itemDiv;
	}
	
	#rendererItem(pictureObj) {
		const itemDiv = document.createElement('div');
			  itemDiv.classList.add('item');
		
		const itemInfoDiv = document.createElement('div');
			  itemInfoDiv.classList.add('item-info');
		
		const picture = document.createElement('img');
			  picture.classList.add('picture');
			  picture.src = `/picture/images/${pictureObj.pictureOriginName}.${pictureObj.extension}`;
		itemDiv.appendChild(picture);
		
		const titlePTag = document.createElement('p');
			  titlePTag.classList.add('title');
			  titlePTag.innerText = (pictureObj.pictureName ? pictureObj.pictureName : pictureObj.pictureOriginName);
		itemInfoDiv.appendChild(titlePTag);
		//TOC Item 제목 클릭 이벤트
		titlePTag.addEventListener('click', () => {
			if(pictureObj.marker) {
				//지도를 마커로 이동시키기
				pictureObj.marker.moveCenter();
			}
		});
		const addressPTag = document.createElement('p');
			  addressPTag.innerText = pictureObj.refineAddress;
		itemInfoDiv.appendChild(addressPTag);
			
		const timePTag = document.createElement('p');
			  timePTag.innerText = pictureObj.refinePictureDate;
		itemInfoDiv.appendChild(timePTag);
		
		itemDiv.appendChild(itemInfoDiv);
		
		return itemDiv;
	}
}