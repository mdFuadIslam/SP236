package com.bracu.sp236

import ImagePreviewActivity
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bracu.sp236.ui.theme.SP236Theme
import com.bumptech.glide.Glide

class MainActivity : ComponentActivity() {

    private val REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val selectImagesButton = findViewById<Button>(R.id.openGallery)

        selectImagesButton.setOnClickListener {
            openGalleryForImages()
        }
    }
    private fun openGalleryForImages() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) // Enable multiple selection
        startActivityForResult(Intent.createChooser(intent, "Select Images"), REQUEST_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val images = mutableListOf<Uri>()

                if (data.clipData != null) {
                    val count = data.clipData!!.itemCount
                    for (i in 0 until count) {
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        images.add(imageUri)
                    }
                } else if (data.data != null) {
                    val imageUri = data.data!!
                    images.add(imageUri)
                }
                if (!images.isEmpty()) {
                    var index=0
                    val imageCount=images.size
//                    val intent = Intent(this, ImagePreviewActivity::class.java)
//                    intent.putExtra("imageUri", firstImageUri)
//                    startActivity(intent)
                    val imageView = findViewById<ImageView>(R.id.showImage)
                    loadImage(images[index],imageView)
                    val nextBtn = findViewById<Button>(R.id.nextBtn)
                    val prevBtn = findViewById<Button>(R.id.prevBtn)
                    nextBtn.setOnClickListener {
                        index=(index+1)%imageCount
                        loadImage(images[index],imageView)
                    }
                    prevBtn.setOnClickListener {
                        index=(index-1)%imageCount
                        if (index<0){
                            index=imageCount-1}
                        loadImage(images[index],imageView)
                    }

                }
            }
        }
    }

    private fun loadImage(imageUri: Uri,imageView: ImageView) {
        if (imageUri != null) {
            Glide.with(this)
                .load(imageUri)
                .into(imageView)
        }
    }
}
