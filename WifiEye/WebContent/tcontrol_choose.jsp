<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!--
	功能：选择要配置和控制的终端
	创建时间：2016.04.08
	创建人：mushao
  -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>终端配置与控制</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" type="text/css" href="jquery-easyui-1.3.3/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="jquery-easyui-1.3.3/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="jquery-easyui-1.3.3/demo/demo.css">
	<script type="text/javascript" src="jquery-easyui-1.3.3/jquery.min.js"></script>
	<script type="text/javascript" src="jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
	<script>
	$(function(){
	$('#single_list').datagrid({
	onClickRow:function(index,data)
	{
	var row=$('#single_list').datagrid('getSelected');
	select_ticke_id = row.id;
	if(row)
	{
		document.getElementById('cur_devicename').innerText=row.device_name;
		document.getElementById('cur_ip').innerText=row.ip;
		document.getElementById('cur_location').innerText=row.area;
	}
	}
	})
	});
	
	function configSingle(){
		var name=document.getElementById('cur_devicename').innerText;
		var ip=document.getElementById('cur_ip').innerText;
		var location=document.getElementById('cur_location').innerText;
		if("--"==name||"--"==ip||"--"==location){
			//使用easyui message
			$.messager.confirm('错误', '请先选择要操作的终端设备！', null);
			return;
		}
		window.location.href="tcontrol_single.jsp?name="+name+"&&ip="+ip+"&&location="+location;
	}
	
	function formatState(value,row){
		switch(value){
			case 0:return '<img width=20px height=20px align="center" id="stateIcon" src="icon/icon_closed.png">';
			case 1:return '<img width=20px height=20px align="center" id="stateIcon" src="icon/icon_pro_err.png">';
			case 2:return '<img width=20px height=20px align="center" id="stateIcon" src="icon/icon_pro_err.png">';
			case 3:return '<img width=20px height=20px align="center" id="stateIcon" src="icon/icon_open.png">';
		}
	}
	</script>
  </head>
  <body>
  	<div class="easyui-accordion" style="width:900px">
  		<!-- 单个设备选择 -->
		<div title="单个终端设备" data-options="iconCls:'icon-ok'">
			<table>
				<tr>
					<td width=350px>
						<table class="easyui-datagrid" id="single_list" title="设备列表" style="width:350px;height:370px" pagination="true" 
							 data-options="singleSelect:true,collapsible:true,url:'mydevice?action=list',method:'get'">
							<thead>
								<tr>
									<th data-options="field:'ck',checkbox:true,width:20"></th>
									<th data-options="field:'device_name',width:85,align:'left'">设备编号</th>
									<th data-options="field:'ip',width:100,align:'left'">IP</th>
									<th data-options="field:'area',width:130,align:'left'">地点</th>
								</tr>
							</thead>
						</table>
					</td>
					<td width=550px>
						<div style="background-color:#95B8E7;height:220px">
							<span style="font-size:22px;color:green">
								<br/><br/><br/>
								&nbsp;&nbsp;&nbsp;&nbsp;当前设备：<label style="color:black" id="cur_devicename">--</label><br/><br/>
								&nbsp;&nbsp;&nbsp;&nbsp;设备ip：<label style="color:black" id="cur_ip">--</label><br/><br/>
								&nbsp;&nbsp;&nbsp;&nbsp;设备地点：<label style="color:black" id="cur_location">--</label><br/><br/>
							</span>
						</div>
						<div style="height:150px" align="center">
							<br/>
							<br/>
							<br/>
							<a href="javascript:configSingle()" style="width:400px;height:70px;background-color:green;text-align:center;border:0px" class="easyui-linkbutton"  plain="true">
								<br/>
								<span style="font-size:40px;margin:10px">配置与控制</span>
							</a>
						</div>
					</td>
				</tr>
			</table>
		</div>
		
		<div title="所有终端设备" data-options="iconCls:'icon-ok'">
			<table class="easyui-datagrid" title="设备列表" pagination="true" style="height:500px"
		 data-options="singleSelect:false,collapsible:true,url:'mydevice?action=list',method:'get'">
				<thead>
					<tr>
						<th data-options="field:'device_name',width:100">设备编号</th>
						<th data-options="field:'ip',width:100">IP</th>
						<th data-options="field:'area',width:140,align:'left'">地点</th>
						<th data-options="field:'status_time',width:140,align:'left'">状态更新时间</th>
						<th data-options="field:'status',align:'center',width:140,formatter:formatState">设备状态</th>
					</tr>
				</thead>
			</table>
					
		</div>
  </body>
</html>
