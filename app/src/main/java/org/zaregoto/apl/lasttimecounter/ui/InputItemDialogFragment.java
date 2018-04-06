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
import org.zaregoto.apl.lasttimecounter.model.Item;
import org.zaregoto.apl.lasttimecounter.model.ItemType;
import org.zaregoto.apl.lasttimecounter.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InputItemDialogFragment extends DialogFragment implements SelectTypeDialogFragment.SelectTypeDialogListener, InputAlarmDialogFragment.InputAlarmDialogListener {

    public enum INPUT_ITEM_DIALOG_MODE {
        ADD_MODE,
        EDIT_MODE,
        REDO_MODE
    }

    private static final String ARGS_ITEM_ID = "item";
    private static final String ARGS_MODE_ID = "mode";
    private InputDialogListener mInputDialogListener;
    private Date selectedDay;

    private final String TAG = "InputItemDialogFragment";
    private View dialogView = null;

    private Item item = null;
    private ItemType type;

    private INPUT_ITEM_DIALOG_MODE mode;

    public static InputItemDialogFragment newInstance(Item item, INPUT_ITEM_DIALOG_MODE _mode) {

        InputItemDialogFragment instance = new InputItemDialogFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARGS_ITEM_ID, item);
        args.putSerializable(ARGS_MODE_ID, _mode);
        instance.setArguments(args);

        return instance;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (null != getArguments()) {
            item = getArguments().getParcelable(ARGS_ITEM_ID);
            mode = (INPUT_ITEM_DIALOG_MODE) getArguments().getSerializable(ARGS_MODE_ID);
        }
        if (null != item) {
            type = item.getType();
        }

        if (isNewItem(item)) {
            selectedDay = new Date();
        }
        else {
            selectedDay = item.getLastTime();
        }

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //final View content;
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialogView = inflater.inflate(R.layout.fragment_item_dialog, null);

        builder.setView(dialogView);
        if (null != item) {
            EditText name = dialogView.findViewById(R.id.name);
            name.setText(item.getName());
            EditText detail = dialogView.findViewById(R.id.detail);
            detail.setText(item.getDetail());
        }


        ImageView typeIcon = dialogView.findViewById(R.id.type_icon);
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
                // TODO: do nothing?
            }

            typeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SelectTypeDialogFragment typeSelectDialog = SelectTypeDialogFragment.newInstance(false);
                    typeSelectDialog.setDialogListener(InputItemDialogFragment.this);
                    typeSelectDialog.show(getFragmentManager(), "");
                }
            });
        }

        TextView typeLabel = dialogView.findViewById(R.id.type_label);
        if (null != typeLabel) {

            if (null != item) {
                typeLabel.setText(item.getType().getLabel());
            }

            typeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SelectTypeDialogFragment typeSelectDialog = SelectTypeDialogFragment.newInstance(false);
                    typeSelectDialog.setDialogListener(InputItemDialogFragment.this);
                    typeSelectDialog.show(getFragmentManager(), "");
                }
            });
        }


        EditText dateText = dialogView.findViewById(R.id.date);
        if (null != dateText) {
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String str = sdf.format(selectedDay);

            dateText.setText(str);
            dateText.setFocusable(false);

            // TODO: deprecated な method に頼るのは愉快でないのだがなにか良い方法ないか?
            dateText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePickerDialog dpd = new DatePickerDialog(getActivity());
                    dpd.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            EditText et = dialogView.findViewById(R.id.date);
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


        EditText alarmText = dialogView.findViewById(R.id.alarm);
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


        if (isEdit()) {
            builder.setMessage(R.string.fragment_item_input_dialog_name);
            builder.setPositiveButton(R.string.dialog_confirm_btn_update, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Item _item = item;

                    EditText _name = dialogView.findViewById(R.id.name);
                    EditText _detail = dialogView.findViewById(R.id.detail);
                    String name = (_name != null) ? _name.getText().toString() : "";
                    String detail = (_detail != null) ? _detail.getText().toString() : "";

                    _item.setName(name);
                    _item.setDetail(detail);
                    _item.setLastTime(selectedDay);

                    mInputDialogListener.updateItem(_item);
                }
            });
        }
        else if (isNew()) {
            builder.setMessage(R.string.fragment_item_input_dialog_name);
            builder.setPositiveButton(R.string.dialog_confirm_btn_create, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Item _item = item;

                    EditText _name = dialogView.findViewById(R.id.name);
                    EditText _detail = dialogView.findViewById(R.id.detail);
                    String name = (_name != null) ? _name.getText().toString() : "";
                    String detail = (_detail != null) ? _detail.getText().toString() : "";

                    _item.setName(name);
                    _item.setDetail(detail);
                    _item.setLastTime(selectedDay);

                    mInputDialogListener.addItem(_item);
                }
            });
        }
        else {
            builder.setMessage(R.string.fragment_item_input_dialog_name);
            builder.setPositiveButton(R.string.dialog_confirm_btn_redo, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Item _item = item;
                    EditText _detail = dialogView.findViewById(R.id.detail);
                    String detail = (_detail != null) ? _detail.getText().toString() : "";

                    _item.setLastTime(selectedDay);
                    _item.setDetail(detail);
                    mInputDialogListener.redoItem(_item);
                }
            });

        }

//        final Item finalItem = item;
//        builder.setMessage(R.string.fragment_item_input_dialog_name);
//        builder.setPositiveButton(R.string.fragment_item_input_dialog_ok, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//                EditText _name = dialogView.findViewById(R.id.name);
//                EditText _detail = dialogView.findViewById(R.id.detail);
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


    public void update(Item item) throws IOException {

        String str = "";

        if (null != dialogView) {
            EditText _name = dialogView.findViewById(R.id.name);
            _name.setText(item.getName());
            EditText _detail = dialogView.findViewById(R.id.detail);
            _detail.setText(item.getDetail());

            EditText _dateText = dialogView.findViewById(R.id.date);
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            str = sdf.format(selectedDay);
            _dateText.setText(str);

            ImageView _typeIcon = dialogView.findViewById(R.id.type_icon);
            Drawable drawable = null;
            drawable = item.getType().getAsDrawableImage(getActivity());
            _typeIcon.setImageDrawable(drawable);

            TextView _typeLabel = dialogView.findViewById(R.id.type_label);
            _typeLabel.setText(item.getType().getLabel());

            EditText alarmText = dialogView.findViewById(R.id.alarm);
            Alarm alarm = item.getAlarm();
            str = alarm.getAlarmLabel(getActivity());
            alarmText.setText(str);
        }
    }

    public void updateAlarm(Item item) {

        String str;

        if (null != dialogView) {
            EditText alarmText = dialogView.findViewById(R.id.alarm);
            Alarm alarm = item.getAlarm();
            str = alarm.getAlarmLabel(getActivity());
            alarmText.setText(str);
        }

    }

    @Override
    public void setAlarm(Alarm alarm) {
        item.setAlarm(alarm);

        updateAlarm(item);

    }


    private boolean isNewItem(Item item) {
        if (item.getId() < 0) {
            return true;
        }
        else {
            return false;
        }
    }


    private boolean isEdit() {
        if (mode == INPUT_ITEM_DIALOG_MODE.EDIT_MODE) {
            return true;
        }
        return false;
    }

    private boolean isNew() {
        if (mode == INPUT_ITEM_DIALOG_MODE.ADD_MODE) {
            return true;
        }
        return false;
    }




    public interface InputDialogListener {
        void addItem(Item item);
        void updateItem(Item item);

        void redoItem(Item item);
    }
}
