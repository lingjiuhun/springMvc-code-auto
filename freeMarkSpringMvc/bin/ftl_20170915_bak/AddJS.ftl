/**
 * 管理新增JS
 * @auther 
 */
	var ${addJSName} = {
		url : '${simpleclassName?uncap_first}/add',
		name : '${simpleclassName?uncap_first}',
		listUrl : '${parentPackageName}/jsp/list_${simpleclassName?uncap_first}.jsp',
		// 提交按钮事件
		submitForm : function(){
			// 设置查询URL
			button_util.listUrl = this.listUrl; 
			// 调用分页插件初始化列表
			button_util.submitForm(this.url,this.name,this.listUrl);
		},
		// 返回按钮事件
		goback : function(){
			button_util.winToUrl(this.listUrl);
		}
	};
