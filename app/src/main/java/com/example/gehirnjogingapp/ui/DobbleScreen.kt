package com.example.gehirnjogingapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gehirnjogingapp.ui.theme.GehirnJogingAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

data class DobbleIconInfo(
    val icon: ImageVector,
    val rotation: Float,
    val scale: Float,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DobbleScreen(onBackClick: () -> Unit) {
    var score by remember { mutableIntStateOf(0) }
    var mistakes by remember { mutableIntStateOf(0) }
    var cardAIcons by remember { mutableStateOf(emptyList<DobbleIconInfo>()) }
    var cardBIcons by remember { mutableStateOf(emptyList<DobbleIconInfo>()) }
    var matchingIcon by remember { mutableStateOf<ImageVector?>(null) }
    
    var feedbackColorA by remember { mutableStateOf(Color.Transparent) }
    var feedbackColorB by remember { mutableStateOf(Color.Transparent) }
    val scope = rememberCoroutineScope()

    fun generateCards(newTopIcons: List<DobbleIconInfo>? = null) {
        val colors = listOf(
            Color(0xFFE53935), Color(0xFF1E88E5), Color(0xFF43A047), 
            Color(0xFFD81B60), Color(0xFF8E24AA), Color(0xFFFB8C00), 
            Color(0xFF00ACC1), Color(0xFF3949AB)
        )
        val scales = listOf(0.5f, 0.7f, 0.9f, 1.1f, 1.3f, 1.5f, 0.8f).shuffled()

        val iconsForA: List<ImageVector>
        if (newTopIcons == null) {
            val pool = memoryIconPool.shuffled()
            iconsForA = pool.take(7)
            cardAIcons = iconsForA.mapIndexed { index, icon ->
                DobbleIconInfo(
                    icon = icon,
                    rotation = Random.nextFloat() * 360f,
                    scale = scales[index],
                    color = colors.random()
                )
            }
        } else {
            cardAIcons = newTopIcons
            iconsForA = newTopIcons.map { it.icon }
        }

        val match = iconsForA.random()
        matchingIcon = match
        
        val remainingPool = memoryIconPool.shuffled().filter { it !in iconsForA }
        val iconsForB = (remainingPool.take(6) + match).shuffled()

        // Card B generation
        val scalesB = scales.shuffled()
        cardBIcons = iconsForB.mapIndexed { index, icon ->
            val infoA = cardAIcons.find { it.icon == icon }
            
            val finalRotation = if (infoA != null) {
                // Ensure match has different rotation
                (infoA.rotation + 90f + Random.nextFloat() * 180f) % 360f
            } else Random.nextFloat() * 360f

            val finalScale = if (infoA != null) {
                // Ensure match has different scale
                scalesB.filter { it != infoA.scale }.random()
            } else scalesB[index]

            val finalColor = if (infoA != null) {
                // Ensure match has different color
                colors.filter { it != infoA.color }.random()
            } else colors.random()

            DobbleIconInfo(
                icon = icon,
                rotation = finalRotation,
                scale = finalScale,
                color = finalColor
            )
        }
    }

    fun handleIconClick(icon: ImageVector, isCardA: Boolean) {
        if (feedbackColorA != Color.Transparent || feedbackColorB != Color.Transparent) return

        if (icon == matchingIcon) {
            score++
            feedbackColorA = Color.Green.copy(alpha = 0.2f)
            feedbackColorB = Color.Green.copy(alpha = 0.2f)
            val currentHand = cardBIcons // Save current hand to move to top
            scope.launch {
                delay(300.milliseconds)
                feedbackColorA = Color.Transparent
                feedbackColorB = Color.Transparent
                generateCards(currentHand)
            }
        } else {
            mistakes++
            if (isCardA) feedbackColorA = Color.Red.copy(alpha = 0.2f)
            else feedbackColorB = Color.Red.copy(alpha = 0.2f)
            
            scope.launch {
                delay(300.milliseconds)
                feedbackColorA = Color.Transparent
                feedbackColorB = Color.Transparent
            }
        }
    }

    LaunchedEffect(Unit) {
        generateCards()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Dobble", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Zurück")
                    }
                },
                actions = {
                    Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(end = 16.dp)) {
                        Text(text = "Score: $score", style = MaterialTheme.typography.labelLarge)
                        Text(text = "Fehler: $mistakes", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DobbleCard(
                icons = cardAIcons,
                modifier = Modifier.size(300.dp),
                feedbackColor = feedbackColorA,
                onIconClick = null // Oben nicht anklickbar
            )
            
            DobbleCard(
                icons = cardBIcons,
                modifier = Modifier.size(300.dp),
                feedbackColor = feedbackColorB,
                onIconClick = { handleIconClick(it, false) } // Unten anklickbar
            )
        }
    }
}

@Composable
fun DobbleCard(
    icons: List<DobbleIconInfo>,
    modifier: Modifier = Modifier,
    feedbackColor: Color,
    onIconClick: ((ImageVector) -> Unit)? = null
) {
    Surface(
        modifier = modifier
            .clip(CircleShape),
        shape = CircleShape,
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(feedbackColor)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            icons.forEachIndexed { index, info ->
                val angle = if (index == 0) 0.0 else (index - 1) * (360.0 / 6.0)
                val distance = if (index == 0) 0.dp else 85.dp
                
                val xOffset = (Math.cos(Math.toRadians(angle)) * distance.value.toDouble()).dp
                val yOffset = (Math.sin(Math.toRadians(angle)) * distance.value.toDouble()).dp

                val iconModifier = Modifier
                    .offset(x = xOffset, y = yOffset)
                    .size(55.dp) // Etwas größere Basisgröße für bessere Trefferfläche
                    .scale(info.scale)
                    .rotate(info.rotation)
                    .then(
                        if (onIconClick != null) {
                            Modifier.clickable(
                                onClick = { onIconClick(info.icon) },
                                indication = null,
                                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                            )
                        } else Modifier
                    )
                
                Icon(
                    imageVector = info.icon,
                    contentDescription = null,
                    modifier = iconModifier,
                    tint = info.color
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DobbleScreenPreview() {
    GehirnJogingAppTheme {
        DobbleScreen(onBackClick = {})
    }
}
