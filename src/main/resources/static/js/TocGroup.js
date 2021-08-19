
export default class TocGroup {
	#type;
	#key;
	#titleDiv;
	#count;
	#itemList = [];
	
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
		this.#itemList.push(pictureObj);
		this.#count = this.#itemList.length;
	}	
	
	/** Public Method */
	
	get key() {
		return this.#key;
	}
	
	get titleDiv() {
		return this.#titleDiv;
	}
	
	/** Private Method */
	
	//TOC Group 그리기
	#rendererTocGroup(type, key) {
		const that = this;
		const tocGroup = document.getElementById(`toc.${type}`);
		const groupMap = (type === 'region' ? this.#regionGroup : this.#timeGroup);
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
		itemGroup.appendChild(countDiv);
		
		tocGroup.appendChild(itemGroup);
		
		return itemGroup
	}
}