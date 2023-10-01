package com.zero.meldcxtests.ui.components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.DrawablePainter
import com.zero.meldcxtests.R

@Composable
fun AppLine(name: String? = null, iconDrawable: Drawable? = null) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Image(painter = iconDrawable?.let {
            DrawablePainter(it)
        } ?: painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = name, modifier = Modifier
            .width(48.dp)
            .height(48.dp))
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = name ?: "")
    }
}