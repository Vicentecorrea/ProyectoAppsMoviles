package com.example.salit.fragments


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v4.content.PermissionChecker
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.salit.CredentialsManager
import com.example.salit.Constants
import com.example.salit.RequestCode
import com.example.salit.activities.MainActivity
import com.example.salit.db.AppDatabase
import com.example.salit.db.models.Category
import com.example.salit.db.models.Sale
import com.example.salit.location.CustomLocationListener
import kotlinx.android.synthetic.main.fragment_create_online_sale.*
import kotlinx.android.synthetic.main.fragment_create_sale.*
import kotlinx.android.synthetic.main.fragment_create_sale.createSaleButton
import kotlinx.android.synthetic.main.fragment_create_sale.normalPriceInput
import kotlinx.android.synthetic.main.fragment_create_sale.offerPriceInput
import kotlinx.android.synthetic.main.fragment_create_sale.saleDescriptionEditText
import kotlinx.android.synthetic.main.fragment_create_sale.saleNameEditText
import kotlinx.android.synthetic.main.fragment_create_sale.spinnerCategories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class CreateSaleFragment : Fragment() {

    private var category = 1
    private var currentPhotoPath: String? = null
    private lateinit var locationManager: LocationManager
    private val customLocationListener = CustomLocationListener()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.example.salit.R.layout.fragment_create_sale, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSpinnerCategories()
        addSpinnerCategoryListener()
        setTakePhotoButtonListener()
        askForGPSAccess()
        createSaleButton.setOnClickListener {
            createSale()
        }
    }

    private fun setTakePhotoButtonListener() {
        takePhotoImageButton.setOnClickListener {
            if (hasCamera()) {
                dispatchToCameraActivity()
            } else {
                Toast.makeText(context!!, "Your device doesn't have a camera built in", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun hasCamera(): Boolean {
        return context!!.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

    private fun dispatchToCameraActivity() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(context!!.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Log.d("ERROR", "Error creating file: ${ex.message}")
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        context!!,
                        "com.example.android.photoProvider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, RequestCode.REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun addSpinnerCategoryListener() {
        spinnerCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val categoryName = spinnerCategories.selectedItem.toString()
                GlobalScope.launch(Dispatchers.IO) {
                    val categoryDao = AppDatabase.getDatabase(context!!).CategoryDao()
                    category = categoryDao.getCategoryId(categoryName)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun setSpinnerCategories() {

        GlobalScope.launch(Dispatchers.IO) {
            val categoryDao = AppDatabase.getDatabase(context!!).CategoryDao()
            val categories = mutableListOf<String>()
            val categoryObjects = categoryDao.getAll()
            launch(Dispatchers.Main) {

                for (category in categoryObjects) {
                    categories.add(category.name!!)
                }
                val spinnerArray = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, categories)
                spinnerArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerCategories!!.adapter = spinnerArray
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCode.REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            setPic()
        }
    }

    private fun setPic() {
        // Get the dimensions of the View
        takePhotoImageButton.setPadding(0, 0, 0, 0)
        val targetW: Int = takePhotoImageButton.width
        val targetH: Int = takePhotoImageButton.height

        val bmOptions = BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(currentPhotoPath, this)
            val photoW: Int = outWidth
            val photoH: Int = outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int = Math.min(photoW / targetW, photoH / targetH)

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
        }
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions)?.also { bitmap ->
            takePhotoImageButton.setImageBitmap(bitmap)
        }
    }

    private fun createSale() {
        val saleObject = createSaleObject()
        if (saleObject.name != "") {
            storeSaleObject(saleObject)
        }
    }

    private fun storeSaleObject(saleObject: Sale) {
        val saleDao = AppDatabase.getDatabase(context!!).SaleDao()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                saleDao.insertAll(saleObject)
                launch(Dispatchers.Main) {
                    (activity as MainActivity).goToHomeFragment()
                }
            } catch (e: Exception) {
                launch(Dispatchers.Main) {
                    Toast.makeText(context, "Error creating sale ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    private fun createSaleObject(): Sale {
        val isOnline = false
        val currentTime = Calendar.getInstance().time.toString()
        val name = saleNameEditText.text.toString()
        val currentUserEmail = CredentialsManager.getInstance(context!!).loadUser()!!.first
        val description = saleDescriptionEditText.text.toString()
        val thisSale: Sale
        if (name.isBlank() || description.isBlank() || normalPriceInput.text.toString().isBlank() || offerPriceInput.text.toString().isBlank()) {
            Toast.makeText(context, "You must fill all the fields", Toast.LENGTH_SHORT).show()
            thisSale = Sale(
                name = "",
                description = "",
                originalPrice = 0,
                salePrice = 0,
                isOnline = isOnline,
                createdAt = currentTime,
                categoryId = category,
                link = null,
                photoUri = currentPhotoPath,
                userEmail = currentUserEmail,
                latitude = null,
                longitude = null
            )
        } else {
            val normalPrice = normalPriceInput.text.toString().toInt()
            val offerPrice = offerPriceInput.text.toString().toInt()
            thisSale = Sale(
                name = name,
                description = description,
                originalPrice = normalPrice,
                salePrice = offerPrice,
                isOnline = isOnline,
                createdAt = currentTime,
                categoryId = category,
                link = null,
                photoUri = currentPhotoPath,
                userEmail = currentUserEmail,
                latitude = customLocationListener.latitude,
                longitude = customLocationListener.longitude
            )
        }
        return thisSale
    }

    private fun askForGPSAccess() {
        // ASK Permission
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // Permission granted by default on previous versions
        } else {
            if (PermissionChecker.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    Array(2) { Manifest.permission.ACCESS_FINE_LOCATION },
                    RequestCode.FINE_LOCATION_REQUEST
                )
            }
        }
    }

    private fun startReadingGPSPosition() {
        // CHECK Permissioncontext!!
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //Permission not enabled
        } else {
            locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val location = getLastKnownLocation()
            if (location != null) {
                customLocationListener.latitude = location.latitude
                customLocationListener.longitude = location.longitude
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10f, customLocationListener)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation(): Location? {
        val providers = locationManager.getProviders(true)
        var bestLocation: Location? = null
        providers.forEach { provider ->
            val l = locationManager.getLastKnownLocation(provider)
            if (l != null) {
                if (bestLocation == null || l.accuracy < bestLocation!!.accuracy) {
                    // Found best last known location: %s", l)
                    bestLocation = l
                }
            }
        }
        return bestLocation
    }

    private fun stopReadingGPSPosition() {
        try {
            locationManager.removeUpdates(customLocationListener)
        } catch (e: Exception) {
            Log.d("INFO", "Exception ocurred while stop reading position -> ${e.message}")
        }
    }

    override fun onResume() {
        super.onResume()
        startReadingGPSPosition()
    }

    override fun onPause() {
        super.onPause()
        stopReadingGPSPosition()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            RequestCode.FINE_LOCATION_REQUEST -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    startReadingGPSPosition()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            else -> {
                // Ignore all other requests.
            }
        }
    }

}
