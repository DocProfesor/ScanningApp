package harper.scanningapp;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import ScanningIntegration.IntentIntegrator;
import ScanningIntegration.IntentResult;

//Custom StaffDB classes
import StaffDB.Comment;
import StaffDB.CommentsDataSource;
import StaffDB.MySQLiteHelper;
import StaffDB.Profile;

//Zxing include classes
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;


public class SecondActivity extends ListActivity implements View.OnClickListener, ListView.OnItemClickListener{

    //Create View instances
    private Button scanBtn;
    private TextView formatTxt, contentTxt, netStatusTxt;
    private EditText editName, editComp;

    private CommentsDataSource datasource;
    Bundle parameters = new Bundle();

    ArrayAdapter<Comment> adapter = (ArrayAdapter<Comment>) getListAdapter();
    Comment comment = null;

   //ListView listView = getListView();


    //Person profile declaration
    Profile person;
    String serverResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //Get parameters from the MainActivity call
        parameters = getIntent().getExtras();

        //Assign listener to button_message
        scanBtn= (Button) findViewById(R.id.scan_button);
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);
        netStatusTxt= (TextView)findViewById(R.id.net_status);
        editName= (EditText)findViewById(R.id.edit_name);
        editComp= (EditText)findViewById(R.id.edit_company);
        scanBtn.setOnClickListener(this);

        //Assign  DB context
        datasource = new CommentsDataSource(this);

        try {
            datasource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<Comment> values = datasource.getAllComments();

        //Show the elements in the list with SimpleCursorAdapter
        ArrayAdapter<Comment> adapter = new ArrayAdapter<Comment>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);

        //Check for connection
        if(isConnected()){
            netStatusTxt.setBackgroundColor(0xFF00CC00);
            netStatusTxt.setText("You are connected!");
        }
        else{
            netStatusTxt.setBackgroundColor(0xFFFF0000);
            netStatusTxt.setText("Connection not found.!");
        }

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

    public String POST(String url, Profile person){
        InputStream inputStream = null;
        String result= "";
        try {
            //HttpClient creation
            HttpClient httpclient = new DefaultHttpClient();

            //make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";


            //Json object
            JSONObject jsonObject = new JSONObject();
            jsonObject = datasource.dbToJson();

            //jsonObject.accumulate("name", person.getName());
            //jsonObject.accumulate("country", person.getCompany());
            //jsonObject.accumulate("twitter", "@hmkcode");

            //json 2 string
            json= jsonObject.toString();
            //httpPost entity
            StringEntity se= new StringEntity(json);

            //Set headers to inform server about the type of content
            httpPost.setHeader("Accept","application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            //Excecute post request to URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            //Receive response as inputStream
            inputStream= httpResponse.getEntity().getContent();

            //Stream 2 String
            if(inputStream!=null)
                result= convertInputStreamToString(inputStream);
            else
                result= "Did not work!!!";

        }catch(Exception e){
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        ArrayAdapter<Comment> adapter = (ArrayAdapter<Comment>) getListAdapter();
        Comment comment = null;

        if(v.getId()==R.id.scan_button){
            //Proceed with scanning
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);

            scanIntegrator.initiateScan();

            //String[] comments = new String[]{"Cool", "Very Nice", "Hate it"};
            //int nextInt = new Random().nextInt(3);

            //Save new comment into DB
            //comment = datasource.createComment(comments[nextInt]);
            //adapter.add(comment);
        }

        if(v.getId()==R.id.plus_button){
            String[] comments = new String[]{"Cool", "Very Nice", "Hate it"};
            int nextInt = new Random().nextInt(3);

            //Save new comment into DB
            comment = datasource.createComment(comments[nextInt]);
            adapter.add(comment);
        }

        if(v.getId()==R.id.del_button) {
            if (getListAdapter().getCount() > 0) {
                comment = (Comment) getListAdapter().getItem(0);
                datasource.deleteComment(comment);
                adapter.remove(comment);
            }
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //setContentView(R.layout.landscapeView);
            Log.e("On Config Change","LANDSCAPE");
            contentTxt.setText("Content: LANDSCAPE");

        } else {
            //setContentView(R.layout.portraitView);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long duration) {
        // Upon clicking item in list pop a toast
      //  String msg = "#Item: " + String.valueOf(position) + " - "
       //         + adapter.getItem(position);
        Toast.makeText(getApplicationContext(), "Hola", Toast.LENGTH_LONG).show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){

        if(intent != null) {
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

            if (scanningResult != null) {
                String scanContent = scanningResult.getContents();
                String scanFormat = scanningResult.getFormatName();
                //scanContent = Integer.toString(parameters.getInt("key"));

                comment = datasource.createComment(scanContent);

                //formatTxt.setText("Content: " + scanContent);
                //contentTxt.setText("Format: " + scanFormat);

                JSONObject jsonObject = new JSONObject();
                jsonObject = datasource.dbToJson();
                scanContent = jsonObject.toString();

                contentTxt.setText("Content: " + scanContent);
                formatTxt.setText("FormatX: " + datasource.tableCursor().getColumnNames().toString());

                //Post information
                if (!validate())
                    Toast.makeText(getBaseContext(), "Enter some data!", Toast.LENGTH_SHORT).show();
                //Call AsynTask to perform network operation on separate thread
                new HttpAsyncTask().execute("http://testserver-904.appspot.com/");


                //Get the same intent used for this Activity for resetting
                //Intent sameIntent = getIntent();
                //finish();
                //startActivity(sameIntent);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "No scan data receiver!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(ListActivity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            return true;
        }else{
            return false;
        }
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls){
            person= new Profile();
            person.setName(editName.getText().toString());
            person.setCompany(editComp.getText().toString());

            return POST(urls[0], person);
        }

        //onPostExecute displays the results of the AsyncTask
        @Override
        protected void onPostExecute(String result){
            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_SHORT).show();
            Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
            //serverResponse= result;
            //formatTxt.setText(serverResponse);
        }
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line= "";
        String result= "";
        while((line = bufferedReader.readLine()) != null)
            result +=line;
        return result;
    }

    //Checks that no Edi
    private boolean validate(){
        if(editName.getText().toString().trim().equals(""))
            return false;
        else if(editComp.getText().toString().trim().equals(""))
            return false;
        else
            return true;
    }

}
