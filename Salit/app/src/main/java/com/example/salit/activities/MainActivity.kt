package com.example.salit.activities

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import android.widget.Toast
import com.example.salit.CredentialsManager
import com.example.salit.R
import com.example.salit.RequestCode
import com.example.salit.db.AppDatabase
import com.example.salit.db.models.User
import com.example.salit.fragments.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var currentLoadedFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setToolbar()
        loadUserDataOrSendToLoginActivity()
    }

    private fun startHomeView() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = HomeFragment()
        fragmentTransaction.add(R.id.contentFrameLayout, fragment)
        fragmentTransaction.commit()
    }

    private fun loadUserDataOrSendToLoginActivity(){
        val userData = loadUserData()
        if (userData != null) {
            initializeHomeFragment()
        } else{
            goToLoginActivity()
        }
    }

    private fun loadUserData(): Pair<String, String>? {
        return CredentialsManager.getInstance(baseContext).loadUser()
    }

    private fun initializeHomeFragment(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.contentFrameLayout, HomeFragment(), "homeFrag")
        transaction.commit()
        navView.menu.getItem(0).isChecked = true
        supportActionBar!!.title = "Home"
    }

    fun goToHomeFragment() {
        Toast.makeText(this, "Sale saved successfully", Toast.LENGTH_SHORT).show()
        val transaction = supportFragmentManager.beginTransaction()
        val homeFragment = supportFragmentManager.findFragmentByTag("homeFrag")
        if (homeFragment != null) {
            transaction.replace(R.id.contentFrameLayout, homeFragment)
        } else {
            transaction.replace(R.id.contentFrameLayout, HomeFragment(), "homeFrag")
        }
        supportActionBar!!.title = getString(R.string.action_bar_home_title)
        transaction.commit()
    }

    private fun goToLoginActivity(){
        if (currentLoadedFragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.remove(currentLoadedFragment!!).commit()
        }

        startActivityForResult(Intent(this, LoginActivity::class.java), RequestCode.GO_TO_LOGIN_FROM_MAIN_ACTIVITY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            RequestCode.GO_TO_LOGIN_FROM_MAIN_ACTIVITY -> {
                if (resultCode == Activity.RESULT_OK){
                    if (data != null){
                        storeUserCredentials(data.extras!!)
                        initializeHomeFragment()
                    }
                }
            }
        }
    }

    private fun storeUserCredentials(bundle: Bundle){
        val userEmail = bundle.getString("EMAIL")!!
        val userPassword = bundle.getString("PASSWORD")!!
        CredentialsManager.getInstance(baseContext).saveUser(userEmail, userPassword)
        GlobalScope.launch(Dispatchers.IO) {
            val user : User? = AppDatabase.getDatabase(baseContext).UserDao().getUser(userEmail)
            if (user == null){
                AppDatabase.getDatabase(baseContext).UserDao().insertAll(User(userEmail))
            }
        }

    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px)
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
                    supportActionBar!!.title = "Create physical sale"
                }

                R.id.createOnlineSale -> {
                    val createOnlineSaleFragment = supportFragmentManager.findFragmentByTag("createOnlineSaleFrag")
                    if (createOnlineSaleFragment != null) {
                        transaction.replace(R.id.contentFrameLayout, createOnlineSaleFragment)
                    } else {
                        transaction.replace(R.id.contentFrameLayout, CreateOnlineSaleFragment(), "createOnlineSaleFrag")
                    }
                    supportActionBar!!.title = "Create online sale"
                }

                R.id.searchSale -> {
                    val searchSaleFragment = supportFragmentManager.findFragmentByTag("searchSaleFrag")
                    if (searchSaleFragment != null) {
                        transaction.replace(R.id.contentFrameLayout, searchSaleFragment)
                    } else {
                        transaction.replace(R.id.contentFrameLayout, SearchSaleFragment(), "searchSaleFrag")
                    }
                    supportActionBar!!.title = "Search"
                }

                R.id.deleteSale -> {
                    val deleteSaleFragment = supportFragmentManager.findFragmentByTag("deleteSaleFrag")
                    if (deleteSaleFragment != null) {
                        transaction.replace(R.id.contentFrameLayout, deleteSaleFragment)
                    } else {
                        transaction.replace(R.id.contentFrameLayout, DeleteSaleFragment(), "deleteSaleFrag")
                    }
                    supportActionBar!!.title = "My sales"
                }

                R.id.signOut -> {
                    onSignOut()
                }
            }

            transaction.commit()
            true
        }
    }

    private fun onSignOut() {
        CredentialsManager.getInstance(baseContext).deleteUser()
        goToLoginActivity()
    }
}
