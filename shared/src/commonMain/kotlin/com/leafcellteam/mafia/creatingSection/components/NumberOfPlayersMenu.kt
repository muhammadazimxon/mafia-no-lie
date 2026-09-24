package com.leafcellteam.mafia.creatingSection.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.leafcellteam.mafia.resources.Res
import com.leafcellteam.mafia.resources.maximum
import com.leafcellteam.mafia.resources.minimum
import com.leafcellteam.mafia.resources.numberOfPlayers

@Composable
fun InputNumberOfPlayers(
    value:  ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit
) {
    Column {
        Text(
            text = stringResource(Res.string.numberOfPlayers),
            fontSize = 15.sp,
            fontWeight = FontWeight.W600,
            color = Color.White
        )
        RangeSlider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 5f..15f,
            colors = SliderDefaults.colors(
                thumbColor = Color(218, 82, 82, 255),
                activeTrackColor = Color(211, 83, 83, 255),
                activeTickColor = Color(180, 47, 47, 255),
                inactiveTickColor = Color.White,
                inactiveTrackColor = Color.White
            )
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(Res.string.minimum) + " " + value.start.toInt(),
                fontSize = 15.sp,
                fontWeight = FontWeight.W600,
                color = Color(0xFFFFD700)
            )

            Text(
                text = stringResource(Res.string.maximum) + " " + value.endInclusive.toInt(),
                fontSize = 15.sp,
                fontWeight = FontWeight.W600,
                color = Color(0xFFFFD700)
            )
        }
    }
}

@Composable
@Preview
fun InputNumberOfPlayersPreview() {
    InputNumberOfPlayers(
        value = 2f..3f,
        onValueChange = {}
    )
}