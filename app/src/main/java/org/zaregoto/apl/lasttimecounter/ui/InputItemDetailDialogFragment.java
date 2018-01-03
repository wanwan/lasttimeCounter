package org.zaregoto.apl.lasttimecounter.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import org.zaregoto.apl.lasttimecounter.model.ItemUnit;
import org.zaregoto.apl.lasttimecounter.model.ItemType;
import org.zaregoto.apl.lasttimecounter.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InputItemDetailDialogFragment extends DialogFragment implements SelectTypeDialogFragment.SelectTypeDialogListener {

    private InputDialogListener mInputDialogListener;
    private Date selectedDay;

    private final String TAG = "InputItemDetailDialogFragment";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        ItemUnit item = null;
        final ItemType type;
        if (null != getArguments()) {
            item = getArguments().getParcelable(MainActivity.ARGS_ITEM_ID);
        }
        if (null != item) {
            type = item.getType();
        }
        else {
            String name = "";
            String detail = "";
            Date now = new Date();

            type = ItemType.createItemType(getActivity(), ItemUnit.DEFAULT_TYPE_ID);
            item = new ItemUnit(name, detail, type, selectedDay, now);
        }

        selectedDay = new Date();

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View content = inflater.inflate(R.layout.fragment_item_input_dialog, null);

        builder.setView(content);
        if (null != item) {
            EditText name = content.findViewById(R.id.name);
            name.setText(item.getName());
            EditText detail = content.findViewById(R.id.detail);
            detail.setText(item.getDetail());
        }


        ImageView typeIcon = content.findViewById(R.id.type_icon);
        if (null != typeIcon) {
            if (null != item) {
                try {
                    Drawable drawable = null;
                    drawable = item.getType().getAsDrawableImage(getActivity());
                    typeIcon.setImageDrawable(drawable);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {

            }

            typeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SelectTypeDialogFragment typeSelectDialog = new SelectTypeDialogFragment();
                    typeSelectDialog.show(getFragmentManager(), "");
                }
            });
        }

        TextView typeLabel = content.findViewById(R.id.type_label);
        if (null != typeLabel) {

            if (null != item) {
                typeLabel.setText(item.getType().getLabel());
            }

            typeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SelectTypeDialogFragment typeSelectDialog = new SelectTypeDialogFragment();
                    typeSelectDialog.show(getFragmentManager(), "");
                }
            });
        }


        EditText dateText = content.findViewById(R.id.date);
        if (null != dateText) {
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String str = sdf.format(selectedDay);

            dateText.setText(str);
            dateText.setFocusable(false);

            dateText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePickerDialog dpd = new DatePickerDialog(getActivity());
                    dpd.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            EditText et = content.findViewById(R.id.date);
                            if (null != et) {
                                String _str = year + "-" + (month+1) + "-" + day;
                                et.setText(_str);
                                selectedDay.setYear(year-1900);
                                selectedDay.setMonth(month);
                                selectedDay.setDate(day);
                                //ItemInputDialogFragment.selectedDay = new GregorianCalendar(selectedYear, selectedMonth, selectedDay);.getTime();
                            }
                        }
                    });
                    dpd.show();
                }
            });

        }


        final ItemUnit finalItem = item;
        builder.setMessage(R.string.fragment_item_input_dialog_name);
        builder.setPositiveButton(R.string.fragment_item_input_dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                EditText _name = content.findViewById(R.id.name);
                EditText _detail = content.findViewById(R.id.detail);
                String name = (_name != null) ? _name.getText().toString() : "";
                String detail = (_detail != null) ? _detail.getText().toString() : "";

                finalItem.setName(name);
                finalItem.setDetail(detail);
                finalItem.setLastTime(selectedDay);

                // TODO: DB 上の有る無しは app 層の概念ではないからこの判定をここに入れるのは正しくない. ItemStore で upsert を作成するなどして吸収すること
                if (finalItem.getId() > 0) {
                    mInputDialogListener.updateItem(finalItem);
                }
                else {
                    mInputDialogListener.addItem(finalItem);
                }
            }
        });

        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (null == mInputDialogListener && context instanceof  InputDialogListener) {
            mInputDialogListener = (InputDialogListener) context;
        }
    }


    @Override
    public void onDetach() {
        if (null != mInputDialogListener) {
            mInputDialogListener = null;
        }
        super.onDetach();
    }

    @Override
    public void selectType(ItemType type) {
        Log.d(TAG, "***** type selected ****");
    }

    public interface InputDialogListener {
        void addItem(ItemUnit item);
        void updateItem(ItemUnit item);
    }
}
