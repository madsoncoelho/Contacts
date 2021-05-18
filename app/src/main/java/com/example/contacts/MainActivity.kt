package com.example.contacts

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.edit
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contacts.ContactDetails.Companion.EXTRA_CONTACT
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity(), ClickItemContactListener {

    private val rvList: RecyclerView by lazy {
        findViewById<RecyclerView>(R.id.rvList)
    }
    private val adapter = ContactAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_menu)

        initDrawer()
        fetchContactList()
        bindViews()
    }

    private fun fetchContactList() {
        val list = arrayListOf(
            Contact(
                "Madson Coelho",
                "(99) 99999-9999",
                ""
            ),
            Contact(
                "Igor Ferrani",
                "(99) 99999-9999",
                ""
            )
        )

        getInstanceSharedPreferences().edit().putString("contacts", Gson().toJson(list)).commit()
    }

    private fun getInstanceSharedPreferences(): SharedPreferences {
        return getSharedPreferences("com.example.contacts.PREFERENCES", Context.MODE_PRIVATE)
    }

    private fun initDrawer() {
        val drawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
            R.string.open_drawer, R.string.close_drawer)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun bindViews() {
        rvList.adapter = adapter
        rvList.layoutManager = LinearLayoutManager(this)
        updateList()
    }

    private fun updateList() {
        adapter.updateList(getContactList())
    }

    private fun getContactList(): List<Contact> {
        val list = getInstanceSharedPreferences().getString("contacts", "[]")
        val turnsType = object : TypeToken<List<Contact>>() {}.type
        return Gson().fromJson(list, turnsType)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.firstOption -> {
                showToast("Número de contatos: ${rvList.adapter?.itemCount}")
                true
            }
            R.id.secondOption -> {
                showToast("Contacts 1.0")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun clickItemContact(contact: Contact) {
        val intent = Intent(this, ContactDetails::class.java)
        intent.putExtra(EXTRA_CONTACT, contact)
        startActivity(intent)
    }
}