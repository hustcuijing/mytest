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
<script type="text/javascript"
	src="style/data.js"></script>
	<script type="text/javascript"
	src="style/datajs.js"></script>
<script>
var url;
$(function() {
		$('#s_area').combobox({
			url : 'myaddr?action=area',
			contentType : 'application/json;charset=UTF-8',
			panelHeight : 250,//自动高度适合
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
					contentType : 'application/json;charset=UTF-8',
					panelHeight : 250,//自动高度适合
					valueField : 'addr',
					textField : 'addr',
					editable : true
		});
	});
	$(document).ready(function(){init("province","湖北","city","咸宁","town","");});
	function searchLocation() {
		$('#mytable').datagrid('load', {
			area : $('#s_area').combobox('getValue'),
			addr:$('#s_addr').combobox('getValue'),
			location_type:$('#s_location_type').combobox('getValue'),
			latitude:$('#s_latitude').val(),
			longtitude:$('#s_longtitude').val()
		});
	}
function openLocationAddDialog(){
		$('#unit_adddlg').dialog("open").dialog("setTitle","添加单位信息");
		$('#addr').removeAttr("readonly");
		url="myunit?action=save";
		
	}
	function openLocationModifyDialog(){
		var selectedRows=$("#mytable").datagrid('getSelections');
		if(selectedRows.length!=1){
			$.messager.alert('系统提示','请选择一条要编辑的数据！');
			return;
		}
		var row=selectedRows[0];
		var addr = row.addr;
		var area = row.area;
		var location_type = row.location_type;
		var latitude = row.latitude;
		var longtitude = row.longtitude;
		var strs = new Array();
		strs = area.split(" ");
		var province = strs[0];
		var city = strs[1];
		var town = strs[2];
		var mydata={"addr":addr,"province":province,"city":city,"town":town,"location_type":location_type,"latitude":latitude,"longtitude":longtitude};
		$("#unit_adddlg").dialog("open").dialog("setTitle","修改场所类型信息");
		$("#fm").form("load",mydata);
		url="myunit?action=modify&location_id="+row.location_id;
	}
	function saveLocation(){
		
		$('#fm').form("submit",{
			url:url,
			onSubmit:function(){
				return $(this).form("validate");
			},
			success:function(result){
				var result=eval('('+result+')');
				if(result.errorMsg){
					$.messager.alert('系统提示',"<font color=red>"+result.errorMsg+"</font>");
					return;
				}else{
					$.messager.alert('系统提示','保存成功');
					closeLocationAddDialog();
					$('#mytable').datagrid("reload");
				}
			}
		});
	}
	function closeLocationAddDialog(){
		$('#unit_adddlg').dialog("close");
		//$('#fm').form('clear');
	}
	function deleteLocation(){
		var selectedRows=$("#mytable").datagrid('getSelections');
		if(selectedRows.length==0){
			$.messager.alert('系统提示','请选择要删除的数据！');
			return;
		}
		var strIds=[];
		for(var i=0;i<selectedRows.length;i++){
			strIds.push(selectedRows[i].location_id);
		}
		var ids=strIds.join(",");
		$.messager.confirm("系统提示","您确认要删除这<font color=red>"+selectedRows.length+"</font>条数据吗？",function(r){
			if(r){
				$.post("myunit?action=delete",{delIds:ids},function(result){
					if(result.success){
						$.messager.alert('系统提示',"您已成功删除<font color=red>"+result.delNums+"</font>条数据！");
						$("#mytable").datagrid("reload");
					}else{
						$.messager.alert('系统提示',result.errorMsg);
					}
				},"json");
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
			<a href="javascript:openLocationAddDialog()" class="easyui-linkbutton"
				iconCls="icon-add" plain="true">添加</a> <a
				href="javascript:openLocationModifyDialog()" class="easyui-linkbutton"
				iconCls="icon-edit" plain="true">修改</a> <a
				href="javascript:deleteLocation()" class="easyui-linkbutton"
				iconCls="icon-remove" plain="true">删除</a>
		</div>
		<div>
			<table cellspacing="5px;">
  		<tr>
  			<td>
  				区域地址:
  			</td>
  			<td><input class="easyui-combobox" id="s_area" name="s_area"/></td>
  			<td>
  				单位地址：
  			</td>
  			<td><input class="easyui-combobox" id="s_addr" name="s_addr"/></td>
  			<td>
  			场所类别：
  			</td>
  			<td ><input class="easyui-combobox"  id="s_location_type"
				name="s_location_type" size="20" data-options="panelHeight:'auto',valueField:'location_type_id',textField:'name',url:'mylocationtype?action=listbox'"/> <label style="padding-top:10px"></td>
  		</tr>
  		</tr>
  			<tr>
  			<td >经&nbsp&nbsp&nbsp&nbsp度：</td>
  			<td ><input type="text" id="s_latitude" name="s_latitude" class="easyui-numberbox"  max="136" min="78" maxlength="17" placeholder="输入78-136之间的数字"/></td>
  			<td >纬&nbsp&nbsp&nbsp&nbsp度：</td>
  			<td ><input type="text" id="s_longtitude" name="s_longtitude"  class="easyui-numberbox"  max="47" min="20" maxlength="16" placeholder="输入20-47之间的数字"/></td>
			<td><a	href="javascript:searchLocation()" class="easyui-linkbutton"
				iconCls="icon-search" plain="true" ">搜索</a>
			</td>  		
  		</tr>
  	</table> 
		</div>
		<br>
	</div>
	<!-- 添加删除修改 -->
	<div id="unit_adddlg" class="easyui-dialog" style="width: 800px;height: 400px;padding: 10px 20px"
  closed="true" modal="true" buttons="#dlg-buttons">
  <form id="fm" method="post">
  	<table cellspacing="5px;">
  		<tr>
  			<td height="60">单位地址：</td>
  			
  			<td height="60"><input type="text" id="addr" name="addr" class="easyui-validatebox" required="true" /></td>
  			
  		</tr>
  			<tr>
  			<td height="60">省份：</td>
  			<td height="60"><select  id="province" name="province" class="easyui-validatebox" required="true"/></td>
  			<td height="60">城市：</td>
  			<td height="60"><select  id="city" name="city" class="easyui-validatebox" required="true"/></td>
  			<td height="60">乡镇：</td>
  			<td height="60"><select id="town" name="town" class="easyui-validatebox" required="true"/></td>
  		</tr>
  		<tr>
  			<td height="60">
  			场所类别：
  			</td>
  			<td height="60"><input class="easyui-combobox" required="true" id="location_type"
				name="location_type" size="20" data-options="panelHeight:'auto',valueField:'location_type_id',textField:'name',url:'mylocationtype?action=listbox'"/> <label style="padding-top:10px"></td>
  		</tr>
  		</tr>
  			<tr>
  			<td height="60">经度：</td>
  			<td height="60"><input type="text" id="latitude" name="latitude" class="easyui-numberbox" required="true" max="136" min="78" maxlength="17" placeholder="输入78-136之间的数字"/></td>
  			<td height="60">纬度：</td>
  			<td height="60"><input type="text" id="longtitude" name="longtitude"  class="easyui-numberbox" required="true" max="47" min="20" maxlength="16" placeholder="输入20-47之间的数字"/></td>
  		</tr>
  	</table>
  </form>
</div>
	<div id="dlg-buttons">
	<a href="javascript:saveLocation()" class="easyui-linkbutton" iconCls="icon-ok" >保存</a>
	<a href="javascript:closeLocationAddDialog()" class="easyui-linkbutton" iconCls="icon-cancel" >关闭</a>
</div>
	<table id="mytable" title="单位列表" class="easyui-datagrid"
		pagination="true" fitColumns="false" rownumbers="true"  url="myunit?action=list"
		fit="true"  toolbar="#tb">
		<thead>
			<tr>
				<th field="cb" checkbox="true" align="center"></th>
				<th field="location_id" width="100" align="center">单位编号</th>
				<th field="area" width="200" align="center">单位区域</th>
				<th field="addr" width="150" align="center" >单位地址</th>
				<th field="location_type" width="150" align="center">场所类型</th>
				<th field="latitude" width="150" align="center" >经度</th>
				<th field="longtitude" width="150" align="center">纬度</th>
			</tr>
		</thead>
	</table>
</body>
</html>





