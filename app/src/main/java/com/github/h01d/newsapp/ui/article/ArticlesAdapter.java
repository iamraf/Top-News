package com.github.h01d.newsapp.ui.article;

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

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.h01d.newsapp.data.remote.model.Article;
import com.github.h01d.newsapp.databinding.ArticleItemBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder>
{
    private List<Article> data;
    private ArticlesAdapterListener listener;

    ArticlesAdapter(ArticlesAdapterListener listener)
    {
        this.listener = listener;

        data = new ArrayList<>();
    }

    public void setData(List<Article> data)
    {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ArticleItemBinding binding = ArticleItemBinding.inflate(layoutInflater, parent, false);

        return new ArticleViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position)
    {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder
    {
        private final ArticleItemBinding binding;

        ArticleViewHolder(@NonNull ArticleItemBinding binding)
        {
            super(binding.getRoot());

            this.binding = binding;
        }

        void bind(Article item)
        {
            binding.getRoot().setOnClickListener(v -> listener.onClicked(binding.getArticle()));
            binding.setArticle(item);
            binding.executePendingBindings();
        }
    }

    interface ArticlesAdapterListener
    {
        void onClicked(Article article);
    }
}