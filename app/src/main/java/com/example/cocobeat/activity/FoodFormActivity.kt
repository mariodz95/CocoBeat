package com.example.cocobeat.activity

import android.Manifest
import android.R.attr
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.cocobeat.R
import com.example.cocobeat.database.entity.Food
import com.example.cocobeat.databinding.ActivityFoodFormBinding
import com.example.cocobeat.model.FoodViewModel
import com.example.cocobeat.model.FoodViewModelFactory
import com.example.cocobeat.repository.FoodRepository
import com.example.cocobeat.view.DateAndTime
import org.koin.android.ext.android.inject
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


private const val REQUEST_IMAGE_CAPTURE = 1
private const val REQUEST_CODE = 1
private const val FILE_NAME = "photo.jpg"



class FoodFormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFoodFormBinding
    lateinit var mDate: Date
    lateinit var mTime: String

    private val repository : FoodRepository by inject()
    private lateinit var foodViewModel: FoodViewModel

    private lateinit var photoFile: File
    private var currentPhotoPath: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var factory = FoodViewModelFactory(repository)
        foodViewModel = ViewModelProvider(this, factory)[FoodViewModel::class.java]

        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(R.layout.food_form_title_layout)
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val doneBtn: Toolbar = findViewById(R.id.done)
        doneBtn.setOnClickListener{
            var name: String = binding.nameTextInput.text.toString()
            var calorie: String = binding.caloriesTextInput.text.toString()

            if(calorie == "")
            {
                calorie = "0"
            }

            var food = Food(
                UUID.randomUUID(),
                name,
                calorie.toInt(),
                currentPhotoPath,
                mDate,
                mTime
            )

            if(name.isEmpty()){
                binding.nameTextInput.error = "Name is required!"
                binding.nameTextInput.hint = "Name is required"
                binding.nameTextInput.setHintTextColor(Color.parseColor("#FF0303"))
            }
            if(calorie == "0")
            {
                binding.caloriesTextInput.error = "Amount of calorie is required!"
                binding.caloriesTextInput.hint = "Amount of calorie is required!"
                binding.caloriesTextInput.setHintTextColor(Color.parseColor("#FF0303"))
            }
            if(name.isNotEmpty() && calorie != "0"){

                Log.v("test", "Food: $food")
                foodViewModel.insertFood(food)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

        binding.dateAndTimeView.setDateAndTimeListener(object :
            DateAndTime.OnDateAndTImeChangeListener {
            override fun getDateAndTime(date: Date, time: String) {
                mDate = date
                mTime = time
            }
        })

        binding.cameraBtn.setOnClickListener {
            verifyPermissions()
        }
    }

    private fun verifyPermissions(){
        val permission  = arrayOf(Manifest.permission.CAMERA)
        if(ContextCompat.checkSelfPermission(this.applicationContext, permission[0]) == PackageManager.PERMISSION_GRANTED){
            dispatchTakePictureIntent()
        }else{
            ActivityCompat.requestPermissions(this, permission, REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val takenImage = BitmapFactory.decodeFile(currentPhotoPath)
            val exif = ExifInterface(currentPhotoPath!!)
            val orientation: Int = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )

            val matrix = Matrix()
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90F)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180F)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270F)
                else -> {
                }
            }
            
            var rotatedBitmap = Bitmap.createBitmap(takenImage, 0, 0, takenImage.width, takenImage.height, matrix, true);
            binding.imgView.setImageBitmap(rotatedBitmap)
        }else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        verifyPermissions()
    }


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }
}




