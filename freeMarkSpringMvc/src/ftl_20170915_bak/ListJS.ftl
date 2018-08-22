/**
 * 管理查询JS
 * @auther
 */
	var ${listJSName} = {
		// 用户管理JS
		init:function(){
			// caption:标题
			var caption = "${simpleclassName}列表";
			// colNames:列名
			var  colNames = [
			'${pid}'
			<#list attrs as a>
				<#if a.field != pid>
				 ,'${a.field}'
				</#if>	
			</#list>
			];
			// colModel:列对应实体类名
			var colModel = [  
		               { name: '${pid}', index: '${pid}', width: 10, key: true, hidden: false , formatter:'showlink', 
		            	   // 设置点击详情页面URL
		            	   formatoptions:{baseLinkUrl:"${simpleclassName?uncap_first}/get",idName: "${pid}"} }
		            	<#list attrs as a>
						  <#if a.field != pid>   
		               ,{ name: '${a.field}', index: '${a.field}', width: 10}
		                  </#if>	
						</#list>
		           ];
			// 设置新增跳转页面地址
			button_util.addUrl = "${simpleclassName?uncap_first}_add";
			// 设置 编辑跳转页面URL
			button_util.editUrl = "${simpleclassName?uncap_first}_edit?${pid}=";
			// 设置 删除参数URL
			button_util.delUrl = "${simpleclassName?uncap_first}/del?${pid}=";
			// 调用分页插件初始化列表
			page_util.tablePlugin("${simpleclassName?uncap_first}/list",caption,colNames,colModel);
		},// 查询
		search:function(){
			button_util.search("${simpleclassName?uncap_first}/list","${simpleclassName?uncap_first}");
		}
	};
	// 初始化加载
	${listJSName}.init();