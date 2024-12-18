package barbieri.claudio.lista

import java.io.File
import java.io.Serializable

data class MyImage(
    val title: String = "",
    val description: String = "",
    val file: File? = null
) : Serializable {
    companion object {
        fun MyImage?.allFieldsFilled(): Boolean {
            return if (this == null) return false
            else this.title.isNotEmpty() && this.description.isNotEmpty() && this.file != null
        }
    }
}
