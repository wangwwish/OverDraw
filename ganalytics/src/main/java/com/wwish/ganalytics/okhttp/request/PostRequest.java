package com.wwish.ganalytics.okhttp.request;



import com.wwish.ganalytics.okhttp.model.HttpHeaders;
import com.wwish.ganalytics.okhttp.utils.HttpUtils;
import com.wwish.ganalytics.okhttp.utils.Logger;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * ================================================
 * 描    述：Post请求的实现类，注意需要传入本类的泛型
 * ================================================
 */
public class PostRequest extends BaseBodyRequest<PostRequest> {

    public PostRequest(String url) {
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
        return requestBuilder.post(requestBody).url(url).tag(tag).build();
    }
}