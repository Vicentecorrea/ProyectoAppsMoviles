package com.example.salit.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import com.example.salit.CredentialsManager
import com.example.salit.R
import com.example.salit.db.AppDatabase
import kotlinx.android.synthetic.main.activity_sale_details.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SaleDetailsActivity : AppCompatActivity() {

    private var currentSaleId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sale_details)
        getCurrentSaleValues()
        setGoToMapButtonListener(locationOnImageButton)
    }

    private fun setGoToMapButtonListener(locationOnImageButton: ImageButton?) {
        locationOnImageButton!!.setOnClickListener {
            startActivity(
                Intent(this, MapActivity::class.java).
                    putExtra("SALE_ID", currentSaleId))
        }
    }

    override fun onResume() {
        super.onResume()
        setViewContent()
    }

    private fun setViewContent() {
        GlobalScope.launch(Dispatchers.IO) {
//            val currentUserEmail = CredentialsManager.getInstance(baseContext).loadUser()!!.first
            val appDatabase = AppDatabase.getDatabase(baseContext)
            val saleDao = appDatabase.SaleDao()
            val categoryDao = appDatabase.CategoryDao()
            val selectedSale = saleDao.getSaleById(currentSaleId)
            val selectedSaleCategoryName = categoryDao.getCategoryName(selectedSale.categoryId)
            launch(Dispatchers.Main) {
                saleNameTextView.text = selectedSale.name
                saleDescriptionTextView.text = selectedSale.description
                originalPriceTextView.text = selectedSale.originalPrice.toString()
                offerPriceTextView.text = selectedSale.salePrice.toString()
                categoryTextView.text = selectedSaleCategoryName
                discountOnPriceTextView.text = ((100 - ((selectedSale.salePrice*100)/selectedSale.originalPrice))*-1).toString() + "%"
                imagePlaceholder.post {
                    if (!selectedSale.photoUri.isNullOrBlank())
                        setPic(selectedSale.photoUri)
                }
            }
        }
    }

    private fun getCurrentSaleValues() {
        currentSaleId = intent.getIntExtra("SALE_ID", 0)
    }

    private fun setPic(imagePath: String) {
        // Get the dimensions of the View
        val targetW: Int = imagePlaceholder.width
        val targetH: Int = imagePlaceholder.height

        val bmOptions = BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(imagePath, this)
            val photoW: Int = outWidth
            val photoH: Int = outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int = Math.min(photoW / targetW, photoH / targetH)

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
        }

        // Reference still exists, but image is missing (deleted?)
//        if (bmOptions.outWidth == 0 && bmOptions.outHeight == 0) imageDeletedTextView.visibility = View.VISIBLE

        BitmapFactory.decodeFile(imagePath, bmOptions)?.also { bitmap ->
            val rotatedBitmap = bitmap.rotate(90)
            imagePlaceholder.setImageBitmap(rotatedBitmap)
            imagePlaceholder.alpha = 1f
        }
    }

    fun Bitmap.rotate(degree:Int):Bitmap {
        // Initialize a new matrix
        val matrix = Matrix()

        // Rotate the bitmap
        matrix.postRotate(degree.toFloat())

        // Resize the bitmap
        val scaledBitmap = Bitmap.createScaledBitmap(
            this,
            width,
            height,
            true
        )

        // Create and return the rotated bitmap
        return Bitmap.createBitmap(
            scaledBitmap,
            0,
            0,
            scaledBitmap.width,
            scaledBitmap.height,
            matrix,
            true
        )
    }
}
