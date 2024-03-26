package io.devexpert.android_firebase.ui.screens.auth

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.ktx.Firebase
import io.devexpert.android_firebase.ui.navigation.Routes
import io.devexpert.android_firebase.ui.theme.Purple40
import io.devexpert.android_firebase.utils.AnalyticsManager
import io.devexpert.android_firebase.utils.AuthManager
import io.devexpert.android_firebase.utils.AuthRes
import kotlinx.coroutines.launch
import com.google.firebase.firestore.FirebaseFirestore


data class UserData(
    val nombre: String,
    val apellidos: String,
    val domicilio: String,
    val claveElector: String,
    val curp: String,
    val anoRegistro: String,
    val fechaNacimiento: String,
    val seccion: String,
    val vigencia: String
)

// ViewModel para gestionar los datos y la conexión a Firestore
class MyViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    // Función para guardar datos en Firestore
    fun saveUserDataToFirestore(userData: UserData) {
        val userDataMap = hashMapOf(
            "nombre" to userData.nombre,
            "apellidos" to userData.apellidos,
            "domicilio" to userData.domicilio,
            "claveElector" to userData.claveElector,
            "curp" to userData.curp,
            "anoRegistro" to userData.anoRegistro,
            "fechaNacimiento" to userData.fechaNacimiento,
            "seccion" to userData.seccion,
            "vigencia" to userData.vigencia
        )

        db.collection("users")
            .add(userDataMap)
            .addOnSuccessListener { documentReference ->
                // Éxito: el documento se agregó correctamente
                println("Documento agregado con ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                // Error: no se pudo agregar el documento
                println("Error al agregar documento: $e")
            }
    }
}


@Composable
fun SignUpScreen(analytics: AnalyticsManager, auth: AuthManager, navigation: NavController) {
    analytics.logScreenView(screenName = Routes.SignUp.route)

    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var userData by remember { mutableStateOf(UserData("", "", "", "", "", "", "", "", "")) }
    val viewModel: MyViewModel = viewModel()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Crear Cuenta",
            style = TextStyle(fontSize = 40.sp, color = Purple40)
        )
        Spacer(modifier = Modifier.height(50.dp))
        TextField(
            label = { Text(text = "Correo electrónico") },
            value = email,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = { email = it })

        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "Contraseña") },
            value = password,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { password = it })
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "Nombre") },
            value = userData.nombre,
            onValueChange = { userData = (userData.copy(nombre = it)) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            label = { Text(text = "Apellidos") },
            value = userData.apellidos,
            onValueChange = { userData =(userData.copy(apellidos = it)) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            label = { Text(text = "Domicilio") },
            value = userData.domicilio,
            onValueChange = { userData =(userData.copy(domicilio = it)) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            label = { Text(text = "Clave de Elector") },
            value = userData.claveElector,
            onValueChange = { userData =(userData.copy(claveElector = it)) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            label = { Text(text = "CURP") },
            value = userData.curp,
            onValueChange = { userData =(userData.copy(curp = it)) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            label = { Text(text = "Año de Registro") },
            value = userData.anoRegistro,
            onValueChange = {userData =(userData.copy(anoRegistro = it)) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            label = { Text(text = "Fecha de Nacimiento") },
            value = userData.fechaNacimiento,
            onValueChange = { userData =(userData.copy(fechaNacimiento = it)) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            label = { Text(text = "Sección") },
            value = userData.seccion,
            onValueChange = { userData =(userData.copy(seccion = it)) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            label = { Text(text = "Vigencia") },
            value = userData.vigencia,
            onValueChange = { userData =(userData.copy(vigencia = it)) }
        )

        Spacer(modifier = Modifier.height(20.dp))


        Spacer(modifier = Modifier.height(30.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    scope.launch {
                        signUp(email, password, auth, analytics, context, navigation, userData, viewModel)
                    }
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Registrarse")
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
        ClickableText(
            text = AnnotatedString("¿Ya tienes cuenta? Inicia sesión"),
            onClick = {
                navigation.popBackStack()
            },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                textDecoration = TextDecoration.Underline,
                color = Purple40
            )
        )
    }
}

private suspend fun signUp(email: String, password: String, auth: AuthManager, analytics: AnalyticsManager, context: Context, navigation: NavController, userData: UserData, viewModel: MyViewModel) {
    if (email.isNotEmpty() && password.isNotEmpty()) {
        when (val result = auth.createUserWithEmailAndPassword(email, password)) {
            is AuthRes.Success -> {
                analytics.logButtonClicked(FirebaseAnalytics.Event.SIGN_UP)
                viewModel.saveUserDataToFirestore(userData)
                Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                navigation.popBackStack()
            }
            is AuthRes.Error -> {
                analytics.logButtonClicked("Error SignUp: ${result.errorMessage}")
                Toast.makeText(context, "Error SignUp: ${result.errorMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    } else {
        Toast.makeText(context, "Existen campos vacios", Toast.LENGTH_SHORT).show()
    }
}
