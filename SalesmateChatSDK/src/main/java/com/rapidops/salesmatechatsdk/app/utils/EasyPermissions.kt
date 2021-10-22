package com.rapidops.salesmatechatsdk.app.utils


import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.rapidops.salesmatechatsdk.R
import java.util.*

object EasyPermissions {

    private val TAG = "EasyPermissions"
    internal var timeWhenRequestingStart: Long = 0
    internal lateinit var anyActivityFragment: Any
    private var finalPermissionDialog: AlertDialog? = null
    private var callbacks: PermissionCallbacks? = null
    private var permissionGroups: HashMap<String, Array<String>>? = null


    fun hasPermissions(context: Context?, vararg perms: String): Boolean {
        // Always return true for SDK < M, let the system deal with the permissions
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.w(TAG, "hasPermissions: API version < M, returning true by default")
            return true
        }

        for (perm in perms) {
            val hasPerm = ContextCompat.checkSelfPermission(
                context!!,
                perm
            ) == PackageManager.PERMISSION_GRANTED
            if (!hasPerm) {
                return false
            }
        }

        return true
    }

    fun requestPermissions(
        anyActivityFragment: Any, rationale: String,
        requestCode: Int, callback: PermissionCallbacks, vararg perms: String
    ) {
        requestPermissions(
            anyActivityFragment, rationale,
            R.string.dialog_ok,
            R.string.dialog_cancel,
            requestCode, callback, *perms
        )
    }

    fun requestPermissions(
        anyActivityFragment: Any,
        requestCode: Int, callback: PermissionCallbacks, vararg perms: String
    ) {
        requestPermissions(
            anyActivityFragment, "",
            R.string.dialog_ok,
            R.string.dialog_cancel,
            requestCode, callback, *perms
        )
    }

    fun requestPermissions(
        obj: Any, rationale: String,
        @StringRes positiveButton: Int,
        @StringRes negativeButton: Int,
        requestCode: Int, callback: PermissionCallbacks, vararg permission: String
    ) {

        callbacks = callback
        anyActivityFragment = obj
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            callbacks!!.onPermissionsGranted(requestCode, ArrayList(Arrays.asList(*permission)))
            return
        }


        checkCallingObjectSuitability(anyActivityFragment)
        val perms = getActualPermissions(
            anyActivityFragment,
            permission
        )

        if (perms.size <= 0) {
            callbacks!!.onPermissionsGranted(requestCode, ArrayList(Arrays.asList(*permission)))
            return
        }

        var shouldShowRationale = false
        for (perm in perms) {
            shouldShowRationale = shouldShowRationale || shouldShowRequestPermissionRationale(
                anyActivityFragment, perm
            )
        }

        if (shouldShowRationale) {
            if (!TextUtils.isEmpty(rationale)) {
                Log.i(TAG, "shouldShowRationale: ")

                val builder = AlertDialog.Builder(getActivity(anyActivityFragment)!!)
                    .setMessage(rationale)
                    .setTitle("Permission necessary")
                    .setPositiveButton(positiveButton) { dialog, which ->
                        executePermissionsRequest(anyActivityFragment, perms, requestCode)
                        finalPermissionDialog!!.dismiss()
                    }
                    .setNegativeButton(
                        negativeButton
                    ) { dialog, which ->
                        callbacks!!.onPermissionsDenied(requestCode, Arrays.asList(*perms))
                        finalPermissionDialog!!.dismiss()
                    }

                finalPermissionDialog = builder.create()
                finalPermissionDialog!!.show()
            } else {
                executePermissionsRequest(anyActivityFragment, perms, requestCode)
            }
        } else {
            for (perm in perms) {
                shouldShowRationale = shouldShowRationale || shouldShowRequestPermissionRationale(
                    anyActivityFragment, perm
                )
            }
            if (shouldShowRationale) {
                Log.d(TAG, "requestPermissions: show dialog")

            } else {
                timeWhenRequestingStart = System.currentTimeMillis()
                executePermissionsRequest(anyActivityFragment, perms, requestCode)
            }

        }
    }


    private fun getActualPermissions(
        anyActivityFragment: Any,
        permission: Array<out String>
    ): Array<String> {
        initPermissionGroups()
        val permissionList = ArrayList<String>()
        for (indiPerm in permission) {
            if (permissionGroups!!.containsKey(indiPerm)) {
                val arr = permissionGroups!![indiPerm]
                for (s in arr!!) {
                    if (!hasPermissions(getActivity(anyActivityFragment), s)) {
                        permissionList.add(s)
                    }
                }
            } else {
                if (!hasPermissions(getActivity(anyActivityFragment), indiPerm)) {
                    permissionList.add(indiPerm)
                }
            }
        }
        val set = LinkedHashSet(permissionList)

        return set.toTypedArray()
    }

    private fun initPermissionGroups() {
        if (permissionGroups == null) {
            permissionGroups = HashMap()
            permissionGroups!![Manifest.permission_group.CALENDAR] =
                arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)
            permissionGroups!![Manifest.permission_group.CAMERA] =
                arrayOf(Manifest.permission.CAMERA)
            permissionGroups!![Manifest.permission_group.CONTACTS] =
                arrayOf(
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS,
                    Manifest.permission.GET_ACCOUNTS
                )
            permissionGroups!![Manifest.permission_group.LOCATION] =
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            permissionGroups!![Manifest.permission_group.MICROPHONE] =
                arrayOf(Manifest.permission.RECORD_AUDIO)
            permissionGroups!![Manifest.permission_group.PHONE] = arrayOf(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.WRITE_CALL_LOG,
                Manifest.permission.ADD_VOICEMAIL,
                Manifest.permission.USE_SIP,
                Manifest.permission.PROCESS_OUTGOING_CALLS
            )
            permissionGroups!![Manifest.permission_group.SENSORS] =
                arrayOf(Manifest.permission.BODY_SENSORS)
            permissionGroups!![Manifest.permission_group.SMS] = arrayOf(
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_WAP_PUSH,
                Manifest.permission.RECEIVE_MMS
            )
            permissionGroups!![Manifest.permission_group.STORAGE] =
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        var isPermenantlyDisabled = false
        val granted = ArrayList<String>()
        val denied = ArrayList<String>()
        for (i in permissions.indices) {
            val perm = permissions[i]
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                granted.add(perm)
            } else {
                if (EasyPermissions::anyActivityFragment.isInitialized) {
                    val showRationale =
                        shouldShowRequestPermissionRationale(anyActivityFragment, perm)
                    isPermenantlyDisabled = !showRationale
                    denied.add(perm)
                }
            }
        }

        if (!granted.isEmpty() && denied.isEmpty()) {
            callbacks!!.onPermissionsGranted(requestCode, granted)
        } else if (granted.isEmpty() && !denied.isEmpty() && isPermenantlyDisabled) {
            val diff = System.currentTimeMillis() - timeWhenRequestingStart

            callbacks!!.onPermissionsPermanentlyDeclined(requestCode, denied)
            Log.i("TAG", diff.toString() + "")
        }
        if (denied.isNotEmpty() && !isPermenantlyDisabled) {
            callbacks!!.onPermissionsDenied(requestCode, denied)
        }
    }

    @TargetApi(23)
    private fun shouldShowRequestPermissionRationale(
        anyActivityFragment: Any,
        perm: String
    ): Boolean {
        return if (anyActivityFragment is Activity) {
            ActivityCompat.shouldShowRequestPermissionRationale(anyActivityFragment, perm)
        } else (anyActivityFragment as? Fragment)?.shouldShowRequestPermissionRationale(perm)
            ?: false
    }

    @TargetApi(23)
    private fun executePermissionsRequest(
        anyActivityFragment: Any,
        perms: Array<String>,
        requestCode: Int
    ) {
        checkCallingObjectSuitability(anyActivityFragment)

        if (anyActivityFragment is Activity) {
            ActivityCompat.requestPermissions(anyActivityFragment, perms, requestCode)
        } else if (anyActivityFragment is Fragment) {
            anyActivityFragment.requestPermissions(perms, requestCode)
        }
    }

    @TargetApi(11)
    private fun getActivity(anyActivityFragment: Any): Activity? {
        return anyActivityFragment as? Activity ?: when (anyActivityFragment) {
            is Fragment -> {
                anyActivityFragment.activity
            }
            else -> {
                null
            }
        }
    }

    private fun checkCallingObjectSuitability(anyActivityFragment: Any) {
        val isActivity = anyActivityFragment is Activity
        val isSupportFragment = anyActivityFragment is Fragment
        val isAppFragment = anyActivityFragment is android.app.Fragment
        val isMinSdkM = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

        if (!(isSupportFragment || isActivity || isAppFragment && isMinSdkM)) {
            if (isAppFragment) {
                throw IllegalArgumentException(
                    "Target SDK needs to be greater than 23 if caller is android.app.Fragment"
                )
            } else {
                throw IllegalArgumentException("Caller must be an Activity or a Fragment.")
            }
        }
    }

    fun startSetting() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", getActivity(anyActivityFragment)!!.packageName, null)
        intent.data = uri
        getActivity(anyActivityFragment)!!.startActivity(intent)
    }

    interface PermissionCallbacks {

        fun onPermissionsGranted(requestCode: Int, perms: List<String>)

        fun onPermissionsDenied(requestCode: Int, perms: List<String>)

        fun onPermissionsPermanentlyDeclined(requestCode: Int, perms: List<String>)
    }

    /*private fun showSamplePermission() {
        EasyPermissions.requestPermissions(this, "Rational message", 102, object : EasyPermissions.PermissionCallbacks {
            override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
                Log.w("Permission:", "GRANTED");
            }

            override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
                Log.w("Permission:", "Denied");
            }

            override fun onPermissionsPermanentlyDeclined(requestCode: Int, perms: List<String>) {
                Log.w("Permission:", "Declined");
            }

        }, Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }*/

}
