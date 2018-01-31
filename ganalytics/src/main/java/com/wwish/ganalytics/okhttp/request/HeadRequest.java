package com.wwish.ganalytics.okhttp.request;



import com.wwish.ganalytics.okhttp.utils.HttpUtils;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * ================================================
 * 描    述：head
 * ================================================
 */
public class HeadRequest extends BaseRequest<HeadRequest> {

    public HeadRequest(String url) {
        super(url);
    }

    @Override
    public RequestBody generateRequestBody() {
        return null;
    }

    @Override
    public Request generateRequest(RequestBody requestBody) {
        Request.Builder requestBuilder = HttpUtils.appendHeaders(headers);
        url = HttpUtils.createUrlFromParams(baseUrl, params.urlParamsMap);
        return requestBuilder.head().url(url).tag(tag).build();
    }
}