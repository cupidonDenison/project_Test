package com.example.track_me.others;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.track_me.SplashScreen;
import com.google.android.gms.maps.model.LatLng;

public class JSONParser {

	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";

	// constructor
	public JSONParser() {

	}

	// function get json from url
	// by making HTTP POST or GET mehtod
	public JSONObject makeHttpRequest(String url, String method,
			List<NameValuePair> params) {

		/* Log.i("Number parser url", url); */
		// Making HTTP request
		try {

			// check for request method
			if (method == "POST") {
				// request method is POST
				// defaultHttpClient
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(params));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();

			} else if (method == "GET") {
				// request method is GET
				DefaultHttpClient httpClient = new DefaultHttpClient();
				String paramString = URLEncodedUtils.format(params, "utf-8");
				url += "?" + paramString;
				HttpGet httpGet = new HttpGet(url);

				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();

		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// return JSON String
		return jObj;

	}// End makeHTTPREquest()

	public Bitmap getImage(String imageURL) {
		Bitmap image;
		
				

		Log.i("Response image url", imageURL);

		try {
			URL locImg = new URL(imageURL);
			Log.d("Response image url", locImg.toString());
			HttpURLConnection connection = (HttpURLConnection) locImg
					.openConnection();
			Log.d("response image connection",
					"Opening new connection for image");

			connection.setDoInput(true);
			Log.d("Response set DoInput", "set DoInput");

			connection.connect();

			Log.d("Response connection connect", "connection connect");

			InputStream in = connection.getInputStream();

			Log.d("Response creating inputStream",
					"Creaing input stream for image");

			image = BitmapFactory.decodeStream(in);
			image = Bitmap.createScaledBitmap(image, 140, 140, true);

			return image;
		} catch (Exception e) {

			e.printStackTrace();
			Log.e("Response connection error ", e.toString(), e);
			return null;
		}

	}// End getImage
	
	public JSONObject postJSON(String url, JSONObject obj){
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost request = new HttpPost(url);
			StringEntity se = new StringEntity(obj.toString());
			request.addHeader("content-type","application/json");
			request.addHeader("Accept","application/json");
			request.setEntity(se);
			HttpResponse response = httpClient.execute(request);
			
			//Get response
			
			HttpEntity httpEntity = response.getEntity();
			is = httpEntity.getContent();
			
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				json = sb.toString();

			} catch (Exception e) {
				Log.e("Buffer Error", "Error converting result " + e.toString());
			}
			
			try {
				jObj = new JSONObject(json);
			} catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}
			
		}//end try block 
		catch (Exception e) {
			
		}//end catch block
		
		return jObj;
	}

}// End classs class JSONParser