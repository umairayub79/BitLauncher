package umairayub.bitlauncher.activites;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import spencerstudios.com.jetdblib.JetDB;
import umairayub.bitlauncher.R;
import umairayub.madialog.MaDialog;
import umairayub.madialog.MaDialogListener;

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
            } else {
                setTheme(R.style.AppThemeFullScreen);
            }
        } else {
            if (theme) {
                setTheme(R.style.AppThemeDark);
            } else {
                setTheme(R.style.AppTheme);
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();


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
            Preference Rate_us = findPreference("rate_us");
            if (Rate_us != null) {
                Rate_us.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        new MaDialog.Builder(getContext())
                                .setImage(R.drawable.rating)
                                .setBackgroundColor(R.color.colorWhite)
                                .setTitleTextColor(R.color.colorBlack)
                                .setMessageTextColor(R.color.colorBlack)
                                .setTitle("Rate BitLauncher")
                                .setMessage("Tell others what you think about this app")
                                .setButtonOrientation(LinearLayout.VERTICAL)
                                .AddNewButton(R.style.btnTheme, "Sure", new MaDialogListener() {
                                    @Override
                                    public void onClick() {
                                        try {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getContext().getPackageName())));
                                        } catch (android.content.ActivityNotFoundException e) {
                                            e.printStackTrace();
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getContext().getPackageName())));
                                        }
                                    }
                                })
                                .AddNewButton(R.style.btnTheme, "I don't want to", new MaDialogListener() {
                                    @Override
                                    public void onClick() {

                                    }
                                })
                                .build();
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