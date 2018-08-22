<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<meta charset="UTF-8">
<jsp:include page="/common/main/jsp/linkSrc.jsp"/> 
</head>
<body>
	<div style="width: 100%; height: 100%;">
		<div class="ibox float-e-margins">
			<div class="ibox-title">
				<h5>新增${simpleclassName}</h5>
			</div>
			<div class="ibox-content  text-center">
				<form class="form-horizontal  m-t" role="form">
				    <#list attrs as a>
				      <#if a.field != pid>
						<div class="form-group">
							<!-- Text input-->
							<label class="col-xs-offset-2 col-xs-2 control-label" for="input01">${a.field}</label>
							<div class="col-xs-5">
								<input type="text" placeholder="" name="${a.field}" class="form-control" gps="form">
							</div>
						</div>
					  </#if>	
					</#list>
						
						<div class="form-group ">
				         <button type="button" class="btn btn-default" onclick="${addJSName}.goback()">返回</button>
				         <button type="button" class="btn btn-primary" onclick="${addJSName}.submitForm()">保存内容</button>
						</div>

				</form>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="${parentPackageName}/js/${addJSName}.js"></script>
</body>
</html>
