package com.example.stutee.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.PopularMovieAdapterViewHolder>{

    private static final String TAG = MovieAdapter.class.getSimpleName();
    private String[] mPopularMoviePath;

    private String[] mPopularMovieDetail;


    private Context mContext;

    private MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler{
        void onClick(String movieDetails);
    }


    public MovieAdapter(Context context, MovieAdapterOnClickHandler clickHandler){
        mContext = context;
        mClickHandler = clickHandler;

    }



    public class PopularMovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mMoviePoster;

        public PopularMovieAdapterViewHolder(View view) {
            super(view);
            mMoviePoster = (ImageView) view.findViewById(R.id.tv_movie_poster);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String informationOfMovieDetails = mPopularMovieDetail[adapterPosition];
            mClickHandler.onClick(informationOfMovieDetails);

        }


    }

    @Override
    public PopularMovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new PopularMovieAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(PopularMovieAdapterViewHolder popularMovieAdapterViewHolder, int position) {
        String currentPosterPath = mPopularMoviePath[position];
        Log.v(TAG, "poster path: " + currentPosterPath);
        Picasso.with(mContext).load(currentPosterPath).into(popularMovieAdapterViewHolder.mMoviePoster);
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     */
    @Override
    public int getItemCount() {
        if (null == mPopularMoviePath) return 0;
        return mPopularMoviePath.length;
    }


    public void setPopularMoviePath(String[] popularMoviePath) {

        mPopularMoviePath = popularMoviePath;

    }

    public void setPopularMovieDetails(String[] popularMovieDetail) {

        mPopularMovieDetail = popularMovieDetail;

    }



}