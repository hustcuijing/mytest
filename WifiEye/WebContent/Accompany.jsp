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
	var url;
	$(function() {
	$('#p').hide();
		init();
		initSearch();
	});
	function init() {
		$('#area').combobox({
			url : 'myaddr?action=area',
			contentType : 'application/json;charset=UTF-8',
			panelHeight : 'auto',//自动高度适合
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
			panelHeight : 'auto',//自动高度适合
			valueField : 'addr',
			textField : 'addr'
		});
		$('#device_name').combobox({
			url : 'myaddr?action=notused',
			contentType : 'application/json;charset=UTF-8',
			panelHeight : 'auto',//自动高度适合
			valueField : 'device_name',
			textField : 'device_name'
		});
	}
	function initSearch() {
		$('#s_area').combobox({
			url : 'myaddr?action=area',
			contentType : 'application/json;charset=UTF-8',
			panelHeight : 'auto',//自动高度适合
			valueField : 'area',
			textField : 'area',
			onSelect : function() {
				$("#s_addr").combobox("setValue", '');
				var area = $('#s_area').combobox('getValue');
				area = encodeURI(encodeURI(area));
				$.ajax({
					type : "POST",
					contentType : 'application/json;charset=UTF-8',
					url : "myaddr?action=addr&&area=" + area,
					cache : false,
					dataType : "json",
					success : function(data) {
						$("#s_addr").combobox("loadData", data);
					}
				});
			}
		});
		$('#s_addr').combobox(
				{
					url : 'myaddr?action=addr',
					contentType : 'application/json;charset=UTF-8',
					panelHeight : 'auto',//自动高度适合
					valueField : 'addr',
					textField : 'addr',
					onSelect : function() {
						$("#s_device_name").combobox("setValue", '');
						var area = $('#s_area').combobox('getValue');
						var addr = $('#s_addr').combobox('getValue');
						area = encodeURI(encodeURI(area));
						addr = encodeURI(encodeURI(addr));
						$.ajax({
							type : "POST",
							contentType : 'application/json;charset=UTF-8',
							url : "myaddr?action=device&&area=" + area
									+ "&&addr=" + addr,
							cache : false,
							dataType : "json",
							success : function(data) {
								$("#s_device_name").combobox("loadData", data);
							}
						});
					}
				});
		$('#s_device_name').combobox({
			contentType : 'application/json;charset=UTF-8',
			panelHeight : 'auto',//自动高度适合
			valueField : 'device_name',
			textField : 'device_name'
		});
		$('#s_ip').combobox({
			url : 'myaddr?action=ip',
			contentType : 'application/json;charset=UTF-8',
			panelHeight : 'auto',//自动高度适合
			valueField : 'ip',
			textField : 'ip'
		});
		var data = {
			"statuss" : [ {
				"id" : "0",
				"value" : "离线"
			}, {
				"id" : "1",
				"value" : "在线"
			} ]
		};
		$('#s_status').combobox({
			contentType : 'application/json;charset=UTF-8',
			panelHeight : 'auto',//自动高度适合
			valueField : 'id',
			textField : 'value'
		});
		$('#s_status').combobox("loadData", data.statuss);
	}
	//EasyUI datagrid 动态导出Excel
	function openDeviceAddDialog() {
		$("#device_adddlg").dialog("open").dialog("setTitle", "添加设备信息");
		url = "mydevice?action=save";
	}
	function openDeviceModifyDialog() {
		var selectedRows = $("#mytable").datagrid('getSelections');
		if (selectedRows.length != 1) {
			$.messager.alert('系统提示', '请选择一条要编辑的数据！');
			return;
		}
		var row = selectedRows[0];
		$("#device_adddlg").dialog("open").dialog("setTitle", "修改设备信息");
		$('#fm').form("load", row);
		url = "mydevice?action=modify&id=" + row.device_name;
	}
	function saveDevice() {

		$('#fm').form(
				"submit",
				{
					url : url,
					onSubmit : function() {
						return $(this).form("validate");
					},
					success : function(result) {
						var result = eval('(' + result + ')');
						if (result.errorMsg) {
							$.messager.alert('系统提示', "<font color=red>"
									+ result.errorMsg + "</font>");
							return;
						} else {
							$.messager.alert('系统提示', '保存成功');
							closeDeviceAddDialog();
							$('#mytable').datagrid("reload");
						}
					}
				});
	}
	function closeDeviceAddDialog() {
		$('#device_adddlg').dialog("close");
		$('#fm').form('clear');
		init();
	}
	function deleteDevice() {
		var selectedRows = $("#mytable").datagrid('getSelections');
		if (selectedRows.length == 0) {
			$.messager.alert('系统提示', '请选择要删除的数据！');
			return;
		}
		var strIds = [];
		for ( var i = 0; i < selectedRows.length; i++) {
			strIds.push(selectedRows[i].device_mac);
		}
		var ids = strIds.join(",");
		$.messager.confirm("系统提示", "您确认要删除这<font color=red>"
				+ selectedRows.length + "</font>条数据吗？", function(r) {
			if (r) {
				$.post("mydevice?action=delete", {
					delIds : ids
				}, function(result) {
					if (result.success) {
						$.messager.alert('系统提示', "您已成功删除<font color=red>"
								+ result.delNums + "</font>条数据！");
						$("#mytable").datagrid("reload");
					} else {
						$.messager.alert('系统提示', result.errorMsg);
					}
				}, "json");
			}
		});
	}
	function searchDevice() {
		$('#mytable').datagrid('load', {
			area : $('#s_area').combobox('getValue'),
			addr : $('#s_addr').combobox('getValue'),
			device_name : $('#s_device_name').combobox('getValue'),
			ip : $('#s_ip').combobox('getValue'),
			status:$('#s_status').combobox('getValue')
		});
	}
	function updateSet(){
		alert("dd");
	}
	function updateStatus(){
		
		progress();
		$.ajax({
							type : "POST",
							contentType : 'application/json;charset=UTF-8',
							url : "mystatus?action=updateStatus",
							cache : false,
							dataType : "json",
							success : function(result) {
								if (result.success) {
									$.messager.alert('系统提示', "您已成功更新<font color=red>"
										+ result.updateNums + "</font>个设备的状态！");
								} else {
									$.messager.alert('系统提示', result.errorMsg);
								}
							}
						});
	}
	function progress(){
	 $('#p').show();
		 var value = $('#p').progressbar('getValue');
      if (value < 100) {
        value += 2;
        $('#p').progressbar('setValue', value);
          setTimeout(arguments.callee, 3000);
        if (value >= 100) {
          $('#p').progressbar('disable');//文件上传成功之后禁用进度条
          $('#p').hide();
          $('#p').progressbar('setValue', 0);
        }
      }

		}
</script>
</head>

<body style="margin: 1px;">
	<!--操作栏  -->
	<div id="tb">
		<br>
		
		<div>
			<label>区域地址：</label> <input class="easyui-combobox" id="s_area"
				name="s_area" size="20" /> <label style="padding-top:10px">&nbsp;&nbsp;场所名称：</label>
			<input class="easyui-combobox" id="s_addr" name="s_addr" size="20" />
			<label>&nbsp;&nbsp;设备编号：</label> <input class="easyui-combobox"
				id="s_device_name" name="s_device_name" size="20" /> <label>&nbsp;</label>
			<br> <label>IP地址：</label><input class="easyui-combobox"
				id="s_ip" name="s_ip" size="20" /> <label>在线状态：</label><input
				class="easyui-combobox" id="s_status" name="s_status" size="20" />
				
			<a href="javascript:searchDevice()" class="easyui-linkbutton"
				iconCls="icon-search" plain="true">搜索</a> <a
				href="javascript:updateSet()" class="easyui-linkbutton"
				iconCls="icon-tip" plain="true">更新系統配置</a>
				<a
				href="javascript:updateStatus()" class="easyui-linkbutton"
				iconCls="icon-tip" plain="true">更新设备状态</a>
			<div id="p" class="easyui-progressbar" data-options="value:0" style="width:400px;" ></div>
			
		</div>
		<br>
	</div>
	<!-- 添加删除修改 -->
	<div id="device_adddlg" class="easyui-dialog"
		style="width: 570px;height: 350px;padding: 10px 20px" closed="true"
		modal="true" buttons="#dlg-buttons">
		<form id="fm" method="post">
			<table cellspacing="5px;">
				<tr>
					<td>设备编号：</td>
					<td><input type="text" id="device_name" name="device_name"
						class="easyui-combobox" required="true" /></td>
					<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td>设备IP地址：</td>
					<td><input type="text" id="ip" name="ip"
						class="easyui-validatebox" required="true" /></td>
				</tr>
				<tr>
					<td>设备区域：</td>
					<td><input type="text" id="area" name="area"
						class="easyui-combobox" required="true" /></td>
					<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td>设备地址：</td>
					<td><input type="text" id="addr" name="addr"
						class="easyui-combobox" required="true" /></td>
				</tr>
			</table>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:saveDevice()" class="easyui-linkbutton"
			iconCls="icon-ok">保存</a> <a href="javascript:closeDeviceAddDialog()"
			class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
	</div>
	<table id="mytable" title="mac信息列表" class="easyui-datagrid"
		pagination="true" fitColumns="false" rownumbers="true"
		url="mystatus?action=list" fit="true" toolbar="#tb">
		<thead>
			<tr>
				<th field="cb" checkbox="true" align="center"></th>
				<th field="device_mac" width="100" align="center">设备mac</th>
				<th field="device_name" width="150" align="center">设备编号</th>
				<th field="ip" width="150" align="center">设备IP地址</th>
				<th field="area" width="150" align="center">设备区域</th>
				<th field="addr" width="150" align="center">设备地址</th>
				<th field="status" width="150" align="center">设备状态</th>
			</tr>
		</thead>
	</table>
</body>
</html>




