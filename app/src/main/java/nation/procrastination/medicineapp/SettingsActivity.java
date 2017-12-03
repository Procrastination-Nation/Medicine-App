package nation.procrastination.medicineapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    private Switch push;
    private Switch refill;
    private Spinner theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        push = (Switch) findViewById(R.id.pushNotif);
        refill = (Switch) findViewById(R.id.refillMeds);
        theme = (Spinner) findViewById(R.id.colorTheme);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.color_themes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        push.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MedicinePrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean("MEDICINE_NOTIFY", isChecked);
                        editor.commit();
                    }
                }
        );

        refill.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MedicinePrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean("MEDICINE_REFILL", isChecked);
                        editor.commit();
                    }
                }
        );
    }

}
