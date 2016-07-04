package com.esp.testvideo.backend;

public interface ResponseListener {
	// API Response Listener
	public void onResponce(String tag, int result, Object obj);
	public void onResponce(String tag, int result, Object obj, Object obj2);
	public void onResponce(String tag, int result, Object obj, Object obj2, Object obj3);
}
