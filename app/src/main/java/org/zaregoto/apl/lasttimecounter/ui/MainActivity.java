package org.zaregoto.apl.lasttimecounter.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import org.zaregoto.apl.lasttimecounter.ItemListAdapter;
import org.zaregoto.apl.lasttimecounter.model.Item;
import org.zaregoto.apl.lasttimecounter.model.ItemType;
import org.zaregoto.apl.lasttimecounter.model.ItemUnit;
import org.zaregoto.apl.lasttimecounter.R;
import org.zaregoto.apl.lasttimecounter.db.ItemStore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends AppCompatActivity implements InputItemDetailDialogFragment.InputDialogListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static final String TAG = "MainActivity";
    private InputItemDetailDialogFragment itemInputDialog;
    private ArrayList<Item> items = new ArrayList<>();
    private ItemListAdapter adapter;

    private ItemStore.OrderType orderType = ItemStore.OrderType.ORDER_TYPE_CURRENT_TO_OLD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ItemStore.loadInitialData(this, items);
        ListView lv = findViewById(R.id.mainlist);
        if (null != lv) {
            adapter = new ItemListAdapter(this, 0, items);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(this);
            lv.setOnItemLongClickListener(this);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        if (null != fab) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String name = "";
                    String detail = "";
                    Date now = new Date();

                    ItemType type = ItemType.createItemType(MainActivity.this, ItemUnit.DEFAULT_TYPE_ID);
                    ItemUnit item = new ItemUnit(name, detail, type, now, now);

                    itemInputDialog = InputItemDetailDialogFragment.newInstance(item);
                    itemInputDialog.show(getFragmentManager(), "");
                }
            });
        }

        NavigationView mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.sort:
                        break;
                    case R.id.filter:
                        break;
                    case R.id.config:
                        break;
                    case R.id.backup:
                        ProgressDialogFragment progressDialog = new ProgressDialogFragment();
                        progressDialog.show(getFragmentManager(), "");

                        String filename;
                        filename = "lasttimecounter-";
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                        Date now = new Date();
                        String datestr = sdf.format(now);
                        filename = filename + datestr;

                        File destDir = new File("/mnt/sdcard");

                        try {
                            ItemStore.makeBackup(MainActivity.this, destDir, filename);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            progressDialog.dismiss();
                        }

                        break;
                    case R.id.restore:
                        break;
                    default:
                        break;
                }

                return false;
            }
        });

    }


    @Override
    public void addItem(ItemUnit item) {
        if (null != adapter) {
            ItemStore.insertData(this, item);

            // reload data
            items.clear();
            ;
            ItemStore.loadData(this, items, orderType);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updateItem(ItemUnit item) {
        if (null != adapter) {
            ItemStore.updateData(this, item);

            // reload data
            items.clear();
            ;
            ItemStore.loadData(this, items, orderType);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {

        Bundle args = new Bundle();
        Item item = adapter.getItem(pos);

        if (item instanceof ItemUnit) {
            itemInputDialog = InputItemDetailDialogFragment.newInstance((ItemUnit) item);
            itemInputDialog.show(getFragmentManager(), "");
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("title");
        builder.setMessage("message");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // OK button pressed
                Item item = adapter.getItem(pos);
                adapter.remove(item);
                ItemStore.deleteData(MainActivity.this, item);
            }
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();

        return true;
    }

//    @Override
//    public void selectType(ItemType type) {
//        Log.d(TAG, "***** MainActivity ItemType selected: " + type.getFilename() + "*****");
//        try {
//            itemInputDialog.selectType(type);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
