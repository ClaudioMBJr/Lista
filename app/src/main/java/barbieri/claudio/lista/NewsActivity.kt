package barbieri.claudio.lista

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import barbieri.claudio.lista.ui.theme.ListaTheme
import coil.compose.rememberAsyncImagePainter

class NewsActivity : ComponentActivity() {

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
        var imageUri by remember { mutableStateOf<Uri?>(null) }
        var title by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            imageUri = uri
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            imageUri?.let {
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
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
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
                if (title.isBlank() || description.isBlank() || imageUri == null) {
                    Toast.makeText(
                        this@NewsActivity,
                        "É necessário selecionar a imagem e preecher os campos",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val resultIntent = Intent().apply {
                        putExtra(
                            "image",
                            MyImage(
                                title = title,
                                description = description,
                                uri = imageUri?.toString().orEmpty()
                            )
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
}