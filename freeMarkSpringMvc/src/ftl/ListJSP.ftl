<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
	<head>
	  <base href="<%=basePath%>">
		<meta charset="UTF-8">
		<jsp:include page="/common/main/jsp/linkSrc.jsp"/> 
	</head>
	<body class="white-bg">
		<div class="wrapper wrapper-content  animated fadeInRight">
			<div class="row">
				<div class="col-sm-12">
					<div class="ibox ">
						<div class="ibox-title">
							<h5>${simpleclassName}管理
							</h5>
						</div>
						<div class="ibox-content">
						    <form class="form-group">
						    <#list attrs as a>
								<#if a.field != pid>
				 				<div class="form-group col-xs-4 ">
									<label class="control-label" for="input01">${a.field}：</label>
									<input type="text" class="input-medium search-query" name="${a.field}" gps="form">
								</div>
								</#if>	
							</#list>
							<div class="form-group col-xs-12">
								<button type="button" id="toAdd" class="btn btn-primary btn-sm" onclick="button_util.addPage()">
									<span class="glyphicon glyphicon-ok"></span> 新增
								</button>
								<button type="button" id="toEdit" class="btn btn-primary btn-sm" onclick="button_util.editPage()">
									<span class="glyphicon glyphicon-file"></span> 修改
								</button>
								<button type="button" id="toDel" class="btn btn-primary btn-sm" onclick="button_util.del()">
									<span class="glyphicon glyphicon-trash"></span> 删除
								</button>
								<button type="button" class="btn btn-primary btn-sm" onclick="${listJSName}.search()">
								<span class="glyphicon glyphicon-search"></span>  查询
								</button>
							</div>
						    </form>
						    <div class="form-group col-xs-12">
								<div class="jqGrid_wrapper">
									<table id="dtable"></table>
									<div id="jqGridPager"></div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
	<script type="text/javascript" src="${parentPackageName}/js/${listJSName}.js"></script>
</html>

