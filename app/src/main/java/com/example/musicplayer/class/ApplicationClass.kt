package com.example.musicplayer.`class`

import android.Manifest
import android.app.Application

class ApplicationClass: Application() {

    override fun onCreate() {
        super.onCreate()

        val permissionClass = PermissionClass(this)
        val readPermission = Manifest.permission.READ_EXTERNAL_STORAGE
        val writePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        permissionClass.permissionFun(readPermission)
        permissionClass.permissionFun(writePermission)

    }
}