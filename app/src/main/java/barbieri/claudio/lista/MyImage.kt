package barbieri.claudio.lista

import java.io.Serializable

data class MyImage(
    val title: String,
    val description: String,
    val uri: String
) : Serializable
