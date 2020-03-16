package com.example.ratenews.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ratenews.MainActivity;
import com.example.ratenews.R;
import com.example.ratenews.model.RssFeedModel;
import com.example.ratenews.WebViewActivity;
import com.example.ratenews.crud.api.AddNews;
import com.example.ratenews.crud.api.AddVote;
import com.example.ratenews.crud.api.get.api.ApiUtils;
import com.example.ratenews.crud.api.get.api.GetUserVotes;
import com.example.ratenews.crud.api.get.api.JSONPlaceHolderAPI;
import com.example.ratenews.model.Result;
import com.example.ratenews.model.ResultVotes;
import com.example.ratenews.fragments.BlicFragment;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsAdapterBlic extends RecyclerView.Adapter<NewsAdapterBlic.NewsViewHolder>{

    private List<RssFeedModel> newsList;
    private Context mContext;

    public NewsAdapterBlic(List<RssFeedModel> newsList, Context mContext) {
        this.newsList = newsList;
        this.mContext = mContext;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NewsViewHolder holder, final int position) {
        final String img = newsList.get(position).getContent();
        final String html = String.format("Image: %s", img);
        final String url = newsList.get(position).getNovostiFeed();
        final String sTitle = newsList.get(position).getTitle();
        final String sDesc = newsList.get(position).getDescription();
        final String sDate = newsList.get(position).getDate();
        final String source = "Blic";
        final String[] sImgUrl = {""};
        final String email = MainActivity.userEmail;
        final String sPositive = String.valueOf(newsList.get(position).getPositiveVotes());
        final String sNegative = String.valueOf(newsList.get(position).getNegativeVotes());


        if(BlicFragment.newsList.get(holder.getAdapterPosition()).isTrueBtnClicked){
            animButton(holder.btnPositive);
            holder.btnPositive.setEnabled(false);
            holder.btnNegative.setEnabled(false);
            //animButton(holder.btnPositive);
            holder.btnNegative.setBackgroundResource(R.drawable.btn_style_transparent);
            holder.btnNegative.setTextColor(mContext.getResources().getColor(R.color.darkGray));
        }else if(BlicFragment.newsList.get(holder.getAdapterPosition()).isLieBtnClicked){
            animButton(holder.btnNegative);
            holder.btnNegative.setEnabled(false);
            holder.btnPositive.setEnabled(false);
            holder.btnPositive.setBackgroundResource(R.drawable.btn_style_transparent);
            holder.btnPositive.setTextColor(mContext.getResources().getColor(R.color.darkGray));
        }else{
            holder.btnPositive.setEnabled(true);
            holder.btnPositive.setBackgroundResource(R.drawable.login_btn_style_red);
            holder.btnPositive.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.btnNegative.setEnabled(true);
            holder.btnNegative.setBackgroundResource(R.drawable.login_btn_style_red);
            holder.btnNegative.setTextColor(mContext.getResources().getColor(R.color.white));
        }

        holder.title.setText(sTitle);
        holder.description.setText(sDesc);
        holder.content.setText(sDate);
        holder.newsSource.setText(source);
        holder.novostiFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", url);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                //Log.i("click", "clicked");
            }
        });

        holder.positive.setText(sPositive);
        holder.negative.setText(sNegative);
        //String s = Html.fromHtml(html).toString();
        //Log.d("tag2",s);

        Html.fromHtml(html, new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(final String source) {
                Drawable d = null;
                Glide.with(mContext)
                        .load(source)
                        .apply(new RequestOptions().override(2400, 800))
                        .into(holder.img);
                sImgUrl[0] = source;
                return d;
            }
        }, null);
        holder.btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btnNegative.setClickable(false);
                holder.btnPositive.setClickable(false);
                isUserVotedPositive(holder, email, url, sTitle, sDesc, sDate, sImgUrl[0], position);
            }
        });
        holder.btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btnNegative.setClickable(false);
                holder.btnPositive.setClickable(false);
                isUserVotedNegative(holder, email, url, sTitle, sDesc, sDate, sImgUrl[0], position);
            }
        });
        //Log.d("sada3", newsList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        public TextView novostiFeed, title, description, content, newsSource, positive, negative;
        public ImageView img;
        private Button btnPositive, btnNegative;

        public NewsViewHolder(View view) {
            super(view);
            novostiFeed = view.findViewById(R.id.novostiFeed);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            content = view.findViewById(R.id.content);
            positive = view.findViewById(R.id.positive);
            negative = view.findViewById(R.id.negative);
            img = view.findViewById(R.id.img);
            btnPositive = view.findViewById(R.id.truee);
            btnNegative = view.findViewById(R.id.lie);
            newsSource = view.findViewById(R.id.news);
        }
    }

    private void isPositiveBtnClicked(NewsViewHolder holder){
        BlicFragment.newsList.get(holder.getAdapterPosition()).setTrueBtnClicked(!BlicFragment.newsList.get(holder.getAdapterPosition()).isTrueBtnClicked);
        notifyDataSetChanged();
        //Log.e("clicked positive button", String.valueOf(holder.getAdapterPosition()));
    }

    private void isNegativeBtnClicked(NewsViewHolder holder){
        BlicFragment.newsList.get(holder.getAdapterPosition()).setLieBtnClicked(!BlicFragment.newsList.get(holder.getAdapterPosition()).isLieBtnClicked);
        notifyDataSetChanged();
    }

    private void animButton (final Button btn){
        AlphaAnimation anim = new AlphaAnimation(0.1f, 1.0f);
        anim.setDuration(300);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                btn.setClickable(false);
                btn.setBackgroundResource(R.drawable.btn_style_transparent);
                btn.setTextColor(mContext.getResources().getColor(R.color.darkGray));
            }

            @Override
            public void onAnimationEnd(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        btn.startAnimation(anim);
    }

    private void updateOrPostNewsToDatabasePositive(final NewsViewHolder holder, final String url, final String sTitle,
                                            final String sDesc, final String sDate, final String sImgUrl){
        JSONPlaceHolderAPI mAPIService;
        mAPIService = ApiUtils.getAPIServiceFetch();
        mAPIService.getNews(url).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body().getSuccess() == 1) {
                    //updating news in DB
                    AddNews update = new AddNews();
                    update.updateNewsVotePositive(response.body().getData().getPositiveVotes() + 1, url);
                    holder.btnNegative.setClickable(true);
                    holder.btnPositive.setClickable(true);
                    isPositiveBtnClicked(holder);
                    //MainActivity.newsList.get(holder.getAdapterPosition()).setTrueBtnClicked(!MainActivity.newsList.get(holder.getAdapterPosition()).isTrueBtnClicked);
                    Log.i("Positive updated -> ", String.valueOf(response.body().getSuccess()));
                    //notifyDataSetChanged();

                } else {
                    //adding news to DB
                    AddNews postingToDb = new AddNews();
                    postingToDb.addNews(sTitle, sDesc, url, sDate, sImgUrl, 1, 0, "Blic");
                    //holder.btnPositive.setVisibility(View.GONE);
                    //Log.i("Positive added -> ", String.valueOf(response.body().getSuccess()));
                    holder.btnNegative.setClickable(true);
                    holder.btnPositive.setClickable(true);
                    isPositiveBtnClicked(holder);
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
    }

    private void isUserVotedPositive(final NewsViewHolder holder, final String email, final String url,
                                     final String sTitle, final String sDesc, final String sDate, final String sImgUrl, final int position){
        final boolean[] voted = {false};
        final Animation fadeIn = AnimationUtils.loadAnimation(mContext, R.anim.fade_in_animation);

        GetUserVotes mApiGetUserVotes;
        mApiGetUserVotes = ApiUtils.getAPIServiceUserVotes();
        mApiGetUserVotes.getVotes(email, url).enqueue(new Callback<ResultVotes>() {
            @Override
            public void onResponse(Call<ResultVotes> call, Response<ResultVotes> response) {
                if(response.body().getSuccess()==1){
                    voted[0] = true;
                }else{ voted[0] = false; }
                if(!voted[0]){
                    //adding vote to vote checker
                    AddVote addVote = new AddVote();
                    addVote.addVote(url, email);
                    holder.positive.startAnimation(fadeIn);
                    updateTrueVotes(position, newsList);
                    //checking if news exist in DB
                    updateOrPostNewsToDatabasePositive(holder, url, sTitle, sDesc, sDate, sImgUrl);
                } else {
                    animButton(holder.btnPositive);
                    holder.btnPositive.setEnabled(false);
                    holder.btnNegative.setEnabled(false);
                    //animButton(holder.btnPositive);
                    holder.btnNegative.setBackgroundResource(R.drawable.btn_style_transparent);
                    holder.btnNegative.setTextColor(mContext.getResources().getColor(R.color.darkGray));
                            Toast.makeText(mContext, "Vec ste glasali za ovu vest", Toast.LENGTH_LONG).show();
                }
                Log.e("username1", response.body().getSuccess().toString() + " " + voted[0] + email);
            }

            @Override
            public void onFailure(Call<ResultVotes> call, Throwable t) {

            }
        });
    }

    private void updateOrPostNewsToDatabaseNegative(final NewsViewHolder holder, final String url, final String sTitle,
                                                    final String sDesc, final String sDate, final String sImgUrl){
        JSONPlaceHolderAPI mAPIService;
        mAPIService = ApiUtils.getAPIServiceFetch();
        mAPIService.getNews(url).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(response.body().getSuccess()==1){
                    AddNews update = new AddNews();
                    update.updateNewsVoteNegative(response.body().getData().getNegativeVotes()+1, url);
                    Log.i("Negative updated -> ", String.valueOf(response.body().getSuccess()));
                    holder.btnNegative.setClickable(true);
                    holder.btnPositive.setClickable(true);
                    isNegativeBtnClicked(holder);

                }else{
                    AddNews postingToDb = new AddNews();
                    postingToDb.addNews(sTitle, sDesc, url, sDate, sImgUrl, 0, 1, "Blic");
                    Log.i("Negative added -> ", String.valueOf(response.body().getSuccess()));
                    holder.btnNegative.setClickable(true);
                    holder.btnPositive.setClickable(true);
                    isNegativeBtnClicked(holder);
                }

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.i("Update failed -> ", String.valueOf(t.getMessage()));
            }
        });
    }

    private void isUserVotedNegative(final NewsViewHolder holder, final String email, final String url,
                                     final String sTitle, final String sDesc, final String sDate,
                                     final String sImgUrl, final int position){

        final boolean[] voted = {false};
        final Animation fadeIn = AnimationUtils.loadAnimation(mContext, R.anim.fade_in_animation);

        GetUserVotes mApiGetUserVotes;
        mApiGetUserVotes = ApiUtils.getAPIServiceUserVotes();
        mApiGetUserVotes.getVotes(email, url).enqueue(new Callback<ResultVotes>() {
            @Override
            public void onResponse(Call<ResultVotes> call, Response<ResultVotes> response) {
                if(response.body().getSuccess()==1){
                    voted[0] = true;
                }else{ voted[0] = false; }
                if(!voted[0]){
                    //adding vote to vote checker
                    AddVote addVote = new AddVote();
                    addVote.addVote(url, email);
                    holder.negative.startAnimation(fadeIn);
                    updateLieVotes(position, newsList);
                    //checking if news exist in DB
                    updateOrPostNewsToDatabaseNegative(holder, url, sTitle, sDesc, sDate, sImgUrl);
                } else {
                    animButton(holder.btnNegative);
                    holder.btnPositive.setEnabled(false);
                    holder.btnNegative.setEnabled(false);
                    //animButton(holder.btnPositive);
                    holder.btnPositive.setBackgroundResource(R.drawable.btn_style_transparent);
                    holder.btnPositive.setTextColor(mContext.getResources().getColor(R.color.darkGray));
                    Toast.makeText(mContext, "Vec ste glasali za ovu vest", Toast.LENGTH_LONG).show();
                }
                Log.e("username1", response.body().getSuccess().toString() + " " + voted[0] + email);
            }

            @Override
            public void onFailure(Call<ResultVotes> call, Throwable t) {

            }
        });

    }

    public void updateTrueVotes(int pos, List<RssFeedModel> list){
        int iLikes = list.get(pos).getPositiveVotes();
        iLikes++;
        list.get(pos).setPositiveVotes(iLikes);
    }
    public void updateLieVotes(int pos, List<RssFeedModel> list){
        int iLikes = list.get(pos).getNegativeVotes();
        iLikes++;
        list.get(pos).setNegativeVotes(iLikes);
    }

}