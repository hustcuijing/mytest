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
		init();
	});
	function init() {
		$.ajax({
			        type: "POST",
			        contentType:'application/json;charset=UTF-8',
			        url: "mysysconf?action=list",
			        cache: false,
			        dataType : "json",
			        success: function(data){	
			        	$('#myform').form("load",data.data);
			        }
		});
	}
	$.extend($.fn.validatebox.defaults.rules, {   
        checkIp : {// 验证IP地址   
            validator : function(value) {   
                var reg = /^((1?\d?\d|(2([0-4]\d|5[0-5])))\.){3}(1?\d?\d|(2([0-4]\d|5[0-5])))$/ ;   
                return reg.test(value);   
            },   
            message : 'IP地址格式不正确'  
    }   
});   
	function save(){
		$('#myform').form(
				"submit",
				{
					url : "mysysconf?action=save",
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
						}
					}
				});
	}
</script>
</head>

<body style="margin: 1px;">
	<!--操作栏  -->
	<div id="tb">
		<br>
		<form id="myform" method="post">
		<div style="margin:20px">
			<lable>存活时间&时间服务器:</lable>
			<br>
			<div style="border:1px solid  #AAAAAA">
				<div style="margin:20px">
					<label>wpa解密信息存活时间：</label> <input id="wpaLiveTime"
						name="wpaLiveTime" size="20" class="easyui-numberbox" required="true"/> <br> <label>时间服务器地址：</label>
					<input id="timeServer" name="timeServer" size="20" class="easyui-validatebox" required="true" validType="checkIp"/>
				</div>
			</div>
			<br>
			<lable>数据库连接:</lable>
			<div style="border:1px solid  #AAAAAA">
				<div style="margin:20px">
					<label>数据库连接url：</label> <input id="url" name="url" size="20" class="easyui-validatebox" required="true" validType="checkIp"/> <br>
					<label>数据库名字：</label> <input id="database" name="database"
						size="20" class="easyui-validatebox" required="true"/> <br> <label>用户名：</label> <input id="username"
						name="username" size="20" class="easyui-validatebox" required="true"/> <br> <label>用户密码：</label> <input
						id="password" name="password" size="20" class="easyui-validatebox" required="true"/>
				</div>
			</div>
			<br>
			<lable>信道停留时间（毫秒，1-13信道）:</lable>
			<div style="border:1px solid  #AAAAAA">
				<div style="margin:20px">
					<label>1=</label> <input id="one" name="one" size="20" class="easyui-numberbox" required="true"/> <br>
					<label>2=</label> <input id="two" name="two" size="20" class="easyui-numberbox" required="true"/> <br>
					<label>3=</label> <input id="three" name="three" size="20" class="easyui-numberbox" required="true"/> <br>
					<label>4=</label> <input id="four" name="four" size="20" class="easyui-numberbox" required="true"/> <br>
					<label>5=</label> <input id="five" name="five" size="20" class="easyui-numberbox" required="true"/> <br>
					<label>6=</label> <input id="six" name="six" size="20" class="easyui-numberbox" required="true"/> <br>
					<label>7=</label> <input id="seven" name="seven" size="20" class="easyui-numberbox" required="true"/> <br>
					<label>8=</label> <input id="eight" name="eight" size="20" class="easyui-numberbox" required="true"/> <br>
					<label>9=</label> <input id="nine" name="nine" size="20" class="easyui-numberbox" required="true"/> <br>
					<label>10=</label> <input id="ten" name="ten" size="20" class="easyui-numberbox" required="true"/> <br>
					<label>11=</label> <input id="eleven" name="eleven" size="20" class="easyui-numberbox" required="true"/> <br>
					<label>12=</label> <input id="twelve" name="twelve" size="20" class="easyui-numberbox" required="true"/> <br>
					<label>13=</label> <input id="thirteen" name="thirteen" size="20" class="easyui-numberbox" required="true"/>
				</div>
			</div>
			<br>
			<div align="right">
				<a href="javascript:save()" class="easyui-linkbutton"
					iconCls="icon-save" plain="true">保存</a>
			</div>

		</div>
		<br>
		</form>
	</div>

</body>
</html>





