package com.wifi.util;

public class StringUtil {

	/**
	 * 判断String类型数据是空的
	 * @param str String类型数据
	 * @return 是空则返回true，否则返回false
	 */
	public static boolean isEmpty(String str){
		if("".equals(str)|| str==null){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 判断String 数据不是空的
	 * @param str String类型数据
	 * @return str不为空则返回true，否则返回false
	 */
	public static boolean isNotEmpty(String str){
		if(!"".equals(str)&&str!=null){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * String数组strArr是否包含str
	 * @param str String类型数据
	 * @param strArr String数组
	 * @return 如果strArr里包含str元素则返回true,否则返回false
	 */
	public static boolean existStrArr(String str,String []strArr){
		for(int i=0;i<strArr.length;i++){
			if(strArr[i].equals(str)){
				return true;
			}
		}
		return false;
	}
	/**
	 * 压缩字符串,最高一位代表一天，然后三位表示今天有多少条数据
	 * @param str
	 * @return
	 */
	public static  String compress(String str){
		StringBuilder sb = new StringBuilder();
		if(str == null)
			return "";
		int len = str.length();
		sb.append('z');
		sb.append("000");//默认0条数据
		int count = 0;//总共有多少条数据
        for(int i = 0;i < len;i++){
        	if(str.charAt(i)=='1'){//如果获得一个1
        		sb.append(String.format("%04d", i+1));//把当前分钟加入，自动填充为四位，前方补0
        		count++;//数据加一条
        	}
        }
        String scount = String.valueOf(Integer.toHexString(count));
        if(scount.length() == 1)
        	scount = "00" + scount;
        else if(scount.length() == 2){
        	scount = "0" + scount;
        }
        	
        sb.replace(1, 4, scount);//修改数据条数将000替换掉
		return sb.toString();
	}
	
	public static String compressDevice(String s){
		return "";
	}
	/**
	 * 左边补充零，只针对16位的表示
	 * @param str
	 * @return
	 */
	public static String zeroPadding(String str){
		StringBuilder sB = new StringBuilder(str);
		int len = str.length();
		int len1 = 4 - len;
		for(int i = 0 ;i < len1;i++){
			sB.insert(0,'0');
		}
		return sB.toString();
	}
	/**
	 * 解压缩字符串
	 * @param str
	 * @return
	 */
	public static String uncompress(String str){
		StringBuilder sb = new StringBuilder();
		int len = str.length();
		for(int i = 0;i < len;i += 4){
			String s = str.substring(i,i+4);
			int day_len = Integer.parseInt(s, 16);
			String day_str = str.substring(i+4,i+4+day_len);
			for(int j = 0;j < day_str.length();j += 4){
				String tmp = day_str.substring(j,j+4);
				
				int first = Integer.parseInt(String.valueOf(tmp.charAt(0))) / 8;//获取首位内容
				int d = (1 << 15) - 1;
				int tmp_num = Integer.parseInt(tmp,16);
				tmp_num = tmp_num & d;
				for(int k = 0;k < tmp_num;k++){
					sb.append(first);
				}
			}
			i = i+day_len;
		}
		return sb.toString();
	}
	/**
	 * 获得设备地址位于第begin个1和第end个1之间的字符串
	 * @param str
	 * @param begin
	 * @param end
	 * @return
	 */
	public static String getBwtween(String str,int begin,int end){
		String s = str.substring(begin*4,end*4);
		return s;
	}
	/**
	 * 获得位于begin与end之间的时间字符串
	 * @param str 时间字符串
	 * @param begin 距离最早时间的开始序号
	 * @param end 距离最早时间的结束序号
	 * @return
	 */
	public static String getCpmressDateStr(String str,int begin,int end){
		String cur = new String(str);
		int j = 0;
		for(int i = 0;i < begin;i++){
			String tmp = cur.substring(j,j + 4);
			int day_len = Integer.parseInt(tmp,16);
			j = j + day_len + 4;
		}
		int k = j;
		for(int i = begin;i < end;i++){
			String tmp = cur.substring(k,k + 4);
			int day_len = Integer.parseInt(tmp,16);
			k = k + day_len + 4;
		}
		String string = cur.substring(j,k);
		return string;
	}
	/**
	 * 更新某一个时间字符串的position 位置的字符
	 * @param str
	 * @param position 位置
	 * @param update 需要更新的字符
	 * @return
	 */
	public static String updateAtPosition(String str,int position,char update){
		//String s = uncompress(str);
		StringBuilder sb = new StringBuilder(str);
		sb.setCharAt(position, update);
		String result = sb.toString();
		//result = compress(result);
		return result;
	}
	/**
	 * 更新某一个时间字符串的position 位置的字符
	 * @param str
	 * @param position 位置
	 * @param update 需要更新的字符
	 * @return
	 */
	public static String updateCompressAtPosition(String str,int position,char update){
		String s = uncompress(str);
		StringBuilder sb = new StringBuilder(s);
		sb.setCharAt(position, update);
		String result = sb.toString();
		result = compress(result);
		return result;
	}
	/**
	 * 初始生成全部是0的字符串
	 * @param length
	 * @return
	 */
	public static String createInitData(int length){
		StringBuilder sb = new StringBuilder();
		for(int i = 0;i < length;i++){
			sb.append("0");
		}
		return sb.toString();
	}
	/**
	 * 把冒号替换成下划线
	 * @param str
	 * @return
	 */
	public static String replaceColonWithUnderline(String str){
		String s = str.replaceAll(":","_");
		return s;
	}
	/**
	 * 合并字符串
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static String merge(String str1,String str2){
		StringBuilder sb = new StringBuilder();
		for(int i = 0;i < str1.length();i++){
			int a = str1.charAt(i) - '0';
			int b = str2.charAt(i) - '0';
			int c = a & b;
			sb.append(c);
		}
		return sb.toString();
	}
	/**
	 * 得到一个字符串中，距离最开始的时间为day天之前1的个数
	 * @param str
	 * @param day
	 * @return
	 */
	public static int getOneNumbers(String str,int day){
		int sum = 0;
		int j = 0;
		for(int i = 0;i < day;i++){
			String tmp = str.substring(j,j + 4);
			int day_len = Integer.parseInt(tmp,16);
			String day_str = str.substring(j+4, j+4+day_len);
			sum += getOneFromString(day_str);
			j = j + day_len + 4;
		}
		
		return sum;
	}
	/**
	 * 获取一个四字节字符串中1 的个数
	 * @param str
	 * @return
	 */
	public static int getOneFromString(String str){
		int sum = 0;
		for(int i = 0;i < str.length();i += 4){
			String s = str.substring(i, i+4);
			char first = s.charAt(0);
			if(first == '8'){
				int a = (1 << 15) - 1;
				int b = Integer.parseInt(s,16);
				int c = a & b;
				sum += c;
			}
		}
		return sum;
	}
	public static String addTwoStrings(String str1,String str2){
		if(str1 == null && str2 == null){
			return "";
		}
		if(str1 == null)
			return str2;
		if(str2 == null)
			return str1;
		return str1 + str2;
	}
	public static String minDateString(String str1,String str2){
		int compare = str1.compareTo(str2);
		if(compare > 0)
			return str2;
		else
			return str1;
	}
	public static String maxDateString(String str1,String str2){
		int compare = str1.compareTo(str2);
		if(compare > 0)
			return str1;
		else
			return str2;
	}
	
	public static  int getCount(String s){//传入16进制数字的字符串，返回代表的整数
		if(s.length() != 3)
			return 0;
		int count = 0;
		char[] a = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
		
		for(int j = 0;j < 16;j++){
			if(s.charAt(2) == a[j]){
				count += j * 1;
			}
			if(s.charAt(1) == a[j]){
				count += j * 16;
			}
			if(s.charAt(0) == a[j]){
				count += j * 16 * 16;
			}
		}
		
		return count;
	}
	
	/**
	 * 根据传入的字符串和相差的天数获得需要往后移多少个树莓派的位置
	 */
	public static int getDevcieIndex(String timeString ,int day){
		String[] s = timeString.split("z");//以  z 将天数隔开

		int index = 0;
		for(int i = 1;i <= day;i++){//第一个是空串，所以从1开始
			index += getCount(String.valueOf(s[i].charAt(0)) 
					+String.valueOf(s[i].charAt(1))
					+String.valueOf(s[i].charAt(2))
					);
			
		}
		return index;
	}
	
//	public static void main(String[] args) {
//		System.out.println(StringUtil.getDevcieIndex("z001z002z003z004",3));
//	}
	
	
	
	
	
}
