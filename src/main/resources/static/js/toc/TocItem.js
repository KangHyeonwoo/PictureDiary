export default class TocItem {
	#itemDiv;
	#context_menu = {
        rename : {
            type : 'all',
            label : '이름 변경',
            onClick : function(pictureObj) {
				
            }
        },
        remove : {
            type : 'all',
            label : '파일 삭제',
            onClick : function(pictureObj) {
				kakao.maps.event.trigger(Toc.Map, 'toc-contextmenu-picture-remove', pictureObj);
            }
        },
        addGeometry : {
            type : 'noGeometry',
            label : '좌표 추가',
            onClick : function(pictureObj) {
				//picture.addGeometry(pictureObj);
				kakao.maps.event.trigger(Toc.Map, 'toc-contextmenu-picture-addGeometry', pictureObj);
            }
        },
		get : function(hasGeometry) {
			const type = (hasGeometry ? 'geometry' : 'noGeometry');
			const types = ['all', type];
			
			return Object.keys(this)
				.filter(key => typeof this[key] == 'object')
				.filter(key => types.includes(this[key]['type']))
				.map(key => this[key]);
		}
    }

	constructor(pictureObj) {
		const item = this.#rendererItem(pictureObj);
		this.#itemDiv = item;
	}
	
	
	//ContextMenu 삭제
	static removeContextMenu() {
		const prevCtxMenu = document.getElementsByClassName('context-menu');
		for(let i=0; i<prevCtxMenu.length; i++) {
			prevCtxMenu[i].remove();
		}
	}
	
	get itemDiv() {
		return this.#itemDiv;
	}
	
	#rendererItem(pictureObj) {
		const that = this;
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
		//TOC Item 제목 클릭 이벤트
		titlePTag.addEventListener('click', () => {
			if(pictureObj.marker) {
				//지도를 마커로 이동시키기
				pictureObj.marker.moveCenter();
			}
		});
		const addressPTag = document.createElement('p');
			  addressPTag.innerText = pictureObj.refineAddress;
		itemInfoDiv.appendChild(addressPTag);
			
		const timePTag = document.createElement('p');
			  timePTag.innerText = pictureObj.refinePictureDate;
		itemInfoDiv.appendChild(timePTag);
		
		const itemButtonDiv = document.createElement('div');
			  itemButtonDiv.classList.add('item-button');
		
		const moreButton = document.createElement('button');
		moreButton.classList.add('btn-more');
		moreButton.addEventListener('click', event => {
			event.stopPropagation();
			TocItem.removeContextMenu();
			that.#addContextMenu(event, pictureObj);
		})
		
		itemButtonDiv.appendChild(moreButton);
		
		itemDiv.appendChild(itemInfoDiv);
		itemDiv.appendChild(itemButtonDiv);
		
		return itemDiv;
	}
	
	//ContextMenu 추가하기
	#addContextMenu(event, pictureObj) {
		const ctxMenuId = 'toc_context';
	    const ctxMenu = document.createElement('div');
		
		ctxMenu.id = ctxMenuId;
		ctxMenu.className = 'context-menu';
		ctxMenu.style.top = event.pageY+'px';
		ctxMenu.style.left = event.pageX+'px';
		
		ctxMenu.appendChild(this.#renderContextMenuList(pictureObj));
		
		document.body.appendChild(ctxMenu);
	}

	//ContextMenu 그리기
	#renderContextMenuList(pictureObj) {
		const ctxMenuList = document.createElement('ul');
	    ctxMenuList.className = 'context-ul'
		
		this.#context_menu.get(pictureObj.hasGeometry).forEach(function(item){
			const ctxMenuItem = document.createElement('li');
			const ctxMenuItem_a = document.createElement('a');
			const ctxMenuItem_a_text = document.createTextNode(item.label);
	
			if( item.onClick ){
				ctxMenuItem.addEventListener( 'click', function(){
					item.onClick(pictureObj);
				}, false);
			}

			ctxMenuItem_a.appendChild( ctxMenuItem_a_text );
			ctxMenuItem.appendChild( ctxMenuItem_a );
			ctxMenuList.appendChild( ctxMenuItem );
		});
	        
		return ctxMenuList;
	}

}