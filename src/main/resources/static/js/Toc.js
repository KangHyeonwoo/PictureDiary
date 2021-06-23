class Toc {
    #dataGroupId = 'data-group';
    #tempGroupId = 'temp-group';
    #data = {};
    #contextMenu = {
        rename : {
            type : 'all',
            label : '이름 변경',
            onClick : function() {
                console.log('rename !');
            }
        },
        remove : {
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
    constructor(list) {
        this.list = list;
    }

    add(pictureObj) {

    }

    remove(tocObj) {

    }

    get(pictureObj) {
        this.tocList.find(e => e.pictureId == id);
    }


}