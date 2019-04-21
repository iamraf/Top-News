package com.github.h01d.newsapp.ui.article;

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

import com.github.h01d.newsapp.data.local.preference.PreferencesManager;
import com.github.h01d.newsapp.data.remote.ApiClient;
import com.github.h01d.newsapp.data.remote.ApiService;
import com.github.h01d.newsapp.data.remote.model.ArticlesResponse;

import androidx.lifecycle.ViewModel;

import io.reactivex.Observable;

public class ArticlesViewModel extends ViewModel
{
    private ApiService apiService;

    public ArticlesViewModel()
    {
        apiService = ApiClient.getClient();
    }

    Observable<ArticlesResponse> getArticles(String countryCode)
    {
        return apiService.getTopHeadlines(countryCode);
    }

    void setCountry(String country, String countryCode)
    {
        PreferencesManager.setCountry(country, countryCode);
    }

    String getCountry()
    {
        return PreferencesManager.getCountry();
    }

    String getCountryCode()
    {
        return PreferencesManager.getCountryCode();
    }
}
