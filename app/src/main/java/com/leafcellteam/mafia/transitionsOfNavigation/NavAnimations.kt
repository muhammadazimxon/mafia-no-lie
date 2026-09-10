package com.leafcellteam.mafia.transitionsOfNavigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically

object NavAnimations {
    val fadeInAnimation = fadeIn(
        animationSpec = tween(400, easing = FastOutSlowInEasing)
    )

    val fadeOutAnimation = fadeOut(
        animationSpec = tween(300, easing = FastOutSlowInEasing)
    )

    val slideInRightAnimation = slideInHorizontally(
        initialOffsetX = { it },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    ) + fadeIn(animationSpec = tween(300))

    val slideOutLeftAnimation = slideOutHorizontally(
        targetOffsetX = { -it / 3 },
        animationSpec = tween(300, easing = FastOutSlowInEasing)
    ) + fadeOut(animationSpec = tween(300))

    val slideInLeftAnimation = slideInHorizontally(
        initialOffsetX = { -it / 3 },
        animationSpec = tween(300, easing = FastOutSlowInEasing)
    ) + fadeIn(animationSpec = tween(300))

    val slideOutRightAnimation = slideOutHorizontally(
        targetOffsetX = { it },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    ) + fadeOut(animationSpec = tween(300))

    val scaleInAnimation = scaleIn(
        initialScale = 0.85f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    ) + fadeIn(animationSpec = tween(300))

    val scaleOutAnimation = scaleOut(
        targetScale = 0.95f,
        animationSpec = tween(200, easing = FastOutSlowInEasing)
    ) + fadeOut(animationSpec = tween(200))

    val slideInUpAnimation = slideInVertically(
        initialOffsetY = { it },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        )
    ) + fadeIn(animationSpec = tween(300))

    val slideOutDownAnimation = slideOutVertically(
        targetOffsetY = { it },
        animationSpec = tween(300, easing = FastOutSlowInEasing)
    ) + fadeOut(animationSpec = tween(300))
}