package org.zaregoto.apl.lasttimecounter.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import org.zaregoto.apl.lasttimecounter.model.ItemUnit;
import org.zaregoto.apl.lasttimecounter.model.ItemType;
import org.zaregoto.apl.lasttimecounter.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ItemDialogFragment extends DialogFragment {

    private InputDialogListener mInputDialogListener;
    private Date selectedDay;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        ItemUnit item = null;
        if (null != getArguments()) {
            item = getArguments().getParcelable(MainActivity.ARGS_ITEM_ID);
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
                if (null == finalItem) {
                    EditText _name = content.findViewById(R.id.name);
                    EditText _detail = content.findViewById(R.id.detail);
                    String name = (_name != null) ? _name.getText().toString() : "";
                    String detail = (_detail != null) ? _detail.getText().toString() : "";
                    Date now = new Date();

                    ItemType type = ItemType.createItemType(getActivity(), ItemUnit.DEFAULT_TYPE_ID);

                    ItemUnit _item = new ItemUnit(name, detail, type, selectedDay, now);
                    mInputDialogListener.addItem(_item);
                }
                else {
                    EditText _name = content.findViewById(R.id.name);
                    EditText _detail = content.findViewById(R.id.detail);
                    String name = (_name != null) ? _name.getText().toString() : "";
                    String detail = (_detail != null) ? _detail.getText().toString() : "";

                    ItemType type = ItemType.createItemType(getActivity(), ItemUnit.DEFAULT_TYPE_ID);

                    finalItem.setName(name);
                    finalItem.setDetail(detail);
                    finalItem.setLastTime(selectedDay);

                    mInputDialogListener.updateItem(finalItem);

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

    public interface InputDialogListener {
        void addItem(ItemUnit item);
        void updateItem(ItemUnit item);
    }
}
