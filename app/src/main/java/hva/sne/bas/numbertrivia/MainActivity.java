package hva.sne.bas.numbertrivia;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private final String BASE_URL = "http://numbersapi.com";

    private ProgressBar progressBar;
    private List<Trivia> trivias;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        trivias = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new TriviaAdapter(trivias);
        recyclerView.setAdapter(mAdapter);

        // Adds a dividerline between each item
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE); // Hide progressbar until it is needed

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadWebpageTask downloadWebpageTask = new DownloadWebpageTask();
                downloadWebpageTask.execute(new String[] {BASE_URL + "/random/trivia?json"});
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DownloadWebpageTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE); // Set progressbar to visible
            Snackbar.make(getCurrentFocus(), "Getting number trivia", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show(); // Inform the user the task is starting
        }

        @Override
        protected String doInBackground(String... params) {
            //resultText.setText("Downloading " + params[0]);
            OkHttpClient client = new OkHttpClient();
            Request request =
                    new Request.Builder()
                            .url(params[0])
                            .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                return "Download failed";
            }
            if (response.isSuccessful()) {
                try {
                    return response.body().string(); //Pass result to onPostExecute
                } catch (IOException e) {
                    return "Failed to read response";
                }
            }
            return "Download failed";
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // executed when publishProgress is called
            progressBar.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            // Called when doInBackground finishes
            //resultText.setText(result);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
                String number = jsonObject.getString("number");
                String text = jsonObject.getString("text");
                trivias.add(new Trivia(number, text));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        }
    }
}
