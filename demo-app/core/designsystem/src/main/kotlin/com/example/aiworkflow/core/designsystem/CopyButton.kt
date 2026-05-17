package com.example.aiworkflow.core.designsystem

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun CopyButton(
    text: String,
    label: String = "已复制到剪贴板"
) {
    val context = LocalContext.current
    IconButton(onClick = {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("copied", text))
        Toast.makeText(context, label, Toast.LENGTH_SHORT).show()
    }) {
        Icon(Icons.Filled.Share, contentDescription = "复制")
    }
}
