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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.h01d.newsapp.R;
import com.github.h01d.newsapp.data.remote.model.Article;
import com.github.h01d.newsapp.databinding.FragmentArticlesBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ArticlesFragment extends Fragment implements ArticlesAdapter.ArticlesAdapterListener
{
    private ArticlesViewModel mViewModel;
    private FragmentArticlesBinding mDataBinding;

    private CompositeDisposable mDisposable;

    public ArticlesFragment()
    {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_articles, container, false);

        mDisposable = new CompositeDisposable();

        mDataBinding.fArticlesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mDataBinding.fArticlesRecycler.setHasFixedSize(true);
        mDataBinding.fArticlesRecycler.setAdapter(new ArticlesAdapter(this));

        mDataBinding.fArticlesSwipe.setOnRefreshListener(() -> mViewModel.fetchNews());

        return mDataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(ArticlesViewModel.class);

        mDisposable.add(mViewModel.getLoadingIndicator()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean ->
                {
                    if(aBoolean)
                    {
                        mDataBinding.fArticlesSwipe.setRefreshing(true);
                        mDataBinding.fArticlesErrorText.setVisibility(View.GONE);
                    }
                    else
                    {
                        mDataBinding.fArticlesSwipe.setRefreshing(false);
                    }
                }));

        mDisposable.add(mViewModel.getArticles()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(articlesResponse ->
                {
                    ((ArticlesAdapter) mDataBinding.fArticlesRecycler.getAdapter()).setData(articlesResponse.getArticles());

                    getActivity().setTitle(mViewModel.getCountryName());
                }));

        mDisposable.add(mViewModel.getErrorMessage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s ->
                {
                    mDataBinding.fArticlesErrorText.setText(s);
                    mDataBinding.fArticlesErrorText.setVisibility(View.VISIBLE);
                    mDataBinding.fArticlesRecycler.setVisibility(View.GONE);
                }));
    }

    @Override
    public void onResume()
    {
        super.onResume();

        mViewModel.fetchNews();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        mDisposable.dispose();
    }

    @Override
    public void onClicked(Article article)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(article.getUrl()));
        startActivity(intent);
    }
}
