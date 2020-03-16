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

import com.example.ratenews.adapters.NewsAdapterLie;
import com.example.ratenews.R;
import com.example.ratenews.crud.api.get.api.ApiUtils;
import com.example.ratenews.crud.api.get.api.GetAllLies;
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
 * {@link LieFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LieFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static List<AllNewsModel> newsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NewsAdapterLie adapter;
    private ProgressBar progressBar;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public LieFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LieFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LieFragment newInstance(String param1, String param2) {
        LieFragment fragment = new LieFragment();
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
        View view = inflater.inflate(R.layout.fragment_lie, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_lie);
        progressBar = view.findViewById(R.id.progressBar);
        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressBar.setVisibility(View.VISIBLE);
                newsList.clear();
                adapter.notifyDataSetChanged();
                new LieFragment.FetchFeedTask().execute((Void) null);
                pullToRefresh.setRefreshing(false);

            }
        });

        new LieFragment.FetchFeedTask().execute((Void) null);

        // Inflate the layout for this fragment
        return view;
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

            try {GetAllLies getApiService;
                getApiService = ApiUtils.getAPIServiceFetchAllLies();
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
                                        newsList.add(item);
                                        //Log.e("iscitano3", "TITLE -> "+ newsList.get(i).getTitle());
                                    }

                                    for (Iterator<AllNewsModel> iterator = newsList.iterator(); iterator.hasNext(); ) {
                                        AllNewsModel news = iterator.next();
                                        if (news.getNegativeVotes()<news.getPositiveVotes()) {
                                            iterator.remove();
                                        }
                                    }

                                    adapter = new NewsAdapterLie(newsList, getActivity());
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

}
