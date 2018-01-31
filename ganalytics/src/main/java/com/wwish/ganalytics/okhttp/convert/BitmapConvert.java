package com.wwish.ganalytics.okhttp.convert;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import okhttp3.Response;

/**
 * ================================================
 * 描    述：字符串的转换器
 * ================================================
 */
public class BitmapConvert implements Converter<Bitmap> {

    public static BitmapConvert create() {
        return ConvertHolder.convert;
    }

    private static class ConvertHolder {
        private static BitmapConvert convert = new BitmapConvert();
    }

    @Override
    public Bitmap convertSuccess(Response value) throws Exception {
        return BitmapFactory.decodeStream(value.body().byteStream());
    }
}