import android.Manifest
import android.app.AppOpsManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.getlife_screentimemonitoring.R

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ScreenTimeViewModel

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Memeriksa dan meminta izin jika belum diberikan
        if (checkUsageStatsPermission()) {
            initializeApp()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private fun checkUsageStatsPermission(): Boolean {
        val appOps = getSystemService(APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(), packageName
        )

        return if (mode != AppOpsManager.MODE_ALLOWED) {
            // Izin belum diberikan, minta izin kepada pengguna
            requestUsageStatsPermission()
            false
        } else {
            // Izin sudah diberikan
            true
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private fun requestUsageStatsPermission() {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        startActivity(intent)
    }



    private fun initializeApp() {
        // Inisialisasi aplikasi setelah mendapatkan izin
        viewModel = ViewModelProvider(this).get(ScreenTimeViewModel::class.java)

        val screenTimeTextView: TextView = findViewById(R.id.screenTimeTextView)
        viewModel.getScreenTime().observe(this, { screenTime ->
            screenTimeTextView.text = "Screen Time: $screenTime"
        })
    }
}
