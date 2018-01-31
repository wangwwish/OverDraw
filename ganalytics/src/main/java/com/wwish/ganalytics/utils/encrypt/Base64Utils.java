package com.wwish.ganalytics.utils.encrypt;

import java.io.UnsupportedEncodingException;

/**
 * Created by gaocaili on 2017/7/11.
 */

public class Base64Utils {
    private static char char62 = 43;
    private static char char63 = 47;
    private static char[] infp;
    public static final boolean NEED_ADD_ENTER_CODE = true;
    public static final int ENTERCOUNT = 76;
    public static final String ENTER = "";
    public static final int MASK_FLOW_8 = 255;
    public static final int MASK_FLOW_6 = 63;

    public Base64Utils() {
    }

    private static byte transS2B(char c) {
        byte result = 0;
        byte result1;
        if(c >= 65 && c <= 90) {
            result1 = (byte)(c - 65);
            return result1;
        } else if(c >= 97 && c <= 122) {
            result1 = (byte)(c - 97 + 26);
            return result1;
        } else if(c >= 48 && c <= 57) {
            result1 = (byte)(c - 48 + 52);
            return result1;
        } else {
            return c == char62?62:(c == char63?63:result);
        }
    }

    public static String encodeMessage(byte[] message) {
        StringBuilder sb = new StringBuilder();
        int message_byte_length = message.length;
        int end = 3 - message_byte_length % 3;
        if(end == 3) {
            end = 0;
        }

        Object operate_message_byte = null;
        byte[] var9;
        if(end > 0) {
            var9 = new byte[message_byte_length + end];
            System.arraycopy(message, 0, var9, 0, message_byte_length);
        } else {
            var9 = message;
        }

        int line_count = 1;
        int i = 0;

        for(int j = var9.length / 3; i < j; ++i) {
            int i_byte_real = i * 3;
            sb.append(infp[var9[i_byte_real] >> 2 & 63]);
            sb.append(infp[(var9[i_byte_real] << 4 & 48 | var9[i_byte_real + 1] >>> 4 & 15) & 63]);
            if(i_byte_real + 1 > message_byte_length - 1) {
                break;
            }

            sb.append(infp[(var9[i_byte_real + 1] << 2 & 60 | var9[i_byte_real + 2] >>> 6 & 3) & 63]);
            if(i_byte_real + 2 > message_byte_length - 1) {
                break;
            }

            sb.append(infp[var9[i_byte_real + 2] & 63]);
            if((line_count + 3) % 76 == 0) {
                sb.append("");
            }

            line_count += 4;
        }

        if(end == 2) {
            sb.append("==");
        } else if(end == 1) {
            sb.append('=');
        }

        return sb.toString();
    }

    private static char[] clearStringMessage(String message) {
        Object result = null;
        char[] message_chararray = message.toCharArray();
        int message_chararray_length = message_chararray.length;
        int controlCharCount = 0;

        int i;
        for(i = 0; i < message_chararray_length; ++i) {
            if(message_chararray[i] == 13 || message_chararray[i] == 10) {
                ++controlCharCount;
            }
        }

        char[] var7 = new char[message_chararray_length - controlCharCount];
        i = 0;

        for(int j = 0; i < message_chararray_length; ++i) {
            if(message_chararray[i] != 13 && message_chararray[i] != 10) {
                var7[j] = message_chararray[i];
                ++j;
            }
        }

        return var7;
    }

    public static byte[] decodeMessage(String message) {
        char[] message_char = clearStringMessage(message);
        int length = message_char.length / 4 * 3;
        boolean realLength = false;
        int var15;
        if(message_char[message_char.length - 2] == 61) {
            var15 = length - 2;
        } else if(message_char[message_char.length - 1] == 61) {
            var15 = length - 1;
        } else {
            var15 = length;
        }

        byte[] result = new byte[var15];

        for(int i = 0; i < length / 3; ++i) {
            int i_base64_real = i * 4;
            int i_byte___real = i * 3;
            byte b1 = transS2B(message_char[i_base64_real]);
            byte b2 = transS2B(message_char[i_base64_real + 1]);
            byte b3 = transS2B(message_char[i_base64_real + 2]);
            byte b4 = transS2B(message_char[i_base64_real + 3]);
            byte bb1 = (byte)(b1 << 2 & 252 | b2 >> 4 & 3 & 255);
            byte bb2 = (byte)(b2 << 4 & 240 | b3 >> 2 & 15 & 255);
            byte bb3 = (byte)(b3 << 6 & 192 | b4 & 63 & 255);
            result[i_byte___real] = bb1;
            if(i_byte___real + 1 >= var15) {
                break;
            }

            result[i_byte___real + 1] = bb2;
            if(i_byte___real + 2 >= var15) {
                break;
            }

            result[i_byte___real + 2] = bb3;
        }

        return result;
    }

    public static void main(String[] args) throws UnsupportedEncodingException, InterruptedException {
        long start = System.currentTimeMillis();
        System.out.println(start);
        byte count = 1;
        String target = "这是一段Base64加密的文本文件，需要转化曾为UTF- 8 的byte数组，然后生成BASE64格式的文本";

        for(int end = 0; end < count; ++end) {
            byte[] tmp = target.getBytes("UTF-8");
            String enStr = encodeMessage(tmp);
            System.out.println(enStr);
            String ouStr = new String(decodeMessage(enStr), "UTF-8");
            System.out.println(ouStr);
            System.out.println(ouStr.equals(target));
        }

        long var9 = System.currentTimeMillis();
        System.out.println(var9);
        System.out.println("耗时" + (var9 - start));
        Thread.sleep(10000000L);
    }

    static {
        infp = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', char62, char63};
    }
}
