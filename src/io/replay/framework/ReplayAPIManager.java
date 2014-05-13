package io.replay.framework;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

public class ReplayAPIManager implements ReplayConfig {

	private String apiKey;
	private String clientUUID;
	private String sessionUUID;

	public ReplayAPIManager(String apiKey, String clientUUID, String sessionUUID) {
		this.apiKey = apiKey;
		this.clientUUID = clientUUID;
		this.sessionUUID = sessionUUID;
		Log.d("REPLAY_IO", "Tracking with { API Key: "+apiKey+", Client UUID: "+clientUUID+", Session UUID: "+sessionUUID);
	}
	
	public void updateSessionUUID(String sessionUUID) {
		this.sessionUUID = sessionUUID;
		Log.d("REPLAY_IO", "Session UUID: "+sessionUUID);
	}
	
	public Request<?> requestForEvent(String event, Map<String, String> data) {
		JsonObjectRequest request = new JsonObjectRequest(REPLAY_URL+"events", jsonForEvent(event, data),
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							VolleyLog.v("Response:%n %s", response.toString(4));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.e("Error: ", error.getMessage());
					}
				});
		//queue.add(request);
		return request;
	}
	
	public Request<?> requestForAlias(String alias) {
		JsonObjectRequest request = new JsonObjectRequest(REPLAY_URL+"alias", jsonForAlias(alias),
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							VolleyLog.v("Response:%n %s", response.toString(4));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.e("Error: ", error.getMessage());
					}
				});
		//queue.add(request);
		return request;
	}
	
	private JSONObject jsonForEvent(String event, Map<String, String> data) {
		JSONObject json = new JSONObject();
		try {
			json.put(KEY_REPLAY_KEY, apiKey);
			json.put(KEY_CLIENT_ID, clientUUID);
			json.put(KEY_SESSION_ID, sessionUUID);
			data.put("event", event);
			json.put(KEY_DATA, data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d("REPLAY_IO", json.toString());
		return json;
	}
	
	private JSONObject jsonForAlias(String alias) {
		JSONObject json = new JSONObject();
		try {
			json.put(KEY_REPLAY_KEY, apiKey);
			json.put(KEY_CLIENT_ID, clientUUID);
			json.put("alias", alias);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d("REPLAY_IO", json.toString());
		return json;
	}
}
