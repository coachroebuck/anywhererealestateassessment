package com.sample.simpsonsviewer.api.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log

class DefaultAppNetworkManager : AppNetworkManager {
    override fun networkSpeed(context: Context?) {
        (context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.let { cm ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                cm.getNetworkCapabilities(cm.activeNetwork)?.let { capabilities ->
                    val downSpeed = capabilities.linkDownstreamBandwidthKbps
                    val upSpeed = capabilities.linkUpstreamBandwidthKbps
                    Log.i("NETWORK:", "downSpeed=[$downSpeed] upSpeed=[$upSpeed]")
                }
            } else {
                try {
                    val nc = cm.getNetworkCapabilities(cm.activeNetwork)
                    val downSpeed = nc!!.linkDownstreamBandwidthKbps
                    val upSpeed = nc.linkUpstreamBandwidthKbps
                    Log.i("NETWORK:", "downSpeed=[$downSpeed] upSpeed=[$upSpeed]")
                } catch (e: Exception) {
                    Log.i("NETWORK:", "Network speed error=[${e.message}]")
                }
            }
        }
    }

    override fun isConnected(context: Context?): Boolean =
        (context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.let { cm ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                cm.getNetworkCapabilities(cm.activeNetwork)?.let { capabilities ->
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        Log.i("NETWORK:", "TRANSPORT_CELLULAR capability...")
                        true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        Log.i("NETWORK:", "TRANSPORT_WIFI capability...")
                        true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        Log.i("NETWORK:", "TRANSPORT_ETHERNET capability...")
                        true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)) {
                        Log.i("NETWORK:", "TRANSPORT_BLUETOOTH capability...")
                        true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN)) {
                        Log.i("NETWORK:", "TRANSPORT_LOWPAN capability...")
                        true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_USB)) {
                        Log.i("NETWORK:", "TRANSPORT_USB capability...")
                        true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                        Log.i("NETWORK:", "TRANSPORT_VPN capability...")
                        true
                    } else {
                        Log.i("NETWORK:", "No type of internet capability...")
                        false
                    }
                } ?: false
            } else {
                try {
                    val activeNetworkInfo = cm.activeNetworkInfo
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                        val networkType = when (activeNetworkInfo.type) {
                            ConnectivityManager.TYPE_WIFI -> "WIFI"
                            ConnectivityManager.TYPE_MOBILE -> {
                                when (activeNetworkInfo.subtype) {
                                    TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN, TelephonyManager.NETWORK_TYPE_GSM -> "2G"
                                    TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP, TelephonyManager.NETWORK_TYPE_TD_SCDMA -> "3G"
                                    TelephonyManager.NETWORK_TYPE_LTE, TelephonyManager.NETWORK_TYPE_IWLAN, 19 -> "4G"
                                    else -> "?"
                                }
                            }
                            else -> {
                                "??"
                            }
                        }
                        Log.i("NETWORK:", "Network is available: networkType=[$networkType]")
                        true
                    } else {
                        Log.i("NETWORK:", "Network is NOT available")
                        false
                    }
                } catch (e: Exception) {
                    Log.i("NETWORK:", "Network connectivity error=[${e.message}]")
                    false
                }
            }
        } ?: false

    override fun isNetworkAvailable(
        context: Context,
        networkCapability: Int?,
    ): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if(networkCapability != null) {
                return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            } else {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        }
        return false
    }
}
