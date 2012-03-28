package edu.iit.cs.cs553.javaclient;

import java.io.IOException;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * A Simple Java client of App on Google App Engine.
 *
 */
public class App {

    static HttpClient client = new HttpClient();
    static String baseUrl = "http://save-files.appspot.com";

    public static boolean testConnection() {
        GetMethod httpget = new GetMethod(baseUrl + "/");

        httpget.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));

        try {
            int statusCode = client.executeMethod(httpget);

            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Connection failed: " + httpget.getStatusLine());
            }

            // Read the response body.
            String responseBody = httpget.getResponseBodyAsString();

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
            httpget.releaseConnection();
        }
    }
    
    public static boolean testInsert() {
        AppInterface ai = new JavaDelegator();
        
        return ai.insert("foo", "bar");
    }

    public static void main(String[] args) {
        System.out.println("Connection: " + (testConnection()?"OK":"Failed"));
        System.out.println("Insert: " + (testInsert()?"OK":"Failed"));
    }
}
