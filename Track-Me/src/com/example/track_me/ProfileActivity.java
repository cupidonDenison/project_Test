package com.example.track_me;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Date;
import java.util.Map;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.track_me.others.ImageParser;
import com.example.track_me.others.JSONParser;
import com.example.track_me.others.LocationTracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View.OnClickListener;


public class ProfileActivity extends DrawerActivity {

	public final int CATEGORY_ID =0;  
	private static int PICK_IMAGE = 1; 
	private static final int PICK_Camera_IMAGE = 2;
	private static final String url_update_profile = SplashScreen.SERVER_IP+"project/editProfile.php";
	private static final String url_load_profile = SplashScreen.SERVER_IP+"project/loadProfile.php";
	private static final String url_update_loc = SplashScreen.SERVER_IP+"project/editLoc.php";
	private String serverUrl = SplashScreen.SERVER_IP+"/project/imagePicture.php";
	String imageUrl = SplashScreen.SERVER_IP + "/project/";
	Double latitude;
	Double longitude;


	private String number,url;
	private ProgressDialog pDialog;
	private Bitmap bitmap;
	private Dialog dialog;
	private Uri fileUri;
	private String[] categoryContent = {
			"Camera", 
			"Gallery"		
	};

	ImageView imgPreview,editName,editPic;
	Button homeLoc;
	EditText name;
	
	JSONParser jsonParser = new JSONParser();
	LocationTracker tracker;
	
	// JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PROFILE = "profile";
    private static final String TAG_PHONE = "phone_number";
    private static final String TAG_NAME = "name";
    private static final String TAG_URL = "imageLoc";
    private static final String TAG_LAT = "lng";
    private static final String TAG_LNG = "lat";
    
    Uri selectedImageUri = null;
	String filePath = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		SharedPreferences pref = getSharedPreferences("number",Context.MODE_PRIVATE);
		number = pref.getString("phone", "default value");
		
		// align icon 5dp-left and 2dp-right
		ImageView view = (ImageView)findViewById(android.R.id.home);
		view.setPadding(5, 0, 2, 0);
		
	    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
		
		// set ImageButton Background transparent
		editName = (ImageView)findViewById(R.id.editName);  
		editName.setBackgroundColor(Color.TRANSPARENT);
		
		// load user details 
		new GetUserDetails().execute();
		
		editName.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (isOnline()){
					new SaveProfileDetails().execute();}
				else{
					Toast.makeText(getApplicationContext(), "Check Your Connection and try again",
							 Toast.LENGTH_LONG).show(); 				
				}		
			}		
		});
		
		// set OnClickListener on ImagePreview 
		imgPreview = (ImageView)findViewById(R.id.preview);
	
		editPic = (ImageView)findViewById(R.id.edit_pic);
		editPic.setOnClickListener(new View.OnClickListener() {	
		
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				showDialog(CATEGORY_ID); 
				
			}	
		});

		
		// set OnClickListener on Button homeLoc
		homeLoc = (Button)findViewById(R.id.btnHomeLoc);
		homeLoc.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {	
				
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileActivity.this);
		        alertDialog.setTitle("Home Location");
		        alertDialog.setMessage("Take current Location as your Home Location?");
		        alertDialog.setIcon(R.drawable.ok);
		        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
		        	
		        	public void onClick(DialogInterface dialog,int which) {
		        		
		        		tracker = new LocationTracker(ProfileActivity.this);
		                // check if GPS enabled     
		                if(tracker.canGetLocation()){
		                     
		                    latitude = tracker.getLatitude();
		                    longitude = tracker.getLongitude();
		                    
		                    if (isOnline()){
		                    	new HomeLoc().execute();
		                    	Toast.makeText(getApplicationContext(), "Home Location set to : " + latitude  + " " + longitude,
		                    			Toast.LENGTH_LONG).show();
		                    }
		                    
		                    else{
		    					Toast.makeText(getApplicationContext(), "Check Your Connection and try again",
		    							 Toast.LENGTH_LONG).show(); 				
		    				}
		                    
		                }else{
		                	// can't get location
		                	// GPS or Network is not enabled
		                	// Ask user to enable GPS/network in settings
		                	tracker.showSettingsAlert();
		                	}		      	
		                }   	
		        });
		        
		        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            // Write your code here to invoke NO event
		            Toast.makeText(getApplicationContext(), "Update Cancelled", Toast.LENGTH_SHORT).show();
		            dialog.cancel();
		            }
		        });	
		        alertDialog.show();	
			}		
		});
	} // end of OnCreate Method
	
	
	// check for Internet connection
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();		
	}
	
	class GetUserDetails extends AsyncTask<String, String, String> {
		
		// Before starting background thread Show Progress Dialog
		 @Override
		 protected void onPreExecute() { 
			 super.onPreExecute();
			 pDialog = new ProgressDialog(ProfileActivity.this);
			 pDialog.setMessage("Loading Your Profile..Please wait...");
			 pDialog.setIndeterminate(false);
			 pDialog.setCancelable(true);
			 pDialog.show();
		}
	 
	     // Getting Profile details in background thread
		 protected String doInBackground(String... params) {
		 	 
		 	 // updating UI from Background Thread
		 	 runOnUiThread(new Runnable() {
		 		 public void run() {
		 			 // Check for success tag
		 			 int success;
		 			 try {
		 				 
		 				if (isOnline()) {
		 					
		 					List<NameValuePair> params = new ArrayList<NameValuePair>();
		 					params.add(new BasicNameValuePair("phone_number", number));
		 					
		 					// getting Profile details by making HTTP request
		 					JSONObject json = jsonParser.makeHttpRequest(url_load_profile, "GET", params);
		 					
		 					Log.d("Single Profile Details", json.toString()); // Check Log for json response		 					
		 					success = json.getInt(TAG_SUCCESS); // json success tag
		 					
		 					if (success == 1) { // Successfully received Profile Details							 
		 						
		 						JSONArray profileObj = json.getJSONArray(TAG_PROFILE); // JSON Array
		 						JSONObject user = profileObj.getJSONObject(0); // get First Profile object from JSON Array
		 						
		 						name = (EditText)findViewById(R.id.name);
		 						name.setText(user.getString(TAG_NAME)); // display Profile name in EditText
		 						url = user.getString(TAG_URL);  // get Image Url
		 						
		 						try {
		 							URL locImg = new URL(imageUrl + url);
		 							Log.i("Image Url", locImg.toString());
		 							setImage(locImg);		 							
		 						} catch (MalformedURLException e) {
		 							e.printStackTrace();            	 		 							
		 						}
		 						
		 					}else{
		 						Toast.makeText(getApplicationContext(), "Sorry could not load your details",
									 Toast.LENGTH_LONG).show(); 	 
		 						}	 					
		 				} // end method isOnline() 
		 				
		 				else{
		 					Toast.makeText(getApplicationContext(), "Check Your Connection and try again",
									 Toast.LENGTH_LONG).show(); 	 	 					
		 				}
		 			 } catch (JSONException e) {		 				  				 
		 				 e.printStackTrace();		 						 				 
		 			 }		 			 
		 		 }	 
		 	 });
		 	 return null; 	 
		 }
	
		 public void setImage(URL url){ 
			 try {
				 HttpURLConnection connection= (HttpURLConnection)url.openConnection();
				 connection.setDoInput(true);
				 connection.connect();
				 InputStream inputStream = connection.getInputStream();
				 bitmap = BitmapFactory.decodeStream(inputStream);//Convert to bitmap
				 imgPreview.setImageBitmap(bitmap);
	          }
			 catch (IOException e) {
				 e.printStackTrace();				 
			 }		  		 
		 }
		 
		 //After completing background task Dismiss the progress dialog
		 protected void onPostExecute(String file_url) {
			 // dismiss the dialog once got all details
			 pDialog.dismiss();
		 }
	}
	
	class SaveProfileDetails extends AsyncTask<String, String, String> {
		
		//Before starting background thread Show Progress Dialog
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ProfileActivity.this);
			pDialog.setMessage("Saving Profile ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		 }
		
		// Updating Profile
		protected String doInBackground(String... args) {
			// getting Updated data from EditText
			name = (EditText)findViewById(R.id.name);
			String usrname = name.getText().toString();
			
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(TAG_PHONE, number));
			params.add(new BasicNameValuePair(TAG_NAME, usrname));
			
			
			
			// sending modified data through http request
			JSONObject json = jsonParser.makeHttpRequest(url_update_profile,"GET", params);
			
			// check json success tag
			try {
				int success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					// successfully updated
					Intent i = getIntent();
					// send result code 100 to notify about profile update
					setResult(100, i);
	             } else {
	            	 Toast.makeText(getApplicationContext(), "An error occured while updating your Profile", Toast.LENGTH_LONG).show();		 
	             }
				} catch (JSONException e) {
	                e.printStackTrace();
	            }
			return null;
			}
		
	        protected void onPostExecute(String file_url) {
	            // dismiss the dialog once profile updated
	            pDialog.dismiss();
	        }
	 } // end class SaveProfileDetails
	
	class HomeLoc extends AsyncTask<String, String, String> {
		
		//Before starting background thread Show Progress Dialog
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ProfileActivity.this);
			pDialog.setMessage("Saving Home Location ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		 }
		
		// Updating Profile
		protected String doInBackground(String... args) {
			
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(TAG_PHONE, number));
			params.add(new BasicNameValuePair(TAG_LAT, latitude.toString()));
			params.add(new BasicNameValuePair(TAG_LNG, longitude.toString()));
			// sending modified data through http request
			JSONObject json = jsonParser.makeHttpRequest(url_update_loc,"GET", params);
			
			// check json success tag
			try {
				int success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					// successfully updated
					Intent i = getIntent();
					// send result code 100 to notify about product update
					setResult(100, i);
	             } else {
	            	 Toast.makeText(getApplicationContext(), "An error occured while setting Home Location", Toast.LENGTH_LONG).show();		 
	             }
				} catch (JSONException e) {
	                e.printStackTrace();
	            }
			return null;
			}
		
		//After completing background task Dismiss the progress dialog
	         
	        protected void onPostExecute(String file_url) {
	            // dismiss the dialog once product uupdated
	            pDialog.dismiss();
	        }
	 } // end class HomeLoc
	
    class UploadImageTask extends AsyncTask<String, String, String> {
    	
    	Bitmap image;

		public UploadImageTask(Bitmap bmp) {
			image = bmp;
		}
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        	pDialog = new ProgressDialog(ProfileActivity.this);
        	pDialog.setMessage("Uploading Image...");
        	pDialog.setIndeterminate(false);
        	pDialog.setCancelable(true);
        	pDialog.show();
        }
 
        @Override
        protected String doInBackground(String... params) {
        	
        	Map<String, String> userInfo = new HashMap<String, String>();
        	userInfo.put(TAG_PHONE, number);
      
			
			JSONObject obj = new ImageParser().uploadImage(image, userInfo);
			Log.d("Image Response", obj.toString());
			return null;
        
        }
 
        @Override
        protected void onPostExecute(String result) {
        	// dismiss the dialog once product uudated
            pDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Upload Successfully",
					Toast.LENGTH_LONG).show();
        }
    }
	// Custom Alert Dialog for Selecting Profile Picture
	protected Dialog onCreateDialog(int id) {     
		
		switch(id) {     
		case CATEGORY_ID:     
			AlertDialog.Builder builder;     
			Context mContext = this;     
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);     
			View layout = inflater.inflate(R.layout.custom_dialog,(ViewGroup) findViewById(R.id.layout_root));     
			GridView gridview = (GridView)layout.findViewById(R.id.dialogGrid);     
			gridview.setAdapter(new ImageAdapter(this));     
			gridview.setOnItemClickListener(new OnItemClickListener() {  
				
				public void onItemClick(AdapterView<?> parent, View v,int position, long id) {     
					switch(position){
					case 0: 
						if (isCameraAvailable()) {
							takePicture();
							}
						break;
						
					case 1: 
						uploadPicture();
						break;
					}	
				}     
			});  // end OnItemClickListener
			
			// Set Close Icon to Alert Dialog
			ImageView close = (ImageView) layout.findViewById(R.id.close);  
			close.setOnClickListener(new View.OnClickListener() {  
				
				public void onClick(View v){  
					dialog.dismiss();  	
				}  
				
			});  
			builder = new AlertDialog.Builder(mContext);     
			builder.setView(layout);     
			dialog = builder.create();     
			break;     
			
		default:     
			dialog = null;     
		}     
		return dialog;     	
	}  

	public class ImageAdapter extends BaseAdapter {     
		
		private Context mContext;  
		private LayoutInflater mInflater;  
		
		public ImageAdapter(Context c) {   
			mInflater = LayoutInflater.from(c);  
			mContext = c;      	
		}     
		
		public int getCount() {     
			return mThumbIds.length;     	 			
		}     
		
		public Object getItem(int position) {     
			return null;     				
		}
		
		public long getItemId(int position) {     
			return 0;			
		}
		
		// create a new ImageView for each item referenced
		public View getView(int position, View convertView, ViewGroup parent) {
			
			ViewHolder holder;
			if (convertView == null) {  // if it's not recycled,     
				convertView = mInflater.inflate(R.layout.custom, null);  
				convertView.setLayoutParams(new GridView.LayoutParams(150, 150));  
				holder = new ViewHolder();  
				holder.title = (TextView) convertView.findViewById(R.id.optionText);  
				holder.icon = (ImageView )convertView.findViewById(R.id.optionImage);  
				convertView.setTag(holder);  				 				
			} else {  
				 holder = (ViewHolder) convertView.getTag();   			 
			}
			holder.icon.setAdjustViewBounds(true);  
			holder.icon.setScaleType(ImageView.ScaleType.CENTER_CROP);     
			holder.icon.setPadding(8, 8, 8, 8);  
			holder.title.setText(categoryContent[position]);  
			holder.title.setGravity(Gravity.CENTER);
			holder.icon.setImageResource(mThumbIds[position]);  
			
			return convertView;     		 		
		}  // end method getView
		
		class ViewHolder {  			
			TextView title;  
			ImageView icon;  		 			
		}
		
		// references to our images     
		private Integer[] mThumbIds = {     
				R.drawable.camera, 
				R.drawable.gallery			
		};  	 		
	} // end class ImageAdapter
	
	// Check if camera is available
	protected boolean isCameraAvailable() {
		if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			// camera is present
			return true;
		} else {
			// camera is absent
			return false;
		}	
	}// end isCameraAvailable
	
	protected void takePicture() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// create a file to save image
		fileUri = getOutputMediaFileUri(1);
		// specifying path to save image
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		// starting the image capture Intent
		startActivityForResult(intent, PICK_Camera_IMAGE);
		dialog.dismiss(); 	
	}
	
	public void uploadPicture() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"),PICK_IMAGE);	
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		
		if (requestCode == PICK_IMAGE) {
			if (resultCode == Activity.RESULT_OK) {
				selectedImageUri = data.getData();				
			}			
		} else if (requestCode == PICK_Camera_IMAGE) {
			if (resultCode == RESULT_OK) {
				//use imageUri here to access the image
				selectedImageUri = fileUri;	
			} else if (resultCode == RESULT_CANCELED) {
					Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
            } else {
            	Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();       	
            }		
		}
		
		if(selectedImageUri != null){
			
			try {
				// OI FILE Manager
				String filemanagerstring = selectedImageUri.getPath();
				// MEDIA GALLERY
                String selectedImagePath = getPath(selectedImageUri);
                
                if (selectedImagePath != null) {
                	filePath = selectedImagePath;                	
                } else if (filemanagerstring != null) {
                	filePath = filemanagerstring;               	
                } else {
                	Toast.makeText(getApplicationContext(), "Unknown path",Toast.LENGTH_LONG).show();
                    Log.e("Bitmap", "Unknown path");                    
                }
                
                if (filePath != null) {
                	decodeFile(filePath);            	
                } else {
                    bitmap = null;
                }              
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "Internal error",Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);			
			}			
		}	
	}
	
	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
		if (cursor != null) {
			
			// HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
			// THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);		
		} else {
			return null;		
		}		
	}
	
	public void decodeFile(String filePath) {
		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);
        
        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;
        
        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        
        while (true) {
        	if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
        		break;
        	width_tmp /= 2;
        	height_tmp /= 2;
        	scale *= 2;	
        }
        
        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);
        imgPreview.setImageBitmap(bitmap);
        //new UploadImageTask(bitmap).execute();
        
    }
	
	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));	
	}
	
	private static File getOutputMediaFile(int type) {
		// External sdcard location
		File mediaStorageDir = new File(Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Project");
		
		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("Project", "Oops! Failed to create "
						+ "Project" + " directory");
				return null;			
			}			
		}
		
		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == 1) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");		
		} else {
			return null;
		}
		return mediaFile;
	} // end method getOutputMediaFile
	
} // end Class ProfileActivity
