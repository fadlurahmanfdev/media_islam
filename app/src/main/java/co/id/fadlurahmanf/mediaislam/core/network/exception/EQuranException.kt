package co.id.fadlurahmanf.mediaislam.core.network.exception

import co.id.fadlurahmanf.mediaislam.R
import co.id.fadlurahmanf.mediaislam.core.constant.AppConstant
import co.id.fadlurahmanf.mediaislam.core.dto.model.BottomsheetModel
import co.id.fadlurahmanf.mediaislam.core.dto.model.CopyWritingModel

data class EQuranException(
    override val message: String,
    val httpCode: Int? = null,
    val enumCode: String? = null,
) : Throwable() {}

fun EQuranException.toBottomsheetModel(): BottomsheetModel {
    when (enumCode) {
        AppConstant.UNKNOWN_HOST_EXCEPTION_CODE -> {
            return BottomsheetModel(
                infoId = enumCode,
                asset = R.drawable.il_sad,
                title = "Jaringan Bermasalah",
                message = "Kami mendeteksi masalah dengan koneksi internet Anda. Pastikan Anda terhubung ke jaringan yang stabil untuk melanjutkan.",
                buttonText = "Ok"
            )
        }

        else -> {
            if (httpCode != null || enumCode != null) {
                return BottomsheetModel(
                    asset = R.drawable.il_sad,
                    title = "Oops, Terjadi Kesalahan",
                    message = "Oops, ada masalah di aplikasi ini. Error Code: [${enumCode ?: httpCode}]. Kami sedang bekerja untuk menyelesaikannya, coba lagi nanti ya!",
                    buttonText = "Ok"
                )
            } else {
                return BottomsheetModel(
                    asset = R.drawable.il_sad,
                    title = "Oops, Terjadi Kesalahan",
                    message = "Oops, ada masalah di aplikasi ini. Kami sedang bekerja untuk menyelesaikannya, coba lagi nanti ya!",
                    buttonText = "Ok"
                )
            }
        }
    }
}

fun EQuranException.toSimpleCopyWriting(): CopyWritingModel {
    when (enumCode) {
        AppConstant.UNKNOWN_HOST_EXCEPTION_CODE -> {
            return CopyWritingModel(
                infoId = enumCode,
                asset = R.drawable.il_sad,
                title = "Jaringan Bermasalah",
                message = "Kami mendeteksi masalah dengan koneksi internet Anda. Pastikan Anda terhubung ke jaringan yang stabil untuk melanjutkan.",
                buttonText = "Ok"
            )
        }

        else -> {
            if (httpCode != null || enumCode != null) {
                return CopyWritingModel(
                    asset = R.drawable.il_sad,
                    title = "Oops, Terjadi Kesalahan",
                    message = "Oops, ada masalah di aplikasi ini. Error Code: [${enumCode ?: httpCode}]. Kami sedang bekerja untuk menyelesaikannya, coba lagi nanti ya!",
                    buttonText = "Ok"
                )
            } else {
                return CopyWritingModel(
                    asset = R.drawable.il_sad,
                    title = "Oops, Terjadi Kesalahan",
                    message = "Oops, ada masalah di aplikasi ini. Kami sedang bekerja untuk menyelesaikannya, coba lagi nanti ya!",
                    buttonText = "Ok"
                )
            }
        }
    }
}

fun Throwable.fromEQuranException(): EQuranException {
    if (this is EQuranException) {
        return this
    } else if (this is Exception) {
        return EQuranException(message = message ?: "-")
    } else {
        return EQuranException(message = message ?: "-")
    }
}
