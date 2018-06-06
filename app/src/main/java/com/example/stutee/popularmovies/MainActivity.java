package com.example.stutee.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler{

    private static final String TAG = MainActivity.class.getSimpleName();

    private Context context = this;

    private String queryParameter;

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    String[] movieOriginalTitle;
    String[] overviewOfMovie;
    String[] rating;
    String[] releaseDate;
    String[] completeInfo;
    String[] parsedMoviePosterPath = null;

    private String[] moviePosterPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_popular_movies);

        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        /*
         * LinearLayoutManager can support HORIZONTAL or VERTICAL orientations. The reverse layout
         * parameter is useful mostly for HORIZONTAL layouts that should reverse for right to left
         * languages.
         */
        GridLayoutManager layoutManager
                = new GridLayoutManager(this,2);

        mRecyclerView.setLayoutManager(layoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mRecyclerView.setHasFixedSize(true);


        mMovieAdapter = new MovieAdapter(context,this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mMovieAdapter);

        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         *
         * Please note: This so called "ProgressBar" isn't a bar by default. It is more of a
         * circle. We didn't make the rules (or the names of Views), we just follow them.
         */
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);


        loadMovieData();
    }

    @Override
    public void onClick(String details) {


        //Bundle extras = new Bundle();

        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        //extras.putString("PosterPathUrl",poster);
        //extras.putString("MovieDetails",details);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT,details);
        startActivity(intentToStartDetailActivity);

    }

    private void loadMovieData() {
        queryParameter = "http://api.themoviedb.org/3/movie/popular?api_key=YOUR_KEY";
        new FetchMovieDataTask().execute(queryParameter);
    }

    private void showPopularMovieDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the weather
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class FetchMovieDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            /* If there's no zip code, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }

            String query = params[0];
            URL popularMovieRequestUrl = NetworkUtils.buildUrl(query);

            try {
                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(popularMovieRequestUrl);


                return jsonMovieResponse;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String popularMoviePath) {

            if (popularMoviePath == null){
                showErrorMessage();
            mLoadingIndicator.setVisibility(View.INVISIBLE);}
            else {
                final String SHOW_RESULTS = "results";
                final String RATING = "vote_average";

                final String POSTER_PATH = "poster_path";
                final String ORIGINAL_TITLE = "original_title";

                final String OVERVIEW_OF_MOVIE = "overview";
                final String RELEASE_DATE = "release_date";


                /* String array to hold each day's weather String */
                try {

                    JSONObject popularMoviesJson = new JSONObject(popularMoviePath);

                    JSONArray jsonArray = popularMoviesJson.getJSONArray(SHOW_RESULTS);


                    parsedMoviePosterPath = new String[jsonArray.length()];
                    movieOriginalTitle = new String[jsonArray.length()];
                    overviewOfMovie = new String[jsonArray.length()];
                    rating = new String[jsonArray.length()];
                    releaseDate = new String[jsonArray.length()];
                    completeInfo = new String[jsonArray.length()];

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String ratingString = jsonObject.getString(RATING);
                        String posterPathString = jsonObject.getString(POSTER_PATH);
                        String originalTitleString = jsonObject.getString(ORIGINAL_TITLE);
                        String overviewString = jsonObject.getString(OVERVIEW_OF_MOVIE);
                        String releaseDateString = jsonObject.getString(RELEASE_DATE);

                        parsedMoviePosterPath[i] = "http://image.tmdb.org/t/p/w342/" + posterPathString;
                        movieOriginalTitle[i] = originalTitleString;
                        overviewOfMovie[i] = overviewString;
                        rating[i] = ratingString;
                        releaseDate[i] = releaseDateString;


                        completeInfo[i] = "http://image.tmdb.org/t/p/w342/" + posterPathString + "\n\n" + getString(R.string.action_title)+"  "+originalTitleString + "\n\n" + getString(R.string.action_overview)+"  "+overviewString + "\n\n" + getString(R.string.action_rating)+"  "+ratingString + "\n\n" + getString(R.string.action_date)+"  "+releaseDateString;


                    }
                } catch (JSONException j) {
                    j.getStackTrace();
                }

                mMovieAdapter.setPopularMovieDetails(completeInfo);

                mLoadingIndicator.setVisibility(View.INVISIBLE);

                showPopularMovieDataView();

                mMovieAdapter.setPopularMoviePath(parsedMoviePosterPath);


            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemThatWasClicked = item.getItemId();
        if(itemThatWasClicked == R.id.action_refresh){
            mMovieAdapter.setPopularMoviePath(null);
            loadMovieData();
            return true;
        }

        if(itemThatWasClicked == R.id.action_sort_by_popular){

            String queryURL = "http://api.themoviedb.org/3/movie/popular?api_key=YOUR_KEY";
            new FetchMovieDataTask().execute(queryURL);
            return true;
        }

        if(itemThatWasClicked == R.id.action_sort_by_top_rated){

            String queryURL = "http://api.themoviedb.org/3/movie/top_rated?api_key=YOUR_KEY";
            new FetchMovieDataTask().execute(queryURL);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
