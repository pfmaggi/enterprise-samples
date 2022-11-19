> **Note**  
> This is not an official Google sample
>
> The Google's sample this is based on has been deprecated and
> removed because Android Enterprise is no longer accepting new
> registrations for custom device policy controllers (DPCs). [Learn more][0].

----

# Android DeviceOwner Sample

This sample demonstrates how to use some device owner features. As a device owner, you can configure
global settings such as automatic time and timezone. You can mandate a specific launcher by
preferred intent handler.

## Introduction

In order to set global settings, use [DevicePolicyManager#setGlobalSetting][1] and specify one of the [Settings.Global][2] keys available. Note that you need to specify its value as a String. As most of the keys accept boolean values, you will mostly use "1" for true and "0" for false.

You can mandate a specific launcher by adding a persistent preferred activity for an IntentFilter with Intent.CATEGORY_HOME category. Call [DevicePolicyManager#addPersistentPreferredActivity][3] to register the activity. You can clear the registration with [clearPackagePersistentPreferredActivities][4].

As a device owner, you can also use the features available for managed profile owner. See
[BasicManagedProfile][5].

[0]: https://developer.android.com/work/dpc/build-dpc
[1]: https://developer.android.com/reference/android/app/admin/DevicePolicyManager#setGlobalSetting(android.content.ComponentName,%20java.lang.String,%20java.lang.String)
[2]: http://developer.android.com/reference/android/provider/Settings.Global.html
[3]: https://developer.android.com/reference/android/app/admin/DevicePolicyManager#addPersistentPreferredActivity(android.content.ComponentName,%20android.content.IntentFilter,%20android.content.ComponentName)
[4]: https://developer.android.com/reference/android/app/admin/DevicePolicyManager.html#clearPackagePersistentPreferredActivities(android.content.ComponentName,%20java.lang.String)
[5]: https://github.com/android/enterprise-samples/tree/main/BasicManagedProfile

## Pre-requisites

- Android SDK 33
- Android Studio Dolphin | 2021.3.1 Patch 1

## Testing

Once the application is installed on a device without a user, you can use `adb` to set the application as the device owner with the command:

```bash
adb shell dpm set-device-owner com.example.android.deviceowner/.DeviceOwnerReceiver
```

To remove the current admin app at the end of the tests you can use:

```bash
adb shell dpm remove-active-admin com.example.android.deviceowner/.DeviceOwnerReceiver
```

## Screenshots

<img src="screenshots/1-main.png" height="400" alt="Screenshot"/>

## Getting Started

This sample uses the Gradle build system. To build this project, use the `gradlew build` command or use "Import Project" in Android Studio.

## Support

- Stack Overflow: <http://stackoverflow.com/questions/tagged/android>

If you've found an error in this sample, please file an issue:
<https://github.com/pfmaggi/enterprise-samples/issues>

Patches are encouraged, and may be submitted by forking this project and submitting a pull request through GitHub. Please see CONTRIBUTING.md for more details.
