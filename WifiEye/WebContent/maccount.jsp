<%@page import="java.sql.Connection"%>
<%@page import="java.sql.*"%>
<%@ page language="java"
	import="java.util.*,com.wifi.model.*,com.wifi.dao.*,com.wifi.util.*"
	pageEncoding="utf-8"%>
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
			panelHeight: 250,
			valueField : 'addr',
			textField : 'addr',
			editable:true, 
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
			valueField : 'device_mac',
			textField : 'device_name'
		});
	});
	function searchUser() {
		$('#mytable').datagrid('load', {
			start_time : $('#start_time').datetimebox('getValue'),
			end_time : $('#end_time').datetimebox('getValue'),
			area : $('#area').combobox('getValue'),
			addr : $('#addr').combobox('getValue'),
			device_name : $('#device_name').combobox('getValue')
		});
	}
	//EasyUI datagrid 动态导出Excel
	function ExporterExcel() {
		var start_time = $('#start_time').datetimebox('getValue');
		var end_time = $('#end_time').datetimebox('getValue');
		var area = $('#area').combobox('getValue');
		var addr = $('#addr').combobox('getValue');
		var device_name = $('#device_name').combobox('getValue');
		window.location.href="import?usr_mac=" + usr_mac + "&&start_time=" + start_time
					+ "&&end_time=" + end_time + "&&ap_mac=" + ap_mac
					+ "&&area=" + area + "&&addr=" + addr + "&&device_name="
					+ device_name;
		
	}
	function formatOper(val,row,index){  
    return '<a href="javascript:getUniqueMac('+index+')">唯一mac</a>';  
} 
function uniquemac(val,row,index){  

    return '<lable id= "'+index + '" ></lable>';  
} 
function getUniqueMac(index){  
    $('#mytable').datagrid('selectRow',index);// 关键在这里   
    var row = $('#mytable').datagrid('getSelected');  
    $('#'+index).text("正在查询...");
    if (row){  
    	var device_mac = row.device_name;
    	var start_time = $('#start_time').datetimebox('getValue');
		var end_time = $('#end_time').datetimebox('getValue');
    	$.ajax({
    		type : "post",
    		contentType:'application/json;charset=UTF-8',
    		url : 'myvisitcount?action=unique&device_mac='+device_mac+'&start_time='+start_time+'&end_time='+end_time,
    		dataType : "json",
    		success:function(data){
    			$('#'+index).text(data.unique);
    		}
    	});
        
    }  
}  

</script>
</head>

<body style="margin: 1px;">
	<div id="tb">
		<br>
		<div style="padding-top:10px;padding-left:190px">
			<label>开始时间： </label><input name="start_time" id="start_time" type="text"
				class="easyui-datetimebox" data-options="editable:false" /> 
				<label>&nbsp;&nbsp;结束时间： </label><input
				name="end_time" id="end_time" type="text" class="easyui-datetimebox"
				data-options="editable:false" /> 
		</div>
		<div style="padding-top:10px;padding-left:190px">
			<label>区域地址：</label> <input class="easyui-combobox" id="area"
				name="area" size="20" /> <label style="padding-top:10px">&nbsp;&nbsp;场所名称：</label>
			<input class="easyui-combobox" id="addr" name="addr" size="20" /> <label>&nbsp;&nbsp;设备编号：</label>
			<input class="easyui-combobox" id="device_name" name="device_name"
				size="20" /> <label>&nbsp;</label> <a
				href="javascript:searchUser()" class="easyui-linkbutton"
				iconCls="icon-search" plain="true">搜索</a> 
				<!-- <a
				href="javascript:ExporterExcel()" class="easyui-linkbutton"
				iconCls="icon-print" plain="true">导出</a> -->

		</div>
		<br>
	</div>
	<table id="mytable" title="mac统计分析" class="easyui-datagrid"
		pagination="true" fitColumns="false" rownumbers="true" url="myvisitcount?action=list"
		fit="true" singleSelect="true" toolbar="#tb">
		<thead>
			<tr>
				<th field="area" width="150" align="center">监控区域</th>
				<th field="addr" width="150" align="center">监控地址</th>
				<th field="device_name" width="150" align="center">设备编号</th>
				<th field="total" width="100" align="center">mac总数</th>
				<th data-options="field:'_operate',width:80,align:'center',formatter:formatOper">操作</th> 
				<th data-options="field:'unique',width:80,align:'center',formatter:uniquemac">唯一mac数</th>
				<!--  <th field="unique" width="200" align="center">mac唯一数</th>-->
			</tr>
		</thead>
	</table>
</body>
</html>





