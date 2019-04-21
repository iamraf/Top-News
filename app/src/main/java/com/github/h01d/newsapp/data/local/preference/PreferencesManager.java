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

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

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
            instance = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        }
    }

    public static void setCountry(String country, String code)
    {
        SharedPreferences.Editor prefsEditor = instance.edit();
        prefsEditor.putString("country", country);
        prefsEditor.putString("country_code", code);
        prefsEditor.apply();
    }

    public static String getCountry()
    {
        return instance.getString("country", "Greece");
    }

    public static String getCountryCode()
    {
        return instance.getString("country_code", "gr");
    }
}
