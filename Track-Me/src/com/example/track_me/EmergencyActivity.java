package com.example.track_me;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.track_me.others.ListAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class EmergencyActivity extends DrawerActivity {

	ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    
    final Context context = this;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
 
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
 
        // preparing list data
        prepareListData();
 
        listAdapter = new ListAdapter(this, listDataHeader, listDataChild);
 
        // setting list adapter
        expListView.setAdapter(listAdapter);
 
        // Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {
 
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                    int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });
 
        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
 
            @Override
            public void onGroupExpand(int groupPosition) {
                
            }
        });
 
        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
 
            @Override
            public void onGroupCollapse(int groupPosition) {
                
            }
        });
 
        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {
 
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
            	TextView num = (TextView)findViewById(R.id.lblListNum);
            	final int numb = Integer.parseInt((String) num.getText());
            	
            	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
        				context);
            	alertDialogBuilder.setTitle("Phone Call");
            	
            	
            	alertDialogBuilder
				.setMessage("Call to : " + numb)
				.setCancelable(true)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						// current activity
						Intent sIntent = new Intent(Intent.ACTION_CALL, Uri
		            		      .parse("tel:"+numb));
		            		    startActivity(sIntent);
					}
				  })
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});
            	
            	AlertDialog alertDialog = alertDialogBuilder.create();
            	 
				// show it
				alertDialog.show();
            	
            	
                // TODO Auto-generated method stub
               
                return false;
            }
        });
    }
 
    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
 
        // Adding child data
        listDataHeader.add("Health Centres");
        listDataHeader.add("Hospital");
        listDataHeader.add("Fire Stations");
        listDataHeader.add("Police Stations");
        listDataHeader.add("Others");
 
        // Adding child data
        List<String> health = new ArrayList<String>();
        health.add("Port Louis");
        health.add("Terre Rouge");
        health.add("Vacoas");
        health.add("Rose Belle");
        health.add("Chemin Grenier");
        health.add("Curepipe");
        health.add("Rose Hill");
 
        List<String> Hospital = new ArrayList<String>();
        Hospital.add("Port Louis");
        Hospital.add("Terre Rouge");
        Hospital.add("Vacoas");
        Hospital.add("Rose Belle");
        Hospital.add("Chemin Grenier");
        Hospital.add("Curepipe");
 
        List<String> Fire = new ArrayList<String>();
        Fire.add("Port Louis");
        Fire.add("Terre Rouge");
        Fire.add("Vacoas");
        Fire.add("Rose Belle");
        Fire.add("Chemin Grenier");
        
        List<String> Police = new ArrayList<String>();
        Police.add("Port Louis");
        Police.add("Terre Rouge");
        Police.add("Vacoas");
        Police.add("Rose Belle");
        Police.add("Chemin Grenier");
        
        List<String> Others = new ArrayList<String>();
        Others.add("Port Louis");
        Others.add("Terre Rouge");
        Others.add("Vacoas");
        Others.add("Rose Belle");
        Others.add("Chemin Grenier");
 
        listDataChild.put(listDataHeader.get(0), health); // Header, Child data
        listDataChild.put(listDataHeader.get(1), Hospital);
        listDataChild.put(listDataHeader.get(2), Fire);
        listDataChild.put(listDataHeader.get(3), Police);
        listDataChild.put(listDataHeader.get(4), Others);
    }

}
