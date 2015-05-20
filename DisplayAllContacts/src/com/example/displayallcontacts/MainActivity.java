package com.example.displayallcontacts;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.displayallcontacts.JSONParser;

public class MainActivity extends Activity {
    ListView list;
    LinearLayout ll;
    Button loadBtn;
    private ProgressDialog pDialog;

	JSONParser jsonParser = new JSONParser();
	// url to create new product
	private static String url_insert_product = "http://192.168.173.1/insert.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ll = (LinearLayout) findViewById(R.id.LinearLayout1);

        list = (ListView) findViewById(R.id.listView1);

        loadBtn = (Button) findViewById(R.id.button1);
        loadBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                LoadContactsAyscn lca = new LoadContactsAyscn();
                lca.execute();
            }
        });

    }

    class LoadContactsAyscn extends AsyncTask<Void, Void, ArrayList<String>> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pd = ProgressDialog.show(MainActivity.this, "Loading Contacts",
                    "Please Wait");
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ArrayList<String> contacts = new ArrayList<String>();

            
            Cursor c = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    null, null, null);
            int i =1;
            while (c.moveToNext()) {

                String contactName = c
                        .getString(c
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).toString();
                String phNumber = c
                        .getString(c
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).toString();
                List<NameValuePair> datas = new ArrayList<NameValuePair>();
    			datas.add(new BasicNameValuePair("name", contactName));
    			datas.add(new BasicNameValuePair("pnum", phNumber));

    			// getting JSON Object
    			// Note that create product url accepts POST method
    			/*JSONObject json = jsonParser.makeHttpRequest(url_insert_product,
    					"POST", datas);*/
    			
    			Log.e("Contact "+i, contactName+" "+phNumber);
                contacts.add(contactName + ":" + phNumber);
                i++;
            }
            c.close();

            return contacts;
        }

        @Override
        protected void onPostExecute(ArrayList<String> contacts) {
            // TODO Auto-generated method stub
            super.onPostExecute(contacts);

            pd.cancel();

            ll.removeView(loadBtn);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getApplicationContext(), R.layout.test, contacts);

            list.setAdapter(adapter);

        }

    }
}
