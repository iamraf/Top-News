package com.github.h01d.newsapp.ui.main;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.github.h01d.newsapp.R;
import com.github.h01d.newsapp.data.local.preference.PreferencesManager;
import com.github.h01d.newsapp.databinding.ActivityMainBinding;
import com.github.h01d.newsapp.ui.article.ArticlesFragment;
import com.github.h01d.newsapp.ui.settings.SettingsActivity;
import com.github.h01d.newsapp.util.ThemeHelper;

public class MainActivity extends AppCompatActivity
{
    private ActivityMainBinding mDataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mDataBinding.aMainToolbar.setTitle("Top News");
        setSupportActionBar(mDataBinding.aMainToolbar);

        PreferencesManager.init(getApplicationContext());

        ThemeHelper.applyTheme(PreferencesManager.getTheme());

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.a_main_frame, new ArticlesFragment());
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.m_main_settings)
        {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setToolbarTitle(String title)
    {
        mDataBinding.aMainToolbar.setTitle(title);
    }
}
