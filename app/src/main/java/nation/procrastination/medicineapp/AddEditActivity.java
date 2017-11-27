package nation.procrastination.medicineapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

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
    DBHelper dbHelper;

    private boolean isEdit;
    private int medId;

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


    public void btnAddEditClick(View view) {
        try {
            String name = nameET.getText().toString();
            int amount = Integer.parseInt(amountET.getText().toString());
            int dosage = Integer.parseInt(dosageET.getText().toString());
            String days = generateBtnString();
            String times = "";

            if(!isEdit) {
                MedicineInfo medicineInfo = new MedicineInfo(name, amount, dosage, days, times);
                dbHelper.addMedicine(medicineInfo);
                resultText.setText(String.format("%s has been successfully added!", name));
            } else {
                MedicineInfo medicineInfo = new MedicineInfo(medId, name, amount, dosage, days, times);
                dbHelper.updateMedicine(medicineInfo);
                resultText.setText(String.format("%s has been successfully updated!", name));
            }
            finish();

        } catch (Exception ex) {
            resultText.setText("Failed to add medicine.");
        }
    }

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
    }

    public void btnDeleteMed(View view) {
        new AlertDialog.Builder(AddEditActivity.this)
                .setTitle("Delete Medicine")
                .setMessage("Are you sure you want to delete: " + nameET.getText().toString())
                .setCancelable(false)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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
