package edu.iit.cs.cs553.javaclient;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author Bo Feng
 */
public class JavaDelegator implements AppInterface {

    private HttpClient client = new DefaultHttpClient();
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
        HttpPost httppost = new HttpPost(baseUrl + method);

        if (parameters != null && !parameters.isEmpty()) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>(1);

            for (String key : parameters.keySet()) {
                pairs.add(new BasicNameValuePair(key, parameters.get(key)));
            }
            try {
                httppost.setEntity(new UrlEncodedFormEntity(pairs));
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(JavaDelegator.class.getName()).log(Level.WARNING, null, ex);
            }
        }
        
        try {
            HttpResponse response = client.execute(httppost);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(instream));
                try {
                    return new Gson().fromJson(br, Result.class);
                } catch (JsonSyntaxException jsonSyntaxException) {
                    logger.log(Level.SEVERE, "Fatal result violation: {0}", jsonSyntaxException.getMessage());
                    logger.log(Level.SEVERE, "Result violation: first line: {0}", br.readLine());
                } finally {
                    // Closing the input stream will trigger connection release
                    instream.close();
                }
            } else {
                logger.log(Level.SEVERE, "Connection failed: {0}", response.getStatusLine());
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Fatal transport error: {0}", e.getMessage());
        }
        return new Result("boolean", false);
    }

    @Override
    public Boolean connect() {
        return (Boolean) rpc(null, null).value;
    }

    @Override
    public Boolean memcache(boolean turn) {
        Map<String, String> parameters = new HashMap<String, String>();
        if (turn) {
            parameters.put("turn", "on");
        } else {
            parameters.put("turn", "off");
        }
        return (Boolean) rpc("memcache", parameters).value;
    }

//    public Boolean upload(String filename) {
//        HttpClient httpclient = new HttpClient();
//        PostMethod httppost = new PostMethod(baseUrl + "/upload");
//        File file = new File(filename);
//
//        FileEntity entiry = new FileEntity(file, "text/plain; charset=\"UTF-i\"");
//        httppost.setRequestEntity(null);
//
//        System.out.println(response.getStatusLine());
//        if (resEntity != null) {
//            System.out.println(EntityUtils.toString(resEntity));
//        }
//        if (resEntity != null) {
//            resEntity.consumeContent();
//        }
//
//        httpclient.getConnectionManager().shutdown();
//
//        return true;
//    }
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

        Object result = rpc("find", parameters).value;
        if (result instanceof Boolean) {
            return null;
        }
        return (String) result;
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
