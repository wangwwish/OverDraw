package com.wwish.ganalytics.okhttp.request;



import java.io.IOException;

import com.wwish.ganalytics.okhttp.model.HttpHeaders;
import com.wwish.ganalytics.okhttp.utils.HttpUtils;
import com.wwish.ganalytics.okhttp.utils.Logger;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * ================================================
 * 描    述：delete
 * ================================================
 */
public class DeleteRequest extends BaseBodyRequest<DeleteRequest> {

    public DeleteRequest(String url) {
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
        return requestBuilder.delete(requestBody).url(url).tag(tag).build();
    }
}