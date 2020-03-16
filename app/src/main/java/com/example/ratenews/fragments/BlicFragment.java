package com.example.ratenews.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.ratenews.adapters.NewsAdapterBlic;
import com.example.ratenews.R;
import com.example.ratenews.model.RssFeedModel;
import com.example.ratenews.crud.api.get.api.ApiUtils;
import com.example.ratenews.crud.api.get.api.JSONPlaceHolderAPI;
import com.example.ratenews.model.Result;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UnknownFormatConversionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlicFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlicFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static List<RssFeedModel> newsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NewsAdapterBlic adapter;
    private ProgressBar progressBar;
    private String mFeedTitle;
    private String mFeedLink;
    private String mFeedDescription;
    private String mContent;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BlicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlicFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlicFragment newInstance(String param1, String param2) {
        BlicFragment fragment = new BlicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blic, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        progressBar = rootView.findViewById(R.id.progressBar);
        final SwipeRefreshLayout pullToRefresh = rootView.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressBar.setVisibility(View.VISIBLE);
                newsList.clear();
                new FetchFeedTask().execute((Void) null);
                pullToRefresh.setRefreshing(false);
            }
        });

        new FetchFeedTask().execute((Void) null);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {

        private String urlLink;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            urlLink = "https://www.blic.rs/rss/Vesti";
            Log.i("info", "onPreExecute started");
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (TextUtils.isEmpty(urlLink))
                return false;

            try {
                if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                    urlLink = "http://" + urlLink;

                URL url = new URL(urlLink);
                InputStream inputStream = url.openConnection().getInputStream();
                newsList = parseFeed(inputStream);
                return true;
            } catch (IOException e) {
                Log.e("sada1", "Error", e);
            } catch (XmlPullParserException e) {
                Log.e("sada1", "Error", e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {

            if (success) {
                adapter = new NewsAdapterBlic(newsList, getActivity());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            } else {
//            Toast.makeText(this,
//                    "Enter a valid Rss feed url",
//                    Toast.LENGTH_LONG).show();
            }
        }

        public List<RssFeedModel> parseFeed(InputStream inputStream) throws XmlPullParserException,
                IOException {
            String link = null;
            String title = null;
            String description = null;
            String img = null;
            String date = null;
            boolean isItem = false;
            List<RssFeedModel> items = new ArrayList<>();

            try {
                XmlPullParser xmlPullParser = Xml.newPullParser();
                xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xmlPullParser.setInput(inputStream, null);

                xmlPullParser.nextTag();
                while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                    int eventType = xmlPullParser.getEventType();
                    //Log.e("sada1", "ušao");
                    String name = xmlPullParser.getName();
                    if(name == null)
                        continue;

                    if(eventType == XmlPullParser.END_TAG) {
                        if(name.equalsIgnoreCase("item")) {
                            isItem = false;
                            //Log.e("sada1", "našao item");
                        }
                        continue;
                    }

                    if (eventType == XmlPullParser.START_TAG) {
                        if(name.equalsIgnoreCase("item")) {
                            isItem = true;
                            continue;
                        }
                    }

                    //Log.d("sada1", "Parsing name ==> " + name);
                    String result = "";
                    if (xmlPullParser.next() == XmlPullParser.TEXT) {
                        result = xmlPullParser.getText();

                        xmlPullParser.nextTag();
                    }

                    if (name.equalsIgnoreCase("title")) {
                        //Log.e("sada2", "Result ==> " + result);
                        title = result;
                    } else if (name.equalsIgnoreCase("description")) {
                        description = result;
                    } else if (name.equalsIgnoreCase("link")) {
                        link = result;
                    } else if (name.equalsIgnoreCase("content:encoded")) {
                        try{
                            img = String.format(result, R.drawable.nothumbnail);
                        }catch (UnknownFormatConversionException e){
                            img = null;
                            e.printStackTrace();
                        }
                        //img = result;
                    } else if (name.equalsIgnoreCase("pubDate")) {
                        //date = result;
                        try {
                            DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzzz");
                            Date date1 = formatter.parse(result);
                            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy. HH:mm", Locale.US);
                            //format.applyPattern("dd MM yyyy HH:mm");
                            date = format.format(date1);
                            //DateFormat format = new SimpleDateFormat ("dd.MM.yyyy. HH:mm");
                            //date1 = format.parse(result);
                            //Log.d("date1", date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Log.d("date1", "error");
                        }
                    }

                    if (title != null && link != null && description != null && img != null && date != null) {
                        if(isItem) {
                            final RssFeedModel item = new RssFeedModel(link, title, description, img, date, 0, 0, false, false);
                            JSONPlaceHolderAPI mAPIService;
                            mAPIService = ApiUtils.getAPIServiceFetch();
                            mAPIService.getNews(link).enqueue(new Callback<Result>() {
                                @Override
                                public void onResponse(Call<Result> call, Response<Result> response) {
                                    if(response.body()!=null) {
                                        if (response.body().getSuccess() == 1) {
                                            item.setPositiveVotes(response.body().getData().getPositiveVotes());
                                            item.setNegativeVotes(response.body().getData().getNegativeVotes());
                                            //negativeVote[0] = response.body().getData().getNegativeVotes();
                                            Log.e("Checking Blic", "Negative: " + item.getNegativeVotes() + ", Positive: " + item.getPositiveVotes());
                                        }
                                    }
                                }
                                @Override
                                public void onFailure(Call<Result> call, Throwable t) {

                                }
                            });
                            items.add(item);
                        }
                        else {
                            mFeedTitle = title;
                            mFeedLink = link;
                            mFeedDescription = description;
                            mContent = date;
                        }

                        title = null;
                        link = null;
                        description = null;
                        img = null;
                        date = null;
                        isItem = false;
                    }
                }

                return items;
            } finally {
                inputStream.close();
            }
        }

    }

}
