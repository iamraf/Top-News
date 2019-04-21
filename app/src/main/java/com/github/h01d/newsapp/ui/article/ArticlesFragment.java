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

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.github.h01d.newsapp.R;
import com.github.h01d.newsapp.data.remote.model.Article;
import com.github.h01d.newsapp.data.remote.model.ArticlesResponse;
import com.github.h01d.newsapp.databinding.FragmentArticlesBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ArticlesFragment extends Fragment implements ArticlesAdapter.ArticlesAdapterListener
{
    private ArticlesViewModel mViewModel;
    private FragmentArticlesBinding mDataBinding;

    private AlertDialog mCountrySelection;

    public ArticlesFragment()
    {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_articles, container, false);

        setHasOptionsMenu(true);

        createCountriesDialog();

        mDataBinding.fArticlesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mDataBinding.fArticlesRecycler.setHasFixedSize(true);
        mDataBinding.fArticlesRecycler.setAdapter(new ArticlesAdapter(this));

        return mDataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(ArticlesViewModel.class);

        reloadData();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.articles_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.m_articles_location:
                mCountrySelection.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClicked(Article article)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(article.getUrl()));
        startActivity(intent);
    }

    private void reloadData()
    {
        getActivity().setTitle(mViewModel.getCountry());

        mViewModel.getArticles(mViewModel.getCountryCode())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArticlesResponse>()
                {
                    @Override
                    public void onSubscribe(Disposable d)
                    {
                        mDataBinding.fArticlesProgress.setVisibility(View.VISIBLE);
                        mDataBinding.fArticlesContent.setVisibility(View.GONE);
                        mDataBinding.fArticlesError.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(ArticlesResponse articlesResponse)
                    {
                        if(articlesResponse.getStatus().equals("ok"))
                        {
                            if(articlesResponse.getArticles().size() > 0)
                            {
                                ((ArticlesAdapter) mDataBinding.fArticlesRecycler.getAdapter()).setData(articlesResponse.getArticles());
                                mDataBinding.fArticlesContent.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                mDataBinding.fArticlesError.setVisibility(View.VISIBLE);
                                mDataBinding.fArticlesErrorText.setText("No top headlines found!");
                            }
                        }
                        else
                        {
                            mDataBinding.fArticlesError.setVisibility(View.VISIBLE);
                            mDataBinding.fArticlesErrorText.setText("An error occurred, please try again.");
                        }
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        mDataBinding.fArticlesError.setVisibility(View.VISIBLE);
                        mDataBinding.fArticlesErrorText.setText("An error occurred, please try again.");
                        mDataBinding.fArticlesProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete()
                    {
                        mDataBinding.fArticlesProgress.setVisibility(View.GONE);
                    }
                });
    }

    private void createCountriesDialog()
    {
        final String[] countries = getResources().getStringArray(R.array.countries);
        final String[] codes = getResources().getStringArray(R.array.codes);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle("Available Countries");

        GridView gridView = new GridView(getContext());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, countries);
        gridView.setAdapter(arrayAdapter);

        dialogBuilder.setView(gridView);
        dialogBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        mCountrySelection = dialogBuilder.create();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                mViewModel.setCountry(countries[position], codes[position]);

                reloadData();

                mCountrySelection.dismiss();
            }
        });
    }
}
