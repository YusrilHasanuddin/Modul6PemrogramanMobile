package com.example.myapplication6;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.*;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;




import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Read extends AppCompatActivity implements
        ListView.OnItemClickListener{
    private ListView listView;
    private String JSON_STRING;
    private void showMahasiswa(){
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Konfigurasi.TAG_JSON_ARRAY);
            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(Konfigurasi.TAG_ID);
                String nama = jo.getString(Konfigurasi.TAG_NAMA);
                Log.i("asd", nama);
                HashMap<String,String> mahasiswa = new HashMap<>();
                mahasiswa.put(Konfigurasi.TAG_ID,id);
                mahasiswa.put(Konfigurasi.TAG_NAMA,nama);
                list.add(mahasiswa);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListAdapter adapter = new SimpleAdapter(
                Read.this, list, R.layout.list_item,
                new String[]{Konfigurasi.TAG_ID,Konfigurasi.TAG_NAMA},
                new int[]{R.id.id, R.id.nama});
        listView.setAdapter(adapter);
    }
    private void getJSON(){
        @SuppressLint("StaticFieldLeak")
        class getJSON extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                JSON_STRING = "s";
                showMahasiswa();
            }
            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                return rh.sendGetRequest(Konfigurasi.URL_GET_ALL);
            }
        }
        getJSON gj = new getJSON();
        gj.execute();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, Select.class);
        HashMap map =(HashMap)parent.getItemAtPosition(position);
        String mhsId = Objects.requireNonNull(map.get(Konfigurasi.TAG_ID)).toString();
        intent.putExtra(Konfigurasi.MHS_ID,mhsId);
        startActivity(intent);
    }
}
