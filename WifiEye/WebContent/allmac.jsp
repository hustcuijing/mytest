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

<title>My JSP 'index.jsp' starting page</title>
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
<script type="text/javascript"
	src="jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
<script>
	$(function() {
		$('#area').combobox({
			url : 'myaddr?action=area',
			contentType:'application/json;charset=UTF-8',
			panelHeight: 'auto',//自动高度适合
			valueField : 'area',
			textField : 'area',
			onSelect: function(){
		        $("#addr").combobox("setValue",'');
		      	var area = $('#area').combobox('getValue');	
		      	area = encodeURI(encodeURI(area));
		      	$.ajax({
			        type: "POST",
			        contentType:'application/json;charset=UTF-8',
			        url: "myaddr?action=addr&&area="+area,
			        cache: false,
			        dataType : "json",
			        success: function(data){
			        $("#addr").combobox("loadData",data);
			                 }
			            }); 	
			          }
		});
		$('#addr').combobox({
			contentType:'application/json;charset=UTF-8',
			panelHeight: 'auto',//自动高度适合
			valueField : 'addr',
			textField : 'addr',
			onSelect: function(){
		        $("#device_name").combobox("setValue",'');
		      	var addr = $('#addr').combobox('getValue');		
		      	var area = $('#area').combobox('getValue');	
		      	area = encodeURI(encodeURI(area));
				addr = encodeURI(encodeURI(addr));
		      	$.ajax({
			        type: "POST",
			        contentType:'application/json;charset=UTF-8',
			        url: "myaddr?action=device&&area="+area+"&&addr="+addr,
			        cache: false,
			        dataType : "json",
			        success: function(data){
			        $("#device_name").combobox("loadData",data);
			                 }
			            }); 	
			          }
		});
		$('#device_name').combobox({
			contentType:'application/json;charset=UTF-8',
			panelHeight: 'auto',//自动高度适合
			valueField : 'device_name',
			textField : 'device_name'
		});
	});
	function searchUser() {
		$('#mytable').datagrid('load', {
			usr_mac : $('#usr_mac').val(),
			start_time : $('#start_time').datetimebox('getValue'),
			end_time : $('#end_time').datetimebox('getValue'),
			ap_mac : $('#ap_mac').val(),
			area : $('#area').combobox('getValue'),
			addr : $('#addr').combobox('getValue'),
			device_name : $('#device_name').combobox('getValue')
		});
	}
	//EasyUI datagrid 动态导出Excel
	function ExporterExcel() {
		var usr_mac = $("#usr_mac").val();
		var start_time = $('#start_time').datetimebox('getValue');
		var end_time = $('#end_time').datetimebox('getValue');
		var ap_mac = $("#ap_mac").val();
		var area = $('#area').combobox('getValue');
		var addr = $('#addr').combobox('getValue');
		var device_name = $('#device_name').combobox('getValue');
		window.location.href="import?usr_mac=" + usr_mac + "&&start_time=" + start_time
					+ "&&end_time=" + end_time + "&&ap_mac=" + ap_mac
					+ "&&area=" + area + "&&addr=" + addr + "&&device_name="
					+ device_name;
		
	}
</script>
</head>

<body style="margin: 1px;">
	<div id="tb">
		<br>
		<div style="padding-top:10px;padding-left:190px">
			<label>开始时间：</label><input name="start_time" id="start_time" type="text"
				class="easyui-datetimebox" /> <label>&nbsp;&nbsp;结束时间：</label><input
				name="end_time" id="end_time" type="text" class="easyui-datetimebox" /> <label>&nbsp;&nbsp;用户地址：</label><input
				name="usr_mac" id="usr_mac" type="text" /> <label>&nbsp;&nbsp;AP地址：</label><input
				name="ap_mac" id="ap_mac" type="text" />
		</div>
		<div style="padding-top:10px;padding-left:190px">
			<label>区域地址：</label> 
			<input class="easyui-combobox" id="area" name="area" size="20" /> 
			<label style="padding-top:10px">&nbsp;&nbsp;场所名称：</label>
			<input class="easyui-combobox" id="addr" name="addr" size="20" /> 
			<label>&nbsp;&nbsp;设备编号：</label>
			<input class="easyui-combobox" id="device_name" name="device_name"
				size="20" /> <label>&nbsp;</label>
			<a href="javascript:searchUser()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a> 
			<a href="javascript:ExporterExcel()" class="easyui-linkbutton" iconCls="icon-print" plain="true">导出</a>
			
		</div>
		<br>
	</div>
	<table id="mytable" title="mac信息列表" class="easyui-datagrid"
		pagination="true" fitColumns="false" rownumbers="true" url="myMac"
		fit="true" singleSelect="true" toolbar="#tb">
		<thead>
			<tr>
				<th field="device_name" width="100" align="center">监控设备</th>
				<th field="address" width="150" align="center">监控地址</th>
				<th field="recordtime" width="150" align="center">时间</th>
				<th field="usr_mac" width="150" align="center">用户地址</th>
				<th field="ap_mac" width="150" align="center">AP地址</th>
				<th field="data_rate" width="50" align="center">传输速率</th>
				<th field="rssi_signal" width="50" align="center">信号强度</th>
				<th field="app_type" width="50" align="center">应用类型</th>
				<th field="app_info" width="150" align="center">应用信息</th>
				<th field="channel_id" width="100" align="center">信道</th>
				<th field="firm" width="200" align="center">用户厂商</th>
			</tr>
		</thead>
	</table>
</body>
</html>





