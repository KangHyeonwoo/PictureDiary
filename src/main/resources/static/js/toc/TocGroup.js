import TocItem from './TocItem.js';

export default class TocGroup {
	#key;
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
	
	get key() {
		return this.#key;
	}
	
	get pictureObjList() {
		return this.#pictureObjList;
	}
	
	/** Private Method */
	
	//TOC Group 그리기
	#rendererTocGroup(type, key) {
		const that = this;
		const tocGroup = document.getElementById(`toc.${type}`);
		const itemGroup = document.createElement('div');
			  itemGroup.classList.add('item-group');
	
		const img = document.createElement('span');
			  img.classList.add('info-folder');
		itemGroup.appendChild(img);
		
		const titleDiv = document.createElement('p');
		titleDiv.classList.add('title');
		titleDiv.innerText = key;
		titleDiv.addEventListener('click', () => {
			that.#tocGroupClickEvent(type)
		})
		
		itemGroup.appendChild(titleDiv);
		
		const countDiv = document.createElement('p');
			  countDiv.classList.add('count');
			  countDiv.innerText = `개수 : 1개`;
		this.#countText = countDiv;
		itemGroup.appendChild(countDiv);
		
		tocGroup.appendChild(itemGroup);
	}
	
	//TOC 그룹 클릭 이벤트 > TOC 그룹 내 리스트 그리기
	#tocGroupClickEvent(type) {
		const tocGroup = document.getElementById(`toc.${type}`);
		tocGroup.classList.add('hidden');
		
		const groupItems = document.getElementById(`toc.${type}.items`);
		const childrens = groupItems.getElementsByClassName('item');
		
		//기존객체들지우기
		Array.from(childrens).forEach(children => {
			groupItems.removeChild(children);
		})
		
		this.#pictureObjList
			.map(item => {
				const tocItem = new TocItem(item);
				return tocItem.itemDiv
			})
			.forEach(itemDiv => groupItems.appendChild(itemDiv));
		
		groupItems.classList.remove('hidden');
	}
}