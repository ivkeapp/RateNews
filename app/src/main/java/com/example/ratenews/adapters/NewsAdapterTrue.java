package com.example.ratenews.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.example.ratenews.WebViewActivity;
import com.example.ratenews.crud.api.AddNews;
import com.example.ratenews.crud.api.AddVote;
import com.example.ratenews.crud.api.get.api.ApiUtils;
import com.example.ratenews.crud.api.get.api.GetUserVotes;
import com.example.ratenews.crud.api.get.api.JSONPlaceHolderAPI;
import com.example.ratenews.model.AllNewsModel;
import com.example.ratenews.model.Result;
import com.example.ratenews.model.ResultVotes;
import com.example.ratenews.fragments.TrueFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsAdapterTrue extends RecyclerView.Adapter<NewsAdapterTrue.NewsViewHolder>{

    private List<AllNewsModel> bookList;
    private Context mContext;

    public NewsAdapterTrue(List<AllNewsModel> bookList, Context mContext) {
        this.bookList = bookList;
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
        //final String img = String.format(bookList.get(position).getContent(), R.drawable.ic_launcher_foreground);
        //final String html = String.format("Image: %s", img);
        //final Resources res = holder.itemView.getContext().getResources();
        //Drawable d1 = res.getDrawable(Integer.parseInt(bookList.get(position).getContent()));
        //d1.setBounds(0, 0, d1.getIntrinsicHeight(), d1.getIntrinsicWidth());
        //Log.d("imgurl", html);
        final String url = bookList.get(position).getLink();
        final String sTitle = bookList.get(position).getTitle();
        final String sDesc = bookList.get(position).getDescription();
        final String sDate = bookList.get(position).getDate();
        final String img = bookList.get(position).getImage();
        final String source = bookList.get(position).getPaper();
        final String[] sImgUrl = {""};
        final int iPositiveVotes = bookList.get(position).getPositiveVotes();
        final int iNegativeVotes = bookList.get(position).getNegativeVotes();
        final String sPositiveVotes = String.valueOf(iPositiveVotes);
        final String sNegativeVotes = String.valueOf(iNegativeVotes);
        final String email = MainActivity.userEmail;
        final Animation fadeIn = AnimationUtils.loadAnimation(mContext, R.anim.fade_in_animation);


    if(TrueFragment.trueNewsList.size()!=0) {
        if (TrueFragment.trueNewsList.get(holder.getAdapterPosition()).isTrueBtnClicked) {
            animButton(holder.btnPositive);
            holder.btnPositive.setEnabled(false);
            holder.btnNegative.setEnabled(false);
            //animButton(holder.btnPositive);
            holder.btnNegative.setBackgroundResource(R.drawable.btn_style_transparent);
            holder.btnNegative.setTextColor(mContext.getResources().getColor(R.color.darkGray));
        } else if (TrueFragment.trueNewsList.get(holder.getAdapterPosition()).isLieBtnClicked) {
            animButton(holder.btnNegative);
            holder.btnNegative.setEnabled(false);
            holder.btnPositive.setEnabled(false);
            holder.btnPositive.setBackgroundResource(R.drawable.btn_style_transparent);
            holder.btnPositive.setTextColor(mContext.getResources().getColor(R.color.darkGray));
        } else {
            holder.btnPositive.setEnabled(true);
            holder.btnPositive.setBackgroundResource(R.drawable.login_btn_style_red);
            holder.btnPositive.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.btnNegative.setEnabled(true);
            holder.btnNegative.setBackgroundResource(R.drawable.login_btn_style_red);
            holder.btnNegative.setTextColor(mContext.getResources().getColor(R.color.white));
        }
    }
        //set animation on switcher


        holder.title.setText(sTitle);
        holder.description.setText(sDesc);
        holder.content.setText(sDate);
        //holder.positive.setCurrentText(sPositiveVotes);
        holder.positive.setText(sPositiveVotes);
        holder.negative.setText(sNegativeVotes);
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


        //String s = Html.fromHtml(html).toString();
        //Log.d("tag2",s);

//        Html.fromHtml(html, new Html.ImageGetter() {
//            @Override
//            public Drawable getDrawable(final String source) {
//                Drawable d = null;
                Glide.with(mContext)
                        .load(img)
                        .apply(new RequestOptions().override(2400, 800))
                        .into(holder.img);
//                sImgUrl[0] = source;
//                return d;
//            }
//        }, null);

        holder.btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean[] voted = {false};
                //holder.positive.setText(String.valueOf(iPositiveVotes + 1));
                GetUserVotes mApiGetUserVotes;
                mApiGetUserVotes = ApiUtils.getAPIServiceUserVotes();
                mApiGetUserVotes.getVotes(email, url).enqueue(new Callback<ResultVotes>() {
                    @Override
                    public void onResponse(Call<ResultVotes> call, Response<ResultVotes> response) {
                        if(response.body().getSuccess()==1){
                            voted[0] = true;
                        }else{ voted[0] = false; }
                        if(!voted[0]){
                            //set animation for positive number and icon
                            holder.positive.startAnimation(fadeIn);
                            updateTrueVotes(position, bookList);
                            //holder.positive.setText("2");
                            //notifyDataSetChanged();
                            //adding vote to vote checker
                            AddVote addVote = new AddVote();
                            addVote.addVote(url, email);
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
                                        //MainActivity.newsList.get(holder.getAdapterPosition()).setTrueBtnClicked(!MainActivity.newsList.get(holder.getAdapterPosition()).isTrueBtnClicked);
                                        Log.i("Positive updated -> ", String.valueOf(response.body().getSuccess()));
                                        //notifyDataSetChanged();

                                    }else{
                                        AddNews postingToDb = new AddNews();
                                        postingToDb.addNews(sTitle, sDesc, url, sDate, sImgUrl[0], 1, 0, "Blic");
                                        //holder.btnPositive.setVisibility(View.GONE);
                                        Log.i("Positive added -> ", String.valueOf(response.body().getSuccess()));
                                        isPositiveBtnClicked(holder);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Result> call, Throwable t) {

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
                //AddNews postingToDb = new AddNews();
                //postingToDb.addNews(sTitle, sDesc, url, sDate, sImgUrl[0], 1, 0, "Blic");


            }
        });

        holder.btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //new LoadDataAsyncTask(holder, iNegativeVotes, email, url, sTitle, sDesc, sDate, fadeIn, sImgUrl, retrofitResponseListener).execute();

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
                            //set animation for negative number and icon
                            holder.negative.startAnimation(fadeIn);
                            //iNegativeVotes[0]++;
                            //holder.negative.setText(String.valueOf(iNegativeVotes[0]));
                            updateLieVotes(position, bookList);
                            //adding vote to vote checker
                            AddVote addVote = new AddVote();
                            addVote.addVote(url, email);
                            //checking if news exist in DB
                            JSONPlaceHolderAPI mAPIService;
                            mAPIService = ApiUtils.getAPIServiceFetch();
                            mAPIService.getNews(url).enqueue(new Callback<Result>() {
                                @Override
                                public void onResponse(Call<Result> call, Response<Result> response) {
                                    if(response.body().getSuccess()==1){
                                        AddNews update = new AddNews();
                                        update.updateNewsVoteNegative(response.body().getData().getNegativeVotes()+1, url);
                                        Log.i("Negative updated -> ", String.valueOf(response.body().getSuccess()));
                                        isNegativeBtnClicked(holder);
                                        //retrofitResponseListener.onSuccess();

                                    }else{
                                        AddNews postingToDb = new AddNews();
                                        postingToDb.addNews(sTitle, sDesc, url, sDate, sImgUrl[0], 0, 1, "Blic");
                                        Log.i("Negative added -> ", String.valueOf(response.body().getSuccess()));
                                        isNegativeBtnClicked(holder);
                                    }

                                }

                                @Override
                                public void onFailure(Call<Result> call, Throwable t) {
                                    Log.i("Update failed -> ", String.valueOf(t.getMessage()));
                                }
                            });
                        } else {
                            //retrofitResponseListener.onFail();
                            animButton(holder.btnNegative);
                            holder.btnPositive.setEnabled(false);
                            holder.btnNegative.setEnabled(false);
                            //animButton(holder.btnPositive);
                            holder.btnPositive.setBackgroundResource(R.drawable.btn_style_transparent);
                            holder.btnPositive.setTextColor(mContext.getResources().getColor(R.color.darkGray));
                            Toast.makeText(mContext, "Vec ste glasali za ovu vest", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultVotes> call, Throwable t) {

                    }
                });


            }
        });
        //Log.d("sada3", bookList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return bookList.size();
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
        TrueFragment.trueNewsList.get(holder.getAdapterPosition()).setTrueBtnClicked(!TrueFragment.trueNewsList.get(holder.getAdapterPosition()).isTrueBtnClicked);
        notifyDataSetChanged();
        //Log.e("clicked positive button", String.valueOf(holder.getAdapterPosition()));
    }

    private void isNegativeBtnClicked(NewsViewHolder holder){
        TrueFragment.trueNewsList.get(holder.getAdapterPosition()).setLieBtnClicked(!TrueFragment.trueNewsList.get(holder.getAdapterPosition()).isLieBtnClicked);
        notifyDataSetChanged();
    }

    private void animButton (final Button btn){
        AlphaAnimation anim = new AlphaAnimation(0.1f, 1.0f);
        anim.setDuration(300);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
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

    public void updateTrueVotes(int pos, List<AllNewsModel> list){
        int iLikes = list.get(pos).getPositiveVotes();
        iLikes++;
        list.get(pos).setPositiveVotes(iLikes);
    }
    public void updateLieVotes(int pos, List<AllNewsModel> list){
        int iLikes = list.get(pos).getNegativeVotes();
        iLikes++;
        list.get(pos).setNegativeVotes(iLikes);
    }

}