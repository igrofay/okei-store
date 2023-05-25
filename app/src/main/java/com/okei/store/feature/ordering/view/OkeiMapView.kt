package com.okei.store.feature.ordering.view

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.okei.store.R

@Composable
fun OkeiMapView(
    distance: Double? = null,
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        tonalElevation = if(isSystemInDarkTheme()) 6.dp else 2.dp,
        shadowElevation = 2.dp,
    ){
        Column {
            AsyncImage(
                model = "https://static-maps.yandex.ru/1.x/?ll=55.124111,51.765334&size=600,300&z=17&l=map&pt=55.124111,51.765334,pm2rdm",
                contentDescription = null,
                modifier = Modifier
                    .height(170.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 10.dp,
                        horizontal = 18.dp
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column{
                    Text(
                        text = stringResource(R.string.ul_сhkalova_11),
                        fontSize = 16.sp,
                    )
                    Text(
                        text = stringResource(R.string.orenburg),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W300,
                    )
                }
                distance?.let { dis->
                    Text(
                        text = "${"%.2f".format(dis)} ${stringResource(R.string.km)}"
                    )
                }
            }
        }
    }

}