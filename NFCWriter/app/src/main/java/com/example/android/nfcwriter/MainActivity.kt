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

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.android.nfcwriter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: NfcWriterViewModel by viewModels()
    private var nfcAdapter: NfcAdapter? = null
    private lateinit var pendingIntent: PendingIntent
    private lateinit var writingTagFilers: Array<IntentFilter>
    private lateinit var techLists: Array<Array<String>>
    private var tag: Tag? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            Toast.makeText(this, "This Device does not support NFC", Toast.LENGTH_SHORT).show()
            finish()
        }
        readFromIntent(intent)

        pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0
        )

        val ndef = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        writingTagFilers = arrayOf(ndef)
        techLists = arrayOf(
            arrayOf(Ndef::class.java.name),
            arrayOf(NdefFormatable::class.java.name)
        )
    }

    private fun readFromIntent(intent: Intent) {
        if ((NfcAdapter.ACTION_TAG_DISCOVERED == intent.action) ||
            (NfcAdapter.ACTION_TECH_DISCOVERED == intent.action) ||
            (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action)
        ) {
            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
                val messages = rawMessages.map { it as NdefMessage }.toTypedArray()
                buildTagsView(messages)
            }
        }
    }

    private fun buildTagsView(messages: Array<NdefMessage>) {
        if (messages.isEmpty()) return

        binding.nfcContentTextview.text = if (viewModel.parseTag(messages[0])) {
            "NFC Content =\n$viewModel}"
        } else {
            "NFC Content Invalid"
        }
        displayProperties()
    }

    private fun displayProperties() {
        binding.daPackageName.setText(viewModel.packageName)
        binding.daDownloadLocation.setText(viewModel.downloadLocation)
    }

    override
    fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        intent?.let {
            readFromIntent(it)
            if (NfcAdapter.ACTION_NDEF_DISCOVERED == it.action) {
                tag = it.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, writingTagFilers, techLists)
        displayProperties()
    }
}
