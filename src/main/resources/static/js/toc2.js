const tocHeader = document.getElementById('toc.header');
const menus = tocHeader.getElementsByTagName('li');

Array.from(menus).forEach(menu => {
	menu.addEventListener('click', tocMenuClickCSS)
});

//TOC 버튼 클릭 CSS
function tocMenuClickCSS(event) {
	Array.from(menus).forEach(menu => {
		menu.classList.remove('active');
	})
	
	const li = event.currentTarget;
	li.classList.add('active');
}
