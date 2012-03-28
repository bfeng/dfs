package edu.iit.cs.cs553.javaclient;

import java.io.IOException;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 *
 * @author Bo Feng
 */
public class JavaDelegator implements AppInterface {

    private static HttpClient client = new HttpClient();
    private static String baseUrl = "http://save-files.appspot.com";

    @Override
    public Boolean insert(String key, String value) {
        PostMethod httppost = new PostMethod(baseUrl + "/insert");
        
        httppost.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));
        
        NameValuePair[] data = {
          new NameValuePair("key", key),
          new NameValuePair("value", value)
        };
        httppost.setRequestBody(data);
        
        try {
            int statusCode = client.executeMethod(httppost);

            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Connection failed: " + httppost.getStatusLine());
            }

            // Read the response body.
            String responseBody = httppost.getResponseBodyAsString();

            // Deal with the response.
            // Use caution: ensure correct character encoding and is not binary data
            System.out.println(responseBody);

            return true;
        }
        catch (HttpException e) {
            System.err.println("Fatal protocol violation: " + e.getMessage());
            return false;
        }
        catch (IOException e) {
            System.err.println("Fatal transport error: " + e.getMessage());
            return false;
        }
        finally {
            // Release the connection.
            httppost.releaseConnection();
        }
    }

    @Override
    public Boolean check(String key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String find(String key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean remove(String key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
