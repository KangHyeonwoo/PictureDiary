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

//TOC 객체 불러오기
function tocLoad() {
	//1. DB에서 목록 조회
	HttpRequest.get('/pictures')
		//2. 좌표데이터 있는 데이터만 추출
		.then(pictureList => pictureList.filter(pictureObj => pictureObj.hasGeometry))
		//3. 루프 돌면서 주소 검색
		.then(pictureList => {
			(async () => {
				//참조 URL : https://cloudnweb.dev/2019/07/promises-inside-a-loop-javascript-es6/
				const result = await Promise.all(
					pictureList.map(pictureObj => {
						return Address.searchDetailLocation(pictureObj.latitude, pictureObj.longitude)
							.then(address => {
								pictureObj.address = address.address.address_name;
								return pictureObj;
							})
							.catch(() => {
								pictureObj.address = '-'
								return pictureObj;
							})
							
				}))
				
				return result;
			})();
			
			return pictureList;
		})
		//4. 목록에 추가
		.then(pictureList => console.log(pictureList));
}