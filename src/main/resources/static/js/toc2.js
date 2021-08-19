import TocItem from './TocItem.js';
import TocGroup from './TocGroup.js';

export default class Toc {
	
	#regionGroupList = new Array();
	#timeGroupList   = new Array();
	
	constructor() {
		const tocHeader = document.getElementById('toc.header');
		const menus = tocHeader.getElementsByTagName('li');
		
		//TOC 헤더에 있는 버튼 이벤트 부여
		Array.from(menus).forEach(menu => {
			menu.addEventListener('click', event => this.#headerToggle(event, menus))
			menu.addEventListener('click', event => this.#changeTocBody(event, menus))
		});
		
		document.addEventListener('click', () => {
			TocItem.removeContextMenu();
		})
	}
	
	/* Public Methods */
	
	//TOC 객체 그리기
	addContent(pictureObj) {
		if(pictureObj.hasGeometry) {
			this.#drawContentInAllToc(pictureObj);		//전체
		} else {
			this.#drawContentInUnregisterToc(pictureObj);	//미등록
		}
		
		this.#drawContentInRegionToc(pictureObj);		//지역별
		this.#drawContentInTimeToc(pictureObj);			//시간별
	}

	removeContent() {
		
	}	
	
	/* Private Methods */
	
	//TOC 버튼 클릭할 때 버튼 CSS 변경하기
	#headerToggle(event, menus) {
		Array.from(menus).forEach(menu => {
			menu.classList.remove('active');
		})
		
		const li = event.currentTarget;
		li.classList.add('active');
	}
	
	//TOC 버튼 클릭할 때 BODY 변경하기
	#changeTocBody(event) {
		const menuId = event.currentTarget.id;
		const tocId = menuId.replace('menu', 'toc');
		const targetTocBody = document.getElementById(tocId);
		const tocBodies = document.getElementById('toc.body').children;
		
		Array.from(tocBodies).forEach(tocBody => {
			const hiddenContains = tocBody.classList.contains('hidden');
			
			if(!hiddenContains) {
				tocBody.classList.add('hidden');
			}
			
			if(tocBody === targetTocBody) {
				tocBody.classList.remove('hidden');
			}
		})
	}

	//전체 TOC Content 그리기
	#drawContentInAllToc(pictureObj) {
		const tocAll = document.getElementById('toc.all');
		const tocItem = new TocItem(pictureObj);
		
		tocAll.appendChild(tocItem.itemDiv);
	}
	
	//미등록 TOC Content 그리기
	#drawContentInUnregisterToc(pictureObj) {
		const tocUnregist = document.getElementById('toc.unregist');
		const tocItem = new TocItem(pictureObj);
		
		tocUnregist.appendChild(tocItem.itemDiv);
	}
	
	//지역별 TOC Content 그리기 (그룹)
	#drawContentInRegionToc(pictureObj) {
		/*
		const key = this.#extractRegionGroupKey(pictureObj);
		const collection = this.#regionGroup.get(key);
		
		if(!collection) {
			this.#regionGroup.set(key, [pictureObj]);
			this.#drawTocItemGroup('region', key)
		} else {
			collection.push(pictureObj);
		}
		
		this.#setItemCount('region', key)
		*/
		const key = this.#extractRegionGroupKey(pictureObj);
		const collection = this.#regionGroupList.find(regionGroup => regionGroup.key === key);
		if(!collection) {
			const regionGroup = new TocGroup('region', key, pictureObj);
			this.#regionGroupList.push(regionGroup);
		} else {
			collection.addItem(pictureObj);
		}
	}
	
	//지역별 TOC 그룹 Map객체의 키 조회
	#extractRegionGroupKey(pictureObj) {
		let key = '';
		
		if(pictureObj.address === null) {
			key = pictureObj.refineAddress;
		} else {
			const address = pictureObj.refineAddress.split(' ');
			key = `${address[0]} ${address[1]}`;
		}
		
		return key;
	}
	
	//시간별 TOC Content 그리기 (그룹)
	#drawContentInTimeToc(pictureObj) {
		const key = this.#extractTimeGroupKey(pictureObj);
		const collection = this.#timeGroupList.find(timeGroup=> timeGroup.key === key);
		if(!collection) {
			const timeGroup = new TocGroup('time', key, pictureObj);
			this.#timeGroupList.push(timeGroup);
		} else {
			collection.addItem(pictureObj);
		}
	}
	
	//시간별 TOC 그룹 Map객체의 키 조회
	#extractTimeGroupKey(pictureObj) {
		const dates = pictureObj.refinePictureDate.split('-');
		const key = dates.length > 1 ? `${dates[0]}년 ${dates[1]}월` : dates[0];
		
		return key;
	}
	//TOC Group 그리기
	/*
	#drawTocItemGroup(type, key) {
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
		titleDiv.onclick = function(){
			const items = groupMap.get(key);
			that.#drawTocItemsInGroup(type, items);
		}
		
		itemGroup.appendChild(titleDiv);
		
		const countDiv = document.createElement('p');
			  countDiv.classList.add('count');
			  countDiv.innerText = `개수 : 1개`;
		itemGroup.appendChild(countDiv);
		
		tocGroup.appendChild(itemGroup);
		
		return itemGroup
	}
	*/
	//TOC 그룹에 그룹 내 아이템 개수 추가하기
	//type : region / time
	/*
	#setItemCount(type, key) {
		const tocGroup = document.getElementById(`toc.${type}`);
		const groupItems = tocGroup.getElementsByClassName('item-group');
		
		for(let i=0; i<groupItems.length; i++) {
			if(groupItems[i].getElementsByClassName('title')[0].innerText === key) {
				const countEl = groupItems[i].getElementsByClassName('count')[0];
				const groupMap = (type === 'region' ? this.#regionGroup : this.#timeGroup);
				const count = groupMap.get(key).length;
				
				countEl.innerText = `개수 : ${count}개`;
				
				break;
			}
		}
	}
	*/
	//TOC 그룹 내 리스트 그리기
	#drawTocItemsInGroup(type, items) {
		const tocGroup = document.getElementById(`toc.${type}`);
		tocGroup.classList.add('hidden');
		
		const groupItems = document.getElementById(`toc.${type}.items`);
		const childrens = groupItems.getElementsByClassName('item');
		
		//기존객체들지우기
		Array.from(childrens).forEach(children => {
			groupItems.removeChild(children);
		})
		
		items
			.map(item => {
				const tocItem = new TocItem(item);
				return tocItem.itemDiv
			})
			.forEach(itemDiv => groupItems.appendChild(itemDiv));
		
		groupItems.classList.remove('hidden');
	}
}