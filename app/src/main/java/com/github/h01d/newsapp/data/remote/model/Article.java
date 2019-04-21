package com.github.h01d.newsapp.data.remote.model;

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

import android.widget.ImageView;
import android.widget.TextView;

import com.github.h01d.newsapp.R;
import com.github.h01d.newsapp.data.local.preference.PreferencesManager;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import androidx.databinding.BindingAdapter;

public class Article implements Serializable
{
    private Source source;
    private String title;
    private String url;
    private String urlToImage;
    private String publishedAt;

    public Article(Source source, String title, String url, String urlToImage, String publishedAt)
    {
        this.source = source;
        this.title = title;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
    }

    public Source getSource()
    {
        return source;
    }

    public String getTitle()
    {
        return title;
    }

    public String getUrl()
    {
        return url;
    }

    public String getUrlToImage()
    {
        return urlToImage;
    }

    public String getPublishedAt()
    {
        return publishedAt;
    }

    @BindingAdapter(value = "imageUrl")
    public static void loadResizedImage(ImageView imageView, String url)
    {
        Picasso.get()
                .load(url)
                .resize(100, 100)
                .centerCrop()
                .placeholder(R.drawable.loading_mark)
                .error(R.drawable.warning_sign)
                .into(imageView);
    }

    @BindingAdapter(value = "date")
    public static void convertDate(TextView textView, String dateString)
    {
        try
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date date = simpleDateFormat.parse(dateString);
            Locale locale = null;

            if(PreferencesManager.getCountryCode().equals("us") || PreferencesManager.getCountryCode().equals("gb"))
            {
                locale = new Locale("en");
            }
            else
            {
                for(Locale tmp : Locale.getAvailableLocales())
                {
                    if(PreferencesManager.getCountry().equals(tmp.getDisplayCountry()))
                    {
                        locale = tmp;
                        break;
                    }
                }
            }

            if(locale == null)
            {
                locale = Locale.getDefault();
            }

            textView.setText(new SimpleDateFormat("d MMMM yyyy HH:mm", locale).format(date));
        }
        catch(ParseException e)
        {
            e.printStackTrace();
            textView.setText("");
        }
    }
}
