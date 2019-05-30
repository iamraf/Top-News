package com.github.h01d.newsapp.data.local.preference;

/*
    Copyright 2019 Raf

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

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class PreferencesManager
{
    private static SharedPreferences instance = null;

    private PreferencesManager()
    {

    }

    public static void init(Context context)
    {
        if(instance == null)
        {
            instance = PreferenceManager.getDefaultSharedPreferences(context);
        }
    }

    public static String getCountry()
    {
        return instance.getString("pref_country", "gr");
    }

    public static String getCountryName()
    {
        return instance.getString("pref_country_name", "Greece");
    }

    public static void setCountryName(String name)
    {
        SharedPreferences.Editor prefsEditor = instance.edit();
        prefsEditor.putString("pref_country_name", name);
        prefsEditor.apply();
    }

    public static String getTheme()
    {
        return instance.getString("pref_theme", "default");
    }
}
