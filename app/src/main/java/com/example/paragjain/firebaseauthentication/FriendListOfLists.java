package com.example.paragjain.firebaseauthentication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.example.paragjain.firebaseauthentication.ListController;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by paragjain on 11/10/17.
 */

public class FriendListOfLists extends NavBar {

    private GridView listOfListsGridView;
    private FriendListAdapter listOfListsAdapter;
    private EditText listEditText;
    private StaticDatabaseHelper db;

    protected void onCreate(Bundle savedInstance){
        //super.onCreate(savedInstance);
        //setContentView(R.layout.activity_list_of_lists_view);

        super.onCreate(savedInstance);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_friend_list_of_lists, null, false);
        drawer.addView(contentView, 0);

        db = new StaticDatabaseHelper(this);
        listOfListsGridView = (GridView) findViewById(R.id.grid_list);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                invalidateOptionsMenu();
            }
        }, 0, 2000);


        setTitle(getIntent().getStringExtra("friendName") + "'s Lists");
        //updateUI();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("", "Refreshed token: " + refreshedToken);
    }

    public void logOut(View v) {
        db.deleteEmail();
        Intent intent = new Intent(this, LoginView.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (db.getEmail() == null) {
            Intent it = new Intent(this, LoginView.class);
            startActivity(it);
            finish();
        };
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        String notif = db.getNotification();
        if (notif != null && notif.equals("true")) {
            getMenuInflater().inflate(R.menu.notification_on, menu);
        } else {
            getMenuInflater().inflate(R.menu.notification_off, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_notification:
                db.setNotificationFalse();
                Intent it = new Intent(this, NotificationView.class);
                startActivity(it);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createDialog() {
        listEditText = new EditText(this);
        listEditText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New List")
                .setMessage("Add a new list")
                .setView(listEditText)
                .setPositiveButton("Add List", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogue, int which) {
                        String listName = String.valueOf(listEditText.getText());
                        /*SQLiteDatabase db = mHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(Task.TaskEntry.COL_TASK_TITLE, task);
                        db.insertWithOnConflict(Task.TaskEntry.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                        db.close();*/
                        List newList = ListController.createList(db.getEmail(), listName, FriendListOfLists.this);
                        updateUI();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    private void updateUI() {
        ArrayList<FriendList> listHolder = ListController.getPeerLists(getIntent().getStringExtra("friendEmail"), FriendListOfLists.this);
        /*SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(Task.TaskEntry.TABLE,
                new String[] {Task.TaskEntry.COL_TASK_TITLE}, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(Task.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(index));
        }
        */
        if (listHolder != null) {
            if (listOfListsAdapter == null) {
                listOfListsAdapter = new FriendListAdapter(this, listHolder);
                listOfListsGridView.setAdapter(listOfListsAdapter);

            } else {
                listOfListsAdapter.clear();
                listOfListsAdapter.addAll(listHolder);
                listOfListsAdapter.notifyDataSetChanged();
            }
        } else if(listOfListsAdapter != null){
            listOfListsAdapter.clear();
        }
        //cursor.close();
        //db.close();
    }

    public void goToItem(View view) {
        //View parent =(View) view.getParent();
        TextView listIDView = (TextView) view.findViewById(R.id.list_id);
        TextView listNameView = (TextView) view.findViewById(R.id.list_title);
        String listID = String.valueOf(listIDView.getText());
        String listName = String.valueOf(listNameView.getText());
        Intent it = new Intent(this, FriendListOfItems.class);
        it.putExtra("listID", listID);
        it.putExtra("listName", listName);
        //it.putExtra("friendEmail", getIntent().getStringArrayExtra("friendEmail"));
        it.putExtra("prevActivity", "friendList");
        startActivity(it);
    }


}
