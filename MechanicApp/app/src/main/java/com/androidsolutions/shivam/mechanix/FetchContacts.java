package com.androidsolutions.shivam.mechanix;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shivam on 9/16/2018.
 */

public class FetchContacts extends AsyncTask<Void, Void, List> {
    private Context activity;
    List<Contact> contactList;
    public FetchContacts(Context context) {
        activity = context;
        contactList=null;
    }
    @Override
    protected void onPostExecute(List list) {
        super.onPostExecute(list);
        MainActivity.contacts = list;
    }

    @Override
    protected List doInBackground(Void... params) {
        List<Contact> contactList = new ArrayList<>();
        // get Contacts here
        ContentResolver cr = activity.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
//                        Log.i("GOT", "Name: " + name);
//                        Log.i("GOT", "Phone Number: " + phoneNo); //working
                        //To our POJO
                        Contact contact = new Contact();
                        contact.setName(name);
                        contact.setNumber(phoneNo);
                        contactList.add(contact);
                    }
                    pCur.close();
                }
            }
        }
        if (cur != null) {
            cur.close();
        }
        return contactList;
    }
}