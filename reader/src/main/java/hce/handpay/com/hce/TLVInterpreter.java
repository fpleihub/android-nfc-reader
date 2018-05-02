package hce.handpay.com.hce;

import android.text.TextUtils;
import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by fplei on 2018/3/29.
 */

public class TLVInterpreter {
    private static final String TAG = "LoyaltyCardReader";
    public static HashMap<String,String> parser(byte[] datas){
        HashMap<String,String> params=new HashMap<>();
        if(datas!=null&&datas.length>0){
            byte[] payload =datas;
            int resultLength = payload.length;
            Log.i(TAG,"payload.length："+resultLength);
            Log.i(TAG,"parser："+LoyaltyCardReader.ByteArrayToHexString(payload));
            byte[] apduHeader={payload[0],payload[1],payload[2],payload[3]};
            Log.i(TAG,"apduHeader："+LoyaltyCardReader.ByteArrayToHexString(apduHeader));
            params.put("apduHeader",LoyaltyCardReader.ByteArrayToHexString(apduHeader));
            byte[] dataLen={payload[4],payload[5]};
            int len=Integer.parseInt(hexStringToString(LoyaltyCardReader.ByteArrayToHexString(dataLen)));
            Log.i(TAG,"dataLen："+len);
            boolean flag=true;
            int startIndex=6;
            int lenEffect=1;
            while (flag){
                //取tag
                byte[] bTag={payload[startIndex],payload[startIndex+lenEffect]};
                String tag=LoyaltyCardReader.ByteArrayToHexString(bTag);
                Log.i(TAG,"tag="+tag);
                //取长度
                startIndex=startIndex+lenEffect+1;
                byte[] datalen={payload[startIndex],payload[startIndex+lenEffect]};
                Log.i(TAG,LoyaltyCardReader.ByteArrayToHexString(datalen));
                int dlen=Integer.parseInt(hexStringToString(LoyaltyCardReader.ByteArrayToHexString(datalen)));
                Log.i(TAG,"dlen="+dlen);
                //取数据
                startIndex=startIndex+lenEffect+1;
                byte[] data=new byte[dlen];
                for(int i=0;i<dlen;i++){
                    data[i]=payload[startIndex];
                    startIndex++;
                }
                String dt=hexStringToString(LoyaltyCardReader.ByteArrayToHexString(data));
                Log.i(TAG,"data="+dt);
                Log.i(TAG,"startIndex="+startIndex);
                params.put(tag,dt);
                if(resultLength==startIndex){
                    //读取到最后结束
                    flag=false;
                }
            }
        }
        return params;
    }

    /**
     * 字符串转换为16进制字符串
     *
     * @param s
     * @return
     */
    public static String stringToHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    /**
     * 16进制字符串转换为字符串
     *
     * @param s
     * @return
     */
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "gbk");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }
}
