const tocHeader = document.getElementById('toc.header');
const menus = tocHeader.getElementsByTagName('li');

Array.from(menus).forEach(menu => {
	menu.addEventListener('click', headerToggle)
	menu.addEventListener('click', changeTocBody)
});

//TOC 버튼 클릭할 때 버튼 CSS 변경하기
function headerToggle(event) {
	Array.from(menus).forEach(menu => {
		menu.classList.remove('active');
	})
	
	const li = event.currentTarget;
	li.classList.add('active');
}

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
		
		if(hiddenContains && tocBody === targetTocBody) {
			tocBody.classList.remove('hidden');
		}
	})
}