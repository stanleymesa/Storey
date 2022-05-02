package com.example.storey.ui.activity

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.datastore.preferences.preferencesDataStore
import com.bumptech.glide.Glide
import com.example.storey.R
import com.example.storey.data.local.datastore.UserPreferences
import com.example.storey.data.local.model.UploadModel
import com.example.storey.data.local.statics.StaticData
import com.example.storey.data.repository.RetrofitRepository
import com.example.storey.databinding.ActivityAddStoryBinding
import com.example.storey.helper.*
import com.example.storey.ui.viewmodel.AddStoryViewModel
import com.example.storey.ui.viewmodel.PrefViewModel
import com.example.storey.ui.viewmodel.PrefViewModelFactory
import com.example.storey.ui.viewmodel.RetrofitViewModelFactory
import com.google.android.gms.maps.model.LatLng
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

private val Context.dataStore by preferencesDataStore(name = "settings")

class AddStoryActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityAddStoryBinding? = null
    private val binding get() = _binding!!
    private var getFile: File? = null
    private var lat: Double? = null
    private var lon: Double? = null
    private val addStoryViewModel by viewModels<AddStoryViewModel> {
        RetrofitViewModelFactory.getInstance(RetrofitRepository())
    }
    private val prefViewModel by viewModels<PrefViewModel> {
        PrefViewModelFactory.getInstance(UserPreferences.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_CAMERA_PERMISSIONS,
                REQUEST_CODE_CAMERA_PERMISSIONS
            )
        }

        setToolbar()
        observeData()
        Glide.with(this)
            .load(R.drawable.peeps)
            .into(binding.includeAddStory.ivPreview)

        binding.includeAddStory.apply {
            btnOpenCamera.setOnClickListener(this@AddStoryActivity)
            btnGallery.setOnClickListener(this@AddStoryActivity)
            btnAddLocation.setOnClickListener(this@AddStoryActivity)
            btnUpload.setOnClickListener(this@AddStoryActivity)
        }
        binding.addStoryToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setToolbar() {
        setSupportActionBar(binding.addStoryToolbar)
        supportActionBar?.title = getString(R.string.add_story_title)
    }

    private fun observeData() {
        addStoryViewModel.fileUploadResponse.observe(this, { fileUploadResponse ->
            when {
                !fileUploadResponse.error -> {
                    Toast.makeText(this,
                        getString(R.string.upload_story_success),
                        Toast.LENGTH_SHORT).show()
                    finish()
                }
                else -> {
                    getString(R.string.error_upload_story).makeSnackbar(binding.root).show()
                }
            }
        })
        addStoryViewModel.isLoading.observe(this, { isLoading ->
            when {
                isLoading -> binding.includeAddStory.progressBar.isVisible = true
                !isLoading -> binding.includeAddStory.progressBar.isVisible = false
            }
        })
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = StaticData.GALLERY_INTENT_TYPE
        val chooser = Intent.createChooser(intent, getString(R.string.choose_a_picture))
        launcherIntentGallery.launch(chooser)
    }

    private fun uploadStory(token: String) {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)


            val desc = when {
                binding.includeAddStory.tvDesc.text.toString() == "" -> {
                    getString(R.string.empty_desc)
                }
                else -> {
                    binding.includeAddStory.tvDesc.text.toString()
                }
            }

            val requestImageFile =
                file.asRequestBody(StaticData.CONTENT_TYPE_IMAGE.toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                StaticData.FORM_PHOTO_NAME,
                file.name,
                requestImageFile
            )

            when {
                lat != null && lon != null -> {

                    addStoryViewModel.uploadStory(
                        UploadModel(
                            token = resources.getString(R.string.input_token, token),
                            file = imageMultipart,
                            description = desc,
                            lat = lat!!.toFloat(),
                            lon = lon!!.toFloat()
                        )
                    )

                }
                else -> {
                    addStoryViewModel.uploadStory(
                        UploadModel(
                            token = resources.getString(R.string.input_token, token),
                            file = imageMultipart,
                            description = desc,
                        )
                    )
                }
            }

        } else {
            getString(R.string.error_no_image).makeSnackbar(binding.root).show()
        }
    }

    private val launcherIntentCameraX =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == CAMERA_X_RESULT) {
                val myFile = it.data?.getSerializableExtra(StaticData.KEY_PICTURE_RESULT) as File
                val isBackCamera =
                    it.data?.getBooleanExtra(StaticData.KEY_ISBACKCAMERA, true) as Boolean

                val result = rotateBitmap(
                    BitmapFactory.decodeFile(myFile.path),
                    isBackCamera
                )

                getFile = bitmapToFile(result, application)

                Glide.with(this)
                    .load(result)
                    .into(binding.includeAddStory.ivPreview)
            }
        }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedPhoto: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedPhoto, this)
            getFile = myFile

            Glide.with(this)
                .load(selectedPhoto)
                .into(binding.includeAddStory.ivPreview)
        }

    }

    private val launcherAddLocation = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == StaticData.RESULT_SAVE_LOCATION) {
            val bundle = result.data?.getParcelableExtra<Bundle>(StaticData.KEY_LATLNG)
            val latLng = bundle?.getParcelable<LatLng>(StaticData.KEY_LATLNG)
            val address = result.data?.getStringExtra(StaticData.KEY_ADDRESS)

            this.lat = latLng?.latitude
            this.lon = latLng?.longitude

            binding.includeAddStory.apply {
                btnAddLocation.text = getString(R.string.change_location)
                tvLocation.isVisible = true
                tvLocation.text = address ?: ""
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_CAMERA_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_CAMERA_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this,
                    getString(R.string.no_camera_permission),
                    Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_open_camera -> startCameraX()
            R.id.btn_gallery -> startGallery()
            R.id.btn_add_location -> {
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra(StaticData.FROM_ACTIVITY, StaticData.ADD_STORY_ACTIVITY)
                launcherAddLocation.launch(intent)
            }
            R.id.btn_upload -> {
                prefViewModel.getSession().observe(this, { userModel ->
                    uploadStory(userModel.token)
                })
            }
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_CAMERA_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_CAMERA_PERMISSIONS = 10
    }


}