package com.wwish.ganalytics.okhttp.convert;

import okhttp3.Response;

/**
 * ================================================
 * 描    述：字符串的转换器
 * ================================================
 */
public class StringConvert implements Converter<String> {

    public static StringConvert create() {
        return ConvertHolder.convert;
    }

    private static class ConvertHolder {
        private static StringConvert convert = new StringConvert();
    }

    @Override
    public String convertSuccess(Response value) throws Exception {
        return value.body().string();
    }
}
