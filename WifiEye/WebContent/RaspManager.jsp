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
		$('#s_name').combobox({
			url : 'myrasp?action=name',
			contentType : 'application/json;charset=UTF-8',
			panelHeight : 250,//自动高度适合
			valueField : 'device_name',
			textField : 'device_name'
		});
	});
	function searchRasp() {
		$('#mytable').datagrid('load', {
			device_name : $('#s_name').combobox('getValue')
		});
	}
	function openRaspAddDialog() {
		$('#Rasp_adddlg').dialog("open").dialog("setTitle", "添加设备信息");
		$('#name').removeAttr("readonly");
		url = "myrasp?action=save";

	}
	function openRaspModifyDialog() {
		var selectedRows = $("#mytable").datagrid('getSelections');
		if (selectedRows.length != 1) {
			$.messager.alert('系统提示', '请选择一条要编辑的数据！');
			return;
		}
		var row = selectedRows[0];
		$("#Rasp_adddlg").dialog("open").dialog("setTitle", "修改设备信息");
		$("#fm").form("load", row);
		var id = encodeURI(encodeURI(row.device_mac))
		url = "myrasp?action=modify&id=" + id;
	}
	function saveRasp() {
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
							closeRaspAddDialog();
							$('#mytable').datagrid("reload");
						}
					}
				});
	}
	function closeRaspAddDialog() {
		$('#Rasp_adddlg').dialog("close");
		//$('#fm').form('clear');
	}
	function deleteRasp() {
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
				$.post("myrasp?action=delete", {
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
</script>
</head>

<body style="margin: 1px;">
	<!--操作栏  -->
	<div id="tb">
		<br>
		<div>
			<a href="javascript:openRaspAddDialog()" class="easyui-linkbutton"
				iconCls="icon-add" plain="true">添加</a> <a
				href="javascript:openRaspModifyDialog()" class="easyui-linkbutton"
				iconCls="icon-edit" plain="true">修改</a> <a
				href="javascript:deleteRasp()" class="easyui-linkbutton"
				iconCls="icon-remove" plain="true">删除</a>
		</div>
		<div>
			<label>设备编号：</label> <input class="easyui-combobox" id="s_name"
				name="s_name" size="20" /> <label style="padding-top:10px"><label>&nbsp;</label>
				<a href="javascript:searchRasp()" class="easyui-linkbutton"
				iconCls="icon-search" plain="true"">搜索</a>
		</div>
		<br>
	</div>
	<!-- 添加删除修改 -->
	<div id="Rasp_adddlg" class="easyui-dialog"
		style="width: 570px;height: 350px;padding: 10px 20px" closed="true"
		modal="true" buttons="#dlg-buttons">
		<form id="fm" method="post">
			<table cellspacing="5px;">
				<tr>
					<td>mac地址：</td>
					<td><input type="text" id="device_mac" name="device_mac"
						class="easyui-validatebox" required="true" /></td>
				</tr>
				<tr>
					<td>设备编号：</td>
					<td><input type="text" id="device_name" name="device_name"
						class="easyui-validatebox" required="true" /></td>
				</tr>
			</table>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:saveRasp()" class="easyui-linkbutton"
			iconCls="icon-ok">保存</a> <a href="javascript:closeRaspAddDialog()"
			class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
	</div>
	<table id="mytable" title="场所类型列表" class="easyui-datagrid"
		pagination="true" fitColumns="false" rownumbers="true"
		url="myrasp?action=list" fit="true" toolbar="#tb">
		<thead>
			<tr>
				<th field="cb" checkbox="true" align="center"></th>
				<th field="device_mac" width="100" align="center">设备MAC地址</th>
				<th field="device_name" width="150" align="center">设备编号</th>
			</tr>
		</thead>
	</table>
</body>
</html>





