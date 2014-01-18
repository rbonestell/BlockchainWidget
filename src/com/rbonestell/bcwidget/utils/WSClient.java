package com.rbonestell.bcwidget.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/***
 * HTTP client
 */
public class WSClient
{
	
	public enum RequestType { GET, POST, UPDATE, DELETE };
	
	/***
	 * Send web request
	 * @param targetURL Target web service URL including query parameters
	 * @return Raw JSON response
	 */
	public static String sendRequest(String targetURL, RequestType reqType)
	{
        String line;
        String response = "";
        try
        {
        	URL url = new URL(targetURL);
        	HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
			conn.setRequestMethod(reqType.toString());
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setDoInput(true);
			
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
			{
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				
				while ((line = rd.readLine()) != null)
					response += line;
				
				rd.close();	
			}
			else
			{
				response = "Error: " + conn.getResponseCode() + " " + conn.getResponseMessage();
			}
			
			conn.disconnect();
		}
        catch (Exception e)
        {
			response = "Error: " + e.getMessage();
		}
        return response;
	}
}
