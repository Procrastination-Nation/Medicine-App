package nation.procrastination.medicineapp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class AddEditActivity extends AppCompatActivity {

    private EditText nameET;
    private EditText amountET;
    private EditText dosageET;
    private ToggleButton btnSunday;
    private ToggleButton btnMonday;
    private ToggleButton btnTuesday;
    private ToggleButton btnWednesday;
    private ToggleButton btnThursday;
    private ToggleButton btnFriday;
    private ToggleButton btnSaturday;
    private TextView resultText;
    private Button btnAddEdit;
    private Button btnDelete;
    private Button btnEditTime;
    private ListView viewMedTimes;
    DBHelper dbHelper;

    private boolean isEdit;
    private int medId;
    private ArrayList<String> times;
    private ArrayAdapter<String> adapter;
    private int selectedString = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addedit);

        nameET = (EditText) findViewById(R.id.nameET);
        amountET = (EditText) findViewById(R.id.amountET);
        dosageET = (EditText) findViewById(R.id.dosageET);
        btnSunday = (ToggleButton) findViewById(R.id.btnSunday);
        btnMonday = (ToggleButton) findViewById(R.id.btnMonday);
        btnTuesday = (ToggleButton) findViewById(R.id.btnTuesday);
        btnWednesday = (ToggleButton) findViewById(R.id.btnWednesday);
        btnThursday = (ToggleButton) findViewById(R.id.btnThursday);
        btnFriday = (ToggleButton) findViewById(R.id.btnFriday);
        btnSaturday = (ToggleButton) findViewById(R.id.btnSaturday);
        resultText = (TextView) findViewById(R.id.resultText);
        btnAddEdit = (Button) findViewById(R.id.btnAddEditMedicine);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnEditTime = (Button) findViewById(R.id.btnEditTime);

        viewMedTimes = (ListView) findViewById(R.id.medTimesList);

        times = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item_time, times);
        viewMedTimes.setOnItemClickListener(mClickListener);
        viewMedTimes.setAdapter(adapter);
        btnEditTime.setVisibility(View.GONE);

        Intent intent = getIntent();
        isEdit = intent.getBooleanExtra("isEdit", false);
        medId = intent.getIntExtra("id", -1);
        dbHelper = new DBHelper(this);

        if(isEdit) {
            btnAddEdit.setText(R.string.btn_edit);
            btnDelete.setVisibility(View.VISIBLE);
            loadMedicineInfo();
        } else {
            btnAddEdit.setText(R.string.btn_add);
            btnDelete.setVisibility(View.GONE);
        }
    }

    public void btnAddTime(View view) {
        TimePickerDialog dialog = new TimePickerDialog(this, mTimeListener, 0, 0, false);
        dialog.show();
    }

    public void btnRemoveTime(View view) {
        if(selectedString == -1) return;
        times.remove(selectedString);
        adapter.notifyDataSetChanged();
    }

    public void btnEditTime(View view) {
        TimePickerDialog dialog = new TimePickerDialog(this, mEditListener, 0, 0, false);
        dialog.show();
    }

    public void btnAddEditClick(View view) {
        try {
            String name = nameET.getText().toString();
            int amount = Integer.parseInt(amountET.getText().toString());
            int dosage = Integer.parseInt(dosageET.getText().toString());
            String days = generateBtnString();
            String times = generateTimeString();
            String alarmIDs = generateAlarmStrings();

            Toast.makeText(getApplicationContext(), times, Toast.LENGTH_SHORT).show();

            if(!isEdit) {
                MedicineInfo medicineInfo = new MedicineInfo(medId, name, amount, dosage, days, times, alarmIDs);
                dbHelper.addMedicine(medicineInfo);
                resultText.setText(String.format("%s has been successfully added!", name));
                setMedicineTimer(medicineInfo);

            } else {
                MedicineInfo medicineInfo = new MedicineInfo(medId, name, amount, dosage, days, times, alarmIDs);
                clearMedicineTimer(medicineInfo);
                setMedicineTimer(medicineInfo);
                dbHelper.updateMedicine(medicineInfo);
                resultText.setText(String.format("%s has been successfully updated!", name));
            }
            finish();

        } catch (Exception ex) {
            resultText.setText("Failed to add medicine.");
        }
    }

    private int generateRandomID() {
        UUID one = UUID.randomUUID();
        String str = "" + one;
        int uid = str.hashCode();
        String filter = "" + uid;
        str = filter.replaceAll("-", "");
        return Integer.parseInt(str);
    }

    private void setMedicineTimer(MedicineInfo info) {
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        String[] alarms = info.getAlarmIDs().split(";");
        String[] alarmTimes = info.getTimes().split(";");
        if(alarms.length != alarmTimes.length)
            return;
        for(String time : info.getTimes().split(";")) {
            Intent i = new Intent(AddEditActivity.this, MedicineReceiver.class);
            i.putExtra("timerMedID", info.getId());
            int id = generateRandomID();
            PendingIntent pendingIntent = PendingIntent.getBroadcast(AddEditActivity.this, id, i, PendingIntent.FLAG_CANCEL_CURRENT);
            Calendar c = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
            try {
                c.setTime(format.parse(time));
            } catch (ParseException e) {
                e.printStackTrace();
                continue;
            }
            am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    private void clearMedicineTimer(MedicineInfo info) {
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        for(String alarm : info.getAlarmIDs().split(";")) {
            Intent i = new Intent(AddEditActivity.this, MedicineReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(AddEditActivity.this, Integer.parseInt(alarm), i, PendingIntent.FLAG_CANCEL_CURRENT);
            am.cancel(pendingIntent);
        }
    }

    TimePickerDialog.OnTimeSetListener mTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);
            times.add(format.format(c.getTime()));
            adapter.notifyDataSetChanged();
        }
    };

    TimePickerDialog.OnTimeSetListener mEditListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);
            times.set(selectedString, format.format(c.getTime()));
            adapter.notifyDataSetChanged();
        }
    };
    ListView.OnItemClickListener mClickListener = new ListView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectedString = position;
            if(selectedString != -1) btnEditTime.setVisibility(View.VISIBLE);
        }
    };

    private String generateBtnString() {
        String days = "";

        if(btnSunday.isChecked()) { days += "U"; }
        if(btnMonday.isChecked()) { days += "M"; }
        if(btnTuesday.isChecked()) { days += "T"; }
        if(btnWednesday.isChecked()) { days += "W"; }
        if(btnThursday.isChecked()) { days += "R"; }
        if(btnFriday.isChecked()) { days += "F"; }
        if(btnSaturday.isChecked()) { days += "S"; }

        return days;
    }

    private String generateTimeString() {
        String ret = "";
        boolean one = times.size() == 1;
        for(String time : times) {
            ret += time;
            if(!one) ret += ";";
        }
        return ret;
    }

    private String generateAlarmStrings() {
        String ret = "";
        for(String time : times) {
            ret += generateRandomID();
            ret += ";";
        }
        return ret;
    }

    private void crackBtnString(String days) {
        for(int i = 0; i < days.length(); i++) {
            switch(days.charAt(i)) {
                case 'U':
                    btnSunday.setChecked(true);
                    break;
                case 'M':
                    btnMonday.setChecked(true);
                    break;
                case 'T':
                    btnTuesday.setChecked(true);
                    break;
                case 'W':
                    btnWednesday.setChecked(true);
                    break;
                case 'R':
                    btnThursday.setChecked(true);
                    break;
                case 'F':
                    btnFriday.setChecked(true);
                    break;
                case 'S':
                    btnSaturday.setChecked(true);
                    break;
            }
        }
    }

    private void loadMedicineInfo() {
        MedicineInfo med = dbHelper.getMedicineByID(medId);
        nameET.setText(med.getName());
        amountET.setText(String.format("%d", med.getAmount()));
        dosageET.setText(String.format("%d", med.getDosage()));
        crackBtnString(med.getDays());
        for(String time : med.getTimes().split(";")) {
            this.times.add(time);
        }
        adapter.notifyDataSetChanged();
    }

    public void btnDeleteMed(View view) {
        new AlertDialog.Builder(AddEditActivity.this)
                .setTitle("Delete Medicine")
                .setMessage("Are you sure you want to delete: " + nameET.getText().toString())
                .setCancelable(false)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        clearMedicineTimer(dbHelper.getMedicineByID(medId));
                        dbHelper.deleteMedicine(medId);
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }
}
