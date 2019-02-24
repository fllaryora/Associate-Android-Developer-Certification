/*
 *
 *   Copyright (C) 2018 Google Inc.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.android.example.notifyme;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * MainActivity for the Notify Me! app. Contains three buttons that deliver,
 * update, and cancel notification.
 */
public class MainActivity extends AppCompatActivity {

    // Constants for the notification actions buttons.
    // Constant for the intent, customize the intent.
    private static final String ACTION_UPDATE_NOTIFICATION =
            "com.android.example.notifyme.ACTION_UPDATE_NOTIFICATION";

    // Notification channel ID.
    //Every notification channel must be associated with an ID
    // that is unique within your package.
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";

    // Notification ID.
    // You need to associate the notification with a notification ID
    // so that your code can update or cancel the notification in the future.
    private static final int NOTIFICATION_ID = 0;

    private Button button_notify;
    private Button button_cancel;
    private Button button_update;

    private NotificationManager mNotifyManager;

    private NotificationReceiver mReceiver = new NotificationReceiver();

    /**
     * Initializes the activity.
     *
     * @param savedInstanceState The current state data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Step 1 and 1A
        // Get mNotifyManager instance
        // & if device is Oreo: Create the notification channel
        //    & attach the channel in mNotifyManager.
        createNotificationChannel();

        // Register the broadcast receiver to receive the update action from
        // the notification.
        registerReceiver(mReceiver,
                new IntentFilter(ACTION_UPDATE_NOTIFICATION));

        // Add onClick handlers to all the buttons.
        button_notify = findViewById(R.id.notify);
        button_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Send the notification
                sendNotification();
            }
        });

        button_update = (Button) findViewById(R.id.update);
        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update the notification.
                updateNotification();
            }
        });

        button_cancel = (Button) findViewById(R.id.cancel);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cancel the notification.
                cancelNotification();
            }
        });

        // Reset the button states. Enable only Notify button and disable
        // update and cancel buttons.
        setNotificationButtonState(true, false, false);
    }

    /**
     * Unregisters the receiver when the app is being destroyed.
     */
    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    /**
     * Creates a Notification channel, for OREO and higher.
     */
    public void createNotificationChannel() {

        // Step 1 - Create a notification manager object.
        mNotifyManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >=
                                    android.os.Build.VERSION_CODES.O) {

            // Step 1 A - Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            getString(R.string.notification_channel_name),
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription
                    (getString(R.string.notification_channel_description));

            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    /**
     * OnClick method for the "Notify Me!" button.
     * Creates and delivers a simple notification.
     */
    //From button_notify
    public void sendNotification() {

        // Sets up the pending intent to update the notification.
        // Corresponds to a press of the Update Me! button.
        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);

        /**
         * A PendingIntent is a token that you give to a foreign application
         (e.g. NotificationManager, AlarmManager, Home Screen AppWidgetManager,
         or other 3rd party applications), which allows the foreign application
         to use your application's permissions to execute a predefined piece of code.

         If you give the foreign application an Intent,
         it will execute your Intent with its own permissions.
         But if you give the foreign application a PendingIntent,
         that application will execute your Intent using your application's permission.*/
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast(this,
                NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT);

        // Build the notification with all of the parameters using helper
        // method.
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();

        //Step 3B PendingIntent (when user click the button actions in the notification
        // like reply, archive, and so on)
        // Add the action button using the pending intent.
        notifyBuilder.addAction(R.drawable.ic_update,
                            getString(R.string.update), updatePendingIntent);

        //Step 5
        // Deliver the notification.
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());

        // Enable the update and cancel buttons but disables the "Notify
        // Me!" button.
        setNotificationButtonState(false, true, true);
    }

    /**
     * Helper method that builds the notification.
     *
     * @return NotificationCompat.Builder: notification build with all the
     * parameters.
     */
    private NotificationCompat.Builder getNotificationBuilder() {

        //Step 2 Intent
        // Set up the pending intent that is delivered when the notification is clicked.
        // There gonna be some MainActivitys in the stack.
        Intent notificationIntent = new Intent(this, MainActivity.class);
        //Step 3 PendingIntent (when user click the notification, not its buttons)
        PendingIntent notificationPendingIntent = PendingIntent.getActivity
                (this, NOTIFICATION_ID, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        //Step 4 NotificationBuilder
        // Build the notification with all of the parameters.
        NotificationCompat.Builder notifyBuilder = new NotificationCompat
                .Builder(this, PRIMARY_CHANNEL_ID) //OREO Builder call
                .setSmallIcon(R.drawable.ic_android) // 1 -Small icon
                //2 -App name: system write it
                //3 Time stamp: system write it, but you can override it- setWhen(xxx)/ setShowWhen(false)
                /** long sendTime = System.currentTimeMillis();
                 * builder.setWhen(sendTime);
                 */
                //4: Large icon: This is optional
                /** BitmapDrawable bitmapDrawable = (BitmapDrawable)getDrawable(largeIconResId);
                 Bitmap largeIconBitmap = bitmapDrawable.getBitmap();
                 builder.setLargeIcon(largeIconBitmap);
                 */
                .setContentTitle(getString(R.string.notification_title)) //5 -Title
                .setContentText(getString(R.string.notification_text)) //6- Context
                /** Autocancel param will make it so the notification is automatically
                 * canceled when the user clicks it in the panel.  The PendingIntent
                 * set with {@link #setDeleteIntent} will be broadcast when the notification
                 * is canceled.
                 */
                .setAutoCancel(true).setContentIntent(notificationPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)//compatibility call
                /**
                 * builder.setSound(Uri.parse(“file:///sdcard/abc.mp3”))
                 */
                // Use both light, sound and vibrate.
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        return notifyBuilder;
    }

    /**
     * OnClick method for the "Update Me!" button. Updates the existing
     * notification to show a picture.
     */
    // from broadcast or from button_update
    public void updateNotification() {

        // Load the drawable resource into the a bitmap image.
        Bitmap androidImage = BitmapFactory
                .decodeResource(getResources(), R.drawable.mascot_1);

        // Build the notification with all of the parameters using helper method.
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();

        //Step 6
        // Update the notification style to BigPictureStyle.
        notifyBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                .bigPicture(androidImage)
                .setBigContentTitle(getString(R.string.notification_updated)));

        // Deliver the notification.
        //Step 7 update
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());

        // Disable the update button, leaving only the cancel button enabled.
        setNotificationButtonState(false, false, true);
    }

    /**
     * OnClick method for the "Cancel Me!" button. Cancels the notification.
     */
    public void cancelNotification() {
        //Step 3 Delete pre existent notification
        // Cancel the notification.
        mNotifyManager.cancel(NOTIFICATION_ID);

        // Reset the buttons.
        setNotificationButtonState(true, false, false);
    }

    /**
     * Helper method to enable/disable the buttons.
     *
     * @param isNotifyEnabled, boolean: true if notify button enabled
     * @param isUpdateEnabled, boolean: true if update button enabled
     * @param isCancelEnabled, boolean: true if cancel button enabled
     */
    void setNotificationButtonState(Boolean isNotifyEnabled, Boolean
                                    isUpdateEnabled, Boolean isCancelEnabled) {
        button_notify.setEnabled(isNotifyEnabled);
        button_update.setEnabled(isUpdateEnabled);
        button_cancel.setEnabled(isCancelEnabled);
    }

    /**
     * The broadcast receiver class for notifications.
     * Responds to the update notification pending intent action.
     */
    //Note: BroadcastReceiver is registered in code and not in manifest.
    //It was registed to process the custom intent ACTION_UPDATE_NOTIFICATION
    // Called by the notification button action and
    public class NotificationReceiver extends BroadcastReceiver {

        public NotificationReceiver() {
        }

        /**
         * Receives the incoming broadcasts and responds accordingly.
         *
         * @param context Context of the app when the broadcast is received.
         * @param intent The broadcast intent containing the action.
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            // Update the notification.
            updateNotification();
        }
    }
}
