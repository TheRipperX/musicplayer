package com.example.musicplayer.`class`

import android.content.Context
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class PermissionClass(private val context: Context) {

    fun permissionFun(re: Any): Boolean{

        var result = false

        val dexterRead = object : PermissionListener {

            override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                result = true
            }

            override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                if (!response?.isPermanentlyDenied!!){
                    permissionFun(re.toString())
                }
                if (response.isPermanentlyDenied){
                    Toast.makeText(context,"is Permanently Denied...", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissin: PermissionRequest?, token: PermissionToken?) {
                token?.continuePermissionRequest()
                result = false
            }

        }

        Dexter.withContext(context).withPermission(re.toString()).withListener(dexterRead).check()

        return result
    }
}