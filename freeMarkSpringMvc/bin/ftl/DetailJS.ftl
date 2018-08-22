/**
 * 管理明细JS
 * @auther 
 */
	var ${detailJSName} = {
		listUrl : '${parentPackageName}/jsp/list_${simpleclassName?uncap_first}.jsp',
		// 返回按钮事件
		goback : function(){
			utilButtons.winToUrl(this.listUrl);
		}
	};
