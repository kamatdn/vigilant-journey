package InventoryManagement;

import java.io.IOException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;


public class SFConnector {
	
  static final String LOGINURL   = "TBD";
  static final String GRANTSERVICE = "TBD";
  static final String REST_ENDPOINT = "TBD";

  private static Header prettyPrintHeader = new BasicHeader("X-PrettyPrint", "1");
  private String username;
  private String password;
  private String cl_id;
  private String cl_secret;
  private String loginAccessToken;
  private Header oauthHeader;
  private String baseUri;

  public SFConnector(String username, String password, String cl_id, String cl_secret) {
	  this.username = username;
	  this.password = password;
	  this.cl_id = cl_id;
	  this.cl_secret = cl_secret;
  }
    
 public void updateInventory(String deviceid, String wt) {
  System.out.println("\n_______________ Inventory UPDATE _______________");

  //Notice, the id for the record to update is part of the URI, not part of the JSON
  String uri = baseUri + "?weight=" + wt + "&devicename=" + deviceid;
  System.out.println("URI:" + uri);
  HttpPatch httpPatch = new HttpPatch(uri);
    try {
      JSONObject lead = new JSONObject();
     
      HttpClient httpClient = HttpClientBuilder.create().build();
      httpPatch.addHeader(oauthHeader);
      //httpPatch.addHeader(prettyPrintHeader);
      //StringEntity body = new StringEntity(lead.toString(1));
      //body.setContentType("application/json");
      //httpPatch.setEntity(body);

      //Make the request
      HttpResponse response = httpClient.execute(httpPatch);

      //Process the response
      int statusCode = response.getStatusLine().getStatusCode();
      if (statusCode == 204 || statusCode == 200) {
        System.out.println("Updated the inventory successfully.");
		
      } else {
        System.out.println("Inventory did not update successfully. Status code is " + statusCode);
      }
	  System.out.println("Response:" + EntityUtils.toString(response.getEntity()));
	  
    } catch (JSONException e) {
      System.out.println("Issue creating JSON or processing results");
      e.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    } catch (NullPointerException npe) {
      npe.printStackTrace();
    }
	
	httpPatch.releaseConnection();
	 
  }
  
  public void login() {

    HttpClient httpclient = HttpClientBuilder.create().build();

    // Assemble the login request URL
    String loginURL = LOGINURL +
             GRANTSERVICE +
             "&client_id=" + cl_id +
             "&client_secret=" + cl_secret +
             "&username=" + username +
             "&password=" + password;
    
	System.out.println("*****Login URL: " + loginURL);
	
  
    // Login requests must be POSTs
    HttpPost httpPost = new HttpPost(loginURL);
    HttpResponse response = null;

    try {
      // Execute the login POST request
      response = httpclient.execute(httpPost);
    } catch (ClientProtocolException cpException) {
      cpException.printStackTrace();
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }

    // verify response is HTTP OK
    final int statusCode = response.getStatusLine().getStatusCode();
    if (statusCode != HttpStatus.SC_OK) {
      System.out.println("Error authenticating to Force.com: "+statusCode);
      //Error is in EntityUtils.toString(response.getEntity())
      return;
    }

    String getResult = null;
    try {
      getResult = EntityUtils.toString(response.getEntity());
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
    JSONObject jsonObject = null;
    String loginAccessToken = null;
    String loginInstanceUrl = null;
    try {
      jsonObject = (JSONObject) new JSONTokener(getResult).nextValue();
      loginAccessToken = jsonObject.getString("access_token");
      loginInstanceUrl = jsonObject.getString("instance_url");
	  baseUri = loginInstanceUrl + REST_ENDPOINT;
      oauthHeader = new BasicHeader("Authorization", "OAuth " + loginAccessToken) ;
    } catch (JSONException jsonException) {
      jsonException.printStackTrace();
    }
    System.out.println(response.getStatusLine());
    System.out.println("Successful login");
    System.out.println(" instance URL: "+loginInstanceUrl);
    System.out.println(" access token/session ID: "+loginAccessToken);

    // release connection
    httpPost.releaseConnection();
  }
  
  
}