package com.example.contactapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val PERMISSIONS_REQUEST_CODE = 100
    private lateinit var contactAdapter: ContactAdapter

    private val requiredPermissions = arrayOf(
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.CALL_PHONE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contactAdapter = ContactAdapter(::makePhoneCall)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewContacts)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = contactAdapter

        checkPermissions()
    }

    private fun checkPermissions() {
        var allGranted = true
        for (permission in requiredPermissions) {
            val result = ContextCompat.checkSelfPermission(this, permission)
            if (result != PackageManager.PERMISSION_GRANTED) {
                allGranted = false
                break
            }
        }

        if (allGranted) {
            loadContacts()
        } else {
            ActivityCompat.requestPermissions(this, requiredPermissions, PERMISSIONS_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            var allGranted = true
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false
                    break
                }
            }

            if (allGranted) {
                loadContacts()
            } else {
                Toast.makeText(this, "Требуются разрешения", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loadContacts() {
        val contacts = ArrayList<Contact>()

        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            ),
            null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        if (cursor != null) {
            val nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (cursor.moveToNext()) {
                val name = cursor.getString(nameIndex)
                val number = cursor.getString(numberIndex)
                contacts.add(Contact(name, number))
            }

            cursor.close()
        }

        contactAdapter.submitList(contacts)
    }

    private fun makePhoneCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phoneNumber")
        startActivity(intent)
    }
}