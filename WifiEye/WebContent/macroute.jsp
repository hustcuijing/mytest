<%@page import="java.sql.Connection"%>
<%@page import="java.sql.*"%>
<%@ page language="java"
	import="java.util.*,com.wifi.model.*,com.wifi.dao.*,com.wifi.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
<base href="<%=basePath%>">

<title>mac轨迹</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">  
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<link rel="stylesheet" type="text/css"
	href="jquery-easyui-1.3.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="jquery-easyui-1.3.3/themes/icon.css">
<script type="text/javascript" src="jquery-easyui-1.3.3/jquery.min.js"></script>
<script type="text/javascript" src="jquery-easyui-1.3.3/easyloader.js"></script>
<script type="text/javascript"
	src="jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
<script>
	$(function() {
		 var curr_time = new Date();
   var strDate = curr_time.getFullYear()+"-";
   var strDate2 = curr_time.getFullYear()+"-";
   strDate += curr_time.getMonth()+1+"-";
    strDate2 += curr_time.getMonth()+1+"-";
   strDate += curr_time.getDate()+" ";
    strDate2 += curr_time.getDate()-1 +" ";
   strDate += curr_time.getHours()+":";
   strDate2 += curr_time.getHours()+":";
   strDate += curr_time.getMinutes()+":";
   strDate2 += curr_time.getMinutes()+":";
   strDate += curr_time.getSeconds();
   strDate2 += curr_time.getSeconds();
   $("#end_time").val(strDate); 
   $("#start_time").val(strDate2); 
		
		var pg = $('#mytable').datagrid("getPager");
		if(pg){
		$(pg).pagination({
			onSelectPage : function(pageNumber,pageSize){
				$.ajax({
					type: "POST",
					contentType:'application/json;charset=UTF-8',
			        url: "myvisit?action=pg&&page="+pageNumber+"&&rows="+pageSize,
			        cache: false,
			        dataType : "json",
			        success: function(data){
			        	$('#mytable').datagrid("loadData",data);
			        }
				});
			}
		});
	}
	});
	function searchUser() {

		<%--('#mytable').datagrid({url:'myroute'});--%>
		$('#mytable').datagrid('load', {
			action:'search',
			usr_mac : $('#usr_mac').val(),
			start_time : $('#start_time').datetimebox('getValue'),
			end_time : $('#end_time').datetimebox('getValue'),
		});
		var pg = $('#mytable').datagrid("getPager");
		if(pg){
		$(pg).pagination({
			onSelectPage : function(pageNumber,pageSize){
				$.ajax({
					type: "POST",
					contentType:'application/json;charset=UTF-8',
			        url: "myroute?action=pg&&page="+pageNumber+"&&rows="+pageSize,
			        cache: false,
			        dataType : "json",
			        success: function(data){
			        	$('#mytable').datagrid("loadData",data);
			        }
				});
			}
		});
	}
	}
	
	//EasyUI datagrid 动态导出Excel
	function ExporterExcel() {
		var usr_mac = $("#usr_mac").val();
		var start_time = $('#start_time').datetimebox('getValue');
		var end_time = $('#end_time').datetimebox('getValue');
		window.location.href="mymacroute?usr_mac=" + usr_mac + "&&start_time=" + start_time
					+ "&&end_time=" + end_time;
		
	}
</script>
</head>

<body style="margin: 1px;">
	<div id="tb">
		<br>
		<div style="padding-top:10px;padding-left:150px">
			<label>开始时间：</label><input name="start_time" id="start_time" type="text"
				class="easyui-datetimebox" /> <label>&nbsp;&nbsp;结束时间：</label><input
				name="end_time" id="end_time" type="text" class="easyui-datetimebox" /> <label>&nbsp;&nbsp;用户地址：</label><input
				name="usr_mac" id="usr_mac" type="text" /> 
			<a href="javascript:searchUser()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a> 
			 <a href="javascript:ExporterExcel()" class="easyui-linkbutton" iconCls="icon-print" plain="true">导出</a> 
			
  
		</div>
		<br>
	</div>
	<table id="mytable" title="mac轨迹" class="easyui-datagrid"
		 fitColumns="false" rownumbers="true" url="myroute"
		fit="true" singleSelect="true" toolbar="#tb">
		<thead>
			<tr>
			     <th field="usr_mac" width="150" align="center">用户地址</th>
			     <th field="start_time" width="150" align="center">进入时间</th>
				<th field="end_time" width="150" align="center">离开时间</th>
				
				<th field="device_mac" width="100" align="center">监控设备</th>
				<th field="area" width="150" align="center">监控区域</th>
				<th field="addr" width="150" align="center">单位地址</th>
				<th field="latitude" width="150" align="center">经度</th>
				<th field="longtitude" width="150" align="center">纬度</th>
				
			</tr>
		</thead>
	</table>
</body>
</html>





