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

<title>伴随分析</title>
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
			        url: "myaccompany?action=pg&&page="+pageNumber+"&&rows="+pageSize,
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
        $('#mytable').datagrid('loadData', { total: 0, rows: [] });
        $('#amactable').datagrid('loadData', { total: 0, rows: [] });
		<%--$('#mytable').datagrid({url:'myroute'});--%>
		$('#mytable').datagrid('load', {
			action:'search',
			usr_mac : $('#usr_mac').val(),
			start_time : $('#start_time').datetimebox('getValue'),
			end_time : $('#end_time').datetimebox('getValue'),
		});
		
		<%--$('#amactable').datagrid({url:'myaccompany',loadMsg:"数据查询中，请等待……"});
		--%>
		$('#amactable').datagrid('load', {
			action:'search',
			usr_mac : $('#usr_mac').val(),
			start_time : $('#start_time').datetimebox('getValue'),
			end_time : $('#end_time').datetimebox('getValue'),
			timewave : $('#timewave option:selected').val(),
		});
		var pg = $('#mytable').datagrid("getPager");
		if(pg){
		$(pg).pagination({
			onSelectPage : function(pageNumber,pageSize){
				$.ajax({
					type: "POST",
					contentType:'application/json;charset=UTF-8',
			        url: "myaccompany?action=pg&&page="+pageNumber+"&&rows="+pageSize,
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
		var ap_mac = $("#ap_mac").val();
		var area = $('#area').combobox('getValue');
		var addr = $('#addr').combobox('getValue');
		var device_name = $('#device_name').combobox('getValue');
		window.location.href="import?usr_mac=" + usr_mac + "&&start_time=" + start_time
					+ "&&end_time=" + end_time + "&&ap_mac=" + ap_mac
					+ "&&area=" + area + "&&addr=" + addr + "&&device_name="
					+ device_name;
		
	}
	
	//导出伴随的mac的详细信息
	function ExporterExcelAcMacInfo(){
	  var row = $('#amactable').datagrid('getSelected'); 
	   alert(row.usr_mac +  $('#usr_mac').val() + $('#end_time').datetimebox('getValue'));
	    var usr_mac = $("#usr_mac").val();
		var start_time = $('#start_time').datetimebox('getValue');
		var end_time = $('#end_time').datetimebox('getValue');
		var	timewave = $('#timewave option:selected').val();
	   window.location.href="myAcMacInfo?usr_mac=" + usr_mac + "&&start_time=" + start_time
					+ "&&end_time=" + end_time + "&&timewave=" + timewave
					+ "&&acusr_mac =" + row.usr_mac;
	}
	
	function formatOper(val,row,index){  
    return '<a  href="javascript:getMacInfoDialog('+index+')">查看</a>';  }
    
    function getMacInfoDialog(index) {
		$('#amactable').datagrid('selectRow',index);// 关键在这里   
        var row = $('#amactable').datagrid('getSelected');  
     
        if (row){  
    	var usr_mac = row.usr_mac;
		$("#device_adddlg").dialog("open").dialog("setTitle", usr_mac + "的详细信息");
		<%--$('#mymacinfotable').datagrid({url:'myacInfo',loadMsg:"数据查询中，请等待……"});--%>
		$('#mymacinfotable').datagrid('load', {
			
			usr_mac : $('#usr_mac').val(),
			acusr_mac:usr_mac,
			start_time : $('#start_time').datetimebox('getValue'),
			end_time : $('#end_time').datetimebox('getValue'),
			timewave : $('#timewave option:selected').val(),
		});
        
    }  
	}
	<%--usr_mac : $('#usr_mac').val(),
			acusr_mac:usr_mac,--%>
</script>
<style type="text/css">

.leftDiv{ float:left;height:80%;width:50%;}
.rightDiv{float:right;height:80%;width:50%;}
</style>
</head>

<body style="margin: 1px;">
	<div id="tb">
		<br>
		<div style="padding-top:10px;padding-left:auto;padding-right:auto">
			<label>&nbsp;&nbsp;用户地址：</label><input name="usr_mac" id="usr_mac"
				type="text" style="width:13%"/> <label>开始时间：</label><input name="start_time"
				id="start_time" type="text" size="16" class="easyui-datetimebox" /> <label>&nbsp;&nbsp;结束时间：</label><input
				name="end_time" id="end_time" type="text" size="16" class="easyui-datetimebox" />
			<label>&nbsp;&nbsp;波动时间：</label><select id="timewave" name="timewave">
				<option value="0" selected="selected">无波动</option>
				<option value="1">1分钟</option>
				<option value="3">3分钟</option>
				<option value="5">5分钟</option>
			</select> <a href="javascript:searchUser()" class="easyui-linkbutton"
				iconCls="icon-search" plain="true">搜索</a>
			<!-- <a href="javascript:ExporterExcel()" class="easyui-linkbutton" iconCls="icon-print" plain="true">导出</a> -->

		</div>
		<br>
	</div>
	
		
	<div id="device_adddlg" class="easyui-dialog"
		style="width: 1000px;height: 350px;padding: 10px 20px" closed="true"
		modal="true" buttons="#dlg-buttons" >
		<form id="fm" method="post">
			<table id="mymacinfotable" title="mac轨迹" class="easyui-dataGrid"
				 fitColumns="true" rownumbers="true" fit="true" url="myacInfo?"
				singleSelect="true">
				<thead >
					<tr>
						<th field="usr_mac" width="15%" align="center">用户地址</th>
						<th field="start_time"  width="15%" align="center">进入时间</th>
						<th field="end_time"  awidth="15%" lign="center">离开时间</th>
						<th field="device_mac"  width="15%" align="center">监控设备</th>
						<th field="area"  width="15%" align="center">监控区域</th>
						<th field="addr"  width="15%" align="center">单位地址</th>
					</tr>
				</thead>
			</table>
			<br><br><br><br>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:ExporterExcelAcMacInfo()" class="easyui-linkbutton"
			iconCls="icon-ok">导出</a>
	</div>
	
	<div  class="easyui-layout" style="width:100%;height:85%;">
		<div data-options="region:'west'"  style="width:850px;height:85%">
			<table id="mytable" title="mac轨迹" class="easyui-datagrid"
				 fitColumns="true" rownumbers="true" fit="true" url="myroute?"
				singleSelect="true">
				<thead >
					<tr>
						<th field="usr_mac" width="15%" align="center">用户地址</th>
						<th field="start_time"  width="15%" align="center">进入时间</th>
						<th field="end_time"  awidth="15%" lign="center">离开时间</th>
						<th field="device_mac"  width="15%" align="center">监控设备</th>
						<th field="area"  width="15%" align="center">监控区域</th>
						<th field="addr"  width="15%" align="center">单位地址</th>
					</tr>
				</thead>
			</table>
		</div> 
		
	
		<div data-options="region:'east'"  style="width:290px;height:85%">
	<table id="amactable" title="伴随mac" class="easyui-datagrid" 
		 fitColumns="true" rownumbers="true" fit="true" url="myaccompany?"
		 singleSelect="true">
		<thead>
			<tr>
				<th field="usr_mac" width="50%" align="center">伴随mac</th>
				<th field="showTime" width="25%" align="center">伴随次数</th>
				<th width="25%" data-options="field:'_operate',align:'center',formatter:formatOper">操作</th> 
			</tr>
		</thead>
	</table>
</div>
</div>
</body>
</html>