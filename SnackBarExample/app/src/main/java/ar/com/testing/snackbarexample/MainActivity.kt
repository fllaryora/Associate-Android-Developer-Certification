package ar.com.testing.snackbarexample

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BaseTransientBottomBar
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView

import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

//https://www.dev2qa.com/android-snackbar-example/
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        this.showSnackbar()

        this.showCustomSnackbar()

        this.showCustomViewSnackbar()

        this.showSnackbarHere()
    }

    // A standard usage of android Snackbar.
    private fun showSnackbar() {
        standarSnackBarButton.setOnClickListener(View.OnClickListener { view :View ->

            // Create a Snackbar instance, the message is displayed at the left side.
            val snackbar : Snackbar = Snackbar.make(view, "A short message is comming!", Snackbar.LENGTH_LONG)

            // Set button with text Open at right side..
            snackbar.setAction("Open", View.OnClickListener() { _: View ->
                // When user click Open button, an AlertDialog will be displayed.
                val alertDialog: AlertDialog = AlertDialog.Builder(this).create()
                alertDialog.setMessage("Hello, welcome to use snackbar.")
                alertDialog.show()
            })

            // Set the Callback function.
            snackbar.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                override fun onShown(transientBottomBar: Snackbar?) {
                    super.onShown(transientBottomBar)
                    Toast.makeText(applicationContext, "Snackbar has been shown.", Toast.LENGTH_SHORT).show()
                }

                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    Toast.makeText(applicationContext, "Snackbar is dismessed.", Toast.LENGTH_SHORT).show()
                }
            })

            // Show it at screen bottom.
            snackbar.show()
        })
    }

    // Custom Snackbar.
    private fun showCustomSnackbar() {
        customSnackBarButton.setOnClickListener(View.OnClickListener { view :View ->

            // Create a Snackbar instance, the message is displayed at the left side.
            val snackbar : Snackbar = Snackbar.make(view, "A short message is comming!", Snackbar.LENGTH_LONG)

            // Set button with text Open at right side..
            snackbar.setAction("Open", View.OnClickListener() { _: View ->
                // When user click Open button, an AlertDialog will be displayed.
                val alertDialog: AlertDialog = AlertDialog.Builder(this).create()
                alertDialog.setMessage("Hello, welcome to use snackbar.")
                alertDialog.show()
            })

            // Set action text color.
            snackbar.setActionTextColor(Color.RED);

            // Get Snackbar view.
            val snackbarView: Snackbar.SnackbarLayout = snackbar.view as Snackbar.SnackbarLayout

            // Set background color.
            snackbarView.setBackgroundColor(Color.BLUE)

            // Set message text color.
            val messageTextView: TextView = snackbarView.findViewById(R.id.snackbar_text)
            messageTextView.setTextColor(Color.GREEN)
            messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)

            snackbar.show()

            // Show it at screen bottom.
            snackbar.show()
        })
    }

    // Custom Snackbar view content from a layout xml file.
    private fun showCustomViewSnackbar() {
        customLayoutButton.setOnClickListener(View.OnClickListener { view :View ->

            // Create a Snackbar instance, the message is displayed at the left side.
            val snackbar : Snackbar = Snackbar.make(view, "A short message is comming!", Snackbar.LENGTH_LONG)

            // Set button with text Open at right side..
            snackbar.setAction("Open", View.OnClickListener() { _: View ->
                // When user click Open button, an AlertDialog will be displayed.
                val alertDialog: AlertDialog = AlertDialog.Builder(this).create()
                alertDialog.setMessage("Hello, welcome to use snackbar.")
                alertDialog.show()
            })

            // Get Snackbar view.
            val snackbarView: Snackbar.SnackbarLayout = snackbar.view as Snackbar.SnackbarLayout

            // Get custom view from external layout xml file.
            val customView : View = layoutInflater.inflate(R.layout.activity_snack_bar_custom_view, null)
            snackbarView.addView(customView, 0)

            // Show it at screen bottom.
            snackbar.show()
        })
    }

    // Open a snackbar near the source widget..
    private fun showSnackbarHere() {
        // The source widget need to be wrapped in a CoordinatorLayout xml element in layout xml file.
        snackBarHereButton.setOnClickListener(View.OnClickListener { view ->
            // Create an instance.
            //the tric is the  view is wrapped by coordinator or frame.
            val snackbar = Snackbar.make(view, "Snack bar is shown near source widget", Snackbar.LENGTH_LONG)
            snackbar.setAction("Show") {
                val alertDialog = AlertDialog.Builder(this).create()
                alertDialog.setMessage("dev2qa.com --- Learn java, android easy and simple.")
                alertDialog.show()
            }

            snackbar.show()
        })
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
}
