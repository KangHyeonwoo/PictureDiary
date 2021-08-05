import HttpRequest from './HttpRequest.js';
import Address from './Address.js';

const tocHeader = document.getElementById('toc.header');
const menus = tocHeader.getElementsByTagName('li');

window.onload = function() {
	
	//TOC 헤더에 있는 버튼 이벤트 부여
	Array.from(menus).forEach(menu => {
		menu.addEventListener('click', headerToggle)
		menu.addEventListener('click', changeTocBody)
	});
	
	HttpRequest.get('/pictures')
		.then(pictureList => {
			const resultPictureList = pictureList
				.filter(pictureObj => pictureObj.hasGeometry)
				.map(pictureObj => {
					const a = await Address.searchDetailLocation(pictureObj.latitude , pictureObj.longitude)
					console.log(a);
					
					return pictureObj;
				});
			console.log(resultPictureList);
			
			return resultPictureList;
		})
		.then(pictureList => {
			console.log(pictureList);
		})
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
		  titlePTag.innerText = '';
	
	const addressPTag = document.createElement('p');
		  addressPTag.classList.add('address');
		  addressPTag.innerText = '';
	
	const timePTag = document.createElement('p');
		  timePTag.classList.add('time');
		  timePTag.innerText = '';
	
	const itemButtonDiv = document.createElement('div');
		  itemButtonDiv.classList.add('item-button');
	
	const moreButton = document.createElement('button');
	moreButton.classList.add('btn-more');
	moreButton.addEventListener('click', event => {
		console.log(event.currentTarget);
	})
	
	itemInfoDiv.appendChild(titlePtag);
	itemInfoDiv.appendChild(addressPTag);
	itemInfoDiv.appendChild(timePTag);
	
	itemButtonDiv.appendChild(moreButton);
	
	itemDiv.appendChild(itemInfoDiv);
	itemDiv.appendChild(itemButtonDiv);
	
	return itemDiv;
}