package umairayub.bitlauncher.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import spencerstudios.com.jetdblib.JetDB;
import umairayub.bitlauncher.R;
import umairayub.bitlauncher.adapters.Adapter;
import umairayub.bitlauncher.model.App;

public class MainActivity extends AppCompatActivity {

    TextView tvSettings;
    ListView listView;
    List<App> HomeAppList;
    MainActivity context = MainActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSettings = (TextView) findViewById(R.id.tvSettings);
        listView = (ListView) findViewById(R.id.listview);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Getting the package name from HomeAppList
                String packname = HomeAppList.get(i).packageName;

                //Getting the launch intent for the package
                Intent intent = getPackageManager().getLaunchIntentForPackage(packname);
                if (intent != null) {
                    //launching the activity if intent is not null
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Getting the List of user selected apps from SharedPrefs using JetDB
        HomeAppList = JetDB.getListOfObjects(context, App.class, "apps");
        //checking if HomeAppList is empty then launching the app chooser activity
        if (HomeAppList.isEmpty()) {
            Intent i = new Intent(context, AppChooserActivity.class);
            startActivity(i);
        }
        // if HomeAppList is not empty  created a adapter and show the  app list
        Adapter adapter = new Adapter(context, HomeAppList);
        listView.setAdapter(adapter);
    }
}
