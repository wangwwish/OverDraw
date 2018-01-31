package com.wwish.ganalytics.okhttp.request;



import com.wwish.ganalytics.okhttp.utils.HttpUtils;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * ================================================
 * 描    述：Get请求的实现类，注意需要传入本类的泛型
 * ================================================
 */
public class GetRequest extends BaseRequest<GetRequest> {

    public GetRequest(String url) {
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
        return requestBuilder.get().url(url).tag(tag).build();
    }
}