<%@ page language="java" import="java.util.*" pageEncoding="GB18030"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String name = request.getParameter("name");
String ip = request.getParameter("ip");
String location=new String( request.getParameter("location").getBytes("iso-8859-1"), "UTF-8");
%>
<!--
	功能：单个ip的配置与控制界面
	创建时间：2016.04.09
	创建人：mushao
  -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>单个终端设备配置与控制</title>
    
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
	<script type="text/javascript" src="jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
	<script>
	//加载时执行的函数
	$(function() {
		$('#setValueDialog').dialog('close');//隐藏输入对话框
		$('#addBssidInfoDialog').dialog('close');//隐藏添加bssidinfo对话框
		if(ping()){
				init();
		}
	});
	//测试终端是否可联通,不可联通则直接返回
	function ping(){
		htmlobj=$.ajax({url:"myTerminalControl?ip="+"<%=ip%>"+"&type=&key=ping&value=",async:false});
		if(htmlobj.responseText!='alive'){
			alert("终端异常，无法访问！");
			window.location.href="tcontrol_choose.jsp";
			return false;
		}
		return true;
	}	
	//初始化函数
	function init(){
		showMac();//显示mac
		showTime();//显示时间
		showVersion();//显示版本
		quickLoad();//先期加载,可以通过注释掉该句来加快加载速度
	}
	//先期间加载
	function quickLoad(){
		showFileLoadType();//显示回传方式
		showMysqlConf();//显示mysql参数
		showFtpConf();//显示ftp参数
		showTimeServer();//显示时间服务器
		showChannelSwitch();//显示信道开关
		showChannelStayTime();//信道停留时间
		showChannelStayTime();//解密信息存活时间
		showWpaLiveTime();//解密信息存活时间
		showBssidInfo();//显示周围wifi信息
		showProVersionState();//显示高级版本状态
		showPacketCompareFlag();//显示报文去重标志
		showCacheSize();//显示缓存大小
		showWriteInterval();//显示写数据间隔
		checkDevice();//检测设备状态
	}
	//返回选择界面的响应函数	
	function back(){
		window.location.href="tcontrol_choose.jsp";
	}
	//关闭输入对话框
	function input_close(){
		$('#setValueDialog').dialog('close');
	}
	//通过对话窗口设置参数的方法
	function setConfigByDlg(func,key){
		//设置回调函数
		document.getElementById("input_ok").href="javascript:"+func+"('"+key+"')";
		$('#setValueDialog').dialog('open');
	}	
	//获得参数的函数
	function getConfig(key){
		htmlobj=$.ajax({url:"myTerminalControl?ip="+"<%=ip%>"+"&type=get&key="+key+"&value=",async:false});
		return htmlobj.responseText;
	}	
	//设置参数的方法
	function setConfig(key,value){
		htmlobj=$.ajax({url:"myTerminalControl?ip="+"<%=ip%>"+"&type=set&key="+key+"&value="+value,async:false});
		if(htmlobj.responseText!="done"){
			$.messager.alert("错误", "设置终端参数失败！","error");
			return;
		}
		$.messager.alert("信息","设置终端参数成功！","info");
	}
	//显示mac
	function showMac(){
		$("#mac").html(getConfig("mac"));
	}
	//显示version
	function showVersion(){
		$("#version").html(getConfig("version"));
	}
	//显示（更新）时间
	function showTime(){
		$("#cur_time").html(getConfig("time"));
	}
	//显示（更新）回传方式
	function showFileLoadType(){
		$("#cur_fileLoadType").html(getConfig("fileloadtype"));
	}
	//显示（更新）数据库参数
	function showMysqlConf(){
		var json=getConfig("mysqlconf");
		if(json){
			var conf=$.parseJSON(json);
			$("#dbhostname").html(conf.hostname);
			$("#dbusername").html(conf.username);
			$("#dbpwd").html(conf.pwd);
			$("#dbname").html(conf.db);
			$("#dbport").html(conf.port);
		}
	}
	//显示（更新）ftp服务器参数
	function showFtpConf(){
		var json=getConfig("ftpconf");
		if(json){
			var conf=$.parseJSON(json);
			$("#ftphostname").html(conf.hostname);
			$("#ftpusername").html(conf.username);
			$("#ftppwd").html(conf.pwd);
		}
	}
	//显示（更新）时间服务器
	function showTimeServer(){
		$("#cur_timeServer").html(getConfig("timeserver"));
	}
	//显示（更新）跳频开关
	function showChannelSwitch(){
		var channelswitch=getConfig("channelswitch");
		switch(channelswitch){
			case '0':$("#cur_channelSwitch").html("跳频开启");break;
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case '10':
			case '11':
			case '12':
			case '13':$("#cur_channelSwitch").html("停留在"+channelswitch+"信道");break;
		}
	}
	//显示信道停留时间
	function showChannelStayTime(){		
		var json=getConfig("channelstaytime");
		if(json){
		var staytime=$.parseJSON(json);
			for(index in staytime){
				$("#channelStayTime"+(parseInt(index)+1)).html(staytime[index]);
			}
		}
	}
	//显示wpa解密信息存活时间
	function showWpaLiveTime(){
		$("#cur_wpaLiveTime").html(getConfig("wpalivetime"));
	}
	//显示周围wifi信息
	function showBssidInfo(){
		var bssidlist=getConfig("bssidinfo");
		if(bssidlist){
			bssidlist = JSON.parse(bssidlist);
			$('#bssidList').datagrid("loadData",bssidlist);
		}
	}
	//显示高级版本开关
	function showProVersionState(){
		var state=getConfig("proversionstate");
		if(state){
			if('1'==state){
				document.getElementById("proVersionStateIcon").src="icon/icon_opened.png";
				$("#cur_proVersionState").html("开启");
			}else{
				document.getElementById("proVersionStateIcon").src="icon/icon_closed.png";
				$("#cur_proVersionState").html("关闭");
			}
		}
	}
	//显示去重方式
	function showPacketCompareFlag(){
		var flag=getConfig("packetcompareflag");
		if(flag){
			flag=parseInt(flag);
			if(1==(flag&1)){//按stamac去重
				document.getElementById("stamacIcon").src="icon/icon_opened.png";
				$("#stamacCompareFlag").html("开启");
			}else{
				document.getElementById("stamacIcon").src="icon/icon_closed.png";
				$("#stamacCompareFlag").html("关闭");
			}
			if(2==(flag&2)){//按stamac去重
				document.getElementById("apmacIcon").src="icon/icon_opened.png";
				$("#apmacCompareFlag").html("开启");
			}else{
				document.getElementById("apmacIcon").src="icon/icon_closed.png";
				$("#apmacCompareFlag").html("关闭");
			}
			if(4==(flag&4)){//按stamac去重
				document.getElementById("appIcon").src="icon/icon_opened.png";
				$("#appCompareFlag").html("开启");
			}else{
				document.getElementById("appIcon").src="icon/icon_closed.png";
				$("#appCompareFlag").html("关闭");
			}
		}
	}	
	//显示缓存区大小
	function showCacheSize(){
		$('#cur_cacheSize').html(getConfig("cachesize"));
	}
	//显示写数据间隔
	function showWriteInterval(){
		$('#cur_writeInterval').html(getConfig("writeinterval"));
	}	
	
	
	//设置时间的响应函数
	function setTime(){
		var time=$('#setTimeValue').datetimebox('getValue');
		if(time){
			setConfig("time",time);//设置终端参数
			showTime();//更新显示时间
		}
	} 
	//设置文件回传方式的响应函数
	function setFileLoadType(){
		var fileloadtype=$('#setFileLoadTypeValue').combobox('getValue');
		if(fileloadtype){
			setConfig("fileloadtype",fileloadtype);
			showFileLoadType();//更新回传方式
		}
	}
	//设置mysql参数(通过窗口设置)
	function setMysqlConf(key){
		$('#setValueDialog').dialog('close');//关闭设置窗口
		//获取原始参数	
		var hostname=document.getElementById('dbhostname').innerText;
		var username=document.getElementById('dbusername').innerText;
		var pwd=document.getElementById('dbpwd').innerText;
		var db=document.getElementById('dbname').innerText;
		var port=document.getElementById('dbport').innerText;
		var input=document.getElementById('dialogInput').value;
		if(input){
			//更新修改值
			switch(key){
			case 'dbhostname':
				hostname=input;
				break;
			case 'dbusername':
				username=input;
				break;
			case 'dbpwd':
				pwd=input;
				break;
			case 'dbname':
				db=input;
				break;
			case 'dbport':
				port=input;
				break;
			}
			var json="{\"hostname\":\""+hostname+"\",\"username\":\""+username+"\",\"pwd\":\""+pwd+"\",\"db\":\""+db+"\",\"port\":\""+port+"\"}";
			setConfig("mysqlconf",json);
			showMysqlConf();
		}
	}
	//设置mysql参数(通过窗口设置)
	function setFtpConf(key){
		$('#setValueDialog').dialog('close');//关闭设置窗口
		//获取原始参数	
		var hostname=document.getElementById('ftphostname').innerText;
		var username=document.getElementById('ftpusername').innerText;
		var pwd=document.getElementById('ftppwd').innerText;
		var input=document.getElementById('dialogInput').value;
		if(input){
			//更新修改值
			switch(key){
			case 'ftphostname':
				hostname=input;
				break;
			case 'ftpusername':
				username=input;
				break;
			case 'ftppwd':
				pwd=input;
				break;
			}
			var json="{\"hostname\":\""+hostname+"\",\"username\":\""+username+"\",\"pwd\":\""+pwd+"\"}";
			setConfig("ftpconf",json);
			showFtpConf();
		}
	}
	//设置时间服务器的响应函数
	function setTimeServer(){
		var timeserver=document.getElementById("setTimeServerValue").value;
		if(timeserver){
			setConfig("timeserver",timeserver);//设置终端参数
			showTimeServer();//更新显示时间
		}
	}
	//设置信道停留开关
	function setChannelSwitch(){
		var channelswitch=$('#setChannelSwitchValue').combobox('getValue');
		if(channelswitch){
			setConfig("channelswitch",channelswitch);
			showChannelSwitch();//更新显示
		}
	}
	//设置信道停留时间
	function setChannelStayTime(index){
		$('#setValueDialog').dialog('close');//关闭设置窗口
		var staytime=new Array(13);
		for(var i=0;i<13;i++){
			staytime[i]=document.getElementById("channelStayTime"+(i+1)).innerText;
		}
		var input=document.getElementById('dialogInput').value;
		if(input){
			staytime[parseInt(index)-1]=input;
			setConfig("channelstaytime",JSON.stringify(staytime));
			showChannelStayTime();
		}
	}
	//设置解密信息保存时间
	function setWpaLiveTime(){
		var livetime=document.getElementById("setWpaLiveTimeValue").value;
		if(livetime){
			setConfig("wpalivetime",livetime);
			showWpaLiveTime();//更新显示
		}
	} 
	//显示添加bssid的对话框
	function showAddBssidInfoDialog(){
		$('#addBssidInfoDialog').dialog('open');
	}
	//关闭按钮
	function addBssidInfoClose(){
		$('#addBssidInfoDialog').dialog('close');
	}
	//添加bssid
	function addBssidInfoOk(){
		$('#addBssidInfoDialog').dialog('close');
		var wifiName=document.getElementById("wifiName").value;
		var wifiPwd=document.getElementById("wifiPwd").value;
		var wifiMac=document.getElementById("wifiMac").value;
		if(wifiName&&wifiPwd&&wifiMac){
			var json="{\"name\":\""+wifiName+"\",\"pwd\":\""+wifiPwd+"\",\"mac\":\""+wifiMac+"\"}";
			setConfig("addbssidinfo",json);
			showBssidInfo();
		}		
	}
	//删除bssid
	function removeBssidInfo(){
		var row = $('#bssidList').datagrid('getSelected');
		if (row){
			var json="{\"name\":\""+row.name+"\",\"pwd\":\""+row.pwd+"\",\"mac\":\""+row.mac+"\"}";
			setConfig("removebssidinfo",json);
			showBssidInfo();
		}
	}
	//设置高级版本开关
	function setProVersionState(){
		var state=$('#setProVersionStateValue').combobox('getValue');
		if(state){
			setConfig("proversionstate",state);
			showProVersionState();//更新显示
		}
	}
	//设置去重标志位
	function setPacketCompareFlag(type){
		var flag=getConfig("packetcompareflag");
		if(flag){
			flag=parseInt(flag);
			switch(type){
				case 'stamac':
					flag=(flag^1);
					break;
				case 'apmac':
					flag=(flag^2);
					break;
				case 'app':
					flag=(flag^4);
					break;
			}
			setConfig("packetcompareflag",flag);
			showPacketCompareFlag();
		}
	}
	//设置缓存大小
	function setCacheSize(){
		var size=document.getElementById("setCacheSizeValue").value;
		if(size){
			setConfig("cachesize",size);
			showCacheSize();
		}
	}
	//设置写数据时间间隔
	function setWriteInterval(){
		var interval=document.getElementById("setWriteIntervalValue").value;
		if(interval){
			setConfig("writeinterval",interval);
			showWriteInterval();
		}
	}
	
	//检测设备状态
	function checkDevice(){
		var status=getConfig("status");
		if(status&&'running'==status){
			document.getElementById("stateIcon").src="icon/icon_opened.png";
			document.getElementById("deviceState").innerText="运行正常";
		}else{
			document.getElementById("stateIcon").src="icon/icon_closed.png";
			document.getElementById("deviceState").innerText="运行异常或关闭";
		}
	}
	//启动程序
	function startPro(){
		if(confirm('是否启动程序？')){
			var result=$.ajax({url:"myTerminalControl?ip="+"<%=ip%>"+"&type=control&key=startpro&value=",async:false});
			if("done"==result.responseText){
			 	$.messager.alert("信息","启动程序成功！","info");
			 	checkDevice();
			 	return;
			 }
			 $.messager.alert("错误","启动程序失败！","error");
			 checkDevice();
		}
		
	}
	//重启系统
	function restartPro(){
		if(confirm('是否重启程序？')){
			var result=$.ajax({url:"myTerminalControl?ip="+"<%=ip%>"+"&type=control&key=restartpro&value=",async:false});
			 if('done'==result.responseText){
			 	$.messager.alert("信息","重启程序成功！","info");
			 	checkDevice();
			 	return;
			 }
			 $.messager.alert("错误","重启程序失败！","error");
			 checkDevice();
		}
	}
	//关闭程序
	function stopPro(){
		if(confirm('是否关闭程序？')){
			var result=$.ajax({url:"myTerminalControl?ip="+"<%=ip%>"+"&type=control&key=stoppro&value=",async:false});
			 if('done'==result.responseText){
			 	$.messager.alert("信息","关闭程序成功！","info");
			 	checkDevice();
			 	return;
			 }
			 $.messager.alert("错误","关闭程序失败！","error");
			 checkDevice();
		}
	}
	//关闭程序
	function reboot(){
		if(confirm('是否重启硬件设备？')){
			var result=$.ajax({url:"myTerminalControl?ip="+"<%=ip%>"+"&type=control&key=reboot&value=",async:false});
			 if('done'==result.responseText){
			 	$.messager.alert("信息","重启硬件设备成功！","info");
			 	checkDevice();
			 	return;
			 }
			 $.messager.alert("错误","重启硬件设备失败！","error");
			 checkDevice();
		}
	}
	
	</script>

  </head>
  
	<body>	
		<div id="setValueDialog" class="easyui-dialog" title="参数设置" data-options="iconCls:'icon-edit'" style="width:400px;padding:10px;model:true">
			<div style="margin-bottom:20px">
				<div style="margin-bottom:10px">请输入要设置的值:</div>
				<input id='dialogInput' style="width:100%;height:32px">
			</div>
			<table>
			<tr>
				<td>
					<a id="input_ok" class="easyui-linkbutton" iconCls="icon-ok" plain="true">设置</a>
				</td>
				<td>
					<a href="javascript:input_close()" class="easyui-linkbutton" iconCls="icon-cancel" plain="true">取消</a>
				</td>
			</tr>
			</table>
		</div>
		
		<div id="addBssidInfoDialog" class="easyui-dialog" title="增加wifi信息" data-options="iconCls:'icon-pencil'" style="width:400px;padding:10px;model:true">
			<div style="margin-bottom:10px">请输入要设置的值:</div>
			<table>
				<tr>
					<td>wifi名称</td>
					<td><input id='wifiName' style="width:100%;height:32px"></td>
				</tr>
				<tr>
					<td>wifi密码</td>
					<td><input id='wifiPwd' style="width:100%;height:32px"></td>
				</tr>
				<tr>
					<td>wifi mac地址</td>
					<td><input id='wifiMac' style="width:100%;height:32px"></td>
				</tr>
			</table>
			<table>
			<tr>
				<td>
					<a href="javascript:addBssidInfoOk()" class="easyui-linkbutton" iconCls="icon-ok" plain="true">设置</a>
				</td>
				<td>
					<a href="javascript:addBssidInfoClose()" class="easyui-linkbutton" iconCls="icon-cancel" plain="true">取消</a>
				</td>
			</tr>
			</table>
		</div>
		<!--网页头部 -->
		<div style="background-color:#95B8E7;height:100px;overflow:auto">
			<br>
			<span style="font-size:30px;margin-top:20px;margin-left:20px;overflow:hidden">终端设备配置与控制</span>
			<table style="height:30px;margin-top:10px;margin-left:20px">
				<tr>
					<td width=200px><%=name%></td>
					<td width=200px>IP:<%=ip%></td>
					<td width=200px>MAC:<span id="mac">--</span></td>
					<td width=200px>地点：<%=location%></td>
					<td width=200px>系统版本：<span id="version">--</span></td>
					<td width=200px>
						<a href="javascript:back()" class="easyui-linkbutton" iconCls="icon-back" plain="true">返回选择界面</a>
					</td>	  		
				</tr>
			</table>
		</div>
		<div class="easyui-accordion">
			<div title="终端控制" data-options="iconCls:'icon-reload'" >
				<table border=1 style="margin-left:100px;margin-top:30px">
					<tr height=70px>
						<td width=200px align=center>设备状态：<img width=20px height=20px align="center" id="stateIcon" src="icon/icon_closed.png">&nbsp;&nbsp;<span id="deviceState">--</span></td>
						<td width=200px align=center>
							<a href="javascript:checkDevice()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷新</a>
						</td>
					</tr>
					<tr height=70px>
						<!--<td width=200px align=center>
							<a href="javascript:startPro()" class="easyui-linkbutton" iconCls="icon-ok" plain="true">启动程序</a>
						</td>
						-->
						<td width=200px align=center>
							<a href="javascript:restartPro()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">重启程序</a>
						</td>
						<!--
						<td width=200px align=center>
							<a href="javascript:stopPro()" class="easyui-linkbutton" iconCls="icon-undo" plain="true">关闭程序</a>
						</td>
						  -->
						<td width=200px align=center>
							<a href="javascript:reboot()" class="easyui-linkbutton" iconCls="icon-no" plain="true">硬件重启</a>
						</td>
					</tr>
				</table>
			</div>
			<div title="终端参数配置" data-options="iconCls:'icon-edit'" >
			<div class="easyui-accordion">
				<!-- 设置时间 -->
				<div title="终端设备时间" data-options="iconCls:'icon-tip'" style="overflow:auto;padding:10px;">
					<table style="margin:30px">
						<tr>
							<td width=100px>
								<a href="javascript:showTime()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷新</a>
							</td>
							<td width=300px>当前系统时间：<span id="cur_time">--</span></td>
							<td>
								<input name="setTimeValue" id="setTimeValue" type="text" class="easyui-datetimebox" style="width:180px" />
							</td>
							<td>
								<a href="javascript:setTime()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">设置</a>
							</td>
						</tr>
					</table>
				</div>
				<!-- 设置回传方式 -->
				<div title="数据回传方式" data-options="iconCls:'icon-tip'" style="overflow:auto;padding:10px;">
					<table style="margin:30px">
						<tr>
							<td width=100px>
								<a href="javascript:showFileLoadType()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷新</a>
							</td>
							<td width=300px>当前回传方式：<span id="cur_fileLoadType">--</span></td>
							<td>
								<select id="setFileLoadTypeValue" class="easyui-combobox" style="width:180px;">
									<option value="ftp">ftp</option>
									<option value="mysql">mysql</option>
								</select>
							</td>
							<td>
								<a href="javascript:setFileLoadType()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">设置</a>
							</td>
						</tr>
					</table>
				</div>
				<!-- 设置mysql参数 -->
				<div title="mysql数据库" data-options="iconCls:'icon-tip'" style="overflow:auto;padding:10px;">
					<div width=100px style="margin-left:32px">
						<a href="javascript:showMysqlConf()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷新</a>
					</div>
					<table border='1' style="margin:30px;">
					<tr height=50px>
						<td width=100px>数据库ip：</td>
						<td width=300px><span id="dbhostname">--</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setMysqlConf','dbhostname')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">设置</a>
						</td>
					</tr>
					<tr height=50px>
						<td width=100px>用户名：</td>
						<td width=300px><span id="dbusername">--</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setMysqlConf','dbusername')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">设置</a>
						</td>
					</tr>
					<tr height=50px>
						<td width=100px>密码：</td>
						<td width=300px><span id="dbpwd">--</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setMysqlConf','dbpwd')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">设置</a>
						</td>
					</tr>
					<tr height=50px>
						<td width=100px>数据库名：</td>
						<td width=300px><span id="dbname">--</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setMysqlConf','dbname')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">设置</a>
						</td>
					</tr>
					<tr height=50px>
						<td width=100px>端口：</td>
						<td width=300px><span id="dbport">--</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setMysqlConf','dbport')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">设置</a>
						</td>
					</tr>
					</table>
				</div>
				<!-- 设置ftp参数 -->
				<div title="ftp参数" data-options="iconCls:'icon-tip'" style="overflow:auto;padding:10px;">
					<div width=100px style="margin-left:32px">
						<a href="javascript:showFtpConf()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷新</a>
					</div>
					<table border='1' style="margin:30px;">
					<tr height=50px>
						<td width=100px>ftp服务器ip：</td>
						<td width=300px><span id="ftphostname">--</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setFtpConf','ftphostname')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">设置</a>
						</td>
					</tr>
					<tr height=50px>
						<td width=100px>用户名：</td>
						<td width=300px><span id="ftpusername">--</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setFtpConf','ftpusername')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">设置</a>
						</td>
					</tr>
					<tr height=50px>
						<td width=100px>密码：</td>
						<td width=300px><span id="ftppwd">--</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setFtpConf','ftppwd')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">设置</a>
						</td>
					</tr>
					</table>
				</div>
				<!-- 设置时间服务器 -->
				<div title="时间服务器" data-options="iconCls:'icon-tip'" style="overflow:auto;padding:10px;">
					<table style="margin:30px">
						<tr>
							<td width=100px>
								<a href="javascript:showTimeServer()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷新</a>
							</td>
							<td width=300px>时间服务器：<span id="cur_timeServer">--</span></td>
							<td>
								<input id='setTimeServerValue' style="width:180px;">
							</td>
							<td>
								<a href="javascript:setTimeServer()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">设置</a>
							</td>
						</tr>
					</table>
				</div>
				<!-- 设置信道调频参数 -->
				<div title="信道跳频开关" data-options="iconCls:'icon-tip'" style="overflow:auto;padding:10px;">
					<table style="margin:30px">
						<tr>
							<td width=100px>
								<a href="javascript:showChannelSwitch()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷新</a>
							</td>
							<td width=300px>当前跳频状态：<span id="cur_channelSwitch">--</span></td>
							<td>
								<select id="setChannelSwitchValue" class="easyui-combobox" style="width:180px;">
									<option value="0">开启跳频</option>
									<option value="1">固定在1信道</option>
									<option value="2">固定在2信道</option>
									<option value="3">固定在3信道</option>
									<option value="4">固定在4信道</option>
									<option value="5">固定在5信道</option>
									<option value="6">固定在6信道</option>
									<option value="7">固定在7信道</option>
									<option value="8">固定在8信道</option>
									<option value="9">固定在9信道</option>
									<option value="10">固定在10信道</option>
									<option value="11">固定在11信道</option>
									<option value="12">固定在12信道</option>
									<option value="13">固定在13信道</option>
								</select>
							</td>
							<td>
								<a href="javascript:setChannelSwitch()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">设置</a>
							</td>
						</tr>
					</table>
				</div>			
				<!-- 信道停留时间 -->
				<div title="跳频信道停留时间" data-options="iconCls:'icon-tip'" style="overflow:auto;padding:10px;">
					<div width=100px style="margin-left:32px">
						<a href="javascript:showChannelStayTime()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷新</a>
					</div>
					<table border='1' style="margin:30px;">
					<tr height=50px>
						<td width=100px>信道1：</td>
						<td width=300px><span id="channelStayTime1">--</span><span>ms</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setChannelStayTime','1')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">设置</a>
						</td>
					</tr>
									<tr height=50px>
						<td width=100px>信道2：</td>
						<td width=300px><span id="channelStayTime2">--</span><span>ms</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setChannelStayTime','2')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">设置</a>
						</td>
					</tr>
									<tr height=50px>
						<td width=100px>信道3：</td>
						<td width=300px><span id="channelStayTime3">--</span><span>ms</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setChannelStayTime','3')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">设置</a>
						</td>
					</tr>
									<tr height=50px>
						<td width=100px>信道4：</td>
						<td width=300px><span id="channelStayTime4">--</span><span>ms</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setChannelStayTime','4')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">设置</a>
						</td>
					</tr>
									<tr height=50px>
						<td width=100px>信道5：</td>
						<td width=300px><span id="channelStayTime5">--</span><span>ms</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setChannelStayTime','5')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">设置</a>
						</td>
					</tr>
									<tr height=50px>
						<td width=100px>信道6：</td>
						<td width=300px><span id="channelStayTime6">--</span><span>ms</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setChannelStayTime','6')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">设置</a>
						</td>
					</tr>
									<tr height=50px>
						<td width=100px>信道7：</td>
						<td width=300px><span id="channelStayTime7">--</span><span>ms</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setChannelStayTime','7')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">设置</a>
						</td>
					</tr>
									<tr height=50px>
						<td width=100px>信道8：</td>
						<td width=300px><span id="channelStayTime8">--</span><span>ms</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setChannelStayTime','8')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">设置</a>
						</td>
					</tr>
									<tr height=50px>
						<td width=100px>信道9：</td>
						<td width=300px><span id="channelStayTime9">--</span><span>ms</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setChannelStayTime','9')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">设置</a>
						</td>
					</tr>
									<tr height=50px>
						<td width=100px>信道10：</td>
						<td width=300px><span id="channelStayTime10">--</span><span>ms</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setChannelStayTime','10')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">设置</a>
						</td>
					</tr>
									<tr height=50px>
						<td width=100px>信道11：</td>
						<td width=300px><span id="channelStayTime11">--</span><span>ms</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setChannelStayTime','12')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">设置</a>
						</td>
					</tr>
									<tr height=50px>
						<td width=100px>信道12：</td>
						<td width=300px><span id="channelStayTime12">--</span><span>ms</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setChannelStayTime','12')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">设置</a>
						</td>
					</tr>
									<tr height=50px>
						<td width=100px>信道13：</td>
						<td width=300px><span id="channelStayTime13">--</span><span>ms</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setChannelStayTime','13')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">设置</a>
						</td>
					</tr>
					</table>
				</div>
				
				<!-- 设置解密信息保留时间 -->
				<div title="解密信息保留时间" data-options="iconCls:'icon-tip'" style="overflow:auto;padding:10px;">
					<table style="margin:30px">
						<tr>
							<td width=100px>
								<a href="javascript:showWpaLiveTime()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷新</a>
							</td>
							<td width=300px>解密信息保留时间：<span id="cur_wpaLiveTime">--</span>min</td>
							<td>
								<input id='setWpaLiveTimeValue' style="width:180px;">
							</td>
							<td>
								<a href="javascript:setWpaLiveTime()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">设置</a>
							</td>
						</tr>
					</table>
				</div>
				
				<!-- 设置bssidinfo -->
				<div title="周围wifi信息" data-options="iconCls:'icon-tip'" style="overflow:auto;padding:10px;">
					<table id="bssidList" class="easyui-datagrid" title="wifi列表" pagination="false" style="height:370px"
						 data-options="singleSelect:true,collapsible:true,method:'get',toolbar:'#toolbar'">
						<thead>
							<tr>
								<th data-options="field:'ck',checkbox:true"></th>
								<th data-options="field:'name',width:100">wifi名称</th>
								<th data-options="field:'pwd',width:100">wifi密码</th>
								<th data-options="field:'mac',width:140,align:'left'">wifi mac</th>
							</tr>
						</thead>
					</table>
					<div id="toolbar">
						<a href="javascript:showBssidInfo()" class="easyui-linkbutton" iconCls="icon-reload" plain="true"></a>
						<a href="javascript:showAddBssidInfoDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true"></a>
						<a href="javascript:removeBssidInfo()" class="easyui-linkbutton" iconCls="icon-cancel" plain="true"></a>
					</div>
				</div>
				<!-- 高级版本开关 -->
				<div title="高级版本开关" data-options="iconCls:'icon-tip'" style="overflow:auto;padding:10px;">
					<table style="margin:30px">
						<tr>
							<td width=100px>
								<a href="javascript:showProVersionState()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷新</a>
							</td>
							<td width=300px>高级版本状态： <img id="proVersionStateIcon"  width=20px height=20px align="center" src="icon/icon_closed.png"><span>&nbsp;&nbsp;</span><span id="cur_proVersionState">--</span></td>
							<td>
								<select id="setProVersionStateValue" class="easyui-combobox" style="width:180px;">
									<option value="0">关闭高级版本</option>
									<option value="1">开启高级版本</option>
								</select>
							</td>
							<td>
								<a href="javascript:setProVersionState()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">设置</a>
							</td>
						</tr>
					</table>
				</div>
				<!-- 报文去重条件 -->
				<div title="报文去重条件" data-options="iconCls:'icon-tip'" style="overflow:auto;padding:10px;">
					<a href="javascript:showPacketCompareFlag()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷新</a>
					<table>
						<tr>
							<td width=150px align="right">按手机mac去重：</td>
							<td width=150px> <img id="stamacIcon"  width=20px height=20px align="center" src="icon/icon_closed.png"><span>&nbsp;&nbsp;</span><span id="stamacCompareFlag">--</span></td>
							<td width=100px><a href="javascript:setPacketCompareFlag('stamac')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">切换</a></td>
						</tr>
						<tr>
							<td width=150px align="right">按ap mac去重：</td>
							<td width=150px> <img id="apmacIcon"  width=20px height=20px align="center" src="icon/icon_closed.png"><span>&nbsp;&nbsp;</span><span id="apmacCompareFlag">--</span></td>
							<td width=100px> <a href="javascript:setPacketCompareFlag('apmac')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">切换</a></td>
						</tr>
						<tr>
							<td width=150px align="right">按应用信息去重：</td>
							<td width=150px> <img id="appIcon"  width=20px height=20px align="center" src="icon/icon_closed.png"><span>&nbsp;&nbsp;</span><span id="appCompareFlag">--</span></td>
							<td width=100px><a href="javascript:setPacketCompareFlag('app')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">切换</a></td>
						</tr>
					</table>
				</div>
				<!-- 设置缓存空间大小 -->
				<div title="缓存空间大小" data-options="iconCls:'icon-tip'" style="overflow:auto;padding:10px;">
					<table style="margin:30px">
						<tr>
							<td width=100px>
								<a href="javascript:showCacheSize()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷新</a>
							</td>
							<td width=300px>缓存空间大小：<span id="cur_cacheSize">--</span>Mb</td>
							<td>
								<input id='setCacheSizeValue' style="width:180px;">
							</td>
							<td>
								<a href="javascript:setCacheSize()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">设置</a>
							</td>
						</tr>
					</table>
				</div>
				<!-- 设置写数据时间间隔 -->
				<div title="写数据时间间隔" data-options="iconCls:'icon-tip'" style="overflow:auto;padding:10px;">
					<table style="margin:30px">
						<tr>
							<td width=100px>
								<a href="javascript:showWriteInterval()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷新</a>
							</td>
							<td width=300px>写数据时间间隔：<span id="cur_writeInterval">--</span>min</td>
							<td>
								<input id='setWriteIntervalValue' style="width:180px;">
							</td>
							<td>
								<a href="javascript:setWriteInterval()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">设置</a>
							</td>
						</tr>
					</table>
				</div>						
			</div>
			</div>
		</div>
	</body>
</html>
