package com.example.track_me.others;

import java.util.TreeMap;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.track_me.entity.Contact;

public class ContactManager {

	Context context;

	public ContactManager(Context context) {
		super();
		this.context = context;
	}

	public TreeMap<String, Contact> readPhoneContact() {
		TreeMap<String, Contact> myContact = new TreeMap<String, Contact>();

		Cursor c = context.getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
				null, null);

		while (c.moveToNext()) {
			String name = c
					.getString(
							c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
					.toString();
			String number = c
					.getString(
							c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
					.toString();

			if (number.length() >= 8) {
				if (number.length() > 8 && number.length() !=13 && number.length() !=11) {
					String newNumber = normalizeNumber(number, number.length());
					number = newNumber;
					Contact contact = new Contact(name, "+230"+number);
					myContact.put("+230"+number, contact);
				} else if (number.length() == 8
						&& (String.valueOf(number.charAt(0)).equals("5"))) {
					Contact contact = new Contact(name, "+230"+number);
					myContact.put("+230"+number, contact);
				}
			}

		}
		return myContact;
	}

	private String normalizeNumber(String number, int numberLength) {
		switch (numberLength) {
		case 9:
			String[] numArr = number.split("-");
			number = numArr[0] + numArr[1];
			break;

		case 12:
			String newNum = "";
			for (int i = 4; i < numberLength; i++) {
				newNum += String.valueOf(number.charAt(i));
			}
			number = newNum;
			break;
		

		}
		return number;
	}
}// End class contactManager
