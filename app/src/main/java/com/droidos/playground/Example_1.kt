package com.droidos.playground

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AnimationExampleApp() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. animate*AsState
        AnimateColorAndSize()

        // 2. updateTransition
        TransitionExample()

        // 3. AnimatedVisibility
        VisibilityExample()

        // 4. InfiniteTransition
        InfiniteRotationExample()

        // 5. Animatable
        CustomAnimatableExample()
    }
}

@Composable
fun AnimateColorAndSize() {
    var isClicked by remember { mutableStateOf(false) }
    val backgroundColor by animateColorAsState(
        targetValue = if (isClicked) Color.Blue else Color.Black,
        animationSpec = tween(500),
        label = ""
    )
    val scale by animateFloatAsState(
        if (isClicked) 1.5f else 1f,
        animationSpec = spring(dampingRatio = 0.3f, stiffness = 500f),
        label = ""
    )

    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .scale(scale)
            .clickable { isClicked = !isClicked },
        contentAlignment = Alignment.Center
    ) {
        Text("Click Me", color = Color.White)
    }
}


@Composable
fun TransitionExample() {
    var isSelected by remember { mutableStateOf(false) }
    val transition = updateTransition(targetState = isSelected, label = "transition")
    val color by transition.animateColor(label = "color") { state ->
        if (state) Color.Blue else Color.Black
    }
    val size by transition.animateDp(label = "size") { state ->
        if (state) 150.dp else 100.dp
    }

    Box(
        modifier = Modifier
            .size(size)
            .background(color)
            .clickable { isSelected = !isSelected },
        contentAlignment = Alignment.Center
    ) {
        Text("Toggle", color = Color.White)
    }
}

@Composable
fun VisibilityExample() {
    var isVisible by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        ButtonExample(
            text = if (isVisible) "Hide" else "Show",
            onClick = { isVisible = !isVisible }
        )

        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn() + expandVertically() + scaleIn(),
            exit = fadeOut() + shrinkVertically() + scaleOut()
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Magenta),
                contentAlignment = Alignment.Center
            ) {
                Text("Visible!", color = Color.White)
            }
        }
    }
}

@Composable
fun InfiniteRotationExample() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        label = "",
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = Modifier
            .size(100.dp)
            .rotate(rotation)
            .background(Color.Cyan),
        contentAlignment = Alignment.Center
    ) {
        Text("Spin", color = Color.White)
    }
}

@Composable
fun CustomAnimatableExample() {
    val animatable = remember { Animatable(0f) }
    var isAnimating by remember { mutableStateOf(false) }

    LaunchedEffect(isAnimating) {
        if (isAnimating) {
            animatable.animateTo(1f, animationSpec = keyframes {
                durationMillis = 1000
                0.5f at 500
            })
        } else {
            animatable.animateTo(0f, animationSpec = tween(1000))
        }
    }

    Box(
        modifier = Modifier
            .size(100.dp)
            .graphicsLayer {
                scaleX = animatable.value
                scaleY = animatable.value
            }
            .background(Color.Gray)
            .clickable { isAnimating = !isAnimating },
        contentAlignment = Alignment.Center
    ) {
        Text("Animate", color = Color.White)
    }
}

@Composable
fun ButtonExample(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.DarkGray)
            .clickable { onClick() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = Color.White, fontSize = 18.sp)
    }
}