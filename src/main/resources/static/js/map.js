const async = new Async();
const map = {};
const picture = {};
const toc = {};

toc.group = {
    data : 'data-group',
    temp : 'temp-group'
}
map.options = {
    divId : 'map',
    center : {
        lat : 37.445009887950526,
        lng : 126.9529891719831,
    },
    level : 9,
}

map.init = function() {
    //1. map on
    this.on();

	picture.getList(pictureList => {
		pictureList.forEach(pictureObj => {
			if(pictureObj.hasGeometry) {
	            picture.setMap(pictureObj);
	        }
	
	        toc.add(pictureObj);
		})
	})
}

map.on = function() {
    const container = document.getElementById(this.options.divId);

    const options = {
        center: new kakao.maps.LatLng(this.options.center.lat, this.options.center.lng),
        level: this.options.level,
    };

    map.obj = new kakao.maps.Map(container, options);
}

picture.list = [];

picture.extract = function() {
    const url = '/picture/extract';
    const data = {};

    async.post(url, data, function(result){
        debugger;
    })
}

picture.setMap = function(pictureObj) {
    pictureObj.marker.setMap(map.obj);

	kakao.maps.event.addListener(pictureObj.marker, 'click', function() {
		picture.selectMarker(pictureObj);
	});
	
	kakao.maps.event.addListener(pictureObj.marker, 'rightclick', function(event) {
		debugger;
	});
}

picture.closeInfowindow = function() {
	picture.list
		.filter(data => data.hasGeometry)
		.forEach(data => {
			data.infowindow.close();
		})
}

picture.findById = function(id) {
	
    return this.list.find(e => e.pictureId == id);
}

picture.getList = function(fnCallback) {
    const url = '/picture/list';
    const data = {};
    if(this.list.length > 0) {
        fnCallback(this.list);
    }

    async.get(url, data, function(result) {
        result.forEach(pictureObj => {
            if(pictureObj.latitude != 0 && pictureObj.longitude != 0) {
				const content = picture.infowindowContent(pictureObj);
				const removeable = true;
				
				//position
                pictureObj.position = new kakao.maps.LatLng(pictureObj.latitude, pictureObj.longitude);

				//infowindow
                pictureObj.infowindow = new kakao.maps.InfoWindow({
                    position : pictureObj.position,
                    content : content,
                    removable : removeable
                });

				//marker
                pictureObj.marker = new kakao.maps.Marker({
                     position: pictureObj.position
                });
            }

			pictureObj.hasGeometry = (pictureObj.latitude != 0 && pictureObj.longitude != 0);
			
            picture.list.push(pictureObj);
        });

        fnCallback(picture.list);
    })
}

picture.infowindowContent = function(pictureObj) {
	const url = '/infowindow/'+pictureObj.pictureId;
	const data = {};
	
	const infowindowContent = async.syncHtml(url, data);
	
	return infowindowContent;
}

picture.selectMarker = function(pictureObj){
	picture.closeInfowindow();
	
	const pictureLocation = new kakao.maps.LatLng(pictureObj.latitude, pictureObj.longitude);    
	map.obj.panTo(pictureLocation);
	
	pictureObj.infowindow.open(map.obj, pictureObj.marker);
}

//TOC에 추가
toc.add = function(pictureObj) {
    const dataGroup = document.getElementById(toc.group.data);
    const tempGroup = document.getElementById(toc.group.temp);

    const li = document.createElement('li');
    li.id = (pictureObj.hasGeometry ? toc.group.data + '_' + pictureObj.pictureId
						 : toc.group.temp + '_' + pictureObj.pictureId);
	li.className = 'toc-object';
    li.innerText = ((pictureObj.pictureName == '' || pictureObj.pictureName == null)? pictureObj.pictureOriginName : pictureObj.pictureName);
	li.addEventListener('click', function() {
		if(pictureObj.hasGeometry) {
			picture.selectMarker(pictureObj);
		}
	})
	
	if(pictureObj.hasGeometry) {
		dataGroup.appendChild(li);
	} else {
		tempGroup.appendChild(li);
	}
}

toc.findElementByPictureId = function(pictureId) {
	const suffix = picture.findById(pictureId).hasGeometry ? 'data-group_' : 'temp-group_';
	const tocId = suffix + pictureId
	
	return document.getElementById(tocId);
}

toc.contextMenu = {
	rename : {
		type : 'all',
		label : '이름 변경',
		onClick : function() {
			console.log('rename !');
		}
	},
	delete : {
		type : 'all',
		label : '파일 삭제',
		onClick : function() {
			console.log('delelte!');
		}
	},
	addGeometry : {
		type : 'noGeometry',
		label : '좌표 추가',
		onClick : function() {
			console.log('add geometry!');
		}
	},
	get : function() {
		return Object.keys(this)
			.filter(key => typeof this[key] == 'object')
			.map(key => this[key]);
	}
}

function renderContextMenuList( list ){
	const ctxMenuList = document.createElement('ul');
    ctxMenuList.className = 'context-ul'

	list.forEach(function(item){
		const ctxMenuItem = document.createElement('li');
		const ctxMenuItem_a = document.createElement('a');
		const ctxMenuItem_a_text = document.createTextNode(item.label);

		if( item.onClick ){
			ctxMenuItem.addEventListener( 'click', item.onClick, false);
		}
	 
		ctxMenuItem_a.appendChild( ctxMenuItem_a_text );
		ctxMenuItem.appendChild( ctxMenuItem_a );
		ctxMenuList.appendChild( ctxMenuItem );
	});
        
	return ctxMenuList;
}
    
function handleCreateContextMenu(event){
	handleClearContextMenu();
	event.preventDefault();
	if(event.target.className == 'toc-object'){
		const ctxMenuId = 'toc_context';
	    const ctxMenu = document.createElement('div');
		const contextMenuList = toc.contextMenu.get();
		
		ctxMenu.id = ctxMenuId;
		ctxMenu.className = 'context-menu';
		ctxMenu.style.top = event.pageY+'px';
		ctxMenu.style.left = event.pageX+'px';
		
		ctxMenu.appendChild(renderContextMenuList(contextMenuList));
		
		document.body.appendChild(ctxMenu);
	} else {
		return;
	}
}

function handleClearContextMenu(event){
	const prevCtxMenu = document.getElementsByClassName('context-menu');
	
	if( prevCtxMenu.length > 0){
		prevCtxMenu[0].remove();
	}
}
      
document.addEventListener('click', handleClearContextMenu, false);
document.addEventListener('contextmenu', handleCreateContextMenu, false);

map.init();
