package nation.procrastination.medicineapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mainLayout;
    public static HashMap<String, String> medIdDictionary;

    ExpandableListView expandableListView;
    CustomListAdapter customListAdapter;
    List<String> expandableListHeader;
    HashMap<String, List<String>> expandableListDetail;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_live_help);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Help Functionality Here", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        dbHelper = new DBHelper(this);

        expandableListView = (ExpandableListView) findViewById(R.id.mainContent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_add) {
            Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    //Dynamically generate an expandable listview for medicines
    public HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> listItems = new HashMap<>();
        List<MedicineInfo> medList = dbHelper.getMedicine();
        List<String> bodyText;
        medIdDictionary = new HashMap<>();

        for(MedicineInfo med : medList) {
            bodyText = new ArrayList<>();
            bodyText.add(String.format("Amount: %d", med.getAmount()));
            bodyText.add(String.format("Dosage: %d", med.getDosage()));
            bodyText.add(String.format("Days: %s", med.getDays()));
            bodyText.add(String.format("Times: %s", med.getTimes()));
            listItems.put(med.getName(), bodyText);
            medIdDictionary.put(med.getName(), String.format("%d", med.getId()));
        }

        return listItems;
    }

    public void getMedicineList() {
        expandableListDetail = getData();
        expandableListHeader = new ArrayList<String>(expandableListDetail.keySet());
        customListAdapter = new CustomListAdapter(this, expandableListHeader, expandableListDetail);
        expandableListView.setAdapter(customListAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getMedicineList();
    }

}
