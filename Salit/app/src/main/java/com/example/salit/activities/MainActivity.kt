package com.example.salit.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import com.example.salit.R
import com.example.salit.fragments.CreateSaleFragment
import com.example.salit.fragments.HomeFragment
import com.example.salit.fragments.SearchSaleFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var currentLoadedFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startHomeView()
        setToolbar()
    }

    private fun startHomeView() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = HomeFragment()
        fragmentTransaction.add(R.id.contentFrameLayout, fragment)
        fragmentTransaction.commit()
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
//            setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
        }
        setupNavViewListener()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupNavViewListener() {
        navView.setNavigationItemSelectedListener { menuItem ->
            // set item as selected to persist highlight
            menuItem.isChecked = true
            // close drawer when item is tapped
            drawerLayout.closeDrawers()

            // Add code here to update the UI based on the item selected
            // For example, swap UI fragments here
            val transaction = supportFragmentManager.beginTransaction()

            when (menuItem.itemId) {
                R.id.home -> {
                    val homeFragment = supportFragmentManager.findFragmentByTag("homeFrag")
                    if (homeFragment != null) {
                        transaction.replace(R.id.contentFrameLayout, homeFragment)
                    } else {
                        transaction.replace(R.id.contentFrameLayout, HomeFragment(), "homeFrag")
                    }
                    supportActionBar!!.title = getString(R.string.action_bar_home_title)
                }

                R.id.createSale -> {
                    val createSaleFragment = supportFragmentManager.findFragmentByTag("createSaleFrag")
                    if (createSaleFragment != null) {
                        transaction.replace(R.id.contentFrameLayout, createSaleFragment)
                    } else {
                        transaction.replace(R.id.contentFrameLayout, CreateSaleFragment(), "createSaleFrag")
                    }
                    supportActionBar!!.title = "Create Sale"
                }

                R.id.searchSale -> {
                    val searchSaleFragment = supportFragmentManager.findFragmentByTag("searchSaleFrag")
                    if (searchSaleFragment != null) {
                        transaction.replace(R.id.contentFrameLayout, searchSaleFragment)
                    } else {
                        transaction.replace(R.id.contentFrameLayout, SearchSaleFragment(), "searchSaleFrag")
                    }
                }
            }

            transaction.commit()
            true
        }
    }
}
