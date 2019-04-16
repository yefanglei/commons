package me.own.learn.commons.base.utils.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Christopher.Wang on 2016/7/13.
 */
public class HttpUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    public static String post(Map<String, String> paramMap, String targetUrl){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(targetUrl);
        String result = "";
        if (paramMap!=null){
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (String key : paramMap.keySet()){
                nvps.add(new BasicNameValuePair(key, paramMap.get(key)));
            }
            UrlEncodedFormEntity form = null;
            try {
                form = new UrlEncodedFormEntity(nvps,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getMessage(), e);
            }
            httpPost.setEntity(form);
        }

        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

            @Override
            public String handleResponse( final HttpResponse response) throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    String result = entity != null ? EntityUtils.toString(entity) : null;
                    EntityUtils.consume(entity);
                    return result;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            }
        };

        try {
            result = httpclient.execute(httpPost, responseHandler);
        } catch (ClientProtocolException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }
}
