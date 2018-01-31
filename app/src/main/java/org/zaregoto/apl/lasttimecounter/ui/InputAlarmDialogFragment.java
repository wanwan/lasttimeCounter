package org.zaregoto.apl.lasttimecounter.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import org.zaregoto.apl.lasttimecounter.R;
import org.zaregoto.apl.lasttimecounter.model.Alarm;

public class InputAlarmDialogFragment extends DialogFragment {

    private static final String ARGS_ALARM_ID = "ALARM_ID";

    private InputAlarmDialogListener mInputDialogListener;
    private final String TAG = "InputAlarmDialogFragment";
    private Alarm alarm;

    private View content;
    private InputAlarmDialogListener mDialogListener;

    public static InputAlarmDialogFragment newInstance(Alarm alarm) {

        InputAlarmDialogFragment instance = new InputAlarmDialogFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARGS_ALARM_ID, alarm);
        instance.setArguments(args);

        return instance;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (null != getArguments()) {
            alarm = getArguments().getParcelable(ARGS_ALARM_ID);
        }

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        content = inflater.inflate(R.layout.fragment_input_alarm_dialog, null);

        Spinner spinner = content.findViewById(R.id.alarmSpinner);
        if (null != spinner) {

            final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.alarm_type_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {

                    Alarm.ALARM_TYPE _type = Alarm.ALARM_TYPE.getAlarmTypeByStrIdx(pos);
                    alarm.setType(_type);

                    if (Alarm.ALARM_TYPE.ALARM_TYPE_SET_SPECIFIC_DAY == _type) {
                        CalendarView cv = content.findViewById(R.id.alarmCalendar);
                        if (null != cv) {
                            cv.setEnabled(true);
                        }
                    } else {
                        CalendarView cv = content.findViewById(R.id.alarmCalendar);
                        if (null != cv) {
                            cv.setEnabled(false);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    // do nothing
                }
            });
        }

        builder.setView(content);
        builder.setPositiveButton(R.string.fragment_item_input_dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (null != mDialogListener) {
                    mDialogListener.setAlarm(alarm);
                }
                dismiss();
            }
        });

        if (null != alarm && null != spinner) {
            spinner.setSelection(alarm.getType().getInt());
        }


        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (null == mInputDialogListener && context instanceof InputAlarmDialogListener) {
            mInputDialogListener = (InputAlarmDialogListener) context;
        }
    }


    @Override
    public void onDetach() {
        if (null != mInputDialogListener) {
            mInputDialogListener = null;
        }
        super.onDetach();
    }

    // TODO: onAttach との整合の問題があるが, 上書きというルールであるとする. 通常は問題ないはずだが, 回転等の再作成を経る場合があるので良いやりかたではないはず
    public void setDialogListener(InputAlarmDialogListener listener) {
        this.mDialogListener = listener;
    }

    public interface InputAlarmDialogListener {
        void setAlarm(Alarm alarm);
    }


}
