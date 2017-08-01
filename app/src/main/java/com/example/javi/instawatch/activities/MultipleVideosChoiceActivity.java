package com.example.javi.instawatch.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.javi.instawatch.R;
import com.example.javi.instawatch.controlador.ControladorUsuarios;
import com.example.javi.instawatch.controlador.ControladorVideos;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MultipleVideosChoiceActivity extends Activity {
    private ControladorVideos controladorVideos;
    Button button;
    ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_videos_choice);
        String usuario = getIntent().getStringExtra("usuario");
        controladorVideos = new ControladorVideos();
        String[] videosBD = controladorVideos.getNombresVideos(usuario);
        listView = (ListView) findViewById(R.id.listVideosMultiple);
        button = (Button) findViewById(R.id.btnAdjuntarVideos);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, videosBD);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SparseBooleanArray checked = listView.getCheckedItemPositions();
                ArrayList<String> selectedItems = new ArrayList<String>();
                for (int i = 0; i < checked.size(); i++) {
                    // Item position in adapter
                    int position = checked.keyAt(i);
                    // Add sport if it is checked i.e.) == TRUE!
                    if (checked.valueAt(i))
                        selectedItems.add(adapter.getItem(position));
                }

                ArrayList<String> outputStrArr = new ArrayList<String>();

                for (int i = 0; i < selectedItems.size(); i++) {
                  outputStrArr.add(selectedItems.get(i));
                }
                Intent i = getIntent();
                i.putStringArrayListExtra("videosSeleccionados",outputStrArr);
                setResult(RESULT_OK, i);
                finish();
            }
        });

    }
}
