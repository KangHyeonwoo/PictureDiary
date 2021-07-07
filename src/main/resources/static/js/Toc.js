class Toc {
    #dataGroupId = 'data-group';
    #tempGroupId = 'temp-group';
    static #CONTEXT_MENU = {
        rename : {
            type : 'all',
            label : '이름 변경',
            onClick : function(pictureObj) {
				Toc.#renderRename(pictureObj);
            }
        },
        remove : {
            type : 'all',
            label : '파일 삭제',
            onClick : function(pictureObj) {
				picture.remove(pictureObj);
            }
        },
        addGeometry : {
            type : 'noGeometry',
            label : '좌표 추가',
            onClick : function(pictureObj) {
				picture.addGeometry(pictureObj);
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
	static #WORKING_PICTURE_OBJ = {};
	
    constructor() {
		document.addEventListener('click', Toc.#removeContextMenu, false);

        const groupTitles = document.getElementsByClassName('group-title');
        Array.from(groupTitles).forEach(title => {
            title.addEventListener('click', event => {
                if(title.nextElementSibling.classList.contains('off')) {
                    title.nextElementSibling.classList.remove('off');
                } else {
                    title.nextElementSibling.classList.add('off');
                }
            })
        })
    }


    add(pictureObj) {
		const dataGroup = document.getElementById(this.#dataGroupId);
	    const tempGroup = document.getElementById(this.#tempGroupId);
	
	    const li = document.createElement('li');
	    li.id = pictureObj.tocId;
		li.className = 'toc-object';
	    li.innerText = ((pictureObj.pictureName == '' || pictureObj.pictureName == null)? pictureObj.pictureOriginName : pictureObj.pictureName);
		
		li.addEventListener('contextmenu', function(event){
			event.preventDefault();
			Toc.#removeContextMenu();
			Toc.#addContextMenu(event ,pictureObj);
		})
		
		li.addEventListener('click', this.contentClickEventHandler);
		li.addEventListener('contextmenu', this.contentClickEventHandler);
		
		if(pictureObj.hasGeometry) {
			dataGroup.appendChild(li);
		} else {
			tempGroup.appendChild(li);
		}
		
		return li;
    }

    remove(pictureObj) {
		//TOC목록에서 제거(view)
		const content = document.getElementById(pictureObj.tocId);
		content.remove();

		return document.getElementById(pictureObj.tocId) == null;
    }

	contentClickEventHandler(event) {
		const li = event.currentTarget;
		
		const contents = document.getElementsByClassName('toc-object');
		Array.from(contents).forEach(content => {
            content.classList.remove('selected');
        })

		li.classList.add('selected');
	}

	static #addContextMenu(event, pictureObj) {
		const ctxMenuId = 'toc_context';
	    const ctxMenu = document.createElement('div');
		
		ctxMenu.id = ctxMenuId;
		ctxMenu.className = 'context-menu';
		ctxMenu.style.top = event.pageY+'px';
		ctxMenu.style.left = event.pageX+'px';
		
		ctxMenu.appendChild(Toc.#renderContextMenuList(pictureObj));
		
		document.body.appendChild(ctxMenu);
	}

	static #renderContextMenuList(pictureObj) {
		const ctxMenuList = document.createElement('ul');
	    ctxMenuList.className = 'context-ul'
		
		Toc.#CONTEXT_MENU.get(pictureObj.hasGeometry).forEach(function(item){
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

	static #removeContextMenu() {
		const prevCtxMenu = document.getElementsByClassName('context-menu');
		for(let i=0; i<prevCtxMenu.length; i++) {
			prevCtxMenu[i].remove();
		}
	}
	
	static #renderRename(pictureObj) {
		Toc.closeRename();
		
		Toc.#WORKING_PICTURE_OBJ = pictureObj;
		
		const contents = document.getElementById(pictureObj.tocId);
		
		const div = document.createElement('div');
		div.className = 'rename-div';
		const text = document.createElement('input');
		text.type = 'text';
		text.id = 'rename';
		text.addEventListener('keydown', function(e){
			if(e.key == 'Escape') {
				Toc.closeRename();
			}	
		}, false);
		
		const button = document.createElement('button');
		button.type = 'button';
		button.innerText = '확인';
		button.addEventListener('click', function(event){
			picture.rename(pictureObj, text.value);
		}, false)
		
		div.appendChild(text);
		div.appendChild(button);
		
		contents.innerHTML = '';
		contents.classList.remove('selected');
		
		contents.appendChild(div);
	}
	
	static closeRename(pictureObj) {
		const data = (pictureObj || Toc.#WORKING_PICTURE_OBJ);
		if(Object.keys(data).length === 0 && data.constructor === Object) {
			return;
		}
		
		const contents = document.getElementById(data.tocId);
		if(contents.firstElementChild != null) {
			contents.firstElementChild.remove();
			contents.innerText = (data.pictureName == null ? data.pictureOriginName : data.pictureName);
		}
	}
	
}


