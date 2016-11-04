package com.iyad.sultan.callme;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.iyad.sultan.callme.Controller.Adapter;
import com.iyad.sultan.callme.Model.RealmModel;

import java.net.URI;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    private static final int STC = 0;
    private static final int MOBILY = 1;
    private static final int ZAIN = 2;
    private RealmModel realmModel;
    private Adapter mAdapter;
    //Graphical
    private Paint p = new Paint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Check Permissions
        isCALL_PHONEPermissionGranted();


        //set Realm Model
        realmModel = new RealmModel();
//Dumb Data
        //  realmModel.InsertDumbData();
        // realmModel.showDumbData();

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rec);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        mAdapter = new Adapter(realmModel.getContacts());
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
//Show the Intro Once
        isFirstTime();


    } //onCreate

    //Show the Intro Once
    void isFirstTime() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);
                int userSelectCompany = getPrefs.getInt("userSelectCompany", 0);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    Intent i = new Intent(MainActivity.this, IntroActivity.class);
                    startActivity(i);

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);


                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();

    }

    //Animation
    private ItemTouchHelper.Callback createHelperCallback() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        //    moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                        return true;
                    }

                    @Override
                    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        if (swipeDir == ItemTouchHelper.LEFT)
                            sendCallMe(realmModel.getUserOperator(), ((TextView) viewHolder.itemView.findViewById(R.id.txt_number)).getText().toString());

                        else
                            doCallMe(realmModel.getUserOperator(), ((TextView) viewHolder.itemView.findViewById(R.id.txt_number)).getText().toString());

                    }

                    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                        Bitmap icon;
                        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                            View itemView = viewHolder.itemView;
                            float height = (float) itemView.getBottom() - (float) itemView.getTop();
                            float width = height / 3;

                            if (dX > 0) {
                                p.setColor(Color.parseColor("#00C853"));
                                RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop() , dX, (float) itemView.getBottom());
                                c.drawRect(background, p);
                                icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_perm_phone_msg_white_24dp);
                                RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                                c.drawBitmap(icon, null, icon_dest, p);
                            } else {
                                p.setColor(Color.parseColor("#558B2F"));
                                RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                                c.drawRect(background, p);
                                icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_mail_outline_white_24dp);
                                RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                                c.drawBitmap(icon, null, icon_dest, p);
                            }
                        }
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    }
                };
        return simpleItemTouchCallback;
    }

    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.seach_item, menu);

        //change operator icon
        MenuItem operator = menu.findItem(R.id.opertor_icon);
        switch (realmModel.getUserOperator()) {
            case STC:
                operator.setIcon(R.drawable.stc);
                operator.setTitle(R.string.stc);
                break;
            case MOBILY:
                operator.setIcon(R.drawable.mobily);
                operator.setTitle(R.string.mobily);
                break;
            case ZAIN:
                operator.setIcon(R.drawable.zain);
                operator.setTitle(R.string.zain);
                break;
        }

        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItemCompat.setOnActionExpandListener(searchItem,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed

                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        mAdapter.setAdapterData(realmModel.getContacts());
                        mAdapter.notifyDataSetChanged();

                        return true; // Return true to expand action view
                    }
                });
        //SearchView
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                mAdapter.setAdapterData(realmModel.getQueryConcats(query));
                mAdapter.notifyDataSetChanged();


                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mAdapter.setAdapterData(realmModel.getQueryConcats(query));
                mAdapter.notifyDataSetChanged();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_reser:
                startActivity(new Intent(getApplicationContext(), IntroActivity.class));
                return true;
        }


        return super.onOptionsItemSelected(item);

    }
//End Menu


    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realmModel.onDestroyed();
    }


    //Permissions Handling
/*
android.permission.READ_CONTACTS
android.permission.CALL_PHONE
 */
    private void isCALL_PHONEPermissionGranted() {


        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 123);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 123:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    alertDialog();

                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }


    }
    //End Permissions Handling


    //Alert
    void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.permission).setMessage(R.string.plz_allow_phone).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.create().show();


    }


    //Logic
    private void sendCallMe(final int userSelectCompany, final String targetNumber) {

        Intent sendCallme = new Intent(Intent.ACTION_CALL);

        switch (userSelectCompany) {

            case STC:
                sendCallme.setData(Uri.parse("tel:*177*" + targetNumber + Uri.encode("#")));

                break;
            case MOBILY:
                sendCallme.setData(Uri.parse("tel:*199*" + targetNumber + Uri.encode("#")));

                break;
            case ZAIN:
                sendCallme.setData(Uri.parse("tel:*123*" + targetNumber + Uri.encode("#")));


                break;
            default:
                Toast.makeText(MainActivity.this, R.string.no_Operate_selected, Toast.LENGTH_SHORT).show();

        }
        try {
            startActivity(sendCallme);
        } catch (Exception e) {
            alertDialog();
        }


    }

    private void doCallMe(final int userSelectCompany, final String targetNumber) {

        Intent Callme = new Intent(Intent.ACTION_CALL);

        switch (userSelectCompany) {

            case STC:
                Callme.setData(Uri.parse("tel:*199*" + targetNumber + Uri.encode("#")));

                break;
            case MOBILY:
                Callme.setData(Uri.parse("tel:*9" + targetNumber));

                break;
            case ZAIN:
                Callme.setData(Uri.parse("tel:*121*" + targetNumber + Uri.encode("#")));

                break;
            default:
                Toast.makeText(MainActivity.this, R.string.no_Operate_selected, Toast.LENGTH_SHORT).show();

        }//switch
        try {
            startActivity(Callme);
        } catch (Exception e) {
            alertDialog();
        }
    }



}


//class
