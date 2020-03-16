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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.ratenews.adapters.NewsAdapterTrue;
import com.example.ratenews.R;
import com.example.ratenews.crud.api.get.api.ApiUtils;
import com.example.ratenews.crud.api.get.api.GETAll;
import com.example.ratenews.model.AllNewsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrueFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrueFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static List<AllNewsModel> trueNewsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NewsAdapterTrue adapter;
    private ProgressBar progressBar;
    public static boolean voted;
    private String mFeedTitle;
    private String mFeedLink;
    private String mFeedDescription;
    private String mContent;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;

    public TrueFragment() {

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
    public static TrueFragment newInstance(String param1, String param2) {
        TrueFragment fragment = new TrueFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_true, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view_true);
        progressBar = rootView.findViewById(R.id.progressBar);
        final SwipeRefreshLayout pullToRefresh = rootView.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressBar.setVisibility(View.VISIBLE);
                trueNewsList.clear();
                adapter.notifyDataSetChanged();
                new TrueFragment.FetchFeedTask().execute((Void) null);
                pullToRefresh.setRefreshing(false);

            }
        });

        new TrueFragment.FetchFeedTask().execute((Void) null);
        //Log.e("iscitano", "PAPER -> "+trueNewsList.get(5).getDescription());
        //Log.e("iscitano2", "TITLE -> "+trueNewsList.get(0).getTitle());

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
                GETAll getApiService;
                getApiService = ApiUtils.getAPIServiceFetchAll();
                getApiService.getAllNews().enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                //Log.i("onSuccess", response.body());

                                String jsonresponse = response.body();
                                try {
                                    //getting the whole json object from the response
                                    JSONObject obj = new JSONObject(response.body());
                                    JSONArray dataArray  = obj.getJSONArray("data");

                                    for (int i = 0; i < dataArray.length(); i++) {
                                        String title, desc, link, date, img, paper;
                                        final Integer id, positive, negative;

                                        JSONObject dataobj = dataArray.getJSONObject(i);
                                        id = dataobj.getInt("id");
                                        title = dataobj.getString("title");
                                        desc = dataobj.getString("description");
                                        link = dataobj.getString("link");
                                        date = dataobj.getString("date");
                                        date = parseDate(date);
                                        img = dataobj.getString("image");
                                        positive = dataobj.getInt("positive_votes");
                                        negative = dataobj.getInt("negative_votes");
                                        paper = dataobj.getString("paper");


                                            //DateFormat format = new SimpleDateFormat ("dd.MM.yyyy. HH:mm");
                                        //date1 = format.parse(result);
                                        //Log.d("date1", date);


                                        AllNewsModel item = new AllNewsModel(id, title, desc, link,
                                                date, img, positive, negative, paper,
                                                false, false);

//
//                                AllNewsModel item = new AllNewsModel(id[0], title[0],
//                                        desc[0], link[0],
//                                        date[0], img[0],
//                                        positive[0], negative[0], paper[0]
//                                );

//                                item[0].setId(dataobj.getInt("id"));
//                                item[0].setTitle(dataobj.getString("title"));
//                                item[0].setDescription(dataobj.getString("description"));
//                                item[0].setLink(dataobj.getString("link"));
//                                item[0].setDate(dataobj.getString("date"));
//                                item[0].setImage(dataobj.getString("image"));
//                                item[0].setPositiveVotes(dataobj.getInt("positive_votes"));
//                                item[0].setNegativeVotes(dataobj.getInt("negative_votes"));
//                                item[0].setPaper(dataobj.getString("paper"));
                                        //Log.e("iscitano", "TITLE -> "+item.getTitle());
                                        //Log.e("iscitano", "DESC -> "+item.getDescription());
                                        //Log.e("iscitano", "PAPER -> "+item.getPaper());
                                        trueNewsList.add(item);
                                        //Log.e("iscitano", "TITLE -> "+ trueNewsList.get(i).getTitle());
                                    }

                                    for (Iterator<AllNewsModel> iterator = trueNewsList.iterator(); iterator.hasNext(); ) {
                                        AllNewsModel news = iterator.next();
                                        if (news.getNegativeVotes()>news.getPositiveVotes()) {
                                            iterator.remove();
                                        }
                                    }

                                    adapter = new NewsAdapterTrue(trueNewsList, getActivity());
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                    recyclerView.setLayoutManager(mLayoutManager);
                                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                                    recyclerView.setAdapter(adapter);
                                    progressBar.setVisibility(View.GONE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            } else {
                                Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
                //trueNewsList = getResult();
//                AllPositive ap = new AllPositive();
//            ap.setTitle("VECERI I NOCI");
//            ap.setDate("21.03.2019.");
//            ap.setDescription("Posveceno Ognjenu i Nadji");
//            ap.setLink("https://ivanzarkovi.com");
//            ap.setImage("img");
//            ap.setPaper("Bljuc");
//            ap.setNegativeVotes(1);
//            ap.setPositiveVotes(5000);
//            ap.setId(122);
//            trueNewsList.add(ap);
                //Log.e("iscitano", "PAPER -> "+trueNewsList.get(5).getDescription());
                return true;
            } catch (Exception e) {
                Log.e("sada1", "Error", e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {

            if (success) {
//                adapter = new NewsAdapterTrue(trueNewsList, getActivity());
//                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//                recyclerView.setLayoutManager(mLayoutManager);
//                recyclerView.setItemAnimator(new DefaultItemAnimator());
//                recyclerView.setAdapter(adapter);
            } else {
//            Toast.makeText(getContext(),
//                    "Enter a valid Rss feed url",
//                    Toast.LENGTH_LONG).show();
            }
        }

        private String parseDate(String date){

            try {
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date1 = null;
                date1 = formatter.parse(date);SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy. HH:mm", Locale.US);
                date = format.format(date1);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date;
        }

    }
//    private void getResult(){
//
//        //final List<AllNewsModel> items = new ArrayList<>();
//       // final AllNewsModel[] item = {new AllNewsModel()};
//        GETAll getApiService;
//        getApiService = ApiUtils.getAPIServiceFetchAll();
//        getApiService.getAllNews().enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if (response.isSuccessful()) {
//                    if (response.body() != null) {
//                        Log.i("onSuccess", response.body());
//
//                        String jsonresponse = response.body();
//                        try {
//                            //getting the whole json object from the response
//                            JSONObject obj = new JSONObject(response.body());
//                            JSONArray dataArray  = obj.getJSONArray("data");
//
//                            for (int i = 0; i < dataArray.length(); i++) {
//                                String title, desc, link, date, img, paper;
//                                final Integer id, positive, negative;
//
//                                JSONObject dataobj = dataArray.getJSONObject(i);
//
//                                AllNewsModel item = new AllNewsModel(dataobj.getInt("id"), dataobj.getString("title"),
//                                        dataobj.getString("description"), dataobj.getString("link"),
//                                        dataobj.getString("date"), dataobj.getString("image"),
//                                        dataobj.getInt("positive_votes"), dataobj.getInt("negative_votes"),
//                                        dataobj.getString("paper"));
//
//                                id = dataobj.getInt("id");
//                                title = dataobj.getString("title");
//                                desc = dataobj.getString("description");
//                                link = dataobj.getString("link");
//                                date = dataobj.getString("date");
//                                img = dataobj.getString("image");
//                                positive = dataobj.getInt("positive_votes");
//                                negative = dataobj.getInt("negative_votes");
//                                paper = dataobj.getString("paper");
////
////                                AllNewsModel item = new AllNewsModel(id[0], title[0],
////                                        desc[0], link[0],
////                                        date[0], img[0],
////                                        positive[0], negative[0], paper[0]
////                                );
//
////                                item[0].setId(dataobj.getInt("id"));
////                                item[0].setTitle(dataobj.getString("title"));
////                                item[0].setDescription(dataobj.getString("description"));
////                                item[0].setLink(dataobj.getString("link"));
////                                item[0].setDate(dataobj.getString("date"));
////                                item[0].setImage(dataobj.getString("image"));
////                                item[0].setPositiveVotes(dataobj.getInt("positive_votes"));
////                                item[0].setNegativeVotes(dataobj.getInt("negative_votes"));
////                                item[0].setPaper(dataobj.getString("paper"));
//                                //Log.e("iscitano", "TITLE -> "+item.getTitle());
//                                //Log.e("iscitano", "DESC -> "+item.getDescription());
//                                //Log.e("iscitano", "PAPER -> "+item.getPaper());
//                                trueNewsList.add(item);
//                                Log.e("iscitano1", "TITLE -> "+trueNewsList.get(0).getTitle());
//                            }
//
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    } else {
//                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//
//            }
//        });
//    }
}


//import android.content.Context;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.util.Log;
//import android.util.Xml;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.example.ratenews.adapters.NewsAdapterBlic;
//import com.example.ratenews.adapters.NewsAdapterTrue;
//import com.example.ratenews.R;
//import com.example.ratenews.model.RssFeedModel;
//import com.example.ratenews.crud.api.get.api.ApiUtils;
//import com.example.ratenews.crud.api.get.api.GETAll;
//import AllPositive;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.xmlpull.v1.XmlPullParser;
//import org.xmlpull.v1.XmlPullParserException;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URL;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link TrueFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link TrueFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class TrueFragment extends Fragment {
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    List<AllPositive> retroModel = new ArrayList<>();
//    NewsAdapterTrue adapter;
//
//
//    private RecyclerView recyclerView;
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    private OnFragmentInteractionListener mListener;
//
//    public TrueFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment TrueFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static TrueFragment newInstance(String param1, String param2) {
//        TrueFragment fragment = new TrueFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View rootView = inflater.inflate(R.layout.fragment_true, container, false);
//        recyclerView = rootView.findViewById(R.id.recycler_view_true);
//        new TrueFragment.FetchFeedTask().execute((Void) null);
//        return rootView;
//
//    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
//
//    public class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {
//
//        private String urlLink;
//
//        @Override
//        protected void onPreExecute() {
//            //urlLink = "https://www.blic.rs/rss/Vesti";
//            Log.i("true", "STARTEEEEEEEEEEEED!!!");
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... voids) {
////            if(getResult()!=null)
////                return true;
//            AllPositive ap = new AllPositive();
//            ap.setTitle("VECERI I NOCI");
//            ap.setDate("21.03.2019.");
//            ap.setDescription("Posveceno Ognjenu i Nadji");
//            ap.setLink("https://ivanzarkovi.com");
//            ap.setImage("img");
//            ap.setPaper("Bljuc");
//            ap.setNegativeVotes(1);
//            ap.setPositiveVotes(5000);
//            ap.setId(122);
//            retroModel.add(ap);
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean success) {
//
//            if (success) {
//                adapter = new NewsAdapterTrue(retroModel, getActivity());
//
//                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//                recyclerView.setLayoutManager(mLayoutManager);
//                recyclerView.setItemAnimator(new DefaultItemAnimator());
//                recyclerView.setAdapter(adapter);
//            } else {
////            Toast.makeText(this,
////                    "Enter a valid Rss feed url",
////                    Toast.LENGTH_LONG).show();
//            }
//        }
//
//
//
//    }
//
//    private List<AllPositive> getResult(){
//        GETAll getApiService;
//        getApiService = ApiUtils.getAPIServiceFetchAll();
//        getApiService.getAllNews().enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if (response.isSuccessful()) {
//                    if (response.body() != null) {
//                        Log.i("onSuccess", response.body());
//
//                        String jsonresponse = response.body();
//                        retroModel = writeJSONToModel(jsonresponse);
//
//                    } else {
//                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//
//            }
//        });
//        return retroModel;
//    }
//
//    private List<AllPositive> writeJSONToModel(String response){
//        ArrayList<AllPositive> retroModelArrayList = new ArrayList<>();
//        try {
//            //getting the whole json object from the response
//            JSONObject obj = new JSONObject(response);
//
//
//            JSONArray dataArray  = obj.getJSONArray("data");
//
//            for (int i = 0; i < dataArray.length(); i++) {
//
//
//                JSONObject dataobj = dataArray.getJSONObject(i);
//
//                AllPositive item = new AllPositive();
//
//                item.setId(dataobj.getInt("id"));
//                item.setTitle(dataobj.getString("title"));
//                item.setDescription(dataobj.getString("description"));
//                item.setLink(dataobj.getString("link"));
//                item.setDate(dataobj.getString("date"));
//                item.setImage(dataobj.getString("image"));
//                item.setPositiveVotes(dataobj.getInt("positive_votes"));
//                item.setNegativeVotes(dataobj.getInt("negative_votes"));
//                item.setPaper(dataobj.getString("paper"));
//                Log.e("iscitano",item.getTitle());
//
//                retroModelArrayList.add(item);
//
//            }
//
////            for (int j = 0; j < retroModelArrayList.size(); j++){
////                Log.e("iscitano",retroModelArrayList.get(j).getTitle());
////            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return retroModelArrayList;
//    }
//
//}
