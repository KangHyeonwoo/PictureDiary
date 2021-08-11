export default class Toc {

	constructor() {
		const tocHeader = document.getElementById('toc.header');
		const menus = tocHeader.getElementsByTagName('li');
		const tocRegion = document.getElementById('toc.region');
		const tocRegionItems = document.getElementById('toc.region.items');
		const tocTime = document.getElementById('toc.time');
		const tocTimeItems = document.getElementById('toc.time.items');
		
		
		//TOC 헤더에 있는 버튼 이벤트 부여
		Array.from(menus).forEach(menu => {
			menu.addEventListener('click', event => this.#headerToggle(event, menus))
			menu.addEventListener('click', event => this.#changeTocBody(event, menus))
		});
		
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
		
		//주소검색 텍스트파일 이벤트
		const addressSearchText = document.getElementById('address-search-text');
		addressSearchText.addEventListener('keyup', event => {
			if(event.code === 'Enter') {
				event.preventDefault();
				picture.searchAddress();
			}
		})
	}
	
	/* Public Methods */
	
	//TOC 객체 그리기
	setContents(pictureList) {
		this.#drawAllTocBodyList(pictureList)			//전체
		this.#drawUnregistTocBodyList(pictureList)		//미등록
		this.#drawRegionTocBodyGroupList(pictureList)	//지역별
		this.#drawTimeTocBodyGroupList(pictureList)		//시간별
	}
	
	add() {
		
	}
	
	remove() {
		
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
	
	//Item Div 그리기
	#getTocItem(pictureObj) {
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
		
		const addressPTag = document.createElement('p');
			  addressPTag.innerText = pictureObj.address;
		itemInfoDiv.appendChild(addressPTag);
			
		const timePTag = document.createElement('p');
			  timePTag.innerText = pictureObj.refinePictureDate;
		itemInfoDiv.appendChild(timePTag);
		
		const itemButtonDiv = document.createElement('div');
			  itemButtonDiv.classList.add('item-button');
		
		const moreButton = document.createElement('button');
		moreButton.classList.add('btn-more');
		moreButton.addEventListener('click', event => {
			console.log(event.currentTarget);
		})
		
		itemButtonDiv.appendChild(moreButton);
		
		itemDiv.appendChild(itemInfoDiv);
		itemDiv.appendChild(itemButtonDiv);
		
		return itemDiv;
	}
	
	//Item Group Div 그리기
	#getTocItemGroup(title, items, groupClickFunction) {
		const itemGroup = document.createElement('div');
			  itemGroup.classList.add('item-group');
	
		const img = document.createElement('span');
			  img.classList.add('info-folder');
		itemGroup.appendChild(img);
		
		const titleDiv = document.createElement('p');
		titleDiv.classList.add('title');
		titleDiv.innerText = title;
		titleDiv.onclick = function(){
			groupClickFunction(items)
		}
		
		itemGroup.appendChild(titleDiv);
		
		const countDiv = document.createElement('p');
			  countDiv.classList.add('count');
			  countDiv.innerText = `개수 : ${items.length}개`;
		itemGroup.appendChild(countDiv);
		
		return itemGroup;
	}
	
	//TOC > 전체 목록 그리기
	#drawAllTocBodyList(pictureList) {
		const tocAll = document.getElementById('toc.all');
		
		pictureList
			.filter(pictureObj => pictureObj.hasGeometry)
			.map(pictureObj => this.#getTocItem(pictureObj))
			.forEach(itemDiv => tocAll.appendChild(itemDiv))
	}
	
	//TOC > 미등록 목록 그리기
	#drawUnregistTocBodyList(pictureList) {
		const tocUnregist = document.getElementById('toc.unregist');
		
		pictureList
			.filter(pictureObj => !pictureObj.hasGeometry)
			.map(pictureObj => this.#getTocItem(pictureObj))
			.forEach(itemDiv => tocUnregist.appendChild(itemDiv));
	}
	
	//TOC > 지역별 목록 그리기
	#drawRegionTocBodyGroupList(pictureList) {
		const tocRegion = document.getElementById('toc.region');
		
		const regionGroup = new Map();
		pictureList.forEach(pictureObj => {
			let key = '';
			
			if(pictureObj.address === null ) {
				key = pictureObj.refineAddress;
			} else {
				const address = pictureObj.refineAddress.split(' ');
				key = `${address[0]} ${address[1]}`;
			}
			
			const collection = regionGroup.get(key);
			
			if(!collection) {
				regionGroup.set(key, [pictureObj]);
			} else {
				collection.push(pictureObj);
			}
		})
		
		//TOC에 그리기
		regionGroup.forEach((value, key) => {
			const that = this;
			const item = this.#getTocItemGroup(key, value, function(callbackData){
				that.#drawRegionTocBodyList(callbackData);
			})
			tocRegion.appendChild(item);
		})
	}
	
	#drawRegionTocBodyList(items) {
		const tocRegion = document.getElementById('toc.region');
		tocRegion.classList.add('hidden');
		
		const tocRegionItems = document.getElementById('toc.region.items');
		const childrens = tocRegionItems.getElementsByClassName('item');
		
		//기존객체들지우기
		Array.from(childrens).forEach(children => {
			tocRegionItems.removeChild(children);
		})
		
		items
			.map(item => this.#getTocItem(item))
			.forEach(itemDiv => tocRegionItems.appendChild(itemDiv));
		
		tocRegionItems.classList.remove('hidden');
	}
	
	//TOC > 시간별 목록 그리기
	#drawTimeTocBodyGroupList(pictureList) {
		const tocTime = document.getElementById('toc.time');
		
		//날짜별로 그룹핑
		const timeGroup = new Map();
		pictureList.forEach(pictureObj => {
			const dates = pictureObj.refinePictureDate.split('-');
			const key = dates.length > 1 ? `${dates[0]}년 ${dates[1]}월` : dates[0];
			
			const collection = timeGroup.get(key);
			
			if(!collection) {
				timeGroup.set(key, [pictureObj]);
			} else {
				collection.push(pictureObj);
			}
		})
		
		//TOC에 그리기
		timeGroup.forEach((value, key) => {
			const that = this;
			const item = this.#getTocItemGroup(key, value, function(callbackData) {
				that.#drawTimeTocBodyList(callbackData);
			})
			tocTime.appendChild(item);
		})
	}
	
	#drawTimeTocBodyList(items) {
		const tocTime = document.getElementById('toc.time');
		tocTime.classList.add('hidden');
		
		const tocTimeItems = document.getElementById('toc.time.items');
		const childrens = tocTimeItems.getElementsByClassName('item');
		
		//기존객체들지우기
		Array.from(childrens).forEach(children => {
			tocTimeItems.removeChild(children);
		})
		
		items
			.map(item => this.#getTocItem(item))
			.forEach(itemDiv => tocTimeItems.appendChild(itemDiv));
		
		tocTimeItems.classList.remove('hidden');
	}

}