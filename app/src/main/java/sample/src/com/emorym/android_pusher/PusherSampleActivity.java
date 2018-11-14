package com.emorym.android_pusher;

/*  Copyright (C) 2011 Emory Myers
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. 
 */

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

//import com.emorym.android_pusher.Pusher.Channel;
import com.pusher.client.PusherOptions;
import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;


public class PusherSampleActivity extends Activity
{
	private static final String PUSHER_APP_KEY = "";
	private static final String PUSHER_CHANNEL_1 = "test1";

	private Pusher mPusher;
	private Context mContext;
	private ToggleButton tgbtn;
	private EditText logedt;

	/** Called when the activity is first created. */
	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		mContext = this;
		setContentView( R.layout.main );

		// This Handler is going to deal with incoming messages
		PusherOptions options = new PusherOptions();
		options.setCluster("eu");
		mPusher = new Pusher( PUSHER_APP_KEY,options );
		// Setup some toggles to subscribe/unsubscribe from our 2 test channels
		tgbtn = (ToggleButton) findViewById( R.id.toggleButton1 );
		logedt = (EditText) findViewById(R.id.editText2);

		tgbtn.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
				if( tgbtn.isChecked() )
				{
					Channel channel1 = mPusher.subscribe( PUSHER_CHANNEL_1 );
					channel1.bind("my-event", new SubscriptionEventListener() {
						@Override
						public void onEvent(String channelName, String eventName, final String data) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LogMessage(data);
                                }
                            });
						}
					});
					mPusher.connect();
				}
				else
				{
					mPusher.unsubscribe( PUSHER_CHANNEL_1 );
				}
			}
		} );
	}

	private void LogMessage(CharSequence msg)
	{
		logedt.append(msg);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		mPusher.disconnect();
	}

}