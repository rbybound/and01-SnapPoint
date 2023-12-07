package com.boostcampwm2023.snappoint.presentation.createpost

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navArgs
import com.boostcampwm2023.snappoint.R
import com.boostcampwm2023.snappoint.databinding.ActivityCreatePostBinding
import com.boostcampwm2023.snappoint.presentation.base.BaseActivity
import com.boostcampwm2023.snappoint.presentation.markerpointselector.MarkerPointSelectorActivity
import com.boostcampwm2023.snappoint.presentation.model.PositionState
import com.boostcampwm2023.snappoint.presentation.model.PostBlockState
import com.boostcampwm2023.snappoint.presentation.model.PostSummaryState
import com.boostcampwm2023.snappoint.presentation.util.MetadataUtil
import com.boostcampwm2023.snappoint.presentation.util.getBitmapFromUri
import com.boostcampwm2023.snappoint.presentation.util.resizeBitmap
import com.boostcampwm2023.snappoint.presentation.util.untilSixAfterDecimalPoint
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.squareup.okhttp.Dispatcher
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.net.URL
import java.util.Locale

@AndroidEntryPoint
class CreatePostActivity : BaseActivity<ActivityCreatePostBinding>(R.layout.activity_create_post) {

    private val viewModel: CreatePostViewModel by viewModels()
    private val args: CreatePostActivityArgs by navArgs()

    private val geocoder: Geocoder by lazy { Geocoder(applicationContext, Locale.KOREA) }

    private val imagePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map.containsValue(false).not()) {
                launchImageSelectionLauncher()
            } else {
                showToastMessage(R.string.message_permission_denied)
            }
        }

    private val imageSelectionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageUri = result.data?.data ?: return@registerForActivityResult
                val inputStream = this.contentResolver.openInputStream(imageUri)
                    ?: return@registerForActivityResult
                val position = MetadataUtil.extractLocationFromInputStream(inputStream)
                    .getOrDefault(PositionState(0.0, 0.0))
                val bitmap = resizeBitmap(getBitmapFromUri(this, imageUri), 1280)
                viewModel.addImageBlock(bitmap, position)

                startMapActivityAndFindAddress(viewModel.uiState.value.postBlocks.lastIndex, position)
            }
        }

    private val addressSelectionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let {
                    viewModel.setAddressAndPosition(
                        index = it.getIntExtra("index", 0),
                        address = it.getStringExtra("address") ?: "",
                        position = PositionState(
                            it.getDoubleExtra("latitude", 0.0).untilSixAfterDecimalPoint(),
                            it.getDoubleExtra("longitude", 0.0).untilSixAfterDecimalPoint()
                        )
                    )
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        collectViewModelData()

        loadPost()
    }

    private fun initBinding() {
        with(binding) {
            vm = viewModel
            btnCheck.setOnClickListener {
                viewModel.onCheckButtonClicked()
            }
        }
    }

    private fun collectViewModelData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    viewModel.event.collect { event ->
                        when (event) {
                            is CreatePostEvent.ShowMessage -> {
                                showToastMessage(event.resId)
                            }

                            is CreatePostEvent.SelectImageFromLocal -> {
                                selectImage()
                            }

                            is CreatePostEvent.NavigatePrev -> {
                                finish()
                            }

                            is CreatePostEvent.FindAddress -> {
                                startMapActivityAndFindAddress(event.index, event.position)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun loadPost() {
        if (args.post.isBlank()) {
            return
        }

        val prevPost: PostSummaryState = Json.decodeFromString<PostSummaryState>(args.post)
        viewModel.updateUuid(prevPost.uuid)
        viewModel.updateTitle(prevPost.title)
        lifecycleScope.launch {
            prevPost.postBlocks.forEach { block ->
                when (block) {
                    is PostBlockState.TEXT -> {
                        addTextBlock(block)
                    }

                    is PostBlockState.IMAGE -> {
                        addImageBlock(block)
                    }

                    is PostBlockState.VIDEO -> {
                        TODO()
                    }
                }
            }
        }
    }

    private fun addTextBlock(block: PostBlockState.TEXT) {
        viewModel.addTextBlock(block.content)
    }

    private suspend fun addImageBlock(block: PostBlockState.IMAGE) {
        val bitmap = withContext(Dispatchers.IO) {
            BitmapFactory.decodeStream(
                URL(block.content).openConnection().getInputStream()
            )
        }

        val addresses = geocoder.getFromLocation(
            block.position.latitude,
            block.position.longitude,
            1
        )
        val address =
            if (addresses.isNullOrEmpty()) "" else addresses[0].getAddressLine(0)

        viewModel.addImageBlock(bitmap, address, block)
    }

    private fun addVideoBlock(block: PostBlockState.VIDEO) {
        //viewModel.addVideoBlock(block)
    }

    private fun selectImage() {
        val permissions = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.ACCESS_MEDIA_LOCATION
            )

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_MEDIA_LOCATION
            )

            else -> arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        getImageWithPermissionCheck(permissions)
    }

    private fun launchImageSelectionLauncher() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            "image/*"
        )
        imageSelectionLauncher.launch(intent)
    }

    private fun getImageWithPermissionCheck(permissions: Array<String>) {
        val permissionCheck = permissions.filter {
            ContextCompat.checkSelfPermission(
                this,
                it
            ) == PackageManager.PERMISSION_DENIED
        }

        if (permissionCheck.isNotEmpty()) {
            if (permissionCheck.any { shouldShowRequestPermissionRationale(it) }) {
                showToastMessage(R.string.message_permission_required)
            }
        }
        imagePermissionLauncher.launch(permissions)
    }

    private fun startMapActivityAndFindAddress(index: Int, position: PositionState) {
        val intent = Intent(this, MarkerPointSelectorActivity::class.java)
        intent.putExtra("index", index)
        intent.putExtra("position", position.asDoubleArray())
        addressSelectionLauncher.launch(intent)
    }
}