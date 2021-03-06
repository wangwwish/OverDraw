package com.wwish.ganalytics.okhttp.request;

import com.wwish.ganalytics.okhttp.model.HttpHeaders;
import com.wwish.ganalytics.okhttp.utils.HttpUtils;
import com.wwish.ganalytics.okhttp.utils.Logger;
import okhttp3.Request;

import java.io.IOException;

import okhttp3.RequestBody;

/**
 * ================================================
 * 描    述：Options请求
 * ================================================
 */
public class OptionsRequest extends BaseBodyRequest<OptionsRequest> {

    public OptionsRequest(String url) {
        super(url);
    }

    @Override
    public Request generateRequest(RequestBody requestBody) {
        try {
            headers.put(HttpHeaders.HEAD_KEY_CONTENT_LENGTH, String.valueOf(requestBody.contentLength()));
        } catch (IOException e) {
            Logger.e(e);
        }
        Request.Builder requestBuilder = HttpUtils.appendHeaders(headers);
        return requestBuilder.method("OPTIONS", requestBody).url(url).tag(tag).build();
    }
}