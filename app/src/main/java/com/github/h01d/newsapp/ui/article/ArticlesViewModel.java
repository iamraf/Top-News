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
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class ArticlesViewModel extends ViewModel
{
    private ApiService apiService;

    private final BehaviorSubject<ArticlesResponse> articles;
    private final BehaviorSubject<Boolean> loading;
    private final PublishSubject<String> error;

    public ArticlesViewModel()
    {
        apiService = ApiClient.getClient();
        articles = BehaviorSubject.create();
        loading = BehaviorSubject.create();
        error = PublishSubject.create();
        loadData(getCountryCode());
    }

    private void loadData(String countryCode) {
        apiService.getTopHeadlines(countryCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArticlesResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        loading.onNext(true);
                    }

                    @Override
                    public void onNext(ArticlesResponse articlesResponse) {
                        articles.onNext(articlesResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        error.onNext(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        loading.onNext(false);
                    }
                });
    }

    Observable<ArticlesResponse> getArticles()
    {
        return articles;
    }

    Observable<Boolean> getLoadingIndicator()
    {
        return loading;
    }

    Observable<String> getErrorMessage()
    {
        return error;
    }

    void setCountry(String country, String countryCode)
    {
        PreferencesManager.setCountry(country, countryCode);
        loadData(countryCode);
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
