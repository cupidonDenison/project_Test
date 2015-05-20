package com.example.track_me.others;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.track_me.SplashScreen;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;
import android.widget.ImageView;

@SuppressWarnings("unused")
public class ImageParser {
	public JSONObject uploadImage(Bitmap bmp,Map<String,String> params) {

		String serverUrl = SplashScreen.SERVER_IP+"/project/imageUpload.php";
		
		String petType = params.get("petType");
		String petAge = params.get("petAge");
		String petAgeMetric = params.get("petAgeMetric");
		String petDesc= params.get("petDesc");
		String username = params.get("username");
		
		Log.d("Pet info", petType+" "+petAge+" "+petAgeMetric+" "+petDesc);
		

		//String imagePath = imageUri.getPath();
		
		HttpClient client = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpPost request = new HttpPost(serverUrl);
		MultipartEntityBuilder builder  = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		
		//File imgFile = new File(imagePath);
		//Bitmap bmp = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		
		Bitmap bmpCompressed = Bitmap.createScaledBitmap(bmp, 300,200,true);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bmpCompressed.compress(CompressFormat.JPEG, 100, bos);
		byte[] data = bos.toByteArray();
		
		 String boundary = "-------------" + System.currentTimeMillis();
		 request.setHeader("Content-type", "multipart/form-data; boundary="+boundary);
		 builder.setBoundary(boundary);
		 
		 String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
			.format(new Date());
		ByteArrayBody byteBody = new ByteArrayBody(data, petType+"_"+timeStamp+".jpg");
		
		builder.addPart("myImage",byteBody);
		
		StringBody type = new StringBody(petType,ContentType.TEXT_PLAIN);
		StringBody ag= new StringBody(petAge,ContentType.TEXT_PLAIN);
		StringBody agM= new StringBody(petAgeMetric,ContentType.TEXT_PLAIN);
		StringBody desc= new StringBody(petDesc,ContentType.TEXT_PLAIN);
		StringBody usr = new StringBody(username,ContentType.TEXT_PLAIN);
		
		builder.addPart("petType", type);
		builder.addPart("petAge", ag);
		builder.addPart("petAgeMetric", agM);
		builder.addPart("petDesc", desc);
		builder.addPart("email", usr);
		

		String jsonString = null;
		JSONObject jObject= null;
		try {
			
			
			
			HttpEntity entity = builder.build();
			
			request.setEntity(entity);

			HttpResponse responseObject = client.execute(request);

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					responseObject.getEntity().getContent(), "UTF-8"));

			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}//End while loop reading response
			
			jsonString = sb.toString();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			jObject = new JSONObject(jsonString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jObject;
	}// End uploadImage()
	


}// En ClassImageParser
