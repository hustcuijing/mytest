package com.wifi.servlet;

import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.hust311.terminal.*;
import com.hust311.terminal.config.BssidInfo;
import com.hust311.terminal.config.FtpConf;
import com.hust311.terminal.config.MysqlConf;

/**
 * 终端控制的servlet
 * @author mushao
 */
public class TerminalControlServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TerminalControlServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String ip=request.getParameter("ip");//获得要操作的设备ip		
		String type = request.getParameter("type");//操作类型
		String  key= request.getParameter("key");//要设置的键
		String value=request.getParameter("value");//要设置的值
		PrintWriter writer=response.getWriter();
		
		//响应ping
		if(key.equals("ping")){
			if(Terminal.isAlive(ip)){
				writer.append("alive");
			}
			return;
		}
		/*此处设计：
		 * （1）由于与终端的连接属于昂贵资源，因此将其保存在session中以复用，提高响应速度
		 * （2）由于有多个终端，在一次登录中可能用到多个终端，因此使用map来存储多个ip与Terminal对，因此session中实际存的是HashMap
		 * （3）与终端的连接需要手动释放，因此要考虑在每次退出登录时遍历Map释放资源
		 * （4）由于采用的session，所以多个客户端之间并没有共享资源，所以不存在线程安全问题
		 */
		//获取Map
		Object object=request.getSession().getAttribute("terminalBeans");
		Map<String,Terminal> terminalBeans=null;
		if(null==object){//如果容器不存在则创建容器
			System.out.println("不存在Terminal容器，创建并加入session");
			terminalBeans=new HashMap<String,Terminal>();
			request.getSession().setAttribute("terminalBeans", terminalBeans);			
		}else{//存在则获取容器
			System.out.println("存在与Terminal容器，直接使用");
			terminalBeans=(Map<String, Terminal>)object;
		}
		
		//获取Terminal
		Terminal terminal=terminalBeans.get(ip);
		if(null==terminal||!terminal.getSsh().getConnected()){
			try{
				terminal=new Terminal(ip, "root", "hust311");//可能抛出IOException
				System.out.println("不存在与终端的连接或连接失效，创建并加入容器");
				terminalBeans.put(ip, terminal);
			}catch(Exception e){
				System.out.println("TerminalControlServlet.doPost()：创建连接失败");
				writer.append("error");
				return;
			}
		}else{
			System.out.println("存在与终端的连接直接使用");
		}
				
		//获取参数部分
		if(type.equals("get")){
			//获得mac
			if(key.equals("mac")){
				writer.append(terminal.getMac());
			}
			//获得version
			if(key.equals("version")){
				writer.append(terminal.getVersion());
			}
			//获取时间
			if(key.equals("time")){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date time=terminal.getTime();
				if(null!=time){
					writer.append(sdf.format(time));
				}
				return;
			}
			//获得回传方式
			if(key.equals("fileloadtype")){
				Integer loadType=terminal.getFileLoadType();
				if(null!=loadType){
					switch (loadType) {
					case 0:
						writer.append("ftp");
						break;
					case 1:
						writer.append("mysql");
					default:
						break;
					}
				}
				return;
			}
			//获得mysql参数
			if(key.equals("mysqlconf")){
				MysqlConf conf=terminal.getMysqlConf();
				if(null!=conf){
					writer.append(JSONObject.fromObject(conf).toString());//以json方式传送
				}
				return;
			}
			//获得ftp参数
			if(key.equals("ftpconf")){
				FtpConf conf=terminal.getFtpConf();
				if(null!=conf){
					writer.append(JSONObject.fromObject(conf).toString());//以json方式传送
				}
				return;
			}
			//获得时间服务器
			if(key.equals("timeserver")){
				writer.append(terminal.getTimeServer());
				return;
			}
			//获得跳频开关
			if(key.equals("channelswitch")){
				Integer confInt=terminal.getChannelSwitch();
				if(null!=confInt&&confInt>=0&&confInt<14){
					writer.append(confInt.toString());
				}
				return;
			}
			//获取信道停留时间
			if(key.equals("channelstaytime")){
				int[] stayTime=terminal.getChannelStayTime();
				if(null!=stayTime){
					writer.append(JSONArray.fromObject(stayTime).toString());
				}
			}
			//获得解密信息存活时间
			if(key.equals("wpalivetime")){
				Integer livetime=terminal.getWpaLiveTime();
				if(null!=livetime){
					writer.append(livetime.toString());
				}
				return;
			}
			//获取bssidinfo
			if(key.equals("bssidinfo")){
				JSONObject jo=new JSONObject(); 
				List<BssidInfo> bssidList=terminal.getBssidInfo();
				jo.put("total", bssidList.size());
				jo.put("rows",JSONArray.fromObject(bssidList));
				writer.append(jo.toString());
				return;
			}
			//获取高级版本开关
			if(key.equals("proversionstate")){
				if(terminal.getProVersionState()){
					writer.append("1");
					return;
				}
				writer.append("0");
				return;
			}
			//获取去重标志
			if(key.equals("packetcompareflag")){
				Integer flag=terminal.getPacketCmpFlag();
				if(null!=flag&&flag>=0&&flag<=7){
					writer.append(flag.toString());
				}
				return;
			}
			//获取缓存大小
			if(key.equals("cachesize")){
				Integer size=terminal.getCacheSize();
				if(null!=size&&size>0&&size<10240){
					writer.append(size.toString());
				}
				return;
			}
			//获取写数据时间间隔
			if(key.equals("writeinterval")){
				Integer interval=terminal.getWriteInterval();
				if(null!=interval&&interval>0){
					writer.append(interval.toString());
				}
				return;
			}
			//获得设备状态
			if(key.equals("status")){
				terminal.getSsh().execute("cd ./wifisniffer;./systemswitch -4");
				if(!terminal.getSsh().getErrFlag()){
					writer.append("running");
				}else{
					writer.append("stoped");
				}
			}
		}
		
		//设置参数部分
		else if(type.equals("set")){
			//设置时间
			if(key.equals("time")){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					Date time=sdf.parse(value);
					if(terminal.setTime(time)){
						writer.append("done");
						return;
					}
				} catch (ParseException e) {
					System.out.println("TerminalControlServlet.doPost():日期转换错误！");
				}
				writer.append("error");
				return;
			}
			//设置文件回传方式
			if(key.equals("fileloadtype")){
				if(value.equals("ftp")&&terminal.setFileLoadType(0)){
					writer.append("done");
					return;
				}
				if(value.equals("mysql")&&terminal.setFileLoadType(1)){
					writer.append("done");
					return;
				}
				writer.append("error");
				return;
			}
			//设置mysqlConf
			if(key.equals("mysqlconf")){
				JSONObject jo = JSONObject.fromObject(value);
				MysqlConf conf=new MysqlConf(jo.getString("hostname"),jo.getString("username"),jo.getString("pwd"), jo.getString("db"), jo.getString("port"));
				if(terminal.setMysqlConf(conf)){
					writer.append("done");
					return;
				}
				writer.append("error");
				return;
			}
			//设置ftpConf
			if(key.equals("ftpconf")){
				JSONObject jo = JSONObject.fromObject(value);
				FtpConf conf=new FtpConf(jo.getString("hostname"),jo.getString("username"),jo.getString("pwd"));
				if(terminal.setFtpConf(conf)){
					writer.append("done");
					return;
				}
				writer.append("error");
				return;
			}
			//设置时间服务器
			if(key.equals("timeserver")){
				if(terminal.setTimeServer(value)){
					writer.append("done");
					return;
				}
				writer.append("error");
				return;
			}
			//设置跳频开关
			if(key.equals("channelswitch")){
				try{
					int channelswitch=Integer.parseInt(value);
					if(terminal.setChannelSwitch(channelswitch)){
						writer.append("done");
						return;
					}
					writer.append("error");
				}catch(NumberFormatException e){
					System.out.println("TerminalControlServlet.doPost():跳频参数转换错误");
				}
				return;
			}
			//设置信道停留时间
			if(key.equals("channelstaytime")){
				JSONArray ja=JSONArray.fromObject(value);
				if(ja.size()==13){
					try{
						int[] stayTime=new int[13];
						for(int i=0;i<13;i++){
							stayTime[i]=Integer.parseInt(ja.getString(i));
						}
						if(terminal.setChannelStayTime(stayTime)){
							writer.write("done");
							return;
						}
					}catch(NumberFormatException e){
						System.out.println("TerminalControlServlet.doPost():信道停留时间参数转换错误");
					}
				}
				writer.write("error");
				return;
			}
			//设置wpa解密信息存活时间
			if(key.equals("wpalivetime")){
				try{
					int liveTime=Integer.parseInt(value);
					if(terminal.setWpaLiveTime(liveTime)){
						writer.append("done");
						return;
					}
					writer.append("error");
				}catch(NumberFormatException e){
					System.out.println("TerminalControlServlet.doPost():解密信息存活时间参数转换错误");
				}
				return;
			}
			//添加bssidinfo
			if(key.equals("addbssidinfo")){
				JSONObject jo=JSONObject.fromObject(value);
				BssidInfo info=new BssidInfo(jo.getString("name"), jo.getString("pwd"), jo.getString("mac"));
				List<BssidInfo> list=terminal.getBssidInfo();
				if(list.add(info) && terminal.setBssidInfo(list)){
					writer.append("done");
					return;
				}
				writer.append("error");
				return;
			}
			//删除bssidinfo
			if(key.equals("removebssidinfo")){
				JSONObject jo=JSONObject.fromObject(value);
				BssidInfo info=new BssidInfo(jo.getString("name"), jo.getString("pwd"), jo.getString("mac"));
				List<BssidInfo> list=terminal.getBssidInfo();
				if(list.remove(info)&&terminal.setBssidInfo(list)){
					writer.append("done");
					return;
				}
				writer.append("error");
				return;
			}
			//设置高级版本开关
			if(key.equals("proversionstate")){
				boolean state=false;
				if(value.equals("1")){
					state=true;
				}
				if(terminal.setProversonState(state)){
					writer.append("done");
					return;
				}
				writer.append("error");
				return;
			}
			//设置去重标志位
			if(key.equals("packetcompareflag")){
				try{
					int flag=Integer.parseInt(value);
					if(terminal.setPacketCmpFlag(flag)){
						writer.append("done");
						return;
					}
				}catch(NumberFormatException e){
					System.out.println("TerminalControlServlet.doPost():报文去重参数格式转换错误");
				}
				writer.append("error");
				return;
			}
			//设置缓存大小
			if(key.equals("cachesize")){
				try{
					int size=Integer.parseInt(value);
					if(terminal.setCacheSize(size)){
						writer.append("done");
						return;
					}
				}catch(NumberFormatException e){
					System.out.println("TerminalControlServlet.doPost():设置缓存大小格式转换错误");
				}
				writer.append("error");
				return;
			}
			//设置写数据时间间隔
			if(key.equals("writeinterval")){
				try{
					int interval=Integer.parseInt(value);
					if(terminal.setWriteInterval(interval)){
						writer.append("done");
						return;
					}
				}catch(NumberFormatException e){
					System.out.println("TerminalControlServlet.doPost():设置写数据时间间隔格式转换错误");
				}
				writer.append("error");
				return;
			}
		}
		else if(type.equals("control")){
			if(key.equals("startpro")){
				if(terminal.restart()){
					writer.append("done");
					return;
				}
				writer.append("error");
				return;
			}
			if(key.equals("restartpro")){
				if(terminal.restart()){
					writer.append("done");
					return;
				}
				writer.append("error");
				return;
			}
			if(key.equals("stoppro")){
				if(terminal.stop()){
					writer.append("done");
					return;
				}
				writer.append("error");
				return;
			}
			if(key.equals("reboot")){
				if(terminal.reboot()){
					writer.append("done");
					return;
				}
				writer.append("error");
				return;
			}
		}
	}
	
}
