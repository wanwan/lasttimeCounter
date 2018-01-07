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
import android.widget.*;
import org.zaregoto.apl.lasttimecounter.model.Alarm;
import org.zaregoto.apl.lasttimecounter.model.ItemUnit;
import org.zaregoto.apl.lasttimecounter.model.ItemType;
import org.zaregoto.apl.lasttimecounter.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InputItemDialogFragment extends DialogFragment implements SelectTypeDialogFragment.SelectTypeDialogListener, InputAlarmDialogFragment.InputAlarmDialogListener {

    public static final String ARGS_ITEM_ID = "item";

    private InputDialogListener mInputDialogListener;
    private Date selectedDay;

    private final String TAG = "InputItemDialogFragment";
    private View root;

    ItemUnit item = null;
    ItemType type;


    public static InputItemDialogFragment newInstance(ItemUnit item) {

        InputItemDialogFragment instance = new InputItemDialogFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARGS_ITEM_ID, item);
        instance.setArguments(args);

        return instance;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (null != getArguments()) {
            item = getArguments().getParcelable(ARGS_ITEM_ID);
        }
        if (null != item) {
            type = item.getType();
        }

        selectedDay = new Date();

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View content;
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (isNewItem(item)) {
            content = inflater.inflate(R.layout.fragment_insert_item_dialog, null);
        }
        else {
            content = inflater.inflate(R.layout.fragment_update_item_dialog, null);
        }

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
                    SelectTypeDialogFragment typeSelectDialog = SelectTypeDialogFragment.newInstance();
                    typeSelectDialog.setDialogListener(InputItemDialogFragment.this);
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
                    SelectTypeDialogFragment typeSelectDialog = SelectTypeDialogFragment.newInstance();
                    typeSelectDialog.setDialogListener(InputItemDialogFragment.this);
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


        EditText alarmText = content.findViewById(R.id.alarm);
        if (null != alarmText) {

            Alarm alarm = item.getAlarm();
            String str = alarm.getAlarmLabel(getActivity());
            alarmText.setText(str);

            alarmText.setFocusable(false);

            alarmText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Alarm alarm = item.getAlarm();
                    InputAlarmDialogFragment dialog = InputAlarmDialogFragment.newInstance(alarm);
                    dialog.setDialogListener(InputItemDialogFragment.this);
                    dialog.show(getFragmentManager(), "");
                }
            });
        }


        if (isNewItem(item)) {
            builder.setMessage(R.string.fragment_item_input_dialog_name);

            Button btn = content.findViewById(R.id.insertBtn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ItemUnit _item = item;

                    EditText _name = content.findViewById(R.id.name);
                    EditText _detail = content.findViewById(R.id.detail);
                    String name = (_name != null) ? _name.getText().toString() : "";
                    String detail = (_detail != null) ? _detail.getText().toString() : "";

                    _item.setName(name);
                    _item.setDetail(detail);
                    _item.setLastTime(selectedDay);

                    mInputDialogListener.addItem(_item);
                    dismiss();
                }
            });
        }
        else {
            builder.setMessage(R.string.fragment_item_input_dialog_name);

            Button btn = content.findViewById(R.id.updateBtn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ItemUnit _item = item;

                    EditText _name = content.findViewById(R.id.name);
                    EditText _detail = content.findViewById(R.id.detail);
                    String name = (_name != null) ? _name.getText().toString() : "";
                    String detail = (_detail != null) ? _detail.getText().toString() : "";

                    _item.setName(name);
                    _item.setDetail(detail);
                    _item.setLastTime(selectedDay);

                    mInputDialogListener.updateItem(_item);
                    dismiss();
                }
            });

            btn = content.findViewById(R.id.redoBtn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ItemUnit _item = item;

                    _item.setLastTime(selectedDay);
                    mInputDialogListener.redoItem(_item);
                    dismiss();
                }
            });

        }

//        final ItemUnit finalItem = item;
//        builder.setMessage(R.string.fragment_item_input_dialog_name);
//        builder.setPositiveButton(R.string.fragment_item_input_dialog_ok, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//                EditText _name = content.findViewById(R.id.name);
//                EditText _detail = content.findViewById(R.id.detail);
//                String name = (_name != null) ? _name.getText().toString() : "";
//                String detail = (_detail != null) ? _detail.getText().toString() : "";
//
//                finalItem.setName(name);
//                finalItem.setDetail(detail);
//                finalItem.setLastTime(selectedDay);
//
//                // TODO: DB 上の有る無しは app 層の概念ではないからこの判定をここに入れるのは正しくない. ItemStore で upsert を作成するなどして吸収すること
//                if (finalItem.getId() > 0) {
//                    mInputDialogListener.updateItem(finalItem);
//                }
//                else {
//                    mInputDialogListener.addItem(finalItem);
//                }
//            }
//        });

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
        Log.d("", "***** InputItemDialogFragment ItemType selected: " + type.getFilename() + "*****");

        if (null != item) {
            item.setType(type);
            try {
                update(item);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void update(ItemUnit item) throws IOException {

        String str = "";

        if (null != root) {
            EditText _name = root.findViewById(R.id.name);
            _name.setText(item.getName());
            EditText _detail = root.findViewById(R.id.detail);
            _detail.setText(item.getDetail());

            EditText _dateText = root.findViewById(R.id.date);
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            str = sdf.format(selectedDay);
            _dateText.setText(str);

            ImageView _typeIcon = root.findViewById(R.id.type_icon);
            Drawable drawable = null;
            drawable = item.getType().getAsDrawableImage(getActivity());
            _typeIcon.setImageDrawable(drawable);

            TextView _typeLabel = root.findViewById(R.id.type_label);
            _typeLabel.setText(item.getType().getLabel());

            EditText alarmText = root.findViewById(R.id.alarm);
            Alarm alarm = item.getAlarm();
            str = alarm.getAlarmLabel(getActivity());
            alarmText.setText(str);
        }
    }

    @Override
    public void setAlarm(Alarm alarm) {
        item.setAlarm(alarm);
        try {
            update(item);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private boolean isNewItem(ItemUnit item) {
        if (item.getId() < 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public interface InputDialogListener {
        void addItem(ItemUnit item);
        void updateItem(ItemUnit item);

        void redoItem(ItemUnit item);
    }
}
