package org.zaregoto.apl.lasttimecounter.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import org.zaregoto.apl.lasttimecounter.GridTypeAdapter;
import org.zaregoto.apl.lasttimecounter.db.ItemStore;
import org.zaregoto.apl.lasttimecounter.model.ItemType;
import org.zaregoto.apl.lasttimecounter.R;

import java.util.ArrayList;

public class SelectTypeDialogFragment extends DialogFragment {

    private final String TAG = "TypeDialogFragment";
    private SelectTypeDialogFragment.SelectTypeDialogListener mDialogListener;

    public static SelectTypeDialogFragment newInstance() {

        SelectTypeDialogFragment instance = new SelectTypeDialogFragment();
        Bundle args = new Bundle();

        instance.setArguments(args);

        return instance;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int dialogWidth = (int) (metrics.widthPixels * 0.8);
        int dialogHeight = (int) (metrics.heightPixels * 0.65);
        double iconViewRatio = dialogHeight / dialogWidth;
        ArrayList<ItemType> types = ItemStore.getAllItemTyps(getActivity());
        final GridTypeAdapter adapter = new GridTypeAdapter(getActivity(), android.R.layout.simple_list_item_1, types);

        Log.d(TAG, "***** width: height " + dialogWidth + " : " + dialogHeight + "*****\n");

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View content = inflater.inflate(R.layout.fragment_select_type, null);

        builder.setView(content);

        GridView gridView = content.findViewById(R.id.gridview);
        if (null != gridView) {
            gridView.setAdapter(adapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                    ItemType type = adapter.getItem(pos);
                    Log.d(TAG, "ItemType clicked: " + type.getSection() + " " + type.getFilename());
                    if (null != mDialogListener) {
                        mDialogListener.selectType(type);
                    }
                    dismiss();
                }
            });

        }


//        GridLayout gridLayout = content.findViewById(R.id.gridview);
//        if (null != gridLayout) {
//            gridLayout.removeAllViews();
//
//            //ArrayList<ItemType> types = ItemStore.getAllItemTyps(getActivity());
//            int total = types.size();
//            int column = 4;
//            int row = total / column;
//
//            gridLayout.setColumnCount(column);
//            gridLayout.setRowCount(row + 1);
//
//            AssetManager am;
//            InputStream is;
//
//            am = getActivity().getAssets();
//
//            for (int i = 0, c = 0, r = 0; i < total; i++, c++) {
//                if (c == column) {
//                    c = 0;
//                    r++;
//                }
//
//                ItemType itemType = types.get(i);
//                String filename = itemType.getFilename();
//                String section = itemType.getSection();
//                Drawable drawable = null;
//
//                try {
//                    is = am.open("typeicons" + "/" + section + "/" + filename);
//                    if (null != is) {
//                        drawable = Drawable.createFromStream(is, null);
//
//                        View typeView = inflater.inflate(R.layout.item_type, null);
//                        Log.d(TAG, "typeView: " + typeView.getId());
//                        //Log.d(TAG, "***** type icon width: height " + typeView.getWidth() + " " + typeView.getHeight());
//
//                        //double iconViewRatio = typeView.getHeight() / typeView.getWidth();
//                        if (null != typeView) {
//                            ImageView iconView = typeView.findViewById(R.id.type_icon);
//                            TextView labelView = typeView.findViewById(R.id.type_label);
//
//                            iconView.setImageDrawable(drawable);
//                            labelView.setText("aaaa");
//
//                            Log.d(TAG, "iconView: " + iconView.getId() + " labelView: " + labelView.getId());
//                        }
//
//                        GridLayout.Spec rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 1);
//                        GridLayout.Spec colspan = GridLayout.spec(GridLayout.UNDEFINED, 1);
//
//                        int iconWidth = dialogWidth / column;
//                        int iconHeight = (int) (iconWidth * iconViewRatio);
//
//                        GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(rowSpan, colspan);
//                        gridParam.width = iconWidth;
//                        gridParam.height = iconHeight;
//                        gridLayout.addView(typeView, gridParam);
//
//                        typeView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Log.d(TAG, "*** CLICKED" + view.getId() + " ***\n");
//                            }
//                        });
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.d(TAG, "cannot read drawable from asset manager");
//                }
//            }
//        }


//        builder.setMessage(R.string.fragment_type_select_dialog_name);
//        builder.setPositiveButton(R.string.fragment_item_input_dialog_ok, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });


        // Create the AlertDialog object and return it
        Dialog dialog = builder.create();

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = dialogWidth;
        lp.height = dialogHeight;
        dialog.getWindow().setAttributes(lp);


        return dialog;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (null == mDialogListener && context instanceof SelectTypeDialogListener) {
            mDialogListener = (SelectTypeDialogListener) context;
        }
    }


    @Override
    public void onDetach() {
        if (null != mDialogListener) {
            mDialogListener = null;
        }
        super.onDetach();
    }


    // TODO: onAttach との整合の問題があるが, 上書きというルールであるとする. 通常は問題ないはずだが, 回転等の再作成を経る場合があるので良いやりかたではないはず
    public void setDialogListener(SelectTypeDialogListener listener) {
        this.mDialogListener = listener;
    }

    public interface SelectTypeDialogListener {
        void selectType(ItemType type);
    }

}
