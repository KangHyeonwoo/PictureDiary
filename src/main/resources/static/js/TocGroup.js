
export default class TocGroup {
	#type;
	#key;
	#titleDiv;
	#countText;
	#pictureObjList = [];
	
	constructor(type, key, pictureObj) {
		const tocRegion = document.getElementById('toc.region');
		const tocRegionItems = document.getElementById('toc.region.items');
		const tocTime = document.getElementById('toc.time');
		const tocTimeItems = document.getElementById('toc.time.items');
		
		//그룹 > 뒤로가기 버튼 클릭 이벤트
		const goRegionGroupButton = document.getElementById('goRegionGroupButton');
		goRegionGroupButton.addEventListener('click', () => {
			tocRegion.classList.remove('hidden')
			tocRegionItems.classList.add('hidden')
		});
		
		const goTimeGroupButton = document.getElementById('goTimeGroupButton');
		goTimeGroupButton.addEventListener('click', () => {
			tocTime.classList.remove('hidden')
			tocTimeItems.classList.add('hidden')
		});
		
		this.#rendererTocGroup(type, key);
		
		this.#type = type;
		this.#key = key;
		this.#pictureObjList.push(pictureObj);
	}	
	
	/** Public Method */
	
	addItem(pictureObj) {
		this.#pictureObjList.push(pictureObj);
		this.#countText.innerText = `개수 : ${this.#pictureObjList.length}개`;
	}
	
	hasItem(pictureObj) {
		const length = this.#pictureObjList
			.filter(obj => obj.pictureId === pictureObj.pictureId)
			.length;
		
		return length > 0
	}
	
	addTitleClickEvent(fnCallback) {
		const that = this;
		that.#titleDiv.addEventListener('click', () => {
			fnCallback(that.#pictureObjList);
		})
	}
	
	get key() {
		return this.#key;
	}
	
	get pictureObjList() {
		return this.#pictureObjList;
	}
	
	/** Private Method */
	
	//TOC Group 그리기
	#rendererTocGroup(type, key) {
		const tocGroup = document.getElementById(`toc.${type}`);
		const itemGroup = document.createElement('div');
			  itemGroup.classList.add('item-group');
	
		const img = document.createElement('span');
			  img.classList.add('info-folder');
		itemGroup.appendChild(img);
		
		const titleDiv = document.createElement('p');
		titleDiv.classList.add('title');
		titleDiv.innerText = key;
		this.#titleDiv = titleDiv;
		
		itemGroup.appendChild(titleDiv);
		
		const countDiv = document.createElement('p');
			  countDiv.classList.add('count');
			  countDiv.innerText = `개수 : 1개`;
		this.#countText = countDiv;
		itemGroup.appendChild(countDiv);
		
		tocGroup.appendChild(itemGroup);
	}
}