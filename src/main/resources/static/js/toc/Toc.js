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
	}
	
	/* Public Methods */
	
	//TOC 객체 그리기
	addContent(pictureObj) {
		this.#drawContentInAllToc(pictureObj);			//전체
		this.#drawContentInRegionToc(pictureObj);		//지역별
		this.#drawContentInTimeToc(pictureObj);			//시간별
	}
	
	removeContent() {
		
	}	
	
	//검색 결과 TOC 그리기 + 마커 만들기

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
	
	//지역별 TOC Content 그리기 (그룹)
	#drawContentInRegionToc(pictureObj) {
		const type = 'region';
		const key = this.#extractRegionGroupKey(pictureObj);
		const collection = this.#regionGroupList.find(regionGroup => regionGroup.key === key);
		if(!collection) {
			const regionGroup = new TocGroup(type, key, pictureObj);
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
	
	//시간별 TOC 그룹 그리기
	#drawContentInTimeToc(pictureObj) {
		const type = 'time';
		const key = this.#extractTimeGroupKey(pictureObj);
		const collection = this.#timeGroupList.find(timeGroup=> timeGroup.key === key);
		if(!collection) {
			const timeGroup = new TocGroup(type, key, pictureObj);
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
}