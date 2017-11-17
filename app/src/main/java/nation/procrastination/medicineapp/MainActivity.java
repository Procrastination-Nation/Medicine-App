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

    ExpandableListView expandableListView;
    CustomListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

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

        expandableListView = (ExpandableListView) findViewById(R.id.mainContent);
        expandableListDetail = getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);


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
            Toast.makeText(getApplicationContext(), "Place Intent to About here", Toast.LENGTH_LONG).show();
        } else if (id == R.id.menu_add) {
            Toast.makeText(getApplicationContext(), "Place Intent to Add Medicine here", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    //Dynamically generate an expandable listview for medicines
    public HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> listItems = new HashMap<>();

        /*Hardcoded values for now, in future, replace with values pulled from database or sharedpref*/

        List<String> test = new ArrayList<>();
        test.add("Days: M T W R F ");
        test.add("Times:8:00pm");
        test.add("Amount: 25");
        test.add(".....");

        List<String> test1 = new ArrayList<>();
        test1.add("Hello 1");
        test1.add("Hello 2");

        List<String> test2 = new ArrayList<>();
        test2.add("Hello 3");
        test2.add("Hello 4");

        listItems.put("Zyrtec", test);
        listItems.put("Test Medicine", test1);
        listItems.put("Another Test", test2);

        return listItems;
    }

}
