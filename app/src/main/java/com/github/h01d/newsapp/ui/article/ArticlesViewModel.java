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
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class ArticlesViewModel extends ViewModel
{
    private ApiService apiService;

    private BehaviorSubject<ArticlesResponse> articles;
    private BehaviorSubject<Boolean> loading;
    private BehaviorSubject<String> error;

    public ArticlesViewModel()
    {
        apiService = ApiClient.getClient();

        articles = BehaviorSubject.create();
        loading = BehaviorSubject.create();
        error = BehaviorSubject.create();

        fetchNews();
    }

    public void fetchNews()
    {
        loading.onNext(true);

        apiService.getTopHeadlines(getCountryCode())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArticlesResponse>()
                {
                    @Override
                    public void onSubscribe(Disposable d)
                    {

                    }

                    @Override
                    public void onNext(ArticlesResponse articlesResponse)
                    {
                        if(articlesResponse.getStatus().equals("ok"))
                        {
                            if(articlesResponse.getArticles().size() > 0)
                            {
                                articles.onNext(articlesResponse);
                            }
                            else
                            {
                                error.onNext("No top headlines found!");
                            }
                        }
                        else
                        {
                            error.onNext("An error occurred, please try again.");
                        }

                        loading.onNext(false);
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        error.onNext("An error occurred, please try again.");
                        loading.onNext(false);
                    }

                    @Override
                    public void onComplete()
                    {

                    }
                });
    }

    public Observable<ArticlesResponse> getArticles()
    {
        return articles;
    }

    public Observable<Boolean> getLoadingIndicator()
    {
        return loading;
    }

    public Observable<String> getErrorMessage()
    {
        return error;
    }

    public void setCountry(String country, String countryCode)
    {
        PreferencesManager.setCountry(country, countryCode);

        fetchNews();
    }

    public String getCountry()
    {
        return PreferencesManager.getCountry();
    }

    public String getCountryCode()
    {
        return PreferencesManager.getCountryCode();
    }
}
