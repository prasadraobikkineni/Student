package com.studentparty.base;

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import com.studentparty.controller.listner.PermissionCallBack

import com.studentparty.controller.utils.SharePref

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


open class BaseActivity : AppCompatActivity() {
    private val REQUEST_PERMISSION = 1111
    private val NEEDED_PERMISSIONS = 2222
    var pCallback: PermissionCallBack? = null
    private val TAG = BaseActivity::class.java.simpleName
    var permissionsNeed: MutableList<String> = mutableListOf()
    private var permissionsRequired = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SharePref.init(this)

        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())

               // Toast.makeText(this,Base64.encodeToString(md.digest(), Base64.DEFAULT),Toast.LENGTH_LONG).show()
                Log.d("MY KEY HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT))

                System.out.println("MY KEY HASH:"+Base64.encodeToString(md.digest(), Base64.DEFAULT).toString())
            }
        } catch (e: PackageManager.NameNotFoundException) {

        } catch (e: NoSuchAlgorithmException) {

        }

    }


    fun requestPermissions(arrays: Array<String>, permissionCallback: PermissionCallBack) {
        permissionsNeed.clear()
        pCallback = permissionCallback
        for (permission in arrays) {
            if (ActivityCompat.checkSelfPermission(applicationContext, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeed.add(permission)
            }
        }
        if (permissionsNeed.size > 0) {
            Log.v("request", "permissions")
            reuestNeededPermission(permissionsNeed)
        } else {
            pCallback?.permissionGranted()
        }
    }

    fun AppCompatActivity.toast(msg: String) {
       // Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }
    private fun reuestNeededPermission(permissionsNeed: MutableList<String>) {

        ActivityCompat.requestPermissions(this@BaseActivity, permissionsNeed.toTypedArray(), NEEDED_PERMISSIONS)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        Log.v("resultss", "" + grantResults[0] + grantResults.toString())
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted, preview can be displayed
                pCallback?.permissionGranted()
            } else {
                pCallback?.permissionDenied()
            }
        } else if (requestCode == NEEDED_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pCallback?.permissionGranted()
            } else {
                pCallback?.permissionDenied()
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}
