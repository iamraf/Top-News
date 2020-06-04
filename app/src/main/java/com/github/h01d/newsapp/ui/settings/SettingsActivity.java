package com.github.h01d.newsapp.ui.settings;

/*
    Copyright 2019-2020 Raf

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.github.h01d.newsapp.R;
import com.github.h01d.newsapp.data.local.preference.PreferencesManager;
import com.github.h01d.newsapp.databinding.SettingsActivityBinding;
import com.github.h01d.newsapp.util.ThemeHelper;

public class SettingsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        SettingsActivityBinding mDataBinding = DataBindingUtil.setContentView(this, R.layout.settings_activity);

        mDataBinding.aSettingsToolbar.setTitle("Settings");
        setSupportActionBar(mDataBinding.aSettingsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.a_settings_frame, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat
    {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
        {
            setPreferencesFromResource(R.xml.preferences, rootKey);

            ListPreference listPreference = findPreference("pref_country");
            if(listPreference != null)
            {
                listPreference.setOnPreferenceChangeListener((preference, newValue) ->
                {
                    final String[] codes = getResources().getStringArray(R.array.codes);
                    final String[] countries = getResources().getStringArray(R.array.countries);

                    for(int i = 0; i < codes.length; i++)
                    {
                        if(newValue.toString().equals(codes[i]))
                        {
                            PreferencesManager.setCountryName(countries[i]);

                            return true;
                        }
                    }

                    //Just in case
                    PreferencesManager.setCountryName("Top News");

                    return true;
                });
            }

            ListPreference themePreference = findPreference("pref_theme");
            if(themePreference != null)
            {
                themePreference.setOnPreferenceChangeListener((preference, newValue) ->
                {
                    ThemeHelper.applyTheme((String) newValue);

                    return true;
                });
            }

            Preference about = findPreference("pref_about");
            if(about != null)
            {
                about.setOnPreferenceClickListener(preference ->
                {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Top News")
                            .setMessage("Open source news application.\n\nIcons made by Freepik.\nPowered by News API.")
                            .setNegativeButton("Close", null)
                            .show();

                    return true;
                });
            }
        }
    }
}