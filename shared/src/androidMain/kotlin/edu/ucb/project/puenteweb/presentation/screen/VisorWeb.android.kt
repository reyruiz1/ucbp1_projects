package edu.ucb.project.puenteweb.presentation.screen

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.webkit.JavaScriptReplyProxy
import androidx.webkit.WebViewCompat
import androidx.webkit.WebViewFeature
import kotlinx.coroutines.flow.Flow

private const val TAG = "PuenteWeb"
private const val TAG_JS = "PuenteWeb-JS"
private const val NOMBRE_PUENTE = "AppNativa"

@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun VisorWeb(
    url: String,
    cartasSalientes: Flow<String>,
    onCartaRecibida: (String) -> Unit,
    onCanalCerrado: () -> Unit,
    modifier: Modifier
) {
    var canal by remember { mutableStateOf<JavaScriptReplyProxy?>(null) }
    val alRecibir by rememberUpdatedState(onCartaRecibida)
    val alCerrar by rememberUpdatedState(onCanalCerrado)

    LaunchedEffect(cartasSalientes) {
        cartasSalientes.collect { carta ->
            val destino = canal
            if (destino == null) {
                Log.w(TAG, "APP → WEB descartada, la web no está lista: $carta")
            } else {
                Log.i(TAG, "APP → WEB $carta")
                destino.postMessage(carta)
            }
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView.setWebContentsDebuggingEnabled(esDepurable(context))
            val origen = origenPermitido(url)
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.useWideViewPort = true
                settings.loadWithOverviewMode = true
                settings.cacheMode = WebSettings.LOAD_NO_CACHE
                settings.userAgentString = "CarteleraUCB/1.0 (Android WebView)"
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        canal = null
                        alCerrar()
                    }

                    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                        if (!request.isForMainFrame || origenPermitido(request.url.toString()) == origen) return false
                        abrirFuera(view.context, request.url)
                        return true
                    }
                }
                webChromeClient = object : WebChromeClient() {
                    override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                        Log.d(TAG_JS, consoleMessage.message())
                        return true
                    }
                }
                instalarPuente(this, origen) { texto, respuesta ->
                    canal = respuesta
                    alRecibir(texto)
                }
                loadUrl(url, mapOf("ngrok-skip-browser-warning" to "true"))
            }
        },
        onRelease = { webView ->
            canal = null
            alCerrar()
            webView.destroy()
        }
    )
}

private fun instalarPuente(
    webView: WebView,
    origen: String?,
    alRecibir: (String, JavaScriptReplyProxy) -> Unit
) {
    if (!WebViewFeature.isFeatureSupported(WebViewFeature.WEB_MESSAGE_LISTENER)) {
        Log.e(TAG, "Este WebView no soporta WEB_MESSAGE_LISTENER, actualiza Android System WebView")
        return
    }
    if (origen == null) {
        Log.e(TAG, "URL inválida o sin https, no se instala el puente")
        return
    }
    Log.i(TAG, "Portero: solo se aceptan cartas de $origen")
    WebViewCompat.addWebMessageListener(
        webView,
        NOMBRE_PUENTE,
        setOf(origen)
    ) { _, mensaje, origenCarta, esMarcoPrincipal, respuesta ->
        val texto = mensaje.data
        if (!esMarcoPrincipal || texto == null) {
            Log.w(TAG, "Carta rechazada de $origenCarta")
            return@addWebMessageListener
        }
        Log.i(TAG, "WEB → APP $texto")
        alRecibir(texto, respuesta)
    }
}

private fun origenPermitido(url: String): String? {
    val uri = Uri.parse(url)
    val host = uri.host
    if (uri.scheme != "https" || host.isNullOrBlank()) return null
    return if (uri.port == -1) "https://$host" else "https://$host:${uri.port}"
}

private fun esDepurable(context: Context): Boolean {
    return (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
}

private fun abrirFuera(context: Context, uri: Uri) {
    Log.w(TAG, "Enlace externo, se abre fuera de la app: $uri")
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, uri).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    } catch (e: ActivityNotFoundException) {
        Log.e(TAG, "Ninguna app puede abrir $uri")
    }
}
