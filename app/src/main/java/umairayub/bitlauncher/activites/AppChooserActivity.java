package umairayub.bitlauncher.activites;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import spencerstudios.com.jetdblib.JetDB;
import umairayub.bitlauncher.R;
import umairayub.bitlauncher.adapters.Adapter;
import umairayub.bitlauncher.model.App;

//this is a one time activity to select apps for home screen
//this activity can be launched from settings again if needed

public class AppChooserActivity extends AppCompatActivity {

    // Declearing vars/views;
    ListView Listview;
    ProgressBar progressBar;
    Adapter adapter;
    ArrayList<App> apps = new ArrayList<>();
    ArrayList<App> SelectedApps = new ArrayList<>();
    AppChooserActivity context = AppChooserActivity.this;
    ChipGroup mChipsGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_chooser);

        //Inintializing views
        Listview = findViewById(R.id.listViewAllApps);
        progressBar = findViewById(R.id.progressBar);
        mChipsGroup = findViewById(R.id.chipGroup);
        FloatingActionButton fab = findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SelectedApps.isEmpty()) {
                    //Saving user selected apps in sharedPrefs with JetDB
                    JetDB.putListOfObjects(context, SelectedApps, "apps");
                    //Finishing this activity
                    finish();
                }
            }
        });

        new LoadApps().execute();
        Listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // getting the app's name from apps array of object at position i
                String AppName = apps.get(i).Appname;

                //Making sure users select 5 apps Only
                if (SelectedApps.size() < 5) {
                    //Making a app object
                    final App app = apps.get(i);
                    //adding the app object in SelectedApps Array
                    SelectedApps.add(app);

                    //Making a Chip
                    final Chip chip = new Chip(context);
                    chip.setText(AppName);
                    chip.setCloseIconResource(R.drawable.ic_action_name);
                    chip.setCloseIconEnabled(true);
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // removing the chip
                            mChipsGroup.removeView(chip);
                            // removing the app from Arraylist
                            SelectedApps.remove(app);
                            //re adding the app to apps array
                            apps.add(app);
                            //Sorting the apps array again
                            Collections.sort(apps, new Comparator<App>() {
                                @Override
                                public int compare(App item, App t1) {
                                    return item.Appname.toLowerCase().compareTo(t1.Appname.toLowerCase());
                                }
                            });

                            adapter.notifyDataSetChanged();

                        }
                    });
                    // adding chip to chipsGroup
                    mChipsGroup.addView(chip);
                    //we remove the selected apps from apps array
                    apps.remove(i);
                    adapter.notifyDataSetChanged();

                    // showing a dialog if user tries to selected more than 5 apps
                } else if (SelectedApps.size() >= 5) {
                    //Dialog goes here
//                    Toast.makeText(context, "You can't select more than 5 Apps", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }


    public class LoadApps extends AsyncTask<Integer, Integer, List<App>> {

        PackageManager packageManager = getPackageManager();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<App> doInBackground(Integer... integers) {
            if (!apps.isEmpty()) {
                apps.clear();
            }
            Intent i = new Intent(Intent.ACTION_MAIN, null);
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ApplicationInfo> applications = getPackageManager().getInstalledApplications(0);
            for (ApplicationInfo application : applications) {
                if (packageManager.getLaunchIntentForPackage(application.packageName) != null) {

                    //Skipping Bit Launcher from apps list
                    if (application.packageName.equals("umairayub.bitlauncher")) {
                        continue;
                    }

                    App app = new App();
                    app.Appname = application.loadLabel(packageManager).toString();
                    app.packageName = application.packageName;
                    apps.add(app);


                }
            }
            return apps;
        }

        @Override
        protected void onPostExecute(List<App> items) {
            progressBar.setVisibility(View.GONE);
            Listview.setVisibility(View.VISIBLE);
            adapter = new Adapter(context, items);
            Collections.sort(items, new Comparator<App>() {
                @Override
                public int compare(App item, App t1) {
                    return item.Appname.toLowerCase().compareTo(t1.Appname.toLowerCase());
                }
            });
            Listview.setAdapter(adapter);

            super.onPostExecute(items);
        }
    }
}