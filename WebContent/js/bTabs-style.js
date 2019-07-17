$(function(){
	mousedown();
	calcHeight();
	$('#mainFrameTabs').bTabs();
});
function addTabs(tab_name){
		var menuId = $(tab_name).attr('mid');
		var url = $(tab_name).attr('funurl');
		var title = $(tab_name).text();
		$('#mainFrameTabs').bTabsAdd(menuId,title,url);
		calcHeight();
}
function calcHeight(){
	var browserHeight = $(window).innerHeight();
	var tabMarginTop = parseInt($('#mainFrameTabs').css('margin-top'));//获取间距
	var tabHeadHeight = $('ul.nav-tabs',$('#mainFrameTabs')).outerHeight(true) + tabMarginTop;
	var contentMarginTop = parseInt($('div.tab-content',$('#mainFrameTabs')).css('margin-top'));//获取内容区间距
	contentHeight = browserHeight - tabHeadHeight - contentMarginTop;
	$('div.tab-content',$('#mainFrameTabs')).height(contentHeight-51);
};
//tabs绑定右键事件
function mousedown(){
	$(".nav-tabs ").bind("mousedown", (function(e) {
	    if (e.which == 3) {
	        var opertionn = {
	            name: "",
	            offsetX: 10,//设置选项出现位置
	            offsetY: 10,//设置选项出现位置
	            textLimit: 10,
	            beforeShow: $.noop,
	            afterShow: $.noop
	        };
	        var imageMenuData = [
	            [ {
	                text: "关闭所有",
	                func: function() {
	                	closeAllTabs();
	                }
	            }],
	            [ {
	                text: "关闭其他",
	                func: function() {
	                	closeOtherTabs();
	                }
	            }],
	            [{
	                text: "取消",
	                func: function() {
	                	$(".nav-tabs").trigger("click");
	                }
	            }]
	        ];
	        $(this).smartMenu(imageMenuData, opertionn);
	    }
	}));
}

function closeAllTabs(){
	$(".navTabsCloseBtn").trigger("click");	
}
//右键关闭其他
function closeOtherTabs(){
	$(".active").siblings().find(".navTabsCloseBtn").trigger("click");
}