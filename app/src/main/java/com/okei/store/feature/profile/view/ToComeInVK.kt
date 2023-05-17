package com.okei.store.feature.profile.view

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.okei.store.R
import com.okei.store.feature.app.view.LocalVkAuthLauncher
import com.vk.api.sdk.auth.VKScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToComeInVK() {
    val activity = LocalContext.current as ComponentActivity
    val authLauncher = LocalVkAuthLauncher.current
    ElevatedCard(
        onClick = {
            authLauncher?.launch(arrayListOf(VKScope.WALL, VKScope.PHONE))
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.elevatedCardElevation(),
        modifier = Modifier.padding(
            horizontal = 20.dp,
            vertical = 28.dp
        ),
    ){
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp , Alignment.CenterHorizontally)
        ){
            Text(
                text = stringResource(R.string.to_come_in),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
            )
            Icon(
                painter = painterResource(R.drawable.ic_vk_logo),
                contentDescription = null,
                modifier = Modifier.size(38.dp)
            )
        }
    }
}