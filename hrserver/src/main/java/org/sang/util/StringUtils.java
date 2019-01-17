package org.sang.util;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author rick
 * @date 2018/07/22
 */
public class StringUtils {

    /**匹配所有特殊字符的正则表达式*/
    public static final String SPECIAL_CHARACTER_REGEX = "[`~!@#$%^&*()\\-+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）—_+|{}【】‘；：”“’。，、\"？\\s]";

    public static final Pattern SPECIAL_CHARACTER_PATTERN = Pattern.compile(SPECIAL_CHARACTER_REGEX) ;

    /**中文正则*/
    public static final String CHINESE_REGEX = "[\u4e00-\u9fa5]" ;

    /**匹配括号中间内容*/
    private static final Pattern BRACKET_CONTENT_PATTERN = Pattern.compile("(?<=\\()(.+?)(?=\\))");

    private StringUtils() {
    }

    /**
     * 二进制转字符串
     */
    public static String byte2hex(byte[] b) {
        String hs = "" ;
        String stmp = "" ;
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF)) ;
            if (stmp.length() == 1){
                hs = hs + "0" + stmp ;
            } else{
                hs = hs + stmp ;
            }
        }
        return hs.toUpperCase();
    }

    /**
     * 字符串去左右空格
     */
    public static  String null2EmptyWithTrim(String s) {
        if (s == null) {
            return "";
        }else{
            return s.trim();
        }
    }

    /**
     * 字符串去左右空格
     */
    public static  String null2EmptyWithTrim(Object s) {
        if (s == null) {
            return "";
        }else{
            return s.toString().trim();
        }
    }

    /**
     * 字符串去左右空格
     */
    public static  String null2EmptyWithTrimNew(Object s)
    {

        if (s == null || "NULL".equalsIgnoreCase(s.toString())) {
            return "";
        } else {
            return s.toString().trim();
        }
    }

    /**
     * 字符串是否为空
     */
    public static  boolean isEmpty(String foo) {
        return (foo == null || foo.trim().length() == 0);
    }

    /**
     * 字符串是否不为空
     */
    public static  boolean isNotEmpty(String foo) {
        return (null != foo && foo.trim().length() > 0);
    }

    /**
     * 字符串是否为空
     */
    public static  boolean isEmpty(Object foo) {
        return (foo == null || foo.toString().trim().length() == 0);
    }

    /**
     * 字符串是否不为空
     */
    public static  boolean isNotEmpty(Object foo) {
        return (null != foo && foo.toString().trim().length() > 0);
    }

    /**
     * 计算一个字符串的长度，汉字当成两个字符计算，ascii英文字符当成一个。
     * @param aStr 要计算长度的目标字符串
     * @return 计算出的长度
     */
    public static int lengthOfCN(String aStr) {
        char c;
        int length = 0;
        if(isNotEmpty(aStr)){
            for (int i = 0; i < aStr.length(); i++) {
                c = aStr.charAt(i);
                if (c >= 127) {
                    length += 2;
                }else{
                    length += 1;
                }
            }
        }
        return length;
    }

    /**
     * 字符串首字母大写或小写
     */
    public static String firstLetterUpperOrLower(String str,boolean isLowerCase) {
        if (isNotEmpty(str)){
            if (str.length() == 1) {
                return isLowerCase ? str.toLowerCase() : str.toUpperCase() ;
            }else{
                String first = str.substring(0, 1).toLowerCase();
                first = isLowerCase ? first.toLowerCase() : first.toUpperCase() ;
                return (first + str.substring(1));
            }
        }
        return str;
    }

    /**
     * 去除字符串所有特殊字符
     */
    public static String removeAllSpecialChar(String str) {
        Matcher m = SPECIAL_CHARACTER_PATTERN.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 判断str是否在strArr中
     */
    public static boolean strInArray(String str, String[] strArr) {
        for(String s : strArr){
            if (str.equals(s)){
                return true;
            }
        }
        return false;
    }

    /**
     * 按分隔符分隔字符串
     */
    @Deprecated
    public static String[] split(String text, String separator) {

        StringTokenizer st = new StringTokenizer(text, separator);
        //分隔符数量大小的字符串数组
        String[] values = new String[st.countTokens()];
        int pos = 0;
        //是否还有分隔符
        while (st.hasMoreTokens()){
            //返回从当前位置到下一个分隔符的字符串
            values[pos++] = st.nextToken();
        }
        return values;
    }

    /**
     * 判断字符串中是否包含中文
     */
    public static boolean isContainChinese(String str) {
        Matcher m = SPECIAL_CHARACTER_PATTERN.matcher(str);
        return  m.find() ;
    }

    /**
     * 获取字符串中所有中文
     */
    public static String getAllChineseInStr(String str){
        return  str.replaceAll("[^\u4e00-\u9fa5]", "") ;
    }

    /**
     * 获取括号内内容
     * @param str
     */
    public static  String getBracketContent(String str){
        Matcher matcher = BRACKET_CONTENT_PATTERN.matcher(str);
        if(matcher.find()){
            return  matcher.group(0);
        }else {
            return "";
        }
    }

    /**
     * 左补全字符
     * @param w84PaddingStr 需要补全的字符
     * @param digit 补全后字符的位数
     * @param paddingStr 补全使用的字符
     */
    public static String leftPadding(String w84PaddingStr,int digit,String paddingStr){
        w84PaddingStr = null2EmptyWithTrim(w84PaddingStr);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < digit - w84PaddingStr.length(); i++) {
            sb.append(paddingStr);
        }
        sb.append(w84PaddingStr);
        return sb.toString();
    }

    /**
     * 右补全字符
     * @param w84PaddingStr 需要补全的字符
     * @param digit 补全后字符的位数
     * @param paddingStr 补全使用的字符
     */
    public static String rightPadding(String w84PaddingStr,int digit,String paddingStr){
        w84PaddingStr = null2EmptyWithTrim(w84PaddingStr);
        StringBuffer sb = new StringBuffer();
        sb.append(w84PaddingStr);
        for (int i = 0; i < digit - w84PaddingStr.length(); i++) {
            sb.append(paddingStr);
        }
        return sb.toString();
    }

    /**
     * 月份补全 2位
     * @param month 月份
     */
    public static String monthPadding(int month){
        return leftPadding(Integer.valueOf(month).toString(),2,"0");
    }

    /**
     * 格式化字符串
     * @param template 模版字符 占位使用%s
     * @param args 参数
     */
    public static String format(String template, Object... args) {
        template = String.valueOf(template);
        StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
        int templateStart = 0;

        int i;
        int placeholderStart;
        for(i = 0; i < args.length; templateStart = placeholderStart + 2) {
            placeholderStart = template.indexOf("%s", templateStart);
            if(placeholderStart == -1) {
                break;
            }

            builder.append(template.substring(templateStart, placeholderStart));
            builder.append(args[i++]);
        }

        builder.append(template.substring(templateStart));
        if(i < args.length) {
            builder.append(" [");
            builder.append(args[i++]);

            while(i < args.length) {
                builder.append(", ");
                builder.append(args[i++]);
            }

            builder.append(']');
        }

        return builder.toString();
    }

    /**
     * 获得用户远程地址
     */
    public static String getRemoteAddr(HttpServletRequest request){
        String remoteAddr = request.getHeader("X-Real-IP");
        if (isEmpty(remoteAddr)) {
            remoteAddr = request.getHeader("X-Forwarded-For");
        }
        if (isEmpty(remoteAddr)) {
            remoteAddr = request.getHeader("Proxy-Client-IP");
        }
        if (isEmpty(remoteAddr)) {
            remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
    }

    /**
     * 是否存在指定值对应的枚举
     * @param enumClass
     * @param value
     */
    public static Boolean enumContainsValue(Class enumClass, String value){
        try{
            Enum.valueOf(enumClass,value);
            return  true;
        }catch(IllegalArgumentException e){
            return  false ;
        }
    }


    public static boolean isBigDecimal(Object str) {
        if (isEmpty(str)) {
            return false;
        }
        try{
            new BigDecimal(null2EmptyWithTrim(str));
        }catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isInteger(Object str) {
        if (isEmpty(str)) {
            return false;
        }
        try{
            Integer.parseInt(null2EmptyWithTrim(str));
        }catch (Exception e) {
            return false;
        }
        return true;
    }

    private static boolean isMatch(String regex, String orginal){
        if (orginal == null || orginal.trim().equals("")) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher isNum = pattern.matcher(orginal);
        return isNum.matches();
    }

    /**
     * 正整数
     * @param orginal
     * @return
     */
    public static boolean isPositiveInteger(String orginal) {
        return isMatch("^[1-9]\\d*", orginal);
    }

    /**
     * 负整数
     * @param orginal
     * @return
     */
    public static boolean isNegativeInteger(String orginal) {
        return isMatch("^-[1-9]\\d*", orginal);
    }

    /**
     * 整数（包括负整数、0、正整数）
     * @param orginal
     * @return
     */
    public static boolean isWholeNumber(String orginal) {
        return isMatch("0", orginal) || isPositiveInteger(orginal) || isNegativeInteger(orginal);
    }

    /**
     * 正小数
     * @param orginal
     * @return
     */
    public static boolean isPositiveDecimal(String orginal){
        return isMatch("\\+{0,1}[0]\\.[1-9]*|\\+{0,1}[1-9]\\d*\\.\\d*", orginal);
    }

    /**
     * 负小数
     * @param orginal
     * @return
     */
    public static boolean isNegativeDecimal(String orginal){
        return isMatch("^-[0]\\.[1-9]*|^-[1-9]\\d*\\.\\d*", orginal);
    }

    /**
     * 小数
     * @param orginal
     * @return
     */
    public static boolean isDecimal(String orginal){
        return isMatch("[-+]{0,1}\\d+\\.\\d*|[-+]{0,1}\\d*\\.\\d+", orginal);
    }

    /**
     * 实数
     * @param orginal
     * @return
     */
    public static boolean isRealNumber(String orginal){
        return isWholeNumber(orginal) || isDecimal(orginal);
    }

    /**
     * 按照指定格式解析字符串型日期值为日期对象
     *
     * @param date
     *            字符串型日期
     * @param pattern
     *            日期格式
     * @return 日期对象
     */
    public static Date parse(String date, String pattern) {
        if (isEmpty(date)) {
            return null;
        }
        DateFormat formater = new SimpleDateFormat(pattern);
        try {
            return formater.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 按照指定格式转换日期对象为字符串型日期
     *
     * @param date
     *            日期对象
     * @param pattern
     *            日期格式
     * @return 字符串型日期
     */
    public static String format(Date date, String pattern) {
        if (StringUtils.isEmpty(date)) {
            return "";
        }
        DateFormat formater = new SimpleDateFormat(pattern);
        return formater.format(date);
    }

    /**=
     * 验证是否是一个有效的日期
     * @param dataTimeStr
     * @return
     */
    public static Boolean isDate(String dataTimeStr){
        if(StringUtils.isEmpty(dataTimeStr)){return false;};
        try{
            Date date = parse(dataTimeStr,"yyyyMMdd");
            if(date == null){
                return false;
            } else {
                //2月转换会有问题
                String formatDate = format(date,"yyyyMMdd");
                if(dataTimeStr.equals(formatDate)) {
                    return true;
                }
            }
            return false;
        }catch (Exception e){

        }
        return false;
    }

    /**=
     * 验证是否是一个有效的时间
     * @param dataTimeStr
     * @return
     */
    public static Boolean isDateTime(String dataTimeStr){
        if(StringUtils.isEmpty(dataTimeStr)){return false;};
        try{
            Date date = parse(dataTimeStr,"yyyyMMddHHmmss");
            if(date == null){return false;}
            return true;
        }catch (Exception e){

        }
        return false;
    }

    /**
     * 根据整数位和小数位检查数字的长度
     * @param str
     * @param intergerLength
     * @param preciseLength
     * @return
     */
    public static boolean isValidNumber(String str,int intergerLength,int preciseLength) {
        String regex = "^([0-9]{1," + intergerLength +"}+(.[0-9]{" + preciseLength + "})?$)|(-[0-9]{1," + intergerLength + "}+(.[0-9]{" + preciseLength + "})?$)";
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(str);
        return match.matches();
    }

    /**
     * 判断时间格式是否合法
     * @param sDate 时间日期
     * @param pattern 日期格式化
     * @return
     */
    public static boolean isLegalDate(String sDate,String pattern) {
        if (StringUtils.isEmpty(sDate) || StringUtils.isEmpty(pattern)) {
            return false;
        }
        DateFormat formatter = new SimpleDateFormat(pattern);
        try {
            Date date = formatter.parse(sDate);
            return sDate.equals(formatter.format(date));
        } catch (Exception e) {
            return false;
        }
    }

}
