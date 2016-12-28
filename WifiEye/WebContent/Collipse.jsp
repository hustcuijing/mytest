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

<title>碰撞</title>
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
	var id = 0;
	var tmpid = "";

	$(function() {
	    
		bindData();

	});
	function bindData() {

		$('#area').combobox({
			url : 'myaddr?action=area',
			contentType : 'application/json;charset=UTF-8',
			panelHeight : 250,//自动高度适合
			valueField : 'area',
			textField : 'area',
			onSelect : function() {
				$("#addr").combobox("setValue", '');
				var area = $('#area').combobox('getValue');
				area = encodeURI(encodeURI(area));
				$.ajax({
					type : "POST",
					contentType : 'application/json;charset=UTF-8',
					url : "myaddr?action=addr&&area=" + area,
					cache : false,
					dataType : "json",
					success : function(data) {
						$("#addr").combobox("loadData", data);
					}
				});
			}
		});
		$('#addr').combobox({
			contentType : 'application/json;charset=UTF-8',
			panelHeight : 250,//自动高度适合
			valueField : 'addr',
			textField : 'addr'
		});
	}
	function addItem() {
		if (id == 10)
			return;
		++id;
		var html0 = "<label> 设备区域：</label> <input class='easyui-validatebox' ondblclick='javascript:selectData(this)' id='area"
				+ id
				+ "' name='area"
				+ id
				+ "' style='width:15%' required='true' placeholder='双击选择区域'/>";
		var html00 = "<label> 设备地址：</label><input class='easyui-validatebox' ondblclick='javascript:selectData(this)' id='addr"
				+ id
				+ "' name='addr"
				+ id
				+ "' style='width:15%' required='true' placeholder='双击选择地址'/>";
		var html000 = "<label> 开始时间：</label><input class='easyui-datetimebox' id='start_time"+id+"' name='start_time" + id +"' size='17' required='true'/>";
		var html0000 = "<label> 结束时间：</label><input class='easyui-datetimebox' id='end_time"+id+"' name='end_time" + id +"' size='17' required='true'/>";
		var html = html0 + html00 + html000 + html0000;
		var html2 = "<div style='margin:10px'>" + html + "</div>";
		$('#items').append(html2);
		$.parser.parse();
	}
	function deleteItem() {
	
		if (id == 0)
			return;
		$("#items div:last").remove();
		id--;
	}
	function selectData(obj) {
		$("#searchButton").linkbutton("enable");
		tmpid = obj.id;
		$("#addrdlg").dialog("open").dialog("setTitle", "选择地址信息");

	}
	function enableBut() {
		$("#searchButton").linkbutton("enable");
	}
	function save() {
		var idnumber = tmpid.substring(4);
		var area_value = $('#area').combobox('getValue');
		var addr_value = $('#addr').combobox('getValue');
		$('#area' + idnumber).val(area_value);
		$('#addr' + idnumber).val(addr_value);
		close();
	}
	function close() {
		$("#addrdlg").dialog("close");
	}

	$(function() {
		var pg = $('#mytable').datagrid("getPager");
		if (pg) {
			$(pg).pagination(
					{
						onSelectPage : function(pageNumber, pageSize) {
							$.ajax({
								type : "POST",
								contentType : 'application/json;charset=UTF-8',
								url : "mycollipse?action=pg&&page="
										+ pageNumber + "&&rows=" + pageSize,
								cache : false,
								dataType : "json",
								success : function(data) {
									$('#mytable').datagrid("loadData", data);
								}
							});
						}
					});
		}
	});

	function searchMac() {
		if (id == 0) {
			alert("请至少输入两组条件！");
			return;
		}
		if (!confirm("此查询所需时间较长，请尽量选择较短的时间间隔，确定执行该程序？"))
			return;
		$("#searchButton").linkbutton("disable");
		var areaall = "";
		var addrall = "";
		var start_timeall = "";
		var end_timeall = "";
		var areas;
		var addrs;
		var start_time1;
		var end_time1;

		for ( var i = 0; i <= id; i++) {

			areas = "area" + i;
			addrs = "addr" + i;
			start_time1 = "start_time" + i;
			end_time1 = "end_time" + i;
			areaall += document.getElementsByName(areas)[0].value + ",";

			addrall += document.getElementsByName(addrs)[0].value + ",";

			start_timeall += document.getElementsByName(start_time1)[0].value
					+ ",";
			end_timeall += document.getElementsByName(end_time1)[0].value + ",";
		}
<%--$('#mytable').datagrid({url:'mycollipse'});--%>
	$('#mytable').datagrid('load', {
			action : 'search',
			start_time : start_timeall,
			end_time : end_timeall,
			area : areaall,
			addr : addrall,
		});
		$("#searchButton").linkbutton("enable");
		var pg = $('#mytable').datagrid("getPager");
		if (pg) {
			$(pg).pagination(
					{
						onSelectPage : function(pageNumber, pageSize) {
							$.ajax({
								type : "POST",
								contentType : 'application/json;charset=UTF-8',
								url : "mycollipse?action=pg&&page="
										+ pageNumber + "&&rows=" + pageSize,
								cache : false,
								dataType : "json",
								success : function(data) {
									$('#mytable').datagrid("loadData", data);
								}
							});
						}
					});
		}
	}
	//EasyUI datagrid 动态导出Excel
	function ExporterExcel() {
		var areaall = "";
		var addrall = "";
		var start_timeall = "";
		var end_timeall = "";
		var areas;
		var addrs;
		var start_time1;
		var end_time1;

		for ( var i = 0; i <= id; i++) {
			areas = "area" + i;
			addrs = "addr" + i;
			start_time1 = "start_time" + i;
			end_time1 = "end_time" + i;
			areaall += document.getElementsByName(areas)[0].value + ",";

			addrall += document.getElementsByName(addrs)[0].value + ",";

			start_timeall += document.getElementsByName(start_time1)[0].value
					+ ",";
			end_timeall += document.getElementsByName(end_time1)[0].value + ",";
		}

		window.location.href = "mycollipseexcel?start_time=" + start_timeall
				+ "&&end_time=" + end_timeall + "&&area=" + areaall + "&&addr="
				+ addrall;

	}
</script>
</head>

<body style="margin: 1px;" style="width:100%;">
	<!--操作栏  -->
	<div id="tb">
		<br>

		<form id="items">
			<div style='margin:10px'>
				<label>设备区域：</label> <input class="easyui-validatebox"
					ondblclick="javascript:selectData(this)" id="area0" name="area0"
					style="width:15%" required="true" placeholder="双击选择区域" /> <label>设备地址：</label><input
					class="easyui-validatebox" ondblclick="javascript:selectData(this)"
					id="addr0" name="addr0" style="width:15%" required="true"
					placeholder="双击选择地址" /> <label>开始时间：</label><input
					class="easyui-datetimebox" 
					id="start_time0" name="start_time0" size="17" required="true" /> <label>结束时间：</label><input
					class="easyui-datetimebox" id="end_time0" name="end_time0"
					size="17" required="true" />

			</div>
		</form>
		<div>
			<a href="javascript:addItem()" class="easyui-linkbutton"
				iconCls="icon-add" plain="true">添加条件</a> <a
				href="javascript:deleteItem()" class="easyui-linkbutton"
				iconCls="icon-remove" plain="true">删除条件</a> <a
				href="javascript:searchMac()" id="searchButton"
				class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
			<a href="javascript:ExporterExcel()" class="easyui-linkbutton"
				iconCls="icon-print" plain="true">导出</a>
		</div>
		<br>
	</div>
	<div id="addrdlg" class="easyui-dialog"
		style="width: 570px;height: 350px;padding: 10px 20px" closed="true"
		modal="true" buttons="#dlg-buttons">
		<form id="fm" method="post">
			<table cellspacing="5px;">
				<tr>
					<td>设备区域：</td>
					<td><input type="text" id="area" name="area"
						class="easyui-combobox" required="true" />
					</td>
					<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td>设备地址：</td>
					<td><input type="text" id="addr" name="addr"
						class="easyui-combobox" required="true" />
					</td>
				</tr>
			</table>
		</form>

	</div>
	<div id="dlg-buttons">
		<a href="javascript:save()" class="easyui-linkbutton"
			iconCls="icon-ok">保存</a> <a href="javascript:close()"
			class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
	</div>
	<table id="mytable" title="碰撞mac列表" class="easyui-datagrid"
		fitColumns="true" rownumbers="true" url="mycollipse?" fit="true"
		singleSelect="true" toolbar="#tb">
		<thead>
			<tr>

				<th field="usr_mac" width="10%" align="center">用户mac</th>

				<th field="start_time" width="15%" align="center">进入时间</th>

				<th field="end_time" width="15%" align="center">离开时间</th>

				<th field="device_mac" width="15%" align="center">设备mac</th>
				<th field="area" width="15%" align="center">设备区域</th>
				<th field="addr" width="15%" align="center">设备地址</th>

			</tr>
		</thead>
	</table>
</body>
</html>




