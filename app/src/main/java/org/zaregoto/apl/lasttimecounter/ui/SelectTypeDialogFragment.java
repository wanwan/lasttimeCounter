package org.zaregoto.apl.lasttimecounter.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import org.zaregoto.apl.lasttimecounter.db.ItemStore;
import org.zaregoto.apl.lasttimecounter.model.ItemType;
import org.zaregoto.apl.lasttimecounter.model.ItemUnit;
import org.zaregoto.apl.lasttimecounter.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SelectTypeDialogFragment extends DialogFragment {

    private final String TAG = "TypeDialogFragment";
    private SelectTypeDialogFragment.SelectTypeDialogListener mDialogListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int dialogWidth = (int) (metrics.widthPixels * 0.8);
        int dialogHeight = (int) (metrics.heightPixels * 0.8);
        double iconViewRatio = dialogHeight / dialogWidth;

        Log.d(TAG, "***** width: height " + dialogWidth + " : " + dialogHeight + "*****\n");

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View content = inflater.inflate(R.layout.fragment_select_type, null);

        builder.setView(content);

        GridLayout gridLayout = content.findViewById(R.id.gridview);
        if (null != gridLayout) {
            gridLayout.removeAllViews();

            ArrayList<ItemType> types = ItemStore.getAllItemTyps(getActivity());
            int total = types.size();
            int column = 4;
            int row = total / column;

            gridLayout.setColumnCount(column);
            gridLayout.setRowCount(row + 1);

            AssetManager am;
            InputStream is;

            am = getActivity().getAssets();

            for (int i = 0, c = 0, r = 0; i < total; i++, c++) {
                if (c == column) {
                    c = 0;
                    r++;
                }

                ItemType itemType = types.get(i);
                String filename = itemType.getFilename();
                String section = itemType.getSection();
                Drawable drawable = null;

                try {
                    is = am.open("typeicons" + "/" + section + "/" + filename);
                    if (null != is) {
                        drawable = Drawable.createFromStream(is, null);

                        View typeView = inflater.inflate(R.layout.item_type, null);

                        //Log.d(TAG, "***** type icon width: height " + typeView.getWidth() + " " + typeView.getHeight());

                        //double iconViewRatio = typeView.getHeight() / typeView.getWidth();
                        if (null != typeView) {
                            ImageView iconView = typeView.findViewById(R.id.type_icon);
                            TextView labelView = typeView.findViewById(R.id.type_label);

                            iconView.setImageDrawable(drawable);
                            labelView.setText("aaaa");
                        }

                        ImageView oImageView = new ImageView(getActivity());
                        oImageView.setImageDrawable(drawable);
                        ViewGroup.LayoutParams lp = new GridLayout.LayoutParams();
                        lp.width = 400;
                        lp.height = 400;
                        oImageView.setLayoutParams(lp);

                        GridLayout.Spec rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 1);
                        GridLayout.Spec colspan = GridLayout.spec(GridLayout.UNDEFINED, 1);
                        //if (r == 0 && c == 0) {
                        //    Log.e("", "spec");
                        //    colspan = GridLayout.spec(GridLayout.UNDEFINED, 2);
                        //    rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 2);
                        //}

                        int iconWidth = dialogWidth / column;
                        int iconHeight = (int) (iconWidth * iconViewRatio);

                        GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(rowSpan, colspan);
                        //gridParam.width = iconWidth;
                        //gridParam.height = iconHeight;
                        gridParam.width = iconWidth;
                        gridParam.height = iconHeight;
                        //gridLayout.addView(oImageView, gridParam);
                        gridLayout.addView(typeView, gridParam);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "cannot read drawable from asset manager");
                }
            }
        }


        builder.setMessage(R.string.fragment_type_select_dialog_name);
        builder.setPositiveButton(R.string.fragment_item_input_dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }
        );

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

    public interface SelectTypeDialogListener {
    }

}
