package com.example.notices;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class NoticeList extends AppCompatActivity {

    private static final String TAG = login.class.getSimpleName();
    private SQLiteHandler db;
    private SessionManager session;
    private ArrayAdapter<String> noticeadapter;
    private ListView noticelist;
    private DeleteNotice delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_list);
        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        getNotice();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (session.isadmin()){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.contextmenu,menu);}
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int i = info.position;
        switch (item.getItemId()) {
            case R.id.edit:
                Intent intent = new Intent(getApplicationContext(),AddNotice.class);
                intent.putExtra("uid", UniqueId.uniqueid[i]);
                startActivity(intent);
                return true;
            case R.id.delete:
                delete = new DeleteNotice(UniqueId.uniqueid[i],NoticeList.this);
                Intent intent1 = new Intent(getApplicationContext(),NoticeList.class);
                startActivity(intent1);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionmenu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (!session.isadmin()){
            menu.getItem(1).setEnabled(false);
            menu.getItem(1).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {


            if (session.isLoggedIn()){
                db.deleteUsers();
            }
            session.setLogin(false);
            session.setAdmin(false);
            // Launching the login activity
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.add) {
            Intent intent = new Intent(getApplicationContext(), AddNotice.class);
            startActivity(intent);
            finish();
        }

        return true;
    }
    private void getNotice() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_NOTICES, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "data Response: " + response.toString());

                try {
                    JSONObject noticejson = new JSONObject(response);
                    JSONArray noticearray = noticejson.getJSONArray("notice");
                    String[] uid = new String[noticearray.length()];
                    String[] title = new String[noticearray.length()];
                    String[] description = new String[noticearray.length()];
                    String[] createdat = new String[noticearray.length()];
                    String[] updatedat = new String[noticearray.length()];
                   // uniqueid = new String[noticearray.length()];

                    //parsing json

                    for (int i = 0; i < noticearray.length(); i++) {

                       JSONObject notice =  noticearray.getJSONObject(i);

                        uid [i] = notice.getString("uniqueid");
                        title[i] = notice.getString("title");
                        description[i] = notice.getString("description");
                        createdat [i] = notice.getString("createdat");
                        updatedat [i] = notice.getString("updatedat");
                       }
                    // passing parameters uid, title, description, createdat, updatedat
                    UniqueId.uniqueid = uid;
                    adapter(uid, title, description, createdat, updatedat);

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {



        };

        // Adding request to request queue
        AppController.getInstance(getApplicationContext()).addToRequestQueue(strReq, tag_string_req);
    }

    // adding title in the list

    private void adapter(String[] uid, String[] title, final String[] description, String[] createdat, String[] updatedat){
        noticeadapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.listitem,
                R.id.noticelistitem,
                title);
        noticelist = (ListView) findViewById(R.id.noticeListView);
        noticelist.setAdapter(noticeadapter);
        noticelist.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(getApplicationContext(),DisplayDescription.class);
                        i.putExtra("description", description[position]);
                        startActivity(i);
                    }
                }
        );
        registerForContextMenu(noticelist);

    }
}