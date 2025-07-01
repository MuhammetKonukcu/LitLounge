package com.muhammetkonukcu.litlounge.utils

import androidx.compose.runtime.Composable

expect class PermissionsManager(callback: PermissionCallback) : PermissionHandler

@Composable
expect fun createPermissionsManager(callback: PermissionCallback): PermissionsManager
