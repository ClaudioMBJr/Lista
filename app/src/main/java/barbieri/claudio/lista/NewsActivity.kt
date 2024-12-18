package barbieri.claudio.lista

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import barbieri.claudio.lista.MyImage.Companion.allFieldsFilled
import barbieri.claudio.lista.ui.theme.ListaTheme
import coil.compose.rememberAsyncImagePainter
import java.io.File
import java.io.FileOutputStream

class NewsActivity : ComponentActivity() {

    private val viewModel: NewsViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListaTheme {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text("Lista") },
                            navigationIcon = {
                                IconButton(onClick = { this.onBackPressedDispatcher.onBackPressed() }) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
                            },
                            actions = {
                                IconButton(onClick = { this@NewsActivity.finish() }) {
                                    Icon(Icons.Filled.Close, contentDescription = "Exit")
                                }
                            },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = Color.Cyan
                            )
                        )
                    }
                ) { innerPadding ->
                    Screen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    @Composable
    fun Screen(modifier: Modifier = Modifier) {
        val myImage = viewModel.myImage.collectAsStateWithLifecycle().value

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            viewModel.updateImage(this.getFileFromUri(uri))
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            myImage.file?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
            }
            Spacer(Modifier.height(8.dp))
            Button(onClick = { launcher.launch("image/*") }) {
                Text("Selcione a imagem")
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = myImage.title,
                onValueChange = { viewModel.updateTitle(it) },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = myImage.description,
                onValueChange = { viewModel.updateDescription(it) },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            HorizontalDivider(modifier, 1.dp, Color.Gray)
            Spacer(Modifier.height(8.dp))
            Button(onClick = {
                if (!myImage.allFieldsFilled()) {
                    Toast.makeText(
                        this@NewsActivity,
                        "É necessário selecionar a imagem e preecher os campos",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val resultIntent = Intent().apply {
                        putExtra(
                            "image",
                            myImage
                        )
                    }
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }
            }) {
                Text("Adicionar Item")
            }
        }
    }

    //Save uri as a file
    private fun Context.getFileFromUri(uri: Uri?): File? {
        if (uri == null) return null

        val file = File(this.filesDir, "image.png")
        val outputStream = FileOutputStream(file)
        val bitmap = this.getBitmapFromUri(uri)

        if (bitmap == null) return null
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.close()

        return file
    }

    //Convert uri to a bitmap
    private fun Context.getBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}