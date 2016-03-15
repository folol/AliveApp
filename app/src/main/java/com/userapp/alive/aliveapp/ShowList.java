package com.userapp.alive.aliveapp;
import com.userapp.alive.aliveapp.MainActivity.*;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ShowList extends AppCompatActivity {
    public final static String DEVICE_NAME = "com.mycompany.myfirstapp.DEVICE_NAME";
    public final static String DEVICE_ADDRESS = "com.mycompany.myfirstapp.DEVICE_ADDRESS";
    ListView listView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_list);
        DeviceList adapter = new DeviceList(this, MainActivity.devicelist);
// Attach the adapter to a ListView
         listView = (ListView) findViewById(R.id.lvItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Devices userApp = (Devices)adapterView.getAdapter().getItem(i);
                Intent intent = new Intent(ShowList.this,MainActivity.class);

                intent.putExtra(DEVICE_NAME,userApp.name);
                intent.putExtra(DEVICE_ADDRESS,userApp.address);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
