/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.managedconfigurations

import android.app.admin.DevicePolicyManager
import android.content.Context
import android.os.Build
import android.os.UserManager

/**
 * Return true if the application is running inside a WorkProfile
 */
fun Context.isRunningOnWorkProfile(): Boolean {
    val userManager = this.getSystemService(Context.USER_SERVICE) as UserManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        return userManager.isManagedProfile
    }
    if (userManager.userProfiles.size < 2) {
        // Accounts for situations where a personal profile has management.
        return false
    }
    val dpm = this.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    val packageManager = this.packageManager
    for (pkg in packageManager.getInstalledPackages( /* flags= */0)) {
        if (dpm.isProfileOwnerApp(pkg.packageName)) {
            return true
        }
    }
    return false
}
