package com.example.ratenews.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import com.example.ratenews.fragments.N1Fragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsAdapterN1 extends RecyclerView.Adapter<NewsAdapterN1.NewsViewHolder>{

    private List<RssFeedModel> newsList;
    private Context mContext;

    public NewsAdapterN1(List<RssFeedModel> newsList, Context mContext) {
        this.newsList = newsList;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new NewsAdapterN1.NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsViewHolder holder, final int position) {
        final String url = newsList.get(position).getNovostiFeed();
        final String sTitle = newsList.get(position).getTitle();
        final String sDesc = newsList.get(position).getDescription();
        final String sDate = newsList.get(position).getDate();
        final String img = newsList.get(position).getContent();
        final String source = "N1";
        final String email = MainActivity.userEmail;
        final String sPositive = String.valueOf(newsList.get(position).getPositiveVotes());
        final String sNegative = String.valueOf(newsList.get(position).getNegativeVotes());
        final Animation fadeIn = AnimationUtils.loadAnimation(mContext, R.anim.fade_in_animation);

        if(N1Fragment.newsList.get(holder.getAdapterPosition()).isTrueBtnClicked){
            animButton(holder.btnPositive);
            holder.btnPositive.setEnabled(false);
            holder.btnNegative.setEnabled(false);
            //animButton(holder.btnPositive);
            holder.btnNegative.setBackgroundResource(R.drawable.btn_style_transparent);
            holder.btnNegative.setTextColor(mContext.getResources().getColor(R.color.darkGray));
        }else if(N1Fragment.newsList.get(holder.getAdapterPosition()).isLieBtnClicked){
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
        Glide.with(mContext)
                .load(img)
                .apply(new RequestOptions().override(2400, 800))
                .into(holder.img);
        holder.novostiFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", url);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        holder.positive.setText(sPositive);
        holder.negative.setText(sNegative);

        holder.btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean[] voted = {false};
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
                            JSONPlaceHolderAPI mAPIService;
                            mAPIService = ApiUtils.getAPIServiceFetch();
                            mAPIService.getNews(url).enqueue(new Callback<Result>() {
                                @Override
                                public void onResponse(Call<Result> call, Response<Result> response) {
                                    if(response.body().getSuccess()==1){
                                        AddNews update = new AddNews();
                                        update.updateNewsVotePositive(response.body().getData().getPositiveVotes()+1, url);
                                        isPositiveBtnClicked(holder);
                                        Log.i("Positive updated -> ", String.valueOf(response.body().getSuccess()));
                                    }else{
                                        AddNews postingToDb = new AddNews();
                                        postingToDb.addNews(sTitle, sDesc, url, sDate, img, 1, 0, "N1");
                                        isPositiveBtnClicked(holder);
                                        Log.i("Positive added -> ", String.valueOf(response.body().getSuccess()));
                                    }
                                }

                                @Override
                                public void onFailure(Call<Result> call, Throwable t) {
                                    Log.i("Update failed -> ", String.valueOf(t.getMessage()));
                                }
                            });
                        } else {
                            animButton(holder.btnPositive);
                            holder.btnPositive.setEnabled(false);
                            holder.btnNegative.setEnabled(false);
                            //animButton(holder.btnPositive);
                            holder.btnNegative.setBackgroundResource(R.drawable.btn_style_transparent);
                            holder.btnNegative.setTextColor(mContext.getResources().getColor(R.color.darkGray));
                            Toast.makeText(mContext, "Vec ste glasali za ovu vest", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultVotes> call, Throwable t) {

                    }
                });


            }
        });
        holder.btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean[] voted = {false};
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
                            JSONPlaceHolderAPI mAPIService;
                            mAPIService = ApiUtils.getAPIServiceFetch();
                            mAPIService.getNews(url).enqueue(new Callback<Result>() {
                                @Override
                                public void onResponse(Call<Result> call, Response<Result> response) {
                                    if(response.body().getSuccess()==1){
                                        AddNews update = new AddNews();
                                        update.updateNewsVoteNegative(response.body().getData().getNegativeVotes()+1, url);
                                        isNegativeBtnClicked(holder);
                                        Log.i("Negative updated N1 ", String.valueOf(response.body().getSuccess()));
                                    }else{
                                        AddNews postingToDb = new AddNews();
                                        postingToDb.addNews(sTitle, sDesc, url, sDate, img, 0, 1, "N1");
                                        isNegativeBtnClicked(holder);
                                        Log.i("Negative added N1 ", String.valueOf(response.body().getSuccess()));
                                    }

                                }

                                @Override
                                public void onFailure(Call<Result> call, Throwable t) {
                                    Log.i("Update failed -> ", String.valueOf(t.getMessage()));
                                }
                            });
                        } else {
                            animButton(holder.btnPositive);
                            holder.btnPositive.setEnabled(false);
                            holder.btnNegative.setEnabled(false);
                            //animButton(holder.btnPositive);
                            holder.btnNegative.setBackgroundResource(R.drawable.btn_style_transparent);
                            holder.btnNegative.setTextColor(mContext.getResources().getColor(R.color.darkGray));
                            Toast.makeText(mContext, "Već si glasao za ovu vest", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultVotes> call, Throwable t) {

                    }
                });

            }
        });
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

    private void isPositiveBtnClicked(NewsAdapterN1.NewsViewHolder holder){
        N1Fragment.newsList.get(holder.getAdapterPosition()).setTrueBtnClicked(!N1Fragment.newsList.get(holder.getAdapterPosition()).isTrueBtnClicked);
        notifyDataSetChanged();
        //Log.e("clicked positive button", String.valueOf(holder.getAdapterPosition()));
    }

    private void isNegativeBtnClicked(NewsAdapterN1.NewsViewHolder holder){
        N1Fragment.newsList.get(holder.getAdapterPosition()).setLieBtnClicked(!N1Fragment.newsList.get(holder.getAdapterPosition()).isLieBtnClicked);
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
