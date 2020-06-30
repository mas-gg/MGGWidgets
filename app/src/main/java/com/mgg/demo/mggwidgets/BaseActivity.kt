package com.mgg.demo.mggwidgets

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

/**
 * created by mgg
 */
open class BaseActivity : AppCompatActivity() {

    val TAG = javaClass.name!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions()
        } else {
            onAllPermissionsGranted()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermissions() {
        if (mustPermissions().isNotEmpty())
            requestPermissions(mustPermissions(), 0)
    }

    open fun mustPermissions(): Array<String> {
        return emptyArray()
    }

    /**
     * 获取到了所有权限，开始干活
     */
    open fun onAllPermissionsGranted() {

    }

    /**
     * 某个权限被拒绝
     */
    open fun onPermissionDenied(permission: String) {
        Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show()
        finish()
    }

    /**
     * 某个权限被拒绝，且不再提醒
     */
    open fun onPermissionIgnore(permission: String) {
        Toast.makeText(this, "权限被拒绝，请手动获取", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissions.isNotEmpty()) {
            run breaking@{
                grantResults.forEachIndexed { index, it ->
                    if (it != PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !shouldShowRequestPermissionRationale(permissions[index])) {
                            onPermissionIgnore(permissions[index])
                        } else {
                            onPermissionDenied(permissions[index])
                        }
                        return@breaking
                    }
                }
            }
        }
        onAllPermissionsGranted()
    }
}