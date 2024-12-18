package barbieri.claudio.lista

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.io.File

class NewsViewModel : ViewModel() {

    val myImage = MutableStateFlow<MyImage>(MyImage())

    fun updateImage(file: File?) {
        myImage.update { it.copy(file = file) }
    }

    fun updateTitle(title: String) {
        myImage.update { it.copy(title = title) }
    }

    fun updateDescription(description: String) {
        myImage.update { it.copy(description = description) }
    }
}