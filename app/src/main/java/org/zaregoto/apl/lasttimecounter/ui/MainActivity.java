package org.zaregoto.apl.lasttimecounter.ui;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import org.zaregoto.apl.lasttimecounter.ItemListAdapter;
import org.zaregoto.apl.lasttimecounter.db.ItemDBException;
import org.zaregoto.apl.lasttimecounter.model.Alarm;
import org.zaregoto.apl.lasttimecounter.model.Item;
import org.zaregoto.apl.lasttimecounter.model.ListableUnit;
import org.zaregoto.apl.lasttimecounter.model.ItemType;
import org.zaregoto.apl.lasttimecounter.R;
import org.zaregoto.apl.lasttimecounter.db.ItemStore;
import org.zaregoto.apl.lasttimecounter.service.NotificationService;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.zaregoto.apl.lasttimecounter.ui.MainActivity.SORT_TYPE.SORT_TYPE_NEARLEST_ALARM;
import static org.zaregoto.apl.lasttimecounter.ui.MainActivity.SORT_TYPE.SORT_TYPE_NEWER_TO_OLD;
import static org.zaregoto.apl.lasttimecounter.ui.MainActivity.SORT_TYPE.SORT_TYPE_OLDER_TO_NEW;

public class MainActivity
        extends AppCompatActivity
        implements InputItemDialogFragment.InputDialogListener,
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener,
        RemoveItemAlertDialogFragment.AlertDialogListener,
        SelectTypeDialogFragment.SelectTypeDialogListener {

    public static final String ARG_ID_SORT_TYPE = "SORT_TYPE";

    public enum SORT_TYPE {
        SORT_TYPE_NEWER_TO_OLD,
        SORT_TYPE_OLDER_TO_NEW,
        SORT_TYPE_NEARLEST_ALARM,
    }

    private static final String TAG = "MainActivity";
    private InputItemDialogFragment inputItemDialog;
    private ArrayList<ListableUnit> items = new ArrayList<>();
    private ItemListAdapter adapter;

    private SORT_TYPE sortType = SORT_TYPE_NEWER_TO_OLD;
    private ItemType selectedType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (null != intent) {
            sortType = (SORT_TYPE) intent.getSerializableExtra(ARG_ID_SORT_TYPE);
            if (null == sortType) {
                sortType = SORT_TYPE_NEWER_TO_OLD;
            }
        }

        setContentView(R.layout.activity_main);

        startService(new Intent(this, NotificationService.class));

        //ItemStore.loadInitialData(this, items);
        ItemStore.loadData(MainActivity.this, items, sortType);
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

                    ItemType type = ItemType.createItemType(MainActivity.this, Item.DEFAULT_TYPE_ID);
                    Alarm alarm = new Alarm(Alarm.ALARM_TYPE.ALARM_TYPE_NONE, 0);
                    Item item = new Item(name, detail, type, now, now, alarm);

                    inputItemDialog = InputItemDialogFragment.newInstance(item);
                    inputItemDialog.show(getFragmentManager(), "");
                }
            });
        }

        NavigationView mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.sort:
                        toggleSortType();
                        if (null != items) {
                            items.clear();
                            ItemStore.loadData(MainActivity.this, items, sortType);
                        }
                        adapter.notifyDataSetChanged();

                        DrawerLayout dl = findViewById(R.id.drawer_layout);
                        if (null != dl) {
                            dl.closeDrawers();
                        }

                        break;
                    case R.id.filter:

                        SelectTypeDialogFragment typeSelectDialog = SelectTypeDialogFragment.newInstance(true);
                        typeSelectDialog.setDialogListener(MainActivity.this);
                        typeSelectDialog.show(getFragmentManager(), "");

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
    public void addItem(Item item) {
        if (null != adapter) {
            ItemStore.insertData(this, item);

            // reload data
            items.clear();
            ;
            ItemStore.loadData(this, items, sortType);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updateItem(Item item) {
        if (null != adapter) {
            ItemStore.updateData(this, item);

            // reload data
            items.clear();

            ItemStore.loadData(this, items, sortType);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void redoItem(Item item) {

        if (null != adapter) {
            try {
                ItemStore.redoData(this, item);

                // reload data
                items.clear();
                ;
                ItemStore.loadData(this, items, sortType);
                adapter.notifyDataSetChanged();
            } catch (ItemDBException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {

        Bundle args = new Bundle();
        ListableUnit item = adapter.getItem(pos);

        if (item instanceof Item) {
            inputItemDialog = InputItemDialogFragment.newInstance((Item) item);
            inputItemDialog.show(getFragmentManager(), "");
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long id) {

        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(100);

        ListableUnit item = adapter.getItem(pos);

        FragmentManager fm = getFragmentManager();
        ItemActionDialogFragment actionDlg = ItemActionDialogFragment.newInstance(item);
        actionDlg.show(fm, "");

        return true;
    }

    @Override
    public void removeItemConfirm(Item item) {
        Log.d(TAG, "*** ok called ***");
        adapter.remove(item);
        ItemStore.deleteData(MainActivity.this, item);
    }

    @Override
    public void cancelRemoveItem(Item item) {
        Log.d(TAG, "*** cancelRemoveItem called ***");
    }


    private void toggleSortType() {
        switch (sortType) {
            case SORT_TYPE_NEWER_TO_OLD:
                sortType = SORT_TYPE_OLDER_TO_NEW;
                break;
            case SORT_TYPE_OLDER_TO_NEW:
                sortType = SORT_TYPE_NEARLEST_ALARM;
                break;
            case SORT_TYPE_NEARLEST_ALARM:
                sortType = SORT_TYPE_NEWER_TO_OLD;
                break;
        }
    }

    @Override
    public void selectType(ItemType type) {
        this.selectedType = type;

        selectedType = type;
        items.clear();
        ItemStore.loadData(MainActivity.this, items, sortType, selectedType);
        adapter.notifyDataSetChanged();
    }


}
