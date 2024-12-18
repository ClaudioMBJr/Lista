package barbieri.claudio.lista

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : ViewModel() {

    val myImages = MutableStateFlow<MutableList<MyImage>>(mutableStateListOf())

    fun addImage(image: MyImage) {
        myImages.value.add(image)
    }

}