package com.leafcellteam.mafia.creatingSection.countrySelection

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LanguageSelectorCard(
    selectedLanguage: String = "EN",
    onLanguageSelected: (String) -> Unit = {}
) {
    val primaryColor = Color(0xFF7B68EE)
    val cardColor = Color(0xFF2E2E3E)
    val textColor = Color.White

    val languages = listOf(
        LanguageOption(
            code = "UZ",
            name = "O'zbek",
            flag = "🇺🇿",
            gradient = Brush.horizontalGradient(
                colors = listOf(Color(0xFF00B4D8), Color(0xFF0077B6))
            )
        ),
        LanguageOption(
            code = "RU",
            name = "Русский",
            flag = "🇷🇺",
            gradient = Brush.horizontalGradient(
                colors = listOf(Color(0xFF1E3A8A), Color(0xFFEF4444))
            )
        ),
        LanguageOption(
            code = "EN",
            name = "English",
            flag = "🇺🇸",
            gradient = Brush.horizontalGradient(
                colors = listOf(Color(0xFFDC2626), Color(0xFF1E3A8A))
            )
        )
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Text(
                text = "Country / Страна / Mamlakat",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp),
                color = primaryColor.copy(alpha = 0.3f)
            )

            // Language Cards
            languages.forEach { language ->
                LanguageCard(
                    language = language,
                    isSelected = selectedLanguage == language.code,
                    primaryColor = primaryColor,
                    onSelect = { onLanguageSelected(language.code) }
                )
            }
        }
    }
}

@Composable
private fun LanguageCard(
    language: LanguageOption,
    isSelected: Boolean,
    primaryColor: Color,
    onSelect: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                indication = ripple(color = primaryColor),
                interactionSource = remember { MutableInteractionSource() }
            ) { onSelect() },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF3E3E4E) else Color(0xFF2A2A3A)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        ),
        border = if (isSelected) {
            BorderStroke(2.dp, primaryColor)
        } else {
            BorderStroke(1.dp, Color(0xFF404050))
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (isSelected) {
                        Modifier
                            .background(language.gradient)
                            .alpha(0.74f)
                    } else Modifier
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left side - Flag and Name
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Flag with gradient background
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                brush = if (isSelected) language.gradient else Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF404050), Color(0xFF404050))
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = language.flag,
                            fontSize = 32.sp
                        )
                    }

                    // Language info
                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = language.name,
                            fontSize = 18.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Color.LightGray
                        )
                        Text(
                            text = language.code,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = if (isSelected) primaryColor else Color.Gray
                        )
                    }
                }

                // Right side - Selection indicator
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(
                            color = if (isSelected) primaryColor else Color.Transparent,
                            shape = CircleShape
                        )
                        .border(
                            width = 2.dp,
                            color = if (isSelected) primaryColor else Color(0xFF606070),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

data class LanguageOption(
    val code: String,
    val name: String,
    val flag: String,
    val gradient: Brush
)