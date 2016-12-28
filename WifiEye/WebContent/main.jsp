<%@page import="com.wifi.dao.GetMacCollipseImpl,com.wifi.dao.GetMacCollipse"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Wifi_Eye系统</title>
<%
	if (session.getAttribute("currentUser") == null) {
		response.sendRedirect("login.jsp");
		return;
	}
%>
<link rel="stylesheet" type="text/css"
	href="jquery-easyui-1.3.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="jquery-easyui-1.3.3/themes/icon.css">
<script type="text/javascript" src="jquery-easyui-1.3.3/jquery.min.js"></script>
<script type="text/javascript" src="images/Clock.js"></script>
<script type="text/javascript"
	src="jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
	$(function() {
		var clock = new Clock();
		clock.display(document.getElementById("clock"));
		$("#tree").tree({
			lines : true,
			url : 'auth?action=menu&parentId=-1',
			onLoadSuccess : function() {
				$("#tree").tree('expandAll');
			},
			onClick : function(node) {
				if (node.text == '安全退出') {
					logout();
				} else if (node.text == '修改密码') {
					openPasswordModifyDialog();
				} else if (node.attributes.authPath) {

	openTab(node);
				}
			}
		});

		function logout() {
			$.messager.confirm('系统提示', '您确定要退出系统吗？', function(r) {
				if (r) {
					window.location.href = 'user?action=logout';
				}
			});
		}

		function openPasswordModifyDialog() {
			url = "user?action=modifyPassword";
			$("#dlg").dialog("open").dialog("setTitle", "修改密码");
		}

		function openTab(node) {
			if ($("#tabs").tabs("exists", node.text)) {
				$("#tabs").tabs("select", node.text);
			} else {
				var content = "<iframe frameborder=0 scrolling='auto' style='width:100%;height:100%' src="
						+ node.attributes.authPath + "></iframe>"
				$("#tabs").tabs("add", {
					title : node.text,
					iconCls : node.iconCls,
					closable : true,
					content : content,
				});
			}
		}

		$("#tabs").tabs({
			onBeforeClose : function(title, index) {
			    if(title == '碰撞分析'){
                 $.ajax({
                     url:"stop",});
				 return true;//关闭页面
				}
				return true;//关闭页面
			}

		});
	});

	function modifyPassword() {
		$("#fm").form("submit", {
			url : url,
			onSubmit : function() {
				var oldPassword = $("#oldPassword").val();
				var newPassword = $("#newPassword").val();
				var newPassword2 = $("#newPassword2").val();
				if (!$(this).form("validate")) {
					return false;
				}
				if (oldPassword != '${currentUser.password}') {
					$.messager.alert('系统提示', '用户名密码输入错误！');
					return false;
				}
				if (newPassword != newPassword2) {
					$.messager.alert('系统提示', '确认密码输入错误！');
					return false;
				}
				return true;
			},
			success : function(result) {
				var result = eval('(' + result + ')');
				if (result.errorMsg) {
					$.messager.alert('系统提示', result.errorMsg);
					return;
				} else {
					$.messager.alert('系统提示', '密码修改成功，下一次登录生效！');
					closePasswordModifyDialog();
				}
			}
		});
	}

	function closePasswordModifyDialog() {
		$("#dlg").dialog("close");
		$("#oldPassword").val("");
		$("#newPassword").val("");
		$("#newPassword2").val("");
	}
</script>
</head>
<body class="easyui-layout">
	<div region="north" style="height: 90px;">
		<DIV style="DISPLAY: block; HEIGHT: 54px"></DIV>
		<div
			style="BACKGROUND-IMAGE: url(images/bg_left_tc.gif); BACKGROUND-REPEAT: repeat-x; HEIGHT: 30px">
			<!-- <table>
	<tr>
		<td><img src="images/logo.png" height="60"/></td>
		<td valign="bottom">欢迎：${currentUser.userName }&nbsp;『${currentUser.roleName }』</td>
	</tr>
</table> -->
			<TABLE cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>

					<TR>
						<TD>
							<DIV>
								<IMG src="images/nav_pre.gif" align=absMiddle>
								欢迎：${currentUser.userName }&nbsp;『${currentUser.roleName }』
							</DIV>
						</TD>
						<TD align=right width="70%"><SPAN style="PADDING-RIGHT: 50px">
								<IMG src="images/menu_seprator.gif" align=absMiddle> <SPAN
								id=clock></SPAN> </SPAN></TD>
					</TR>
				</TBODY>
			</TABLE>
		</div>

	</div>
	<div region="center">
		<div class="easyui-tabs" fit="true" border="false" id="tabs">
			<div title="首页" data-options="iconCls:'icon-home'">
				<div align="center" style="padding-top: 100px;">
					<font color="red" size="10">欢迎使用</font>
				</div>
			</div>
		</div>
	</div>

	<div region="west" style="width: 200px;padding: 5px;" split="true">
		<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR>
					<TD width=10 height=29><IMG src="images/bg_left_tl.gif">
					</TD>
					<TD
						style="FONT-SIZE: 18px; BACKGROUND-IMAGE: url(images/bg_left_tc.gif); COLOR: white; FONT-FAMILY: system">主菜单</TD>
					<TD width=10><IMG src="images/bg_left_tr.gif"></TD>
				</TR>
				<TR>
					<TD style="BACKGROUND-IMAGE: url(images/bg_left_ls.gif)"></TD>
					<TD id=tree
						style="PADDING-RIGHT: 10px; PADDING-LEFT: 10px; PADDING-BOTTOM: 10px; PADDING-TOP: 10px; HEIGHT: 100%; BACKGROUND-COLOR: white"
						vAlign=top></TD>
					<TD style="BACKGROUND-IMAGE: url(images/bg_left_rs.gif)"></TD>
				</TR>
				<TR>
					<TD width=10><IMG src="images/bg_left_bl.gif"></TD>
					<TD style="BACKGROUND-IMAGE: url(images/bg_left_bc.gif)"></TD>
					<TD width=10><IMG src="images/bg_left_br.gif"></TD>
				</TR>
			</TBODY>
		</TABLE>
		<!-- <ul id="tree" class="easyui-tree"></ul> -->
	</div>
	<div region="south" style="height: 25px;padding: 5px;" align="center">
		版权所有 2015 -
		<script>
			document.write(new Date().getFullYear())
		</script>
		hust311
	</div>

	<div id="dlg" class="easyui-dialog"
		style="width: 400px;height: 220px;padding: 10px 20px" closed="true"
		modal="true" buttons="#dlg-buttons"
		data-options="iconCls:'icon-modifyPassword'">
		<form id="fm" method="post">
			<table cellspacing="4px;">
				<tr>
					<td>用户名：</td>
					<td><input type="hidden" name="userId" id="userId"
						value="${currentUser.userId }"><input type="text"
						name="userName" id="userName" readonly="readonly"
						value="${currentUser.userName }" style="width: 200px;" /></td>
				</tr>
				<tr>
					<td>原密码：</td>
					<td><input type="password" class="easyui-validatebox"
						name="oldPassword" id="oldPassword" style="width: 200px;"
						required="true" /></td>
				</tr>
				<tr>
					<td>新密码：</td>
					<td><input type="password" class="easyui-validatebox"
						name="newPassword" id="newPassword" style="width: 200px;"
						required="true" /></td>
				</tr>
				<tr>
					<td>确认新密码：</td>
					<td><input type="password" class="easyui-validatebox"
						name="newPassword2" id="newPassword2" style="width: 200px;"
						required="true" /></td>
				</tr>
			</table>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:modifyPassword()" class="easyui-linkbutton"
			iconCls="icon-ok">保存</a> <a
			href="javascript:closePasswordModifyDialog()"
			class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
	</div>

</body>
</html>