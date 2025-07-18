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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.kashif.cameraK.enums.Directory
import com.kashif.imagesaverplugin.ImageSaverConfig
import com.kashif.imagesaverplugin.rememberImageSaverPlugin
import com.muhammetkonukcu.litlounge.AlertMessageDialog
import com.muhammetkonukcu.litlounge.model.AddBookUiState
import com.muhammetkonukcu.litlounge.theme.Blue500
import com.muhammetkonukcu.litlounge.theme.Red500
import com.muhammetkonukcu.litlounge.theme.White
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
import litlounge.composeapp.generated.resources.author_name
import litlounge.composeapp.generated.resources.author_name_hint
import litlounge.composeapp.generated.resources.back
import litlounge.composeapp.generated.resources.book_name
import litlounge.composeapp.generated.resources.book_name_hint
import litlounge.composeapp.generated.resources.camera_permission_message
import litlounge.composeapp.generated.resources.cancel
import litlounge.composeapp.generated.resources.clear
import litlounge.composeapp.generated.resources.done
import litlounge.composeapp.generated.resources.finished
import litlounge.composeapp.generated.resources.gallery_permission_message
import litlounge.composeapp.generated.resources.how_many_pages_is_the_book
import litlounge.composeapp.generated.resources.open_camera
import litlounge.composeapp.generated.resources.permission_required_title
import litlounge.composeapp.generated.resources.ph_arrow_left
import litlounge.composeapp.generated.resources.remove_book
import litlounge.composeapp.generated.resources.save
import litlounge.composeapp.generated.resources.select_from_gallery
import litlounge.composeapp.generated.resources.settings
import litlounge.composeapp.generated.resources.the_day_i_finished
import litlounge.composeapp.generated.resources.the_day_i_started
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
        bottomBar = { BottomBar(viewModel = viewModel, navController = navController) }
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
    removeBook: () -> Unit = {},
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
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val isClearBtnEnabled = uiState != AddBookUiState()
    val isSaveBtnEnabled = uiState.name.isNotBlank() && uiState.authorName.isNotBlank() &&
            uiState.totalPage > 0 && uiState.startTimestamp.isNotBlank()
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
                viewModel.saveBookToTheDatabase()
                navController.navigateUp()
            }
        )
    }
}

@Composable
private fun OpenCameraField(
    imageURL: String,
    onImageSelected: (ByteArray?) -> Unit
) {
    var openCameraClicked by remember { mutableStateOf(false) }
    var openGalleryClicked by remember { mutableStateOf(false) }

    if (imageURL.isBlank()) {
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
                onClick = { openCameraClicked = true }
            )

            BottomButton(
                modifier = Modifier,
                label = stringResource(Res.string.select_from_gallery),
                colors = GetClearButtonColors(),
                isEnabled = true,
                onClick = { openGalleryClicked = true }
            )
        }
    } else {
        PlatformImage(
            imageURL = imageURL,
            modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
    }

    if (openCameraClicked) {
        OpenCamera { byteArray ->
            onImageSelected.invoke(byteArray)
        }
        openCameraClicked = false
    }

    if (openGalleryClicked) {
        OpenGallery { byteArray ->
            onImageSelected.invoke(byteArray)
        }
        openGalleryClicked = false
    }
}

@Composable
private fun OpenCamera(imageCapture: (ByteArray?) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    var shouldLaunch by remember { mutableStateOf(false) }
    val cameraManager = rememberCameraManager { bitmap ->
        coroutineScope.launch {
            imageCapture(bitmap?.toByteArray())
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
private fun OpenGallery(imageSelect: (ByteArray?) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    var shouldLaunch by remember { mutableStateOf(false) }
    val cameraManager = rememberGalleryManager { bitmap ->
        coroutineScope.launch {
            val bitmap = withContext(Dispatchers.Default) {
                bitmap?.toByteArray()
            }
            imageSelect.invoke(bitmap)
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
private fun DateField(
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
            textStyle = MaterialTheme.typography.bodyMedium,
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
    label: String,
    value: String,
    placeholder: String = "",
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.small
                )
                .clip(MaterialTheme.shapes.small)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = if (value.isNotEmpty()) value else placeholder,
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