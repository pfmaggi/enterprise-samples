/*
 * Copyright 2022 The Android Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.example.android.nfcwriter

import android.nfc.NdefMessage
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import java.io.ByteArrayInputStream
import java.util.Properties

private const val TAG = "NfcWriterViewModel"
const val NFC_WRITER_PACKAGE_NAME_KEY = "NFC_WRITER_PACKAGE_NAME_KEY"
const val NFC_WRITER_DOWNLOAD_LOCATION_KEY = "NFC_WRITER_DOWNLOAD_LOCATION_KEY"

class NfcWriterViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    val packageName: String
        get() = daPackageName

    val downloadLocation: String
        get() = daDownloadLocation

    /* This EditText holds the KVP for EXTRA_PROVISIONING_DEVICE_ADMIN_PACKAGE_NAME */
    private var daPackageName: String
        get() = savedStateHandle[NFC_WRITER_PACKAGE_NAME_KEY] ?: ""
        set(value) = savedStateHandle.set(NFC_WRITER_PACKAGE_NAME_KEY, value)

    /* This EditText holds the KVP for EXTRA_PROVISIONING_DEVICE_ADMIN_PACKAGE_DOWNLOAD_LOCATION */
    private var daDownloadLocation: String
        get() = savedStateHandle[NFC_WRITER_DOWNLOAD_LOCATION_KEY] ?: ""
        set(value) = savedStateHandle.set(NFC_WRITER_DOWNLOAD_LOCATION_KEY, value)

    /* This EditText holds the KVP for EXTRA_PROVISIONING_DEVICE_ADMIN_PACKAGE_CHECKSUM (note: must be URL-safe)*/
    private var daPackageChecksum: String = ""

    /* This EditText holds the KVP for EXTRA_PROVISIONING_WIFI_SSID */
    private var daWifiSsid: String = ""
    private var daWifiSecurityType: String = "NONE"
    private var daWifiPassword: String = ""

    /* This EditText holds the KVP for EXTRA_PROVISIONING_TIME_ZONE*/
    private var daTimeZone: String = ""
    private var daLocalTime: String = ""

    /* This EditText holds the KVP for EXTRA_PROVISIONING_LOCALE */
    private var daLocale: String = ""

    fun parseTag(message: NdefMessage): Boolean {
        val record = message.records[0]

        if ((record.tnf != 2.toShort()) or (record.toMimeType() != "application/com.android.managedprovisioning")) return false

        val properties = Properties()
        properties.load(ByteArrayInputStream(record.payload))
        daPackageName = properties.getProperty("android.app.extra.PROVISIONING_DEVICE_ADMIN_PACKAGE_NAME", "")
        daDownloadLocation = properties.getProperty("android.app.extra.PROVISIONING_DEVICE_ADMIN_PACKAGE_DOWNLOAD_LOCATION", "")
        daPackageChecksum = properties.getProperty("android.app.extra.PROVISIONING_DEVICE_ADMIN_PACKAGE_CHECKSUM", "")
        daWifiSsid = properties.getProperty("android.app.extra.PROVISIONING_WIFI_SSID", "")
        daWifiSecurityType = properties.getProperty("android.app.extra.PROVISIONING_WIFI_SECURITY_TYPE", "NONE")
        daWifiPassword = properties.getProperty("android.app.extra.PROVISIONING_WIFI_PASSWORD", "")
        daTimeZone = properties.getProperty("android.app.extra.PROVISIONING_TIME_ZONE", "")
        daLocalTime = properties.getProperty("android.app.extra.PROVISIONING_LOCAL_TIME", "")
        daLocale = properties.getProperty("android.app.extra.PROVISIONING_LOCALE", "")

        Log.d(TAG, "Read TAG: $this")

        return true
    }

    override
    fun toString(): String {
        val sb = StringBuilder()
        sb.append("Package Name: $daPackageName\n")
        sb.append("Download Location: $daDownloadLocation\n")
        sb.append("Package Checksum: $daPackageChecksum\n")
        sb.append("WiFi SSID: $daWifiSsid\n")
        sb.append("WiFi Security Type: $daWifiSecurityType\n")
        sb.append("WiFi Password: $daWifiPassword\n")
        sb.append("TimeZone: $daTimeZone\n")
        sb.append("LocalTime: $daLocalTime\n")
        sb.append("Locale: $daLocale\n")
        return sb.toString()
    }
}
