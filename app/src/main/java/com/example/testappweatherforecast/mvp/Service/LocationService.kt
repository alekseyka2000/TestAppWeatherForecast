import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task


class LocationService{


    fun getLocationRespond(context: Context) : Task<Location> {
        val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        var location: Task<Location> = mFusedLocationClient!!.lastLocation
        if(checkPermissions(context)){
            if (isLocationEnable(context)){
                //get current position
                  location = mFusedLocationClient.lastLocation
                        //Toast.makeText(context, (lat., Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(intent) }
        } else {
            requestPermissions(context)
        }
return location
    }

    //check if location has turned on from the setting
    fun isLocationEnable(context: Context) :Boolean {
        val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                ||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun checkPermissions(context: Context): Boolean{
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED)
            return true
       return false
    }

    fun requestPermissions(context: Context){
        ActivityCompat.requestPermissions(
            context as Activity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION), 200)
    }

    fun handlePermissionsResult(
        requestCode: Any,
        grantResults: IntArray,
        context: Context
    ) {

        if (requestCode == 200) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(context,"Check permission setting",Toast.LENGTH_LONG).show()
            }
        }
    }
}