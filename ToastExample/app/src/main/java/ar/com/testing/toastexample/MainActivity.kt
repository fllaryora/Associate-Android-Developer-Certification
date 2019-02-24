package ar.com.testing.toastexample

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.lang.ref.WeakReference

//https://www.dev2qa.com/android-custom-toast-example/
class MainActivity : AppCompatActivity() {

    val SHOW_TOAST_MESSAGE: Int = 1

    // This Handler is used to handle message from child thread.
    private class MyHandler(activity: MainActivity) : Handler() {
        private val mActivity: WeakReference<MainActivity> = WeakReference<MainActivity>(activity)

        override fun handleMessage(msg: Message) {
            val activity = mActivity.get()
            if (activity != null) {
                Log.e("UPITEEE","LECTOR")
                if(msg.what == activity.SHOW_TOAST_MESSAGE){
                    Toast.makeText(activity.applicationContext, "Default Toast from child",
                            Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val mHandler = MyHandler(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        showDefaultToast()
        showToastInLocation()
        showImageInToast()
        showFullyCustomToast()
        showChildThreadToast()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Show default Toast prompt.
    private fun showDefaultToast() {
        defaultButton.setOnClickListener(View.OnClickListener {
            Toast.makeText(applicationContext, "Default Toast Style", Toast.LENGTH_SHORT).show()
        })
    }

    // Show Toast prompt in a specified location.
    private fun showToastInLocation() {
        changeLocationButton.setOnClickListener(View.OnClickListener {
            val toast : Toast = Toast.makeText(applicationContext, "Custom Toast Location", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP, 100,100)
            toast.show()
        })
    }

    // Show images in Toast prompt.
    private fun showImageInToast() {
        imageButton.setOnClickListener(View.OnClickListener {
            val toast : Toast = Toast.makeText(applicationContext, "Custom Toast Location", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            val toastContentView : LinearLayout = toast.view as LinearLayout
            val imageView : ImageView = ImageView(applicationContext)
            imageView.setImageResource(R.drawable.snow)
            toastContentView.addView(imageView, 0);
            toast.show()
        })
    }

    // Fully customize Toast view content.
    private fun showFullyCustomToast() {
        layoutButton.setOnClickListener(View.OnClickListener {
            // Get the custom layout view.
            val toastView : View = layoutInflater.inflate(R.layout.activity_toast_custom_view, null)
            val toast : Toast = Toast(applicationContext)
            toast.view = toastView
            toast.duration = Toast.LENGTH_SHORT
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        })
    }

    // Fully customize Toast view content.
    private fun showChildThreadToast() {
        Log.e("UPITEEE","SETEADOR")
        childButton.setOnClickListener(View.OnClickListener {
            Log.e("UPITEEE","CLICKEADOR")
            // Create a child thread.
            val childThread:Thread = object : Thread() {
                override fun run() {
                    val message: Message = Message()
                    message.what = SHOW_TOAST_MESSAGE
                    Log.e("UPITEEE","UPITEEEE")
                    mHandler.sendMessage(message)
                }
            }
            Log.e("UPITEEE","STARTTTT")
            childThread.start()
        })
    }

}
