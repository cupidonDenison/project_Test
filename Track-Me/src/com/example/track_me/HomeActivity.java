package com.example.track_me;

import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.track_me.entity.Place;
import com.example.track_me.others.GridImage;
import com.example.track_me.others.JSONParser;
import com.example.track_me.others.MyLocation;
import com.google.android.gms.maps.model.LatLng;

public class HomeActivity extends DrawerActivity implements LocationListener {

	GridView Grid;
	ArrayList<GridImage> gridArray = new ArrayList<GridImage>();
	GridAdapter GridViewAdapter;
	ProgressDialog pDialog;
	public static List<Place> placesList = new ArrayList<Place>();
	static int responseCount;
	static String type;
	JSONObject responseObj;
	boolean connectionOK;
	Bitmap place2Image;
	JSONParser parser = new JSONParser();
	Exception error;
	private static final String placesURL = SplashScreen.SERVER_IP
			+ "project/getPlacesType.php";

	LocationManager locationManager;

	LatLng myLatLng, placeLatLng;
	MyLocation loc;
	Location myLoc;

	Document mapDoc;
	String provider;
	Criteria criteria = new Criteria();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		ImageView view = (ImageView) findViewById(android.R.id.home);
		view.setPadding(5, 0, 2, 0); // left, top, right, bottom

		Grid = (GridView) findViewById(R.id.gridView);

		gridArray.add(new GridImage(R.drawable.ic_car, "Car Rental"));
		gridArray.add(new GridImage(R.drawable.ic_doctor, "Doctor"));
		gridArray.add(new GridImage(R.drawable.ic_hotel, "Hotel"));
		gridArray.add(new GridImage(R.drawable.ic_people, "People"));
		gridArray.add(new GridImage(R.drawable.ic_restaurant, "Restaurant"));
		gridArray.add(new GridImage(R.drawable.ic_tourist, "Tourist Place"));

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// loc = new MyLocation(getApplicationContext(), locationManager);

		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		provider = this.locationManager.getBestProvider(criteria, true);

		int index = 0;
		String[] locationProvider = new String[] {
				LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER };
		do {
			locationManager.requestLocationUpdates(provider, 2000, 10, this);

			Log.i("provider location ", locationProvider[index]);
			myLoc = locationManager
					.getLastKnownLocation(locationProvider[index]);

			if (myLoc != null) {
				double myLat = myLoc.getLatitude();
				double myLon = myLoc.getLongitude();
				// myLoc = loc.getLocation();
				Log.i("Location Current home activity location ", "" + myLat
						+ ", " + myLon);

				myLatLng = new LatLng(myLat, myLon);
				locationManager.removeUpdates(this);
				locationManager
						.requestLocationUpdates(provider, 2000, 10, this);
			}

			index++;
		} while (myLoc == null);

		locationManager.removeUpdates(this);
		GridViewAdapter = new GridAdapter();
		Grid.setAdapter(GridViewAdapter);
		Grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos,
					long id) {
				placesList = new ArrayList<Place>();
				onGridViewClick(pos);
			}
		});
	} // end onCreate

	@Override
	protected int getLayout() {
		return R.layout.activity_main;
	}

	class GridAdapter extends ArrayAdapter<GridImage> {

		public GridAdapter() {
			super(getApplicationContext(), R.layout.gridview_layout, gridArray);
		}// end constructor

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			if (itemView == null) {
				itemView = getLayoutInflater().inflate(
						R.layout.gridview_layout, parent, false);
			}
			GridImage currentIcon = gridArray.get(position);
			ImageView imageView = (ImageView) itemView
					.findViewById(R.id.item_image);
			TextView textView = (TextView) itemView
					.findViewById(R.id.item_text);
			imageView.setImageResource(currentIcon.getImageId());
			textView.setText(currentIcon.imageName);
			return itemView;
		}

	}// end class MyGridAdapter

	public void onGridViewClick(int position) {
		/*
		 * Intent categoryIntent = new Intent(this, MenuActivity.class); type =
		 * gridArray.get(position).getImageName();
		 * categoryIntent.putExtra("Type", type); startActivity(categoryIntent);
		 */
		if (isOnline()) {
			type = gridArray.get(position).getImageName();
			if (type.equals("People")) {
				Intent intent = new Intent(getApplicationContext(),
						PeopleActivity.class);
				startActivity(intent);
			} else {
				new places().execute();
			}

		} else {
			Toast.makeText(getApplicationContext(),
					"You need to switch on wifi or data connection ",
					Toast.LENGTH_LONG).show();
		}// end else check for connection

	}// end onGridViewClick()

	private void setPlaces(List<Place> newList) {
		this.placesList = newList;
	}// end SetPlaces()

	public static List<Place> getPlaces() {
		return placesList;
	}// end getPlaces

	// check for internet connection
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}

	public static String getType() {
		return type;
	}

	class places extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(HomeActivity.this);
			pDialog.setMessage("Loading " + type + ".....");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}// end onPreExcute()

		@Override
		protected Void doInBackground(Void... arg0) {

			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("type", type));
				params.add(new BasicNameValuePair("startLat", myLoc
						.getLatitude() + ""));
				params.add(new BasicNameValuePair("startLong", myLoc
						.getLongitude() + ""));

				Log.i("my location : ", "startLat " + myLoc.getLatitude() + " "
						+ "startLong " + myLoc.getLongitude());

				InetAddress inet = InetAddress.getByAddress(new byte[] {
						(byte) 192, (byte) 168, 43, (byte) 181 });

				String reachable = inet.isReachable(5000) ? "Host is reachable"
						: "Host is NOT reachable";

				Log.i("host reachable ? ", reachable);
				
				inet =InetAddress.getByName(new URL(placesURL).getHost());
				String ip =  inet.getHostAddress();
				
				Log.i("host ip  ", ip);

				//if (inet.isReachable(5000)) {
					responseObj = parser.makeHttpRequest(placesURL, "POST",
							params);

					try {
						if (isOnline()) {
							Log.i("Response Type", type);
							Log.i("Response object", responseObj.toString());

							responseCount = responseObj.getInt("count");
							String responseStatus = responseObj
									.getString("status");

							Log.i("Response status : ", responseStatus);

							Log.i("response count",
									String.valueOf(responseCount));

							if (responseCount > 0) {
								JSONArray placesArray = responseObj
										.getJSONArray("places");
								
								Log.i("Places array ", placesArray.toString());

								connectionOK = true;
								try {
									for (int i = 0; i < responseCount; i++) {
										JSONObject obj = placesArray
												.getJSONObject(i);

										String id = obj.getString("place_id");
										String icon = obj.getString("icon");
										String name = obj.getString("name");
										double latitude = obj
												.getDouble("latitude");
										double longitude = obj
												.getDouble("longitude");

										JSONArray typeArray = obj.getJSONArray(
												"types").getJSONArray(0);

										Log.i("response types",
												typeArray.toString());

										ArrayList<String> typesArrayList = new ArrayList<String>();

										for (int j = 0; j < typeArray.length(); j++) {
											String placeType = typeArray
													.getString(j);

											if (!(placeType
													.equals("establishment"))) {
												typesArrayList.add(placeType);
												// Log.i("Response type",
												// placeType);
												// Log.i("Response type list size",
												// typesArrayList.size()+"");
											}
										}// end for loop

										Log.i("Response out of type loop",
												"out");
										Place newPlace = new Place(id, type,
												name, icon, latitude, longitude);

										newPlace.setTypes(typesArrayList);
										// get place image from server

										String imageURL = newPlace.getIcon();

										Bitmap image = parser
												.getImage(imageURL);
										if (image == null)
											Log.i("response image",
													"image is null");

										newPlace.setImage(image);
										LatLng currentLatLng = new LatLng(
												latitude, longitude);

										List<NameValuePair> pr = new ArrayList<NameValuePair>();

										pr.add(new BasicNameValuePair(
												"startLat", myLoc.getLatitude()
														+ ""));
										pr.add(new BasicNameValuePair(
												"startLong", myLoc
														.getLongitude() + ""));
										pr.add(new BasicNameValuePair("endLat",
												latitude + ""));
										pr.add(new BasicNameValuePair(
												"endLong", longitude + ""));

										JSONObject object = parser
												.makeHttpRequest(
														SplashScreen.SERVER_IP
																+ "project/getLocationDetails.php",
														"POST", pr);
										// JSONObject object =
										// parser.makeHttpRequest("http://192.168.137.1/project/getLocationDetails.php","POST",
										// pr);
										Log.i("Current location",
												myLoc.getLatitude() + " ,"
														+ myLoc.getLongitude());

										String dist = object
												.getString("distance");

										newPlace.setDistance(Double.parseDouble(String.format(
												"%.2f",
												Double.parseDouble(dist) / 1000)));

										// newPlace.setImage(image);

										placesList.add(newPlace);

									}// end loop in placeArray
								} catch (Exception e) {
									connectionOK = false;

								}

							}// End if statement if no data corresponding to
								// type is
								// found
								// in database

						}// end if error in connection

					} catch (JSONException e) {
						e.printStackTrace();
					}// end try-catch statement

				//}
			} catch (Exception e) {
				connectionOK = false;
				error = e;
			}

			return null;
		}// end doInBackground()

		@Override
		protected void onPostExecute(Void result) {
			pDialog.dismiss();
			if (connectionOK) {

				if (placesList.size() > 0) {

					for (Place place : placesList) {
						double placelat = place.getLatitude();
						double placelon = place.getLongitude();

						placeLatLng = new LatLng(placelat, placelat);

						Location placeLoc = new Location("");
						placeLoc.setLatitude(placelat);
						placeLoc.setLongitude(placelon);

						// double distance = (myLoc.distanceTo(placeLoc)) /
						// 1000;
						// String dist = String.format("%.2f", distance);

						// distance = Double.parseDouble(dist);
						// place.setDistance(distance);

					}// end for loop

					Intent categoryIntent = new Intent(HomeActivity.this,
							MenuActivity.class);
					categoryIntent.putExtra("Type", type);
					startActivity(categoryIntent);
				} else {
					Toast.makeText(getApplicationContext(),
							"No result found for : " + type, Toast.LENGTH_LONG)
							.show();
				}// No REcord

			}// End if statement if no data corresponding to type is found in
				// database
			else {
				Log.e("Response error ", "response error occur", error);
				Toast.makeText(getApplicationContext(),
						"Unable to connect to Server ", Toast.LENGTH_LONG)
						.show();

			}// end else check for connection

			responseObj = null;
		}// end onPostExecute()

	}// end AsyncTask

	@Override
	public void onLocationChanged(Location location) {
		myLoc = location;
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}
}// End Activity
