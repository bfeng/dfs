package edu.iit.cs.cs553.javaclient;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 *
 * @author Bo Feng
 */
public class JavaDelegator implements AppInterface {

    private HttpClient client = new HttpClient();
    private String baseUrl = "http://save-files.appspot.com";
    private final static Logger logger = Logger.getLogger(JavaDelegator.class.getCanonicalName());

    private class Result {

        protected String type;
        protected Object value;

        public Result() {
        }

        private Result(String type, boolean b) {
            this.type = type;
            this.value = b;
        }
    }

    public JavaDelegator(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    private Result rpc(String method, Map<String, String> parameters) {
        if (method == null || method.isEmpty()) {
            method = "/";
        }
        if (!method.startsWith("/")) {
            method = "/" + method;
        }
        PostMethod httppost = new PostMethod(baseUrl + method);

        httppost.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));

        if (parameters != null && !parameters.isEmpty()) {
            NameValuePair[] data = new NameValuePair[parameters.size()];

            int i = 0;
            for (String key : parameters.keySet()) {
                data[i] = new NameValuePair(key, parameters.get(key));
                i++;
            }

            httppost.setRequestBody(data);
        }

        try {
            int statusCode = client.executeMethod(httppost);

            if (statusCode == HttpStatus.SC_OK) {
                Reader r = new InputStreamReader(httppost.getResponseBodyAsStream());
                try {
                    return new Gson().fromJson(r, Result.class);
                } catch (JsonSyntaxException jsonSyntaxException) {
                    logger.log(Level.SEVERE, "Fatal result violation: {0}", jsonSyntaxException.getMessage());
                    BufferedReader br = new BufferedReader(r);
                    logger.log(Level.SEVERE, "Result violation: first line: {0}", br.readLine());
                    br.close();
                }
            } else {
                logger.log(Level.SEVERE, "Connection failed: {0}", httppost.getStatusLine());
            }
        } catch (HttpException e) {
            logger.log(Level.SEVERE, "Fatal protocol violation: {0}", e.getMessage());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Fatal transport error: {0}", e.getMessage());
        } finally {
            // Release the connection.
            httppost.releaseConnection();
        }
        return new Result("boolean", false);
    }

    @Override
    public Boolean connect() {
        return (Boolean) rpc(null, null).value;
    }

    @Override
    public Boolean cache(boolean turn) {
        Map<String, String> parameters = new HashMap<String, String>();
        if (turn) {
            parameters.put("turn", "on");
        } else {
            parameters.put("turn", "off");
        }
        return (Boolean) rpc("cache", parameters).value;
    }

    @Override
    public Boolean insert(String key, String value) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("key", key);
        parameters.put("value", value);

        Boolean result = (Boolean) rpc("insert", parameters).value;
        return result;
    }

    @Override
    public Boolean check(String key) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("key", key);

        Boolean result = (Boolean) rpc("check", parameters).value;
        return result;
    }

    @Override
    public String find(String key) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("key", key);

        String result = (String) rpc("find", parameters).value;
        return result;
    }

    @Override
    public Boolean remove(String key) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("key", key);

        Boolean result = (Boolean) rpc("remove", parameters).value;
        return result;
    }

    @Override
    public String[] listing() {
        String[] result = (String[]) rpc("listing", null).value;
        return result;
    }
}
