<%@ page language="java" import="java.util.*" pageEncoding="GB18030"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String name = request.getParameter("name");
String ip = request.getParameter("ip");
String location=new String( request.getParameter("location").getBytes("iso-8859-1"), "UTF-8");
%>
<!--
	���ܣ�����ip����������ƽ���
	����ʱ�䣺2016.04.09
	�����ˣ�mushao
  -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>�����ն��豸���������</title>
    
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
	//����ʱִ�еĺ���
	$(function() {
		$('#setValueDialog').dialog('close');//��������Ի���
		$('#addBssidInfoDialog').dialog('close');//�������bssidinfo�Ի���
		if(ping()){
				init();
		}
	});
	//�����ն��Ƿ����ͨ,������ͨ��ֱ�ӷ���
	function ping(){
		htmlobj=$.ajax({url:"myTerminalControl?ip="+"<%=ip%>"+"&type=&key=ping&value=",async:false});
		if(htmlobj.responseText!='alive'){
			alert("�ն��쳣���޷����ʣ�");
			window.location.href="tcontrol_choose.jsp";
			return false;
		}
		return true;
	}	
	//��ʼ������
	function init(){
		showMac();//��ʾmac
		showTime();//��ʾʱ��
		showVersion();//��ʾ�汾
		quickLoad();//���ڼ���,����ͨ��ע�͵��þ����ӿ�����ٶ�
	}
	//���ڼ����
	function quickLoad(){
		showFileLoadType();//��ʾ�ش���ʽ
		showMysqlConf();//��ʾmysql����
		showFtpConf();//��ʾftp����
		showTimeServer();//��ʾʱ�������
		showChannelSwitch();//��ʾ�ŵ�����
		showChannelStayTime();//�ŵ�ͣ��ʱ��
		showChannelStayTime();//������Ϣ���ʱ��
		showWpaLiveTime();//������Ϣ���ʱ��
		showBssidInfo();//��ʾ��Χwifi��Ϣ
		showProVersionState();//��ʾ�߼��汾״̬
		showPacketCompareFlag();//��ʾ����ȥ�ر�־
		showCacheSize();//��ʾ�����С
		showWriteInterval();//��ʾд���ݼ��
		checkDevice();//����豸״̬
	}
	//����ѡ��������Ӧ����	
	function back(){
		window.location.href="tcontrol_choose.jsp";
	}
	//�ر�����Ի���
	function input_close(){
		$('#setValueDialog').dialog('close');
	}
	//ͨ���Ի��������ò����ķ���
	function setConfigByDlg(func,key){
		//���ûص�����
		document.getElementById("input_ok").href="javascript:"+func+"('"+key+"')";
		$('#setValueDialog').dialog('open');
	}	
	//��ò����ĺ���
	function getConfig(key){
		htmlobj=$.ajax({url:"myTerminalControl?ip="+"<%=ip%>"+"&type=get&key="+key+"&value=",async:false});
		return htmlobj.responseText;
	}	
	//���ò����ķ���
	function setConfig(key,value){
		htmlobj=$.ajax({url:"myTerminalControl?ip="+"<%=ip%>"+"&type=set&key="+key+"&value="+value,async:false});
		if(htmlobj.responseText!="done"){
			$.messager.alert("����", "�����ն˲���ʧ�ܣ�","error");
			return;
		}
		$.messager.alert("��Ϣ","�����ն˲����ɹ���","info");
	}
	//��ʾmac
	function showMac(){
		$("#mac").html(getConfig("mac"));
	}
	//��ʾversion
	function showVersion(){
		$("#version").html(getConfig("version"));
	}
	//��ʾ�����£�ʱ��
	function showTime(){
		$("#cur_time").html(getConfig("time"));
	}
	//��ʾ�����£��ش���ʽ
	function showFileLoadType(){
		$("#cur_fileLoadType").html(getConfig("fileloadtype"));
	}
	//��ʾ�����£����ݿ����
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
	//��ʾ�����£�ftp����������
	function showFtpConf(){
		var json=getConfig("ftpconf");
		if(json){
			var conf=$.parseJSON(json);
			$("#ftphostname").html(conf.hostname);
			$("#ftpusername").html(conf.username);
			$("#ftppwd").html(conf.pwd);
		}
	}
	//��ʾ�����£�ʱ�������
	function showTimeServer(){
		$("#cur_timeServer").html(getConfig("timeserver"));
	}
	//��ʾ�����£���Ƶ����
	function showChannelSwitch(){
		var channelswitch=getConfig("channelswitch");
		switch(channelswitch){
			case '0':$("#cur_channelSwitch").html("��Ƶ����");break;
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
			case '13':$("#cur_channelSwitch").html("ͣ����"+channelswitch+"�ŵ�");break;
		}
	}
	//��ʾ�ŵ�ͣ��ʱ��
	function showChannelStayTime(){		
		var json=getConfig("channelstaytime");
		if(json){
		var staytime=$.parseJSON(json);
			for(index in staytime){
				$("#channelStayTime"+(parseInt(index)+1)).html(staytime[index]);
			}
		}
	}
	//��ʾwpa������Ϣ���ʱ��
	function showWpaLiveTime(){
		$("#cur_wpaLiveTime").html(getConfig("wpalivetime"));
	}
	//��ʾ��Χwifi��Ϣ
	function showBssidInfo(){
		var bssidlist=getConfig("bssidinfo");
		if(bssidlist){
			bssidlist = JSON.parse(bssidlist);
			$('#bssidList').datagrid("loadData",bssidlist);
		}
	}
	//��ʾ�߼��汾����
	function showProVersionState(){
		var state=getConfig("proversionstate");
		if(state){
			if('1'==state){
				document.getElementById("proVersionStateIcon").src="icon/icon_opened.png";
				$("#cur_proVersionState").html("����");
			}else{
				document.getElementById("proVersionStateIcon").src="icon/icon_closed.png";
				$("#cur_proVersionState").html("�ر�");
			}
		}
	}
	//��ʾȥ�ط�ʽ
	function showPacketCompareFlag(){
		var flag=getConfig("packetcompareflag");
		if(flag){
			flag=parseInt(flag);
			if(1==(flag&1)){//��stamacȥ��
				document.getElementById("stamacIcon").src="icon/icon_opened.png";
				$("#stamacCompareFlag").html("����");
			}else{
				document.getElementById("stamacIcon").src="icon/icon_closed.png";
				$("#stamacCompareFlag").html("�ر�");
			}
			if(2==(flag&2)){//��stamacȥ��
				document.getElementById("apmacIcon").src="icon/icon_opened.png";
				$("#apmacCompareFlag").html("����");
			}else{
				document.getElementById("apmacIcon").src="icon/icon_closed.png";
				$("#apmacCompareFlag").html("�ر�");
			}
			if(4==(flag&4)){//��stamacȥ��
				document.getElementById("appIcon").src="icon/icon_opened.png";
				$("#appCompareFlag").html("����");
			}else{
				document.getElementById("appIcon").src="icon/icon_closed.png";
				$("#appCompareFlag").html("�ر�");
			}
		}
	}	
	//��ʾ��������С
	function showCacheSize(){
		$('#cur_cacheSize').html(getConfig("cachesize"));
	}
	//��ʾд���ݼ��
	function showWriteInterval(){
		$('#cur_writeInterval').html(getConfig("writeinterval"));
	}	
	
	
	//����ʱ�����Ӧ����
	function setTime(){
		var time=$('#setTimeValue').datetimebox('getValue');
		if(time){
			setConfig("time",time);//�����ն˲���
			showTime();//������ʾʱ��
		}
	} 
	//�����ļ��ش���ʽ����Ӧ����
	function setFileLoadType(){
		var fileloadtype=$('#setFileLoadTypeValue').combobox('getValue');
		if(fileloadtype){
			setConfig("fileloadtype",fileloadtype);
			showFileLoadType();//���»ش���ʽ
		}
	}
	//����mysql����(ͨ����������)
	function setMysqlConf(key){
		$('#setValueDialog').dialog('close');//�ر����ô���
		//��ȡԭʼ����	
		var hostname=document.getElementById('dbhostname').innerText;
		var username=document.getElementById('dbusername').innerText;
		var pwd=document.getElementById('dbpwd').innerText;
		var db=document.getElementById('dbname').innerText;
		var port=document.getElementById('dbport').innerText;
		var input=document.getElementById('dialogInput').value;
		if(input){
			//�����޸�ֵ
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
	//����mysql����(ͨ����������)
	function setFtpConf(key){
		$('#setValueDialog').dialog('close');//�ر����ô���
		//��ȡԭʼ����	
		var hostname=document.getElementById('ftphostname').innerText;
		var username=document.getElementById('ftpusername').innerText;
		var pwd=document.getElementById('ftppwd').innerText;
		var input=document.getElementById('dialogInput').value;
		if(input){
			//�����޸�ֵ
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
	//����ʱ�����������Ӧ����
	function setTimeServer(){
		var timeserver=document.getElementById("setTimeServerValue").value;
		if(timeserver){
			setConfig("timeserver",timeserver);//�����ն˲���
			showTimeServer();//������ʾʱ��
		}
	}
	//�����ŵ�ͣ������
	function setChannelSwitch(){
		var channelswitch=$('#setChannelSwitchValue').combobox('getValue');
		if(channelswitch){
			setConfig("channelswitch",channelswitch);
			showChannelSwitch();//������ʾ
		}
	}
	//�����ŵ�ͣ��ʱ��
	function setChannelStayTime(index){
		$('#setValueDialog').dialog('close');//�ر����ô���
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
	//���ý�����Ϣ����ʱ��
	function setWpaLiveTime(){
		var livetime=document.getElementById("setWpaLiveTimeValue").value;
		if(livetime){
			setConfig("wpalivetime",livetime);
			showWpaLiveTime();//������ʾ
		}
	} 
	//��ʾ���bssid�ĶԻ���
	function showAddBssidInfoDialog(){
		$('#addBssidInfoDialog').dialog('open');
	}
	//�رհ�ť
	function addBssidInfoClose(){
		$('#addBssidInfoDialog').dialog('close');
	}
	//���bssid
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
	//ɾ��bssid
	function removeBssidInfo(){
		var row = $('#bssidList').datagrid('getSelected');
		if (row){
			var json="{\"name\":\""+row.name+"\",\"pwd\":\""+row.pwd+"\",\"mac\":\""+row.mac+"\"}";
			setConfig("removebssidinfo",json);
			showBssidInfo();
		}
	}
	//���ø߼��汾����
	function setProVersionState(){
		var state=$('#setProVersionStateValue').combobox('getValue');
		if(state){
			setConfig("proversionstate",state);
			showProVersionState();//������ʾ
		}
	}
	//����ȥ�ر�־λ
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
	//���û����С
	function setCacheSize(){
		var size=document.getElementById("setCacheSizeValue").value;
		if(size){
			setConfig("cachesize",size);
			showCacheSize();
		}
	}
	//����д����ʱ����
	function setWriteInterval(){
		var interval=document.getElementById("setWriteIntervalValue").value;
		if(interval){
			setConfig("writeinterval",interval);
			showWriteInterval();
		}
	}
	
	//����豸״̬
	function checkDevice(){
		var status=getConfig("status");
		if(status&&'running'==status){
			document.getElementById("stateIcon").src="icon/icon_opened.png";
			document.getElementById("deviceState").innerText="��������";
		}else{
			document.getElementById("stateIcon").src="icon/icon_closed.png";
			document.getElementById("deviceState").innerText="�����쳣��ر�";
		}
	}
	//��������
	function startPro(){
		if(confirm('�Ƿ���������')){
			var result=$.ajax({url:"myTerminalControl?ip="+"<%=ip%>"+"&type=control&key=startpro&value=",async:false});
			if("done"==result.responseText){
			 	$.messager.alert("��Ϣ","��������ɹ���","info");
			 	checkDevice();
			 	return;
			 }
			 $.messager.alert("����","��������ʧ�ܣ�","error");
			 checkDevice();
		}
		
	}
	//����ϵͳ
	function restartPro(){
		if(confirm('�Ƿ���������')){
			var result=$.ajax({url:"myTerminalControl?ip="+"<%=ip%>"+"&type=control&key=restartpro&value=",async:false});
			 if('done'==result.responseText){
			 	$.messager.alert("��Ϣ","��������ɹ���","info");
			 	checkDevice();
			 	return;
			 }
			 $.messager.alert("����","��������ʧ�ܣ�","error");
			 checkDevice();
		}
	}
	//�رճ���
	function stopPro(){
		if(confirm('�Ƿ�رճ���')){
			var result=$.ajax({url:"myTerminalControl?ip="+"<%=ip%>"+"&type=control&key=stoppro&value=",async:false});
			 if('done'==result.responseText){
			 	$.messager.alert("��Ϣ","�رճ���ɹ���","info");
			 	checkDevice();
			 	return;
			 }
			 $.messager.alert("����","�رճ���ʧ�ܣ�","error");
			 checkDevice();
		}
	}
	//�رճ���
	function reboot(){
		if(confirm('�Ƿ�����Ӳ���豸��')){
			var result=$.ajax({url:"myTerminalControl?ip="+"<%=ip%>"+"&type=control&key=reboot&value=",async:false});
			 if('done'==result.responseText){
			 	$.messager.alert("��Ϣ","����Ӳ���豸�ɹ���","info");
			 	checkDevice();
			 	return;
			 }
			 $.messager.alert("����","����Ӳ���豸ʧ�ܣ�","error");
			 checkDevice();
		}
	}
	
	</script>

  </head>
  
	<body>	
		<div id="setValueDialog" class="easyui-dialog" title="��������" data-options="iconCls:'icon-edit'" style="width:400px;padding:10px;model:true">
			<div style="margin-bottom:20px">
				<div style="margin-bottom:10px">������Ҫ���õ�ֵ:</div>
				<input id='dialogInput' style="width:100%;height:32px">
			</div>
			<table>
			<tr>
				<td>
					<a id="input_ok" class="easyui-linkbutton" iconCls="icon-ok" plain="true">����</a>
				</td>
				<td>
					<a href="javascript:input_close()" class="easyui-linkbutton" iconCls="icon-cancel" plain="true">ȡ��</a>
				</td>
			</tr>
			</table>
		</div>
		
		<div id="addBssidInfoDialog" class="easyui-dialog" title="����wifi��Ϣ" data-options="iconCls:'icon-pencil'" style="width:400px;padding:10px;model:true">
			<div style="margin-bottom:10px">������Ҫ���õ�ֵ:</div>
			<table>
				<tr>
					<td>wifi����</td>
					<td><input id='wifiName' style="width:100%;height:32px"></td>
				</tr>
				<tr>
					<td>wifi����</td>
					<td><input id='wifiPwd' style="width:100%;height:32px"></td>
				</tr>
				<tr>
					<td>wifi mac��ַ</td>
					<td><input id='wifiMac' style="width:100%;height:32px"></td>
				</tr>
			</table>
			<table>
			<tr>
				<td>
					<a href="javascript:addBssidInfoOk()" class="easyui-linkbutton" iconCls="icon-ok" plain="true">����</a>
				</td>
				<td>
					<a href="javascript:addBssidInfoClose()" class="easyui-linkbutton" iconCls="icon-cancel" plain="true">ȡ��</a>
				</td>
			</tr>
			</table>
		</div>
		<!--��ҳͷ�� -->
		<div style="background-color:#95B8E7;height:100px;overflow:auto">
			<br>
			<span style="font-size:30px;margin-top:20px;margin-left:20px;overflow:hidden">�ն��豸���������</span>
			<table style="height:30px;margin-top:10px;margin-left:20px">
				<tr>
					<td width=200px><%=name%></td>
					<td width=200px>IP:<%=ip%></td>
					<td width=200px>MAC:<span id="mac">--</span></td>
					<td width=200px>�ص㣺<%=location%></td>
					<td width=200px>ϵͳ�汾��<span id="version">--</span></td>
					<td width=200px>
						<a href="javascript:back()" class="easyui-linkbutton" iconCls="icon-back" plain="true">����ѡ�����</a>
					</td>	  		
				</tr>
			</table>
		</div>
		<div class="easyui-accordion">
			<div title="�ն˿���" data-options="iconCls:'icon-reload'" >
				<table border=1 style="margin-left:100px;margin-top:30px">
					<tr height=70px>
						<td width=200px align=center>�豸״̬��<img width=20px height=20px align="center" id="stateIcon" src="icon/icon_closed.png">&nbsp;&nbsp;<span id="deviceState">--</span></td>
						<td width=200px align=center>
							<a href="javascript:checkDevice()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">ˢ��</a>
						</td>
					</tr>
					<tr height=70px>
						<!--<td width=200px align=center>
							<a href="javascript:startPro()" class="easyui-linkbutton" iconCls="icon-ok" plain="true">��������</a>
						</td>
						-->
						<td width=200px align=center>
							<a href="javascript:restartPro()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">��������</a>
						</td>
						<!--
						<td width=200px align=center>
							<a href="javascript:stopPro()" class="easyui-linkbutton" iconCls="icon-undo" plain="true">�رճ���</a>
						</td>
						  -->
						<td width=200px align=center>
							<a href="javascript:reboot()" class="easyui-linkbutton" iconCls="icon-no" plain="true">Ӳ������</a>
						</td>
					</tr>
				</table>
			</div>
			<div title="�ն˲�������" data-options="iconCls:'icon-edit'" >
			<div class="easyui-accordion">
				<!-- ����ʱ�� -->
				<div title="�ն��豸ʱ��" data-options="iconCls:'icon-tip'" style="overflow:auto;padding:10px;">
					<table style="margin:30px">
						<tr>
							<td width=100px>
								<a href="javascript:showTime()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">ˢ��</a>
							</td>
							<td width=300px>��ǰϵͳʱ�䣺<span id="cur_time">--</span></td>
							<td>
								<input name="setTimeValue" id="setTimeValue" type="text" class="easyui-datetimebox" style="width:180px" />
							</td>
							<td>
								<a href="javascript:setTime()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">����</a>
							</td>
						</tr>
					</table>
				</div>
				<!-- ���ûش���ʽ -->
				<div title="���ݻش���ʽ" data-options="iconCls:'icon-tip'" style="overflow:auto;padding:10px;">
					<table style="margin:30px">
						<tr>
							<td width=100px>
								<a href="javascript:showFileLoadType()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">ˢ��</a>
							</td>
							<td width=300px>��ǰ�ش���ʽ��<span id="cur_fileLoadType">--</span></td>
							<td>
								<select id="setFileLoadTypeValue" class="easyui-combobox" style="width:180px;">
									<option value="ftp">ftp</option>
									<option value="mysql">mysql</option>
								</select>
							</td>
							<td>
								<a href="javascript:setFileLoadType()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">����</a>
							</td>
						</tr>
					</table>
				</div>
				<!-- ����mysql���� -->
				<div title="mysql���ݿ�" data-options="iconCls:'icon-tip'" style="overflow:auto;padding:10px;">
					<div width=100px style="margin-left:32px">
						<a href="javascript:showMysqlConf()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">ˢ��</a>
					</div>
					<table border='1' style="margin:30px;">
					<tr height=50px>
						<td width=100px>���ݿ�ip��</td>
						<td width=300px><span id="dbhostname">--</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setMysqlConf','dbhostname')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">����</a>
						</td>
					</tr>
					<tr height=50px>
						<td width=100px>�û�����</td>
						<td width=300px><span id="dbusername">--</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setMysqlConf','dbusername')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">����</a>
						</td>
					</tr>
					<tr height=50px>
						<td width=100px>���룺</td>
						<td width=300px><span id="dbpwd">--</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setMysqlConf','dbpwd')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">����</a>
						</td>
					</tr>
					<tr height=50px>
						<td width=100px>���ݿ�����</td>
						<td width=300px><span id="dbname">--</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setMysqlConf','dbname')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">����</a>
						</td>
					</tr>
					<tr height=50px>
						<td width=100px>�˿ڣ�</td>
						<td width=300px><span id="dbport">--</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setMysqlConf','dbport')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">����</a>
						</td>
					</tr>
					</table>
				</div>
				<!-- ����ftp���� -->
				<div title="ftp����" data-options="iconCls:'icon-tip'" style="overflow:auto;padding:10px;">
					<div width=100px style="margin-left:32px">
						<a href="javascript:showFtpConf()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">ˢ��</a>
					</div>
					<table border='1' style="margin:30px;">
					<tr height=50px>
						<td width=100px>ftp������ip��</td>
						<td width=300px><span id="ftphostname">--</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setFtpConf','ftphostname')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">����</a>
						</td>
					</tr>
					<tr height=50px>
						<td width=100px>�û�����</td>
						<td width=300px><span id="ftpusername">--</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setFtpConf','ftpusername')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">����</a>
						</td>
					</tr>
					<tr height=50px>
						<td width=100px>���룺</td>
						<td width=300px><span id="ftppwd">--</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setFtpConf','ftppwd')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">����</a>
						</td>
					</tr>
					</table>
				</div>
				<!-- ����ʱ������� -->
				<div title="ʱ�������" data-options="iconCls:'icon-tip'" style="overflow:auto;padding:10px;">
					<table style="margin:30px">
						<tr>
							<td width=100px>
								<a href="javascript:showTimeServer()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">ˢ��</a>
							</td>
							<td width=300px>ʱ���������<span id="cur_timeServer">--</span></td>
							<td>
								<input id='setTimeServerValue' style="width:180px;">
							</td>
							<td>
								<a href="javascript:setTimeServer()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">����</a>
							</td>
						</tr>
					</table>
				</div>
				<!-- �����ŵ���Ƶ���� -->
				<div title="�ŵ���Ƶ����" data-options="iconCls:'icon-tip'" style="overflow:auto;padding:10px;">
					<table style="margin:30px">
						<tr>
							<td width=100px>
								<a href="javascript:showChannelSwitch()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">ˢ��</a>
							</td>
							<td width=300px>��ǰ��Ƶ״̬��<span id="cur_channelSwitch">--</span></td>
							<td>
								<select id="setChannelSwitchValue" class="easyui-combobox" style="width:180px;">
									<option value="0">������Ƶ</option>
									<option value="1">�̶���1�ŵ�</option>
									<option value="2">�̶���2�ŵ�</option>
									<option value="3">�̶���3�ŵ�</option>
									<option value="4">�̶���4�ŵ�</option>
									<option value="5">�̶���5�ŵ�</option>
									<option value="6">�̶���6�ŵ�</option>
									<option value="7">�̶���7�ŵ�</option>
									<option value="8">�̶���8�ŵ�</option>
									<option value="9">�̶���9�ŵ�</option>
									<option value="10">�̶���10�ŵ�</option>
									<option value="11">�̶���11�ŵ�</option>
									<option value="12">�̶���12�ŵ�</option>
									<option value="13">�̶���13�ŵ�</option>
								</select>
							</td>
							<td>
								<a href="javascript:setChannelSwitch()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">����</a>
							</td>
						</tr>
					</table>
				</div>			
				<!-- �ŵ�ͣ��ʱ�� -->
				<div title="��Ƶ�ŵ�ͣ��ʱ��" data-options="iconCls:'icon-tip'" style="overflow:auto;padding:10px;">
					<div width=100px style="margin-left:32px">
						<a href="javascript:showChannelStayTime()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">ˢ��</a>
					</div>
					<table border='1' style="margin:30px;">
					<tr height=50px>
						<td width=100px>�ŵ�1��</td>
						<td width=300px><span id="channelStayTime1">--</span><span>ms</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setChannelStayTime','1')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">����</a>
						</td>
					</tr>
									<tr height=50px>
						<td width=100px>�ŵ�2��</td>
						<td width=300px><span id="channelStayTime2">--</span><span>ms</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setChannelStayTime','2')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">����</a>
						</td>
					</tr>
									<tr height=50px>
						<td width=100px>�ŵ�3��</td>
						<td width=300px><span id="channelStayTime3">--</span><span>ms</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setChannelStayTime','3')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">����</a>
						</td>
					</tr>
									<tr height=50px>
						<td width=100px>�ŵ�4��</td>
						<td width=300px><span id="channelStayTime4">--</span><span>ms</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setChannelStayTime','4')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">����</a>
						</td>
					</tr>
									<tr height=50px>
						<td width=100px>�ŵ�5��</td>
						<td width=300px><span id="channelStayTime5">--</span><span>ms</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setChannelStayTime','5')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">����</a>
						</td>
					</tr>
									<tr height=50px>
						<td width=100px>�ŵ�6��</td>
						<td width=300px><span id="channelStayTime6">--</span><span>ms</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setChannelStayTime','6')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">����</a>
						</td>
					</tr>
									<tr height=50px>
						<td width=100px>�ŵ�7��</td>
						<td width=300px><span id="channelStayTime7">--</span><span>ms</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setChannelStayTime','7')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">����</a>
						</td>
					</tr>
									<tr height=50px>
						<td width=100px>�ŵ�8��</td>
						<td width=300px><span id="channelStayTime8">--</span><span>ms</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setChannelStayTime','8')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">����</a>
						</td>
					</tr>
									<tr height=50px>
						<td width=100px>�ŵ�9��</td>
						<td width=300px><span id="channelStayTime9">--</span><span>ms</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setChannelStayTime','9')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">����</a>
						</td>
					</tr>
									<tr height=50px>
						<td width=100px>�ŵ�10��</td>
						<td width=300px><span id="channelStayTime10">--</span><span>ms</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setChannelStayTime','10')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">����</a>
						</td>
					</tr>
									<tr height=50px>
						<td width=100px>�ŵ�11��</td>
						<td width=300px><span id="channelStayTime11">--</span><span>ms</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setChannelStayTime','12')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">����</a>
						</td>
					</tr>
									<tr height=50px>
						<td width=100px>�ŵ�12��</td>
						<td width=300px><span id="channelStayTime12">--</span><span>ms</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setChannelStayTime','12')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">����</a>
						</td>
					</tr>
									<tr height=50px>
						<td width=100px>�ŵ�13��</td>
						<td width=300px><span id="channelStayTime13">--</span><span>ms</span></td>
						<td width=100px>
							<a href="javascript:setConfigByDlg('setChannelStayTime','13')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">����</a>
						</td>
					</tr>
					</table>
				</div>
				
				<!-- ���ý�����Ϣ����ʱ�� -->
				<div title="������Ϣ����ʱ��" data-options="iconCls:'icon-tip'" style="overflow:auto;padding:10px;">
					<table style="margin:30px">
						<tr>
							<td width=100px>
								<a href="javascript:showWpaLiveTime()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">ˢ��</a>
							</td>
							<td width=300px>������Ϣ����ʱ�䣺<span id="cur_wpaLiveTime">--</span>min</td>
							<td>
								<input id='setWpaLiveTimeValue' style="width:180px;">
							</td>
							<td>
								<a href="javascript:setWpaLiveTime()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">����</a>
							</td>
						</tr>
					</table>
				</div>
				
				<!-- ����bssidinfo -->
				<div title="��Χwifi��Ϣ" data-options="iconCls:'icon-tip'" style="overflow:auto;padding:10px;">
					<table id="bssidList" class="easyui-datagrid" title="wifi�б�" pagination="false" style="height:370px"
						 data-options="singleSelect:true,collapsible:true,method:'get',toolbar:'#toolbar'">
						<thead>
							<tr>
								<th data-options="field:'ck',checkbox:true"></th>
								<th data-options="field:'name',width:100">wifi����</th>
								<th data-options="field:'pwd',width:100">wifi����</th>
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
				<!-- �߼��汾���� -->
				<div title="�߼��汾����" data-options="iconCls:'icon-tip'" style="overflow:auto;padding:10px;">
					<table style="margin:30px">
						<tr>
							<td width=100px>
								<a href="javascript:showProVersionState()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">ˢ��</a>
							</td>
							<td width=300px>�߼��汾״̬�� <img id="proVersionStateIcon"  width=20px height=20px align="center" src="icon/icon_closed.png"><span>&nbsp;&nbsp;</span><span id="cur_proVersionState">--</span></td>
							<td>
								<select id="setProVersionStateValue" class="easyui-combobox" style="width:180px;">
									<option value="0">�رո߼��汾</option>
									<option value="1">�����߼��汾</option>
								</select>
							</td>
							<td>
								<a href="javascript:setProVersionState()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">����</a>
							</td>
						</tr>
					</table>
				</div>
				<!-- ����ȥ������ -->
				<div title="����ȥ������" data-options="iconCls:'icon-tip'" style="overflow:auto;padding:10px;">
					<a href="javascript:showPacketCompareFlag()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">ˢ��</a>
					<table>
						<tr>
							<td width=150px align="right">���ֻ�macȥ�أ�</td>
							<td width=150px> <img id="stamacIcon"  width=20px height=20px align="center" src="icon/icon_closed.png"><span>&nbsp;&nbsp;</span><span id="stamacCompareFlag">--</span></td>
							<td width=100px><a href="javascript:setPacketCompareFlag('stamac')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">�л�</a></td>
						</tr>
						<tr>
							<td width=150px align="right">��ap macȥ�أ�</td>
							<td width=150px> <img id="apmacIcon"  width=20px height=20px align="center" src="icon/icon_closed.png"><span>&nbsp;&nbsp;</span><span id="apmacCompareFlag">--</span></td>
							<td width=100px> <a href="javascript:setPacketCompareFlag('apmac')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">�л�</a></td>
						</tr>
						<tr>
							<td width=150px align="right">��Ӧ����Ϣȥ�أ�</td>
							<td width=150px> <img id="appIcon"  width=20px height=20px align="center" src="icon/icon_closed.png"><span>&nbsp;&nbsp;</span><span id="appCompareFlag">--</span></td>
							<td width=100px><a href="javascript:setPacketCompareFlag('app')" class="easyui-linkbutton" iconCls="icon-edit" plain="true">�л�</a></td>
						</tr>
					</table>
				</div>
				<!-- ���û���ռ��С -->
				<div title="����ռ��С" data-options="iconCls:'icon-tip'" style="overflow:auto;padding:10px;">
					<table style="margin:30px">
						<tr>
							<td width=100px>
								<a href="javascript:showCacheSize()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">ˢ��</a>
							</td>
							<td width=300px>����ռ��С��<span id="cur_cacheSize">--</span>Mb</td>
							<td>
								<input id='setCacheSizeValue' style="width:180px;">
							</td>
							<td>
								<a href="javascript:setCacheSize()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">����</a>
							</td>
						</tr>
					</table>
				</div>
				<!-- ����д����ʱ���� -->
				<div title="д����ʱ����" data-options="iconCls:'icon-tip'" style="overflow:auto;padding:10px;">
					<table style="margin:30px">
						<tr>
							<td width=100px>
								<a href="javascript:showWriteInterval()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">ˢ��</a>
							</td>
							<td width=300px>д����ʱ������<span id="cur_writeInterval">--</span>min</td>
							<td>
								<input id='setWriteIntervalValue' style="width:180px;">
							</td>
							<td>
								<a href="javascript:setWriteInterval()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">����</a>
							</td>
						</tr>
					</table>
				</div>						
			</div>
			</div>
		</div>
	</body>
</html>
