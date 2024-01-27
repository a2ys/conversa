package dev.a2ys.conversa.main.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.a2ys.conversa.Manifest
import com.a2ys.conversa.R
import com.a2ys.conversa.databinding.ActivityMainBinding
import dev.a2ys.conversa.utils.Constants

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_navigation)
        val navController = navHostFragment!!.findNavController()
        binding.bottomNavigation.setupWithNavController(navController)

        val name = "Default Communications"
        val descriptionText = "This is the default channel."
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val mChannel = NotificationChannel(Constants.CHANNEL_ID, name, importance)
        mChannel.description = descriptionText

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)

        val builder = NotificationCompat.Builder(this, Constants.CHANNEL_ID)
            .setContentTitle("Hello")
            .setContentText("This is my first notification")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with (NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), PERMISSION_REQUEST_CODE)
            }
            notify(1, builder.build())
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    with(NotificationManagerCompat.from(this)) {
                        notify(NOTIFICATION_ID, builder.build())
                    }
                } else {
                    // Permission not granted, handle accordingly (e.g., show a message to the user)
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}