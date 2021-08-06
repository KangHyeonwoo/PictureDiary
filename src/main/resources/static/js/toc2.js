import HttpRequest from './HttpRequest.js';
import Address from './Address.js';

const tocHeader = document.getElementById('toc.header');
const menus = tocHeader.getElementsByTagName('li');

const tocAll = document.getElementById('toc.all');
const tocTime = document.getElementById('toc.time');

window.onload = function() {
	
	//TOC 헤더에 있는 버튼 이벤트 부여
	Array.from(menus).forEach(menu => {
		menu.addEventListener('click', headerToggle)
		menu.addEventListener('click', changeTocBody)
	});

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
function getTocAllItem(pictureObj) {
	const itemDiv = document.createElement('div');
		  itemDiv.classList.add('item');
	
	const itemInfoDiv = document.createElement('div');
		  itemInfoDiv.classList.add('item-info');
	
	const titlePTag = document.createElement('p');
		  titlePTag.classList.add('title');
		  titlePTag.innerText = (pictureObj.pictureName ? pictureObj.pictureName : pictureObj.pictureOriginName);
	itemInfoDiv.appendChild(titlePTag);
	
	const addressPTag = document.createElement('p');
		  addressPTag.classList.add('address');
		  addressPTag.innerText = pictureObj.address;
	itemInfoDiv.appendChild(addressPTag);
		
	const timePTag = document.createElement('p');
		  timePTag.classList.add('time');
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

function getTocDateItemGroup(title, count) {
	const itemGroup = document.createElement('div');
		  itemGroup.classList.add('item-group');

	const img = document.createElement('span');
		  img.classList.add('info-folder');
	itemGroup.appendChild(img);
	
	const titleDiv = document.createElement('p');
		  titleDiv.classList.add('title');
		  titleDiv.innerText = title;
	itemGroup.appendChild(titleDiv);
	
	const countDiv = document.createElement('p');
		  countDiv.classList.add('count');
		  countDiv.innerText = `개수 : ${count}개`;
	itemGroup.appendChild(countDiv);
	
	return itemGroup;
}

function getTocUnregistItem(pictureObj) {


}

//TOC 객체 불러오기
function tocLoad() {
	//1. DB에서 목록 조회
	HttpRequest.get('/pictures')
		//2. TOC 그리기
		.then(pictureList => {
			drawTocAllBodyList(pictureList)
			
			return pictureList;
		})
		.then(pictureList => {
			drawDateTocBodyList(pictureList);
		});
}

//TOC > 전체 목록 그리기
function drawTocAllBodyList(pictureList) {
	pictureList
		.filter(pictureObj => pictureObj.hasGeometry)
		.map(pictureObj => getTocAllItem(pictureObj))
		.forEach(itemDiv => tocAll.appendChild(itemDiv))
}

//TOC > 시간별 목록 그리기
function drawDateTocBodyList(pictureList) {
	//날짜별로 그룹핑
	const dateGroup = new Map();
	pictureList.forEach(pictureObj => {
		const dates = pictureObj.refinePictureDate.split('-');
		const key = dates.length > 1 ? `${dates[0]}년 ${dates[1]}월` : dates[0];
		
		const collection = dateGroup.get(key);
		
		if(!collection) {
			dateGroup.set(key, [pictureObj]);
		} else {
			collection.push(pictureObj);
		}
	})
	
	//TOC에 그리기
	dateGroup.forEach((value, key) => {
		const item = getTocDateItemGroup(key, value.length)
		tocTime.appendChild(item);
	})
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
		} catch (error) {
			
		}
	}
	
	return pictureList;
}