package umairayub.bitlauncher.activites;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import spencerstudios.com.jetdblib.JetDB;
import umairayub.bitlauncher.R;

public class SettingsActivity extends AppCompatActivity {

    Boolean status_bar;
    Boolean theme;
    SettingsActivity context = SettingsActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        status_bar = JetDB.getBoolean(context, "status", false);
        theme = JetDB.getBoolean(context, "theme", false);

        if (status_bar) {
            if (theme) {
                setTheme(R.style.AppThemeDarkFullScreen);
                Toast.makeText(context, "FullScreen Dark", Toast.LENGTH_SHORT).show();
            } else {
                setTheme(R.style.AppThemeFullScreen);
                Toast.makeText(context, "FullScreen Light", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (theme) {
                setTheme(R.style.AppThemeDark);
                Toast.makeText(context, " Dark", Toast.LENGTH_SHORT).show();
            } else {
                setTheme(R.style.AppTheme);
                Toast.makeText(context, " Light", Toast.LENGTH_SHORT).show();
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            Preference theme = findPreference("theme");
            if (theme != null) {
                theme.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        getActivity().recreate();

                        return true;
                    }
                });
            }
            Preference statusBar = findPreference("status");
            if (statusBar != null) {
                statusBar.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        getActivity().recreate();
                        return true;
                    }
                });
            }

            Preference sysSettings = findPreference("sysSettings");
            if (sysSettings != null) {
                sysSettings.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        startActivity(new Intent(Settings.ACTION_SETTINGS));
                        return true;
                    }
                });
            }
            Preference launcherSettings = findPreference("launcherSettings");
            if (launcherSettings != null) {
                launcherSettings.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        startActivity(new Intent(Settings.ACTION_HOME_SETTINGS));
                        return true;
                    }
                });
            }
            Preference appChooser = findPreference("appchooser");
            if (appChooser != null) {
                appChooser.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        Intent intent = new Intent(getContext(), AppChooserActivity.class);
                        startActivity(intent);
                        return true;
                    }
                });
            }
            Preference feedback = findPreference("feedback");
            if (feedback != null) {
                feedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {

                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                        emailIntent.setData(Uri.parse("mailto:umairayub79@gmail.com"));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "BitLauncher Feedback");
                        startActivity(Intent.createChooser(emailIntent, "Send Feedback!"));

                        return true;
                    }
                });
            }

        }

    }
}