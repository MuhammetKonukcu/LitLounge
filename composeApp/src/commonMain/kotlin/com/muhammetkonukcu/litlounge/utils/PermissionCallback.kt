package com.muhammetkonukcu.litlounge.utils

interface PermissionCallback {
    fun onPermissionStatus(permissionType: PermissionType, status: PermissionStatus)
}