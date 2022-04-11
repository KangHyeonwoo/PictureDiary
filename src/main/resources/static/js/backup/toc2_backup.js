import HttpRequest from './HttpRequest.js';
import Address from './Address.js';

const tocHeader = document.getElementById('toc.header');
const menus = tocHeader.getElementsByTagName('li');
const tocAll = document.getElementById('toc.all');
const tocUnregist = document.getElementById('toc.unregist');
const tocRegion = document.getElementById('toc.region');
const tocRegionItems = document.getElementById('toc.region.items');
const tocTime = document.getElementById('toc.time');
const tocTimeItems = document.getElementById('toc.time.items');



window.onload = function() {
	
	//TOC 헤더에 있는 버튼 이벤트 부여
	Array.from(menus).forEach(menu => {
		menu.addEventListener('click', headerToggle)
		menu.addEventListener('click', changeTocBody)
	});
	
	//그룹 > 뒤로가기 버튼 클릭 이벤트
	const goRegionGroupButton = document.getElementById('goRegionGroupButton');
	goRegionGroupButton.onclick = function() {
		tocRegionItems.classList.add('hidden');
		tocRegion.classList.remove('hidden');
	}
	
	const goTimeGroupButton = document.getElementById('goTimeGroupButton');
	goTimeGroupButton.onclick = function() {
		tocTimeItems.classList.add('hidden');
		tocTime.classList.remove('hidden');
	}
	
	//주소검색 텍스트파일 이벤트
	const addressSearchText = document.getElementById('address-search-text');
	
	addressSearchText.addEventListener('keyup', event => {
		if(event.code === 'Enter') {
			event.preventDefault();
			picture.searchAddress();
		}
	})
	
	
	//목록 불러오기
	tocLoad();
}

//TOC 버튼 클릭할 때 버튼 CSS 변경하기
function headerToggle(event) {
	Array.from(menus).forEach(menu => {
		menu.classList.remove('active');
	})
	
	const li = event.currentTarget;
	li.classList.add('active');
}

//TOC 버튼 클릭할 때 BODY 변경하기
function changeTocBody(event) {
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
function getTocItem(pictureObj) {
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
function getTocItemGroup(title, items, groupClickFunction) {
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

//TOC 객체 불러오기
function tocLoad() {
	//1. DB에서 목록 조회
	HttpRequest.get('/pictures')
		//2. TOC 그리기
		.then(pictureList => drawAllTocBodyList(pictureList))		//전체
		.then(pictureList => drawUnregistTocBodyList(pictureList))	//미등록
		.then(pictureList => drawRegionTocBodyGroupList(pictureList))	//지역별
		.then(pictureList => drawTimeTocBodyGroupList(pictureList))		//시간별
}

//TOC > 전체 목록 그리기
function drawAllTocBodyList(pictureList) {
	pictureList
		.filter(pictureObj => pictureObj.hasGeometry)
		.map(pictureObj => getTocItem(pictureObj))
		.forEach(itemDiv => tocAll.appendChild(itemDiv))
	
	return pictureList;
}

//TOC > 미등록 목록 그리기
function drawUnregistTocBodyList(pictureList) {
	pictureList
		.filter(pictureObj => !pictureObj.hasGeometry)
		.map(pictureObj => getTocItem(pictureObj))
		.forEach(itemDiv => tocUnregist.appendChild(itemDiv));
	
	return pictureList;
}

//TOC > 지역별 목록 그리기
function drawRegionTocBodyGroupList(pictureList) {
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
		const item = getTocItemGroup(key, value, drawRegionTocBodyList)
		tocRegion.appendChild(item);
	})
	
	return pictureList;
}

function drawRegionTocBodyList(items) {
	tocRegion.classList.add('hidden');
	const childrens = tocRegionItems.getElementsByClassName('item');
	
	//기존객체들지우기
	Array.from(childrens).forEach(children => {
		tocRegionItems.removeChild(children);
	})
	
	items
		.map(item => getTocItem(item))
		.forEach(itemDiv => tocRegionItems.appendChild(itemDiv));
	
	tocRegionItems.classList.remove('hidden');
}

//TOC > 시간별 목록 그리기
function drawTimeTocBodyGroupList(pictureList) {
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
		const item = getTocItemGroup(key, value, drawTimeTocBodyList)
		tocTime.appendChild(item);
	})
	
	return pictureList;
}

function drawTimeTocBodyList(items) {
	tocTime.classList.add('hidden');
	const childrens = tocTimeItems.getElementsByClassName('item');
	
	//기존객체들지우기
	Array.from(childrens).forEach(children => {
		tocTimeItems.removeChild(children);
	})
	
	items
		.map(item => getTocItem(item))
		.forEach(itemDiv => tocTimeItems.appendChild(itemDiv));
	
	tocTimeItems.classList.remove('hidden');
}

//파일 추출
function extract() {
	HttpRequest.post('/extract')
		.then(pictureList => pictureList.filter(pictureObj => pictureObj.hasGeometry))
		.then(getAddressList)
		.then(pictureList => HttpRequest.put('/pictures/addresses', pictureList))
		.then(result => console.log(result));
}

//사진리스트 주소 조회
async function getAddressList(pictureList) {
	for(let i = 0; i<pictureList.length; i++) {
		const pictureObj = pictureList[i];
		
		try {
			const location = await Address.searchDetailLocation(pictureObj.latitude, pictureObj.longitude);
			pictureObj.address = location.address.address_name;
		} catch (error) {}
	}
	
	return pictureList;
}