package co.id.fadlurahmanf.mediaislam.core.domain.usecase

import android.content.Context
import co.id.fadlurahmanfdev.kotlin_core_platform.data.repository.CorePlatformRepository

class PermissionUseCaseImpl(
    private val corePlatformRepository: CorePlatformRepository
) : PermissionUseCase {
    override fun checkLocationPermission(context: Context): Boolean {
        return corePlatformRepository.isLocationPermissionEnabled(context)
    }
}