package com.example.whereintheham


import android.Manifest
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationRequest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.example.whereintheham.ui.theme.WhereInTheHamTheme
import com.google.android.gms.location.FusedLocationProviderClient



private lateinit var fusedLocationClient: FusedLocationProviderClient



class MainActivity : ComponentActivity() {
    var nowLocation: Array<Float> = getLastKnownLocation()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WhereInTheHamTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                {
                    Layout(nowLocation[0], nowLocation[1], nowLocation[2])
                }
            }
        }
    }


    fun getLastKnownLocation(): Array<Float> {
        var lat: Float = (0).toFloat()
        var lon: Float = (0).toFloat()
        var alt: Float = (0).toFloat()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            try {
                return arrayOf(lat, lon, alt)
            } catch (e: Exception) {
                TODO("Not yet implemented")
            }
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    lat = (location.latitude).toFloat()
                    lon = (location.longitude).toFloat()
                    alt = (location.altitude).toFloat()

                }

            }
        return arrayOf(lat, lon, alt)
    }
}

@Composable
fun Layout(lat: Float, lon: Float, alt: Float) {
    val worldIcon = painterResource(R.drawable.world)
    val grid: String = calculateGrid(lat, lon)
    Column {
        Row {
            Image(
                painter = worldIcon,
                contentDescription = null
            )
            Text(
                text = "Where in the world am I",
                fontSize = 50.sp,
                lineHeight = 50.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Row {
            Text(
                text = "An app for expanded location information",
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = "Lat: $lat",
                textAlign = TextAlign.Left,

            )
            Text(
                text = "Lon: $lon",
                textAlign = TextAlign.Left,
            )
            Text(
                text = "Alt: $alt",
                textAlign = TextAlign.Left,
            )
            Text(
                text = "Grid: $grid",
                textAlign = TextAlign.Left,
            )
        }
    }
}

fun calculateGrid(lat: Float, lon: Float): String {
    val alphabet = arrayOf("A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z")
    val posLon: Float = lon + (180).toFloat()
    val posLat: Float = lat + (90).toFloat()
    //First Pair
    val lonLetter: String = alphabet[(posLon/20).toInt()]
    val latLetter: String = alphabet[(posLat/10).toInt()]
    val firstPair = "$lonLetter$latLetter"
    // Second Pair
    val lonNum: String = ((posLon - ((posLon/20).toInt() * 20))/2).toInt().toString()
    val latNum: String = ((posLat - ((posLat/10).toInt() * 10))/1).toInt().toString()
    val secondPair = "$lonNum$latNum"
    val grid  = "$firstPair$secondPair"
    //Third Pair

    return grid
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun UIPreview() {
    WhereInTheHamTheme {
        //Layout((37.3789).toFloat(), (-122.0517).toFloat(), (40.00).toFloat())
        Layout()
    }
}

