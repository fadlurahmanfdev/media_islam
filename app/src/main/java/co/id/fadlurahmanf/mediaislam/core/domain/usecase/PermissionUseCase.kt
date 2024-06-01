package co.id.fadlurahmanf.mediaislam.core.domain.usecase

import android.content.Context

interface PermissionUseCase {
    fun checkLocationPermission(context: Context): Boolean
}