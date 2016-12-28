<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>WIFI信息采集系统</title>
    <link href="style/alogin.css" rel="stylesheet" type="text/css" />


</head>
<body>

    <form id="form1" runat="server"  action="doregister">
    <div class="Main">
    
        <ul>
      
            <li class="top"></li>
            <li class="top2"></li>
            <li class="topA"></li>
            <li class="topB"><span>
                <img src="images/login/2.jpg" alt="" style="" />
            </span></li>
             
            <li class="topC"></li>
            <li class="topD">
                <ul class="login">
                 
                
                
                    <li><span class="left">用&nbsp户&nbsp名</span> <span style="left">
                        <input id="username" type="text" class="txt" name="username"/>  
                     
                    </span></li>
                    
                    
                    <br/>
                    <li><span class="left">密&nbsp&nbsp&nbsp&nbsp 码</span> <span style="left">
                       <input id="password1" type="password" class="txt" name = "password1"/>  
                    </span></li>
                    
                    
                    <br/>
                    <li><span class="left">确认密码</span> <span style="left">
                       <input id="password2" type="password" class="txt" name = "password2"/>  
                    </span></li>
                   
                    
                </ul>
            </li>
            <li class="topE"></li>
            <li class="middle_A">  </li>
            <li class="middle_B"></li>
          
            <li class="middle_C">
            
            <span class="btn">
               
                    <% 
                         if(request.getAttribute("registerfor") != null){
                              out.println("<font color = red>" +request.getAttribute("registerfor") + "</font>");
   
                          }
                        %>
                    &nbsp&nbsp&nbsp<input type="image" src="images/login/btnregister.gif" />
    
           
            </span>
                               
            </li>
            <li class="middle_D"> </li>
            <li class="bottom_A"></li>
            <li class="bottom_B">
             
            </li>
        </ul>
    </div>
    </form>
</body>
</html>
