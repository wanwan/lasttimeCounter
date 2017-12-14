package org.zaregoto.apl.lasttimecounter.ui;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import org.zaregoto.apl.lasttimecounter.ItemAdapter;
import org.zaregoto.apl.lasttimecounter.Item;
import org.zaregoto.apl.lasttimecounter.R;
import org.zaregoto.apl.lasttimecounter.db.ItemStore;

import java.util.*;

public class MainActivity extends AppCompatActivity implements ItemInputDialogFragment.InputDialogListener {

    ItemInputDialogFragment itemInputDialog;
    ArrayList<Item> items = new ArrayList<>();
    ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ItemStore.loadInitialData(this, items);
        ListView lv = findViewById(R.id.mainlist);
        if (null != lv) {
            adapter = new ItemAdapter(this, 0, items);
            lv.setAdapter(adapter);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        if (null != fab) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemInputDialog = new ItemInputDialogFragment();
                    itemInputDialog.show(getFragmentManager(), "");
                }
            });
        }
    }


    @Override
    public void addItem(Item item) {
        if (null != adapter) {
            adapter.add(item);
            ItemStore.insertData(this, item);
        }
    }
}
