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

<title>群组分析</title>
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
   $('#s_name').combobox({
			url : 'mygrouptype?action=listbox',
			contentType : 'application/json;charset=UTF-8',
			panelHeight : 150,//自动高度适合
			valueField : 'name',
			textField : 'name',
			editable:true,
		});
	
	
	});
	
	
	function searchUser() {
        $('#mytable').datagrid('loadData', { total: 0, rows: [] });
		<%--$('#mytable').datagrid({url:'myroute'});--%>
		$('#mytable').datagrid('load', {
			action:'search',
			group : $('#s_name').combobox('getValue'),
			start_time : $('#start_time').datetimebox('getValue'),
			end_time : $('#end_time').datetimebox('getValue'),
		});
		
		<%--$('#amactable').datagrid({url:'myaccompany',loadMsg:"数据查询中，请等待……"});
		--%>

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
</head>

<body style="margin: 1px;">
	<div id="tb">
		<br>
		<div style="padding-top:10px;padding-left:auto;padding-right:auto">
			<label>&nbsp;&nbsp;&nbsp;&nbsp;群组类别：</label> <input class="easyui-combobox" id="s_name"
				name="s_name" size="20" /> <label>开始时间：</label><input name="start_time"
				id="start_time" type="text" size="16" class="easyui-datetimebox" /> <label>&nbsp;&nbsp;结束时间：</label><input
				name="end_time" id="end_time" type="text" size="16" class="easyui-datetimebox" />
	
              <a href="javascript:searchUser()" class="easyui-linkbutton"
				iconCls="icon-search" plain="true">搜索</a>
			<!-- <a href="javascript:ExporterExcel()" class="easyui-linkbutton" iconCls="icon-print" plain="true">导出</a> -->

		</div>
		<br>
	</div>	
			<table id="mytable" title="mac信息" class="easyui-datagrid"
				 fitColumns="true" rownumbers="true" fit="true" url="mygroupanalysis?"
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
</body>
</html>