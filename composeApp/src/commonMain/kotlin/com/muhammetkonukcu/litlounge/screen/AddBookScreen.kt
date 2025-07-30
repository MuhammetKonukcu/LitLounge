package com.muhammetkonukcu.litlounge.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.kashif.cameraK.enums.Directory
import com.kashif.imagesaverplugin.ImageSaverConfig
import com.kashif.imagesaverplugin.rememberImageSaverPlugin
import com.muhammetkonukcu.litlounge.AlertMessageDialog
import com.muhammetkonukcu.litlounge.model.AddBookUiState
import com.muhammetkonukcu.litlounge.theme.Black
import com.muhammetkonukcu.litlounge.theme.Blue500
import com.muhammetkonukcu.litlounge.theme.Green500
import com.muhammetkonukcu.litlounge.theme.Red500
import com.muhammetkonukcu.litlounge.theme.White
import com.muhammetkonukcu.litlounge.utils.ImageUrlStatus
import com.muhammetkonukcu.litlounge.utils.PermissionCallback
import com.muhammetkonukcu.litlounge.utils.PermissionStatus
import com.muhammetkonukcu.litlounge.utils.PermissionType
import com.muhammetkonukcu.litlounge.utils.PlatformImage
import com.muhammetkonukcu.litlounge.utils.createPermissionsManager
import com.muhammetkonukcu.litlounge.utils.rememberCameraManager
import com.muhammetkonukcu.litlounge.utils.rememberGalleryManager
import com.muhammetkonukcu.litlounge.viewmodel.AddBookViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import litlounge.composeapp.generated.resources.Res
import litlounge.composeapp.generated.resources.add_a_book
import litlounge.composeapp.generated.resources.add_photo
import litlounge.composeapp.generated.resources.add_photo_with_url
import litlounge.composeapp.generated.resources.author_name
import litlounge.composeapp.generated.resources.author_name_hint
import litlounge.composeapp.generated.resources.back
import litlounge.composeapp.generated.resources.book_name
import litlounge.composeapp.generated.resources.book_name_hint
import litlounge.composeapp.generated.resources.book_photo
import litlounge.composeapp.generated.resources.camera_permission_message
import litlounge.composeapp.generated.resources.cancel
import litlounge.composeapp.generated.resources.clear
import litlounge.composeapp.generated.resources.done
import litlounge.composeapp.generated.resources.finished
import litlounge.composeapp.generated.resources.gallery_permission_message
import litlounge.composeapp.generated.resources.how_many_pages_is_the_book
import litlounge.composeapp.generated.resources.image_url
import litlounge.composeapp.generated.resources.invalid_url
import litlounge.composeapp.generated.resources.open_camera
import litlounge.composeapp.generated.resources.permission_required_title
import litlounge.composeapp.generated.resources.ph_arrow_left
import litlounge.composeapp.generated.resources.ph_cancel
import litlounge.composeapp.generated.resources.remove_book
import litlounge.composeapp.generated.resources.save
import litlounge.composeapp.generated.resources.saving_image
import litlounge.composeapp.generated.resources.select_from_gallery
import litlounge.composeapp.generated.resources.settings
import litlounge.composeapp.generated.resources.the_day_i_finished
import litlounge.composeapp.generated.resources.the_day_i_started
import litlounge.composeapp.generated.resources.valid_url
import litlounge.composeapp.generated.resources.validating_url
import litlounge.composeapp.generated.resources.which_page_are_you_left_on
import network.chaintech.kmp_date_time_picker.ui.datepicker.WheelDatePickerView
import network.chaintech.kmp_date_time_picker.utils.DateTimePickerView
import network.chaintech.kmp_date_time_picker.utils.MIN
import network.chaintech.kmp_date_time_picker.utils.WheelPickerDefaults
import network.chaintech.kmp_date_time_picker.utils.now
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun AddBookScreen(bookId: Int? = null, navController: NavController, innerPadding: PaddingValues) {
    val viewModel: AddBookViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    var finishedDateEmptyError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (bookId != null && bookId != 0) {
            viewModel.getBookById(bookId)
        }
    }

    Scaffold(
        modifier = Modifier.padding(innerPadding),
        topBar = {
            TopAppBar(
                navController = navController,
                bookId = bookId,
                removeBook = {
                    viewModel.deleteBookFromTheDatabase(bookId)
                    navController.navigateUp()
                }
            )
        },
        bottomBar = {
            BottomBar(
                viewModel = viewModel,
                navController = navController,
                handleErrorMessage = { finishedDateEmptyError = true })
        }
    ) { contentPadding ->
        Column(
            Modifier
                .padding(horizontal = 12.dp, vertical = contentPadding.calculateTopPadding())
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BookTextField(
                Res.string.book_name,
                uiState.name,
                viewModel::onNameChange,
                Res.string.book_name_hint
            )
            BookTextField(
                Res.string.author_name,
                uiState.authorName,
                viewModel::onAuthorNameChange,
                Res.string.author_name_hint
            )
            BookTextField(
                Res.string.how_many_pages_is_the_book,
                uiState.totalPage.toString(),
                viewModel::onTotalPageChange,
                keyboardType = KeyboardType.Number
            )
            BookTextField(
                Res.string.which_page_are_you_left_on,
                uiState.currentPage.toString(),
                viewModel::onCurrentPageChange,
                keyboardType = KeyboardType.Number
            )

            DateField(
                labelRes = Res.string.the_day_i_started,
                dateString = uiState.startTimestamp,
                onDateChange = viewModel::onStartTimestampChange
            )

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(Res.string.finished),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Checkbox(
                    checked = uiState.finished,
                    onCheckedChange = viewModel::onFinishedToggle,
                    colors = GetCheckboxColors()
                )
            }

            if (uiState.finished) {
                DateField(
                    isError = finishedDateEmptyError,
                    labelRes = Res.string.the_day_i_finished,
                    dateString = uiState.finishTimestamp,
                    startDateString = uiState.startTimestamp,
                    onDateChange = viewModel::onFinishTimestampChange
                )
            }

            val imageSaverPlugin = rememberImageSaverPlugin(
                config = ImageSaverConfig(
                    isAutoSave = false,
                    prefix = "LitLounge",
                    directory = Directory.PICTURES,
                    customFolderName = "LitLounge"
                )
            )

            OpenCameraField(
                imageURL = uiState.imageURL,
                isLoading = uiState.isImageSaving,
                imageUrlStatus = uiState.imageUrlStatus,
                isNetworkImage = uiState.isNetworkImage,
                onImageSaving = viewModel::onImageSaving,
                onImageUrlChanged = viewModel::onImageURLChange,
                onImageRemoved = { viewModel.onImageURLChange("") },
                onImageSelected = { imageUrl ->
                    viewModel.saveImage(imageSaverPlugin = imageSaverPlugin, byteArray = imageUrl)
                }
            )
        }
    }
}

@Composable
fun TopAppBar(
    navController: NavController,
    bookId: Int?,
    removeBook: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ph_arrow_left),
                    contentDescription = stringResource(Res.string.back),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(Modifier.width(4.dp))
            Text(
                text = stringResource(Res.string.add_a_book),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (bookId != null && bookId != 0) {
            TextButton(
                onClick = { removeBook.invoke() },
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = stringResource(Res.string.remove_book),
                    style = MaterialTheme.typography.titleSmall,
                    color = Red500
                )
            }
        }
    }
}

@Composable
private fun BottomBar(
    viewModel: AddBookViewModel,
    navController: NavController,
    handleErrorMessage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val isClearBtnEnabled = uiState != AddBookUiState() && !uiState.isImageSaving
    val isSaveBtnEnabled = uiState.name.isNotBlank() && uiState.authorName.isNotBlank() &&
            uiState.totalPage > 0 && uiState.startTimestamp.isNotBlank() && !uiState.isImageSaving
    Row(modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp)) {
        BottomButton(
            modifier = Modifier.weight(1f),
            label = stringResource(Res.string.clear),
            colors = GetClearButtonColors(),
            isEnabled = isClearBtnEnabled,
            onClick = { viewModel.clearUiState() }
        )

        Spacer(modifier.width(16.dp))

        BottomButton(
            modifier = Modifier.weight(1f),
            label = stringResource(Res.string.save),
            colors = GetSaveButtonColors(),
            isEnabled = isSaveBtnEnabled,
            onClick = {
                if (uiState.finished && uiState.finishTimestamp.isBlank()) {
                    handleErrorMessage.invoke()
                } else {
                    viewModel.saveBookToTheDatabase()
                    navController.navigateUp()
                }
            }
        )
    }
}

@Composable
private fun OpenCameraField(
    imageURL: String,
    isLoading: Boolean = false,
    onImageRemoved: () -> Unit,
    imageUrlStatus: ImageUrlStatus,
    isNetworkImage: Boolean = false,
    onImageSaving: (Boolean) -> Unit,
    onImageSelected: (ByteArray?) -> Unit,
    onImageUrlChanged: (String, Boolean) -> Unit
) {
    var openCameraClicked by remember { mutableStateOf(false) }
    var openGalleryClicked by remember { mutableStateOf(false) }
    var addImageUrlClicked by remember { mutableStateOf(false) }

    if (imageURL.isBlank() && !isLoading) {
        Column {
            Text(
                text = stringResource(Res.string.add_photo),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
            BottomButton(
                modifier = Modifier,
                label = stringResource(Res.string.open_camera),
                colors = GetClearButtonColors(),
                isEnabled = true,
                onClick = {
                    openCameraClicked = !openCameraClicked
                    addImageUrlClicked = false
                }
            )

            BottomButton(
                modifier = Modifier,
                label = stringResource(Res.string.select_from_gallery),
                colors = GetClearButtonColors(),
                isEnabled = true,
                onClick = {
                    openGalleryClicked = !openGalleryClicked
                    addImageUrlClicked = false
                }
            )

            if (!addImageUrlClicked) {
                BottomButton(
                    modifier = Modifier,
                    label = stringResource(Res.string.add_photo_with_url),
                    colors = GetClearButtonColors(),
                    isEnabled = true,
                    onClick = { addImageUrlClicked = !addImageUrlClicked }
                )
            }
        }
    } else if (isLoading) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(Res.string.saving_image),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    } else if (!isNetworkImage) {
        Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(12.dp))) {
            PlatformImage(
                imageURL = imageURL,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            FilledTonalIconButton(
                onClick = { onImageRemoved.invoke() },
                modifier = Modifier.align(Alignment.TopEnd),
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = Black.copy(alpha = 0.4f)
                )
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ph_cancel),
                    contentDescription = stringResource(Res.string.cancel),
                    tint = White
                )
            }
        }
    }

    if (openCameraClicked) {
        OpenCamera(
            onImageSaving = { isLoading -> onImageSaving(isLoading) },
            imageCapture = { byteArray ->
                onImageSelected.invoke(byteArray)
                openCameraClicked = false
            }
        )
    }

    if (openGalleryClicked) {
        OpenGallery(
            onImageSaving = { isLoading -> onImageSaving(isLoading) },
            imageSelect = { byteArray ->
                onImageSelected.invoke(byteArray)
                openGalleryClicked = false
            }
        )
    }

    if (addImageUrlClicked && !isLoading) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ImageUrlTextField(
                value = imageURL,
                labelRes = Res.string.image_url,
                keyboardType = KeyboardType.Uri,
                onValueChange = {
                    onImageUrlChanged.invoke(it, true)
                },
                textStyle = LocalTextStyle.current.copy(
                    color = when (imageUrlStatus) {
                        ImageUrlStatus.INVALID -> Red500
                        ImageUrlStatus.VALID -> Green500
                        else -> MaterialTheme.colorScheme.tertiary
                    }
                )
            )

            when (imageUrlStatus) {
                ImageUrlStatus.VALIDATING -> {
                    Text(
                        text = stringResource(Res.string.validating_url),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }

                ImageUrlStatus.INVALID -> {
                    Text(
                        text = stringResource(Res.string.invalid_url),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Red500
                    )
                }

                ImageUrlStatus.VALID -> {
                    Text(
                        text = stringResource(Res.string.valid_url),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Green500
                    )
                }

                ImageUrlStatus.IDLE -> {}
            }

            if (imageUrlStatus == ImageUrlStatus.VALID && imageURL.isNotBlank()) {
                Box(
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    AsyncImage(
                        model = imageURL,
                        contentDescription = stringResource(Res.string.book_photo),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                }
            }
        }
    }
}

@Composable
private fun OpenCamera(
    imageCapture: (ByteArray?) -> Unit,
    onImageSaving: (Boolean) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var shouldLaunch by remember { mutableStateOf(false) }
    val cameraManager = rememberCameraManager { bitmap ->
        coroutineScope.launch {
            onImageSaving.invoke(true)
            imageCapture(bitmap?.toByteArray())
            onImageSaving.invoke(false)
        }
    }
    var launchSetting by remember { mutableStateOf(value = false) }
    var permissionRationalDialog by remember { mutableStateOf(value = false) }
    val permissionsManager = createPermissionsManager(object : PermissionCallback {
        override fun onPermissionStatus(
            permissionType: PermissionType,
            status: PermissionStatus
        ) {
            when (status) {
                PermissionStatus.GRANTED -> {
                    shouldLaunch = true
                }

                else -> {
                    permissionRationalDialog = true
                }
            }
        }
    })

    val hasPermission = permissionsManager.isPermissionGranted(PermissionType.CAMERA)

    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            shouldLaunch = true
        }
    }

    LaunchedEffect(shouldLaunch) {
        if (shouldLaunch) {
            cameraManager.launch()
            shouldLaunch = false
        }
    }

    if (!hasPermission) {
        permissionsManager.askPermission(PermissionType.CAMERA)
    }

    if (launchSetting) {
        permissionsManager.launchSettings()
        launchSetting = false
    }
    if (permissionRationalDialog) {
        AlertMessageDialog(
            title = stringResource(Res.string.permission_required_title),
            message = stringResource(Res.string.camera_permission_message),
            positiveButtonText = stringResource(Res.string.settings),
            negativeButtonText = stringResource(Res.string.cancel),
            onPositiveClick = {
                permissionRationalDialog = false
                launchSetting = true

            },
            onNegativeClick = {
                permissionRationalDialog = false
            }
        )
    }
}

@Composable
private fun OpenGallery(
    imageSelect: (ByteArray?) -> Unit,
    onImageSaving: (Boolean) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var shouldLaunch by remember { mutableStateOf(false) }
    val cameraManager = rememberGalleryManager { bitmap ->
        coroutineScope.launch {
            onImageSaving.invoke(true)
            val bitmap = withContext(Dispatchers.Default) {
                bitmap?.toByteArray()
            }
            imageSelect.invoke(bitmap)
            onImageSaving.invoke(false)
        }
    }
    var launchSetting by remember { mutableStateOf(value = false) }
    var permissionRationalDialog by remember { mutableStateOf(value = false) }
    val permissionsManager = createPermissionsManager(object : PermissionCallback {
        override fun onPermissionStatus(
            permissionType: PermissionType,
            status: PermissionStatus
        ) {
            when (status) {
                PermissionStatus.GRANTED -> {
                    shouldLaunch = true
                }

                else -> {
                    permissionRationalDialog = true
                }
            }
        }
    })

    val hasPermission = permissionsManager.isPermissionGranted(PermissionType.GALLERY)

    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            shouldLaunch = true
        }
    }

    LaunchedEffect(shouldLaunch) {
        if (shouldLaunch) {
            cameraManager.launch()
            shouldLaunch = false
        }
    }

    if (!hasPermission) {
        permissionsManager.askPermission(PermissionType.GALLERY)
    }

    if (launchSetting) {
        permissionsManager.launchSettings()
        launchSetting = false
    }
    if (permissionRationalDialog) {
        AlertMessageDialog(
            title = stringResource(Res.string.permission_required_title),
            message = stringResource(Res.string.gallery_permission_message),
            positiveButtonText = stringResource(Res.string.settings),
            negativeButtonText = stringResource(Res.string.cancel),
            onPositiveClick = {
                permissionRationalDialog = false
                launchSetting = true

            },
            onNegativeClick = {
                permissionRationalDialog = false
            }
        )
    }
}

@Composable
private inline fun BottomButton(
    modifier: Modifier,
    label: String,
    colors: ButtonColors,
    isEnabled: Boolean,
    crossinline onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        colors = colors,
        enabled = isEnabled,
        onClick = { onClick.invoke() }
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

@Composable
private fun BookTextField(
    labelRes: StringResource,
    value: String,
    onValueChange: (String) -> Unit,
    placeholderRes: StringResource? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    LabeledOutlinedTextField(
        label = stringResource(labelRes),
        value = value,
        keyboardType = keyboardType,
        onValueChange = onValueChange,
        placeholder = placeholderRes?.let { stringResource(it) } ?: ""
    )
}

@Composable
private fun ImageUrlTextField(
    labelRes: StringResource,
    value: String,
    onValueChange: (String) -> Unit,
    placeholderRes: StringResource? = null,
    textStyle: TextStyle,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    LabeledOutlinedTextField(
        label = stringResource(labelRes),
        value = value,
        textStyle = textStyle,
        keyboardType = keyboardType,
        onValueChange = onValueChange,
        placeholder = placeholderRes?.let { stringResource(it) } ?: ""
    )
}

@Composable
private fun DateField(
    isError: Boolean = false,
    labelRes: StringResource,
    dateString: String,
    startDateString: String = "",
    onDateChange: (LocalDate) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }
    val initialDate =
        dateString.takeIf { it.isNotBlank() }?.let(LocalDate::parse) ?: LocalDate.now()
    val minDate =
        if (startDateString.isNotBlank()) LocalDate.parse(startDateString)
        else LocalDate.MIN()

    LabeledClickableField(
        isError = isError,
        label = stringResource(labelRes),
        value = dateString,
        onClick = { showPicker = true }
    )

    if (showPicker) {
        WheelDatePickerView(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 22.dp, bottom = 26.dp),
            showDatePicker = showPicker,
            title = stringResource(labelRes),
            doneLabel = stringResource(Res.string.done),
            startDate = initialDate,
            minDate = minDate,
            maxDate = LocalDate.now(),
            titleStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary),
            doneLabelStyle = MaterialTheme.typography.bodyLarge.copy(color = Blue500),
            dateTextColor = Blue500,
            selectorProperties = WheelPickerDefaults.selectorProperties(
                borderColor = Color.LightGray,
            ),
            rowCount = 5,
            height = 180.dp,
            containerColor = MaterialTheme.colorScheme.background,
            dateTextStyle = TextStyle(
                fontWeight = FontWeight(600),
            ),
            dragHandle = {
                HorizontalDivider(
                    modifier = Modifier.padding(top = 8.dp).width(40.dp)
                        .clip(CircleShape),
                    thickness = 4.dp,
                    color = Color(0xFFE8E4E4)
                )
            },
            shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp),
            dateTimePickerView = DateTimePickerView.BOTTOM_SHEET_VIEW,
            onDoneClick = {
                onDateChange(it)
                showPicker = false
            },
            onDismiss = {
                showPicker = false
            }
        )
    }
}

@Composable
private fun LabeledOutlinedTextField(
    label: String,
    value: String,
    readOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.labelMedium
                )
            },
            readOnly = readOnly,
            textStyle = textStyle,
            singleLine = true,
            colors = GetTextFieldColors(),
            modifier = Modifier.fillMaxWidth().clickable {
                if (readOnly) {
                    onClick.invoke()
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            )
        )
    }
}

@Composable
private fun LabeledClickableField(
    isError: Boolean = false,
    label: String,
    value: String,
    placeholder: String = "",
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = if (isError && value.isBlank()) Red500 else MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(
                    width = 1.dp,
                    color = if (isError && value.isBlank()) Red500 else MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.small
                )
                .clip(MaterialTheme.shapes.small)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = value.ifEmpty { placeholder },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
private fun GetTextFieldColors(): TextFieldColors {
    val colors = TextFieldDefaults.colors().copy(
        focusedTextColor = MaterialTheme.colorScheme.primary,
        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
        focusedPlaceholderColor = MaterialTheme.colorScheme.tertiary,
        focusedContainerColor = MaterialTheme.colorScheme.background,
        unfocusedTextColor = MaterialTheme.colorScheme.primary,
        unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.tertiary,
        unfocusedContainerColor = MaterialTheme.colorScheme.background,
        errorContainerColor = MaterialTheme.colorScheme.background,
        disabledTextColor = MaterialTheme.colorScheme.primary,
        disabledIndicatorColor = MaterialTheme.colorScheme.tertiary,
        disabledContainerColor = MaterialTheme.colorScheme.background,
        disabledPlaceholderColor = MaterialTheme.colorScheme.tertiary,
    )
    return colors
}

@Composable
private fun GetCheckboxColors(): CheckboxColors {
    val colors = CheckboxDefaults.colors().copy(
        checkedCheckmarkColor = White,
        uncheckedCheckmarkColor = MaterialTheme.colorScheme.tertiary,
        checkedBoxColor = Blue500,
        uncheckedBoxColor = MaterialTheme.colorScheme.background,
        checkedBorderColor = Blue500,
        uncheckedBorderColor = MaterialTheme.colorScheme.tertiary
    )
    return colors
}

@Composable
private fun GetClearButtonColors(): ButtonColors {
    val colors = ButtonDefaults.buttonColors().copy(
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.background,
        disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
        disabledContentColor = MaterialTheme.colorScheme.background
    )
    return colors
}

@Composable
private fun GetSaveButtonColors(): ButtonColors {
    val colors = ButtonDefaults.buttonColors().copy(
        containerColor = Blue500,
        contentColor = White,
        disabledContainerColor = Blue500.copy(alpha = 0.5f),
        disabledContentColor = White
    )
    return colors
}