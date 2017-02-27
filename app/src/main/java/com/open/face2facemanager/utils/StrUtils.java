package com.open.face2facemanager.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.open.face2facemanager.business.baseandcommon.TApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import okhttp3.MultipartBody;

//import com.github.junrar.Archive;
//import com.github.junrar.rarfile.FileHeader;


public class StrUtils {

    /**
     * 判断字符串为网址, 提取如 www.p-pass.com/123456zx 中的 123456
     *
     * @param content
     * @return
     */
    public static String regexZxContent(String content) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }

        String src = content.toLowerCase();
        String reg = "(http://|https://|www){1}[\\w\\.\\-/:]+";
        Pattern pt = Pattern.compile(reg);
        Matcher mt = pt.matcher(src);

        String zxBar = "";
        while (mt.find()) {
            zxBar = mt.group();
            break;
        }

        if (!TextUtils.isEmpty(zxBar) && zxBar.equals(src)
                && (zxBar.lastIndexOf("/") > 0)
                && zxBar.toLowerCase().endsWith("zx")) {
            try {
                String endStr = zxBar.substring(zxBar.lastIndexOf("/") + 1,
                        zxBar.length() - "zx".length());
                if (!TextUtils.isEmpty(endStr)
                        && endStr.equals(getBeginNumber(endStr))) {
                    return endStr;
                } else {
                    return "";
                }
            } catch (IndexOutOfBoundsException e) {
                return "";
            } catch (Exception e) {
                return "";
            }
        }

        return "";
    }

    /**
     * 匹配数字
     */
    public static String findNum(String str) {
        str = str.trim();
        String str2 = "";
        if (str != null && !"".equals(str)) {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
                    str2 += str.charAt(i);
                }
            }

        }
        return str2;
    }

    /**
     * gbk 转 utf8
     *
     * @param str
     * @return
     */
    public static String gb2utf8(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }

        String newstr = "";
        try {
            newstr = new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            newstr = str;
        }

        return newstr;
    }

    /**
     * String 转 int 值
     *
     * @param str
     * @return
     */
    public static int strToInt(String str) {
        int i = 0;
        if (TextUtils.isEmpty(str) || str.equals("null")) {
            return 0;
        } else {
            try {
                i = Integer.parseInt(str);
            } catch (Exception e) {
                i = 0;
            }
        }

        return i;
    }

    /**
     * 去掉空指针或为null情况
     *
     * @param str
     * @return
     */
    public static String tripNull(String str) {
        if (TextUtils.isEmpty(str) || str.equals("null")) {
            return "";
        } else {
            return str;
        }
    }

    /**
     * 将URL中的中文转为UTF-8编码
     *
     * @param str
     * @return
     */
    public static String decodeUrl(String str) {
        return URLDecoder.decode(str);
    }

    /**
     * 将字符串中的中文转为URL编码
     *
     * @param str
     * @return
     */
    public static String encodeUrl(String str) {
        String tstr = "";
        try {
            tstr = URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            tstr = str;
        }

        return tstr;
    }

    /**
     * 将字符串转为md5
     *
     * @param str
     * @return
     */
    public static String string2md5(String str) {
        MessageDigest msgDigest = null;

        try {
            msgDigest = MessageDigest.getInstance("MD5");
            msgDigest.reset();
            msgDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return str;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] byteArray = msgDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(
                        Integer.toHexString(0xFF & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }

        return md5StrBuff.toString();
    }

    /**
     * 获取字符串内部第一段数字子串
     *
     * @param srcStr
     * @return
     */
    public static String getBeginNumber(String srcStr) {
        String childStr = "";
        if (TextUtils.isEmpty(srcStr)) {
            return childStr;
        }

        String reg = "\\d*";
        Pattern pt = Pattern.compile(reg);
        Matcher mt = pt.matcher(srcStr);
        while (mt.find()) {
            childStr = mt.group();
            break;
        }

        if (!(TextUtils.isEmpty(childStr))) {
            if (!(srcStr.startsWith(childStr))) {
                childStr = "";
            }
        }

        return childStr;
    }

    /**
     * 根据路径创建图片的url
     *
     * @return
     */
/*	public static String createImgUrl(String path) {
        return new StringBuffer("http://" + Config.SERVER_HOST_NAME + ":"
				+ Config.SERVER_PORT + "/" + path).toString();
	}*/
    public static String subString(String str, int len) {
        if (str.length() < len)
            return str;
        int size = str.length() / len;
        boolean flag = (str.length() % len == 0);
        StringBuilder mstr = new StringBuilder();
        for (int i = 0; i < size; i++) {
            mstr.append(str.substring(i * len, (i + 1) * len));
            mstr.append("\n");
        }
        if (!flag) {
            mstr.append(str.substring(size * len));
        } else {
            mstr.deleteCharAt(mstr.length() - 1);
        }
        return mstr.toString();
    }

    /**
     * 判断是不是手机号
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNum(String mobiles) {
        Pattern p = Pattern
                .compile("^(0|86|17951)?(13[0-9]|15[012356789]|17[6780]|18[0-9]|14[57])[0-9]{8}$");
        Matcher m = p.matcher(mobiles);

        return m.matches();
    }

    /**
     * 判断是不是身份证号
     *
     * @param mobiles
     * @return
     */
    public static boolean isIdentify(String identifyNum) {
        Pattern p = Pattern
                .compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$");
        Matcher m = p.matcher(identifyNum);

        return m.matches();
    }


    /**
     * 17位系数分别为
     * 7－9－10－5－8－4－2－1－6－3－7－9－10－5－8－4－2
     * 乘积求和对11取莫，对照表
     * 1－0－X －9－8－7－6－5－4－3－2
     *
     * @param identifyNum
     * @return
     */
    public static boolean isIdentify18(String identifyNum) {
        if (identifyNum == null || identifyNum.length() != 18) return false;
        int[] num = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += num[i] * Integer.parseInt(identifyNum.substring(i, i + 1));
        }
        int j = sum % 11;
        String[] shcem = new String[]{"1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2"};
        Log.i("onion", identifyNum.substring(17));
        return identifyNum.substring(17).equalsIgnoreCase(shcem[j]);
    }

    //获取天数
    public static int isAbigerB(String dateA, String dateB)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long date = sdf.parse(dateA).getTime() - sdf.parse(dateB).getTime();
        return (int) (date / 1000);
    }

    //获取天数
    public static String getDay4Date(String begin, String end)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long date = sdf.parse(end).getTime() - sdf.parse(begin).getTime();
        return date / (1000 * 60 * 60 * 24) + "";
    }

    //获取时长
    public static String getTime(int millions) {
        int hour = millions / (60 * 60);
        int minute = (millions / 60) % 60;
        int second = millions % 60;
//        int day=millions/360/24;
        return hour + "小时" + minute + "分" + second + "秒";
    }

    public static boolean isInServiceTime(String startTime, String endTime) {
        SimpleDateFormat formatters = new SimpleDateFormat("HH:mm");
        Date curDates = new Date(System.currentTimeMillis());// 获取当前时间
        String strs = formatters.format(curDates);
        int cTime = Integer.parseInt(strs.replace(":", ""));
        //三位变两位
        String[] starts = startTime.split(":");
        int sTime = Integer.parseInt(starts[0]) * 100 + Integer.parseInt(starts[1]);
        String[] ends = endTime.split(":");
        int eTime = Integer.parseInt(ends[0]) * 100 + Integer.parseInt(ends[1]);
        if (eTime < sTime) {//跨越时间周期
            return (sTime < cTime && cTime <= 2400) || (0 <= cTime && cTime < eTime);
        } else {//一个时间周期内
            return sTime < cTime && cTime < eTime;
        }
    }

    //获取日期
    public static String getDate4Second(int millions) {

        Date date = new Date(millions * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    //获取日期
    public static String getTime4Millions(long millions) {

        Date date = new Date(millions);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        return sdf.format(date);
    }

    //获取日期时间
    public static String getTime6Millions(long millions) {

        Date date = new Date(millions);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sdf.format(date);
    }

    //获取日期
    public static String getTime42Millions(long millions) {
        Date date = new Date(millions);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    //获取星期
    public static String getDate4Millions(long millions) {

        Date date = new Date(millions);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        String[] weekOfDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return 1 + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "\t" + weekOfDays[w];
    }


    //获取星期
    public static String getDate24Millions(long millions) {

        Date date = new Date(millions);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        String[] weekOfDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return 1 + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
    }


    //获取星期
    public static String getWeek4Millions(long millions) {

        Date date = new Date(millions);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        String[] weekOfDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekOfDays[w];
    }


    //流转String
    public static String Stream2String(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is), 16 * 1024); //强制缓存大小为16KB，一般Java类默认为8KB
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {  //处理换行符
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    /**
     * 装配商品列表item字符串
     */
    public static String str2SingleGoods(String name, TextPaint paint, int textviewwidth) {
        name = name.trim();
        if (paint.measureText(name) < textviewwidth)
            return name + "\n";
        else {
            if (name.length() > 15) name = name.substring(0, 15);
            if (paint.measureText(name) > textviewwidth) {
                while (true) {
                    name = name.substring(0, name.length() - 1);
                    if (paint.measureText(name) < textviewwidth - 40)
                        return name + "..\n";
                }

            }
        }
        return "";
    }

    /**
     * 装配通知字符串
     */
    public static String str2SingleNotify(String name, TextPaint paint, int textviewwidth) {
        name = name.trim();
        if (paint.measureText(name) < textviewwidth)
            return name + "\n";
        else {
            int sublength = 0;
            if (name.length() > 40) {
                sublength = 40;
            } else {
                sublength = name.length();
            }
            String in = "";
            int i = 1;
            if (paint.measureText(name) > textviewwidth) {
                while (true) {
                    in = name.substring(0, sublength - i++);
                    if (paint.measureText(in) < textviewwidth - 40)
                        return in + "\n" + str2SingleNotify(name.substring(in.length(), name.length()), paint, textviewwidth);
                }

            }
        }
        return name;
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static String dataStr(JSONObject jsonObject, String key) {
        StringBuffer stringBuffer = new StringBuffer("");
        List<String> keyList = null;
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            int len = jsonArray.length();

            for (int i = 0; i < len; i++) {
                keyList = new ArrayList<String>();
                jsonObject = (JSONObject) jsonArray.get(i);
//				JSONObject jsonObject2 = new JSONObject(object);
                Iterator<String> it = jsonObject.keys();
                while (it.hasNext()) {
                    String str = it.next();
                    keyList.add(str);
                }
                Collections.sort(keyList);
                int keyListLen = keyList.size();
                StringBuffer buffer = new StringBuffer("");
                for (int j = 0; j < keyListLen; j++) {
                    key = keyList.get(j);
                    Object objectTemp = null;

                    objectTemp = dataStr(jsonObject, key);
                    if ("".equals(buffer.toString())) {
                        buffer.append(key).append("=").append(objectTemp);
                    } else {
                        buffer.append(",").append(key).append("=").append(objectTemp);
                    }
                }

                if ("".equals(stringBuffer.toString())) {
                    stringBuffer.append(buffer.toString());
                } else {
                    stringBuffer.append(",").append(buffer.toString());
                }
                buffer.setLength(0);
            }
        } catch (Exception e) {
            // TODO: handle exception
//			System.out.println("不是数组");
            try {
                stringBuffer.append(jsonObject.get(key));
                return stringBuffer.toString();
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }
        return stringBuffer.toString();
    }

    //public static void addFiled(JSONObject jsonObject,String method){
//    try {
//        jsonObject.put("nonce", new Date().getTime());
//        jsonObject.put("timestamp", new Date().getTime() / 1000);
//        jsonObject.put("method", method);
//    }catch (Exception e){
//        e.printStackTrace();
//    }
//}
    public static String dataHeader(JSONObject object) {
        try {
//           jsonObject.put("nonce",new Date().getTime());
//           jsonObject.put("timestamp",new Date().getTime()/1000);
//           jsonObject.put("method", method);
            return addSignatureItem(object);
        } catch (Exception e) {
            Log.e("onion", "e:" + e.toString());
        }
        return "";
    }

    private static String addSignatureItem(JSONObject jsonObject) throws JSONException {
        ArrayList<String> keyList = new ArrayList<String>();
        Iterator<String> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String str = iterator.next();
            keyList.add(str);
        }
        Collections.sort(keyList);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < keyList.size(); i++) {
            stringBuilder.append(keyList.get(i)).append("=");
            Object object = jsonObject.get(keyList.get(i));
            if (object instanceof JSONObject) {
                stringBuilder.append(addSignatureItem(((JSONObject) object)) + ",");
            } else if (object instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) object;
                for (int j = 0; j < jsonArray.length(); j++) {
                    stringBuilder.append(dataHeader(jsonArray.getJSONObject(j)) + ",");
                }
                if (jsonArray.length() > 0) stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            } else {
                stringBuilder.append(object);
            }
            stringBuilder.append(",");
        }
        if (!keyList.isEmpty()) stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public static boolean isInDay(long time) {
        Date currentTime = new Date(time * 1000);
        Date today = new Date();
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(today);
        calendarStart.set(Calendar.HOUR_OF_DAY, 0);
        calendarStart.set(Calendar.MINUTE, 0);
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(today);
        calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
        calendarEnd.set(Calendar.MINUTE, 59);
        return currentTime.after(calendarStart.getTime()) && currentTime.before(calendarEnd.getTime());
    }

    public static String getPrecent(double precent) {

        DecimalFormat formater = new DecimalFormat("#0.00");
        return formater.format(precent);
    }

    /**
     * 解析userId=1&pwd=2这样的字符串为map
     *
     * @param string
     * @return
     */
    public static HashMap<String, String> getParamsFromString(String string) {
        if (null == string || string.isEmpty()) return new HashMap<>();
        HashMap<String, String> hashMap = new HashMap();
        String[] strings = string.split("&");
        for (int i = 0; i < strings.length; i++) {
            String params[] = strings[i].split("=");
            hashMap.put(params[0], params[1]);
        }
        return hashMap;
    }


    /**
     * 装配通知字符串
     */
    /*public static SpannableString str2SingleLine(String name, TextPaint paint, int textviewwidth, Context context) {
        String line1 = "";
        String line2 = "";
        SpannableString sp = null;
        name = name.trim();
        if (paint.measureText(name) < textviewwidth)
            return new SpannableString(name + "\n");
        else {
            int sublength = 0;
            if (name.length() > 40) {
                sublength = 40;
            } else {
                sublength = name.length();
            }
            String in = "";


            int i = 1;
            if (paint.measureText(name) > textviewwidth) {
                while (true) {
                    in = name.substring(0, sublength - i++);
                    if (paint.measureText(in) < textviewwidth - 40) {
                        line1 = in;
                        break;
                    }
                }

            }

            int count = in.length();
            i = 1;
            name = name.substring(count, name.length());
            if (paint.measureText(name) > textviewwidth) {
                while (true) {
                    in = name.substring(0, name.length() - i++);
                    if (paint.measureText(in) < textviewwidth - 40) {
                        line2 = in;
                        sp = new SpannableString(line1 + "\n" + line2);
                        sp.setSpan(new VerticalImageSpan(context, R.drawable.spread_txt), sp.length() - 2, sp.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        break;
                    }
                }
            } else {
                sp = new SpannableString(line1 + "\n" + name);
            }
        }
        return sp;
    }

    public static SpannableString str2AllLine(String name, Context context) {
        SpannableString sp = null;
        sp = new SpannableString(name + "  ");
        sp.setSpan(new VerticalImageSpan(context, R.drawable.put_away_txt), sp.length() - 2, sp.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return sp;
    }*/
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    public static ArrayList<String> upZipFile(File zipFile, String folderPath) throws ZipException,
            IOException {
        File desDir = new File(folderPath);
        if (!desDir.exists()) {
            desDir.mkdirs();
        }
        ArrayList<String> files = new ArrayList<>();
        ZipFile zf = new ZipFile(zipFile);
        for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = ((ZipEntry) entries.nextElement());
            InputStream in = zf.getInputStream(entry);
            String str = folderPath + File.separator + entry.getName();
            // str = new String(str.getBytes("8859_1"), "GB2312");
            File desFile = new File(str);
            if (!desFile.exists()) {
                File fileParentDir = desFile.getParentFile();
                if (!fileParentDir.exists()) {
                    fileParentDir.mkdirs();
                }
                desFile.createNewFile();
                files.add(str);
            }
            OutputStream out = new FileOutputStream(desFile);
            byte buffer[] = new byte[1024];
            int realLength;
            while ((realLength = in.read(buffer)) > 0) {
                out.write(buffer, 0, realLength);
            }
            in.close();
            out.close();
        }
        return files;
    }

    public static String checkEditString(TextView editText, String msg4Null) throws InputNullException {
        String input = editText.getText().toString();
        if (TextUtils.isEmpty(input)) {
//            throw new InputNullException("editText input is Null");
            throw new InputNullException(msg4Null);
        }
        return input;
    }

    public static void checkString(boolean flag, String msg4Null) throws InputNullException {
        if (!flag) {
            throw new InputNullException(msg4Null);
        }
    }

    public static String checkPassword(EditText editText) throws InputNullException {
        String input = editText.getText().toString();
        if (input.length() < 6 || input.length() > 16) {
            throw new InputNullException("请输入6-16位密码");
        }

        return input;
    }
    /**
     * 根据原始rar路径，解压到指定文件夹下.          * @param srcRarPath 原始rar路径     * @param dstDirectoryPath 解压到的文件夹
     */
    /*public static ArrayList<String> unRarFile(String srcRarPath, String dstDirectoryPath) {
        if (!srcRarPath.toLowerCase().endsWith(".rar")) {
            System.out.println("非rar文件！");
            return null;
        }
        ArrayList<String> files = new ArrayList<>();
        File dstDiretory = new File(dstDirectoryPath);
        if (!dstDiretory.exists()) {// 目标目录不存在时，创建该文件夹
            dstDiretory.mkdirs();
        }
        Archive a = null;
        try {
            a = new Archive(new File(srcRarPath));
            if (a != null) {
                a.getMainHeader().print(); // 打印文件信息.
                FileHeader fh = a.nextFileHeader();
                while (fh != null) {
                    if (fh.isDirectory()) { // 文件夹
                        File fol = new File(dstDirectoryPath + File.separator + fh.getFileNameString());
                        fol.mkdirs();
                    } else { // 文件
                        File out = new File(dstDirectoryPath + File.separator + fh.getFileNameString().trim());
                        files.add(out.getAbsolutePath());
                        System.out.println(out.getAbsolutePath());
                        try {// 之所以这么写try，是因为万一这里面有了异常，不影响继续解压.
                            if (!out.exists()) {
                                if (!out.getParentFile().exists()) {
                                    // 相对路径可能多级，可能需要创建父目录.
                                    out.getParentFile().mkdirs();
                                }
                                out.createNewFile();
                            }
                            FileOutputStream os = new FileOutputStream(out);
                            a.extractFile(fh, os);
                            os.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    fh = a.nextFileHeader();
                }
                a.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return files;
        }
        return files;
    }*/


    /**
     * 异或加密
     *
     * @param strOld
     * @param strKey
     * @return
     */
    public static String encrypt(String strOld, String strKey) {
        byte[] data = strOld.getBytes();
        byte[] keyData = strKey.getBytes();
        int keyIndex = 0;
        for (int x = 0; x < strOld.length(); x++) {
            data[x] = (byte) (data[x] ^ keyData[keyIndex]);
            if (++keyIndex == keyData.length) {
                keyIndex = 0;
            }
        }
        return new String(data);
    }


    public static String enCodeing(String ost) {
        StringBuilder dest = new StringBuilder();
        dest.append(ost.substring(0, ost.lastIndexOf("/") + 1));
        dest.append(Uri.encode(ost.substring(ost.lastIndexOf("/") + 1, ost.length()), "UTF-8"));
        return dest.toString();
    }

    public static String getFileType(String fileType) {
        switch (fileType) {
            case "txt":
                return "【文档】";
            case "word":
                return "【WORD】";
            case "excel":
                return "【EXCEL】";
            case "pdf":
                return "【PDF】";
            case "powerpoint":
                return "【PPT】";
            case "video":
                return "【视频】";
            default:
                return "【其他】";
        }
    }

    public static String getQQAvatarPath() throws Exception {
        return getPackgePath("qqAvatar");
    }

    public static String getPackgePath(String fileName) throws Exception {

        return TApplication.getInstance().getApplicationContext().getExternalFilesDir(fileName).getAbsolutePath();
    }

    public static void talk2QQ(Context context, String qqNum) {
        String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qqNum;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(intent, 0);
        if (resolveInfos.size() > 0) {
            context.startActivity(intent);
        } else {
            TApplication.getInstance().showToast("请先安装qq");
        }
    }

    //帮builder加签名  时间 userId
    public static void buildSign(MultipartBody.Builder builder, HashMap<String, String> params) {
        StringBuilder stringBuilder = new StringBuilder();
        if (params != null) {
            TreeSet<String> treeSet = new TreeSet();
            for (String key : params.keySet()) {
                treeSet.add(key);
            }
            for (String strs : treeSet) {
                stringBuilder.append(strs);
                stringBuilder.append("=");
                stringBuilder.append(params.get(strs));
                stringBuilder.append("|");
                builder.addFormDataPart(strs, params.get(strs));
            }

        }
        int time = (int) (System.currentTimeMillis() / 1000);
        builder.addFormDataPart("time", String.valueOf(time));
        int timeC = time % 10000;
        time = (timeC % 10 + timeC % 100 / 10 + timeC % 1000 / 100 + timeC / 1000) % 10;
        stringBuilder.append(String.valueOf(time));
        builder.addFormDataPart("sign", string2md5(stringBuilder.toString()));
    }

    public static String list2str(List list) {
        String str = "";
        for (int i = 0; i < list.size(); i++) {
            str = str + list.get(i).toString() + ",";
        }
        if(str.length()>0){
            return str.substring(0, str.length() - 1);
        }else {
            return "";
        }
    }
    public static String lessonIndex2Hanzi(int lessonIndex){
        return  "第"+ number2Hanzi(lessonIndex)+"节";
    }

    public static String weekday2Hanzi(int weekday){
        return  "周"+ number2Hanzi(weekday);
    }

    public static String number2Hanzi(int number){
        String str = "";
        switch (number) {
            case 1:
                str = "一";
                break;
            case 2:
                str = "二";
                break;
            case 3:
                str = "三";
                break;
            case 4:
                str = "四";
                break;
            case 5:
                str = "五";
                break;
            case 6:
                str = "六";
                break;
            case 7:
                str = "七";
                break;
            case 8:
                str = "八";
                break;
            case 9:
                str = "九";
                break;
            case 10:
                str = "十";
                break;
        }
        return  str;
    }

}
