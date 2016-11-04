package com.iyad.sultan.callme;


import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.iyad.sultan.callme.Model.Contacts;
import com.iyad.sultan.callme.Model.RealmModel;

import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoadContactsData extends Fragment {

    private static final int STC = 0;
    private static final int MOBILY = 1;
    private static final int ZAIN = 2;

    private RealmModel realmModel;

    public LoadContactsData() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_load_contacts_data, container, false);

        //create Realm
        realmModel = new RealmModel();

        //Spanner
        Spinner spinner = (Spinner) v.findViewById(R.id.operator_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.operator_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                realmModel.insertUserOperator(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        final Button loadData = (Button) v.findViewById(R.id.loadData);

        loadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestReadContactsPermission();
                }

                //API 22 and Down
                else loadContacts();

                loadData.setEnabled(false);
                //   loadData.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorAccent));
                //loadData.setText(R.string.load_data_faild);
            }
        });

        // Inflate the layout for this fragment
        return v;
    }


    void loadContacts() {

        List<Contacts> contactsList = new ArrayList<>();
        Cursor cursor = null;
try {


     cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
    while (cursor.moveToNext()) {
        String Name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
        String Number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        Contacts contact = new Contacts();
        contact.setName(Name);
        contact.setNumber(Number.replace(" ", ""));
        contactsList.add(contact);
    }
} catch (Exception e){
    Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
}
        finally {
    if (cursor != null)
        cursor.close();
        }

        if (contactsList.size() > 0) {

            List<Contacts> con = removeDuplicate(contactsList);
            addToRealm(con);
        } else Toast.makeText(getActivity(), R.string.no_contacts_found, Toast.LENGTH_SHORT).show();
    }


    void addToRealm(List<Contacts> contactsList) {
        boolean isFailed = false;

        //Add new Data To Realm
        if (contactsList.size() > 0)
            isFailed = realmModel.addContacts(contactsList);

//sow result
        if (isFailed)
            Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getActivity(), R.string.success, Toast.LENGTH_SHORT).show();


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestReadContactsPermission() {

        int permissionCheck = getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS);
        //no permission Ask for it
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 456);
            // else do the work
        } else loadContacts();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 456:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(getActivity(), R.string.plz_allow_contacts, Toast.LENGTH_SHORT).show();
                else {
                    loadContacts();
                }
                break;
            default:
                Toast.makeText(getActivity(), "No Default ", Toast.LENGTH_SHORT).show();

                super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (realmModel != null)
            realmModel.onDestroyed();

    }


    private List<Contacts> removeDuplicate(List<Contacts> list) {


        HashSet<String> mobileNoSet = new HashSet<String>();
        List<Contacts> newList = new ArrayList<>();
        for (Contacts contacts : list) {

            if (!mobileNoSet.contains(contacts.getNumber())) {

                newList.add(contacts);
                mobileNoSet.add(contacts.getNumber());
            }
        }
        return newList;
    }


}
