package com.example.gehirnjogingapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gehirnjogingapp.ui.theme.GehirnJogingAppTheme
import kotlin.random.Random

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.alpha
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

val memoryIconPool = listOf(
    Icons.Default.Favorite, Icons.Default.Star, Icons.Default.ThumbUp,
    Icons.Default.Home, Icons.Default.Settings, Icons.Default.Person,
    Icons.Default.Notifications, Icons.Default.Email, Icons.Default.Search,
    Icons.Default.PlayArrow, Icons.Default.Build, Icons.Default.Face,
    Icons.Default.Menu, Icons.Default.Close, Icons.Default.Add,
    Icons.Default.Check, Icons.Default.Delete, Icons.Default.Share,
    Icons.Default.ShoppingCart, Icons.Default.LocationOn,
    Icons.Default.AccountBox, Icons.Default.AccountCircle, Icons.Default.AddCircle,
    Icons.AutoMirrored.Filled.ArrowBack, Icons.AutoMirrored.Filled.ArrowForward, Icons.Default.ArrowDownward,
    Icons.Default.ArrowUpward, Icons.Default.Assessment, Icons.AutoMirrored.Filled.Assignment,
    Icons.Default.AutoFixHigh, Icons.Default.BackHand, Icons.Default.BatteryFull,
    Icons.Default.Bluetooth, Icons.Default.Bookmark, Icons.Default.Call,
    Icons.Default.CameraAlt, Icons.Default.CheckCircle, Icons.Default.Cloud,
    Icons.Default.Computer, Icons.Default.Directions, Icons.Default.Done,
    Icons.Default.Edit, Icons.AutoMirrored.Filled.ExitToApp, Icons.Default.Extension,
    Icons.Default.FavoriteBorder, Icons.Default.Fingerprint, Icons.Default.Flag,
    Icons.Default.FlashlightOn, Icons.Default.Flight, Icons.Default.Folder,
    Icons.Default.GetApp, Icons.Default.Group, Icons.Default.History,
    Icons.Default.Info, Icons.Default.Keyboard, Icons.Default.Language,
    Icons.Default.Laptop, Icons.AutoMirrored.Filled.List, Icons.Default.Lock,
    Icons.Default.Loop, Icons.Default.Mail, Icons.Default.Map,
    Icons.Default.Mic, Icons.Default.Mood, Icons.Default.MusicNote,
    Icons.Default.Nature, Icons.Default.Navigation, Icons.Default.Palette,
    Icons.Default.Phone, Icons.Default.Photo, Icons.Default.Place,
    Icons.Default.Public, Icons.Default.Refresh, Icons.Default.School,
    Icons.AutoMirrored.Filled.Send, Icons.Default.StarBorder, Icons.Default.Sync,
    Icons.Default.Tv, Icons.Default.Videocam, Icons.Default.Wallet,
    Icons.Default.Warning, Icons.Default.Watch, Icons.Default.Wifi,
    Icons.Default.Work, Icons.Default.WarningAmber, Icons.Default.WbSunny,
    Icons.Default.VpnKey, Icons.AutoMirrored.Filled.VolumeUp, Icons.Default.ThumbDown,
    Icons.AutoMirrored.Filled.TextSnippet, Icons.Default.Tag, Icons.Default.Support,
    Icons.AutoMirrored.Filled.StickyNote2, Icons.Default.SportsBasketball, Icons.Default.Speed,
    Icons.Default.Sms, Icons.Default.SkipNext, Icons.Default.ShoppingBag,
    Icons.Default.Shield, Icons.Default.SettingsVoice, Icons.Default.Security,
    Icons.Default.Save, Icons.Default.RocketLaunch, Icons.Default.Restaurant,
    Icons.Default.Report, Icons.AutoMirrored.Filled.Reply, Icons.Default.Receipt,
    Icons.Default.QuestionAnswer, Icons.Default.PushPin, Icons.Default.Print,
    Icons.Default.Policy, Icons.Default.PlayCircle, Icons.Default.Pets,
    Icons.Default.PersonAdd, Icons.Default.Payments, Icons.Default.Paid,
    Icons.Default.Output, Icons.Default.Opacity, Icons.Default.Nightlight,
    Icons.Default.MoreVert, Icons.Default.MoreHoriz, Icons.Default.Monitor,
    Icons.Default.Money, Icons.AutoMirrored.Filled.Message, Icons.Default.MedicalServices,
    Icons.Default.LockOpen, Icons.Default.Lightbulb, Icons.Default.Layers,
    Icons.AutoMirrored.Filled.Label, Icons.Default.Key, Icons.Default.Inventory,
    Icons.AutoMirrored.Filled.Help, Icons.Default.Handshake, Icons.Default.Groups,
    Icons.AutoMirrored.Filled.Grading, Icons.Default.Gavel, Icons.Default.Games,
    Icons.Default.Forum, Icons.Default.FilterList, Icons.Default.FileDownload,
    Icons.Default.Event, Icons.Default.Error, Icons.Default.Description,
    Icons.Default.Dashboard, Icons.AutoMirrored.Filled.ContactSupport, Icons.Default.ConfirmationNumber,
    Icons.AutoMirrored.Filled.Comment, Icons.Default.Cake, Icons.Default.Brightness4,
    Icons.Default.Bolt, Icons.Default.Backup, Icons.Default.AssignmentInd,
    Icons.Default.AttachFile, Icons.AutoMirrored.Filled.Article, Icons.AutoMirrored.Filled.Announcement,
    Icons.Default.AddShoppingCart, Icons.Default.AddLocation
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoryScreen(onBackClick: () -> Unit) {
    var seenIcons by remember { mutableStateOf(setOf<ImageVector>()) }
    var currentIcon by remember { mutableStateOf(memoryIconPool.random()) }
    var score by remember { mutableIntStateOf(0) }
    var mistakes by remember { mutableIntStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }
    var showHistory by remember { mutableStateOf(false) }
    
    var feedbackColor by remember { mutableStateOf(Color.Transparent) }
    val scope = rememberCoroutineScope()


    fun nextRound() {
        val n = seenIcons.size
        val shouldShowNewIcon = Random.nextInt(n + 1) == 0 // 0 is 1 out of n+1
        
        if (shouldShowNewIcon){
            val remainingIcons = memoryIconPool.filter { it !in seenIcons }
            currentIcon = if (remainingIcons.isNotEmpty()) {
                remainingIcons.random()
            } else {
                seenIcons.random()
            }
        } else {
            currentIcon = seenIcons.random()
        }
    }

    fun handleAnswer(answeredSeen: Boolean) {
        val wasActuallySeen = seenIcons.contains(currentIcon)
        val isCorrect = answeredSeen == wasActuallySeen

        feedbackColor = if (isCorrect) Color.Green.copy(alpha = 0.1f) else Color.Red.copy(alpha = 0.1f)

        if (isCorrect) {
            score++
        } else {
            mistakes++
            if (mistakes >= 3) {
                isGameOver = true
            }
        }

        scope.launch {
            delay(300.milliseconds)
            feedbackColor = Color.Transparent
            if (!isGameOver) {
                seenIcons = seenIcons + currentIcon
                nextRound()
            }
        }
    }

    // Neustart Vars
    fun restartGame() {
        seenIcons = emptySet()
        currentIcon = memoryIconPool.random()
        score = 0
        mistakes = 0
        isGameOver = false
        showHistory = false
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Gedächtnis-Spiel", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Zurück")
                    }
                },
                actions = {
                    // Das Augen-Icon zum Anzeigen der History
                    IconButton(onClick = { showHistory = !showHistory }) {
                        Icon(Icons.Default.Visibility, contentDescription = "History")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (showHistory) {
            HistoryDialog(seenIcons = seenIcons.toList(),innerPadding)
        }
        else if (isGameOver) {
            GameOverView(score, innerPadding, onRestart = { restartGame() })
        } else {
            GameActiveView(score, mistakes, currentIcon, innerPadding, feedbackColor, onAnswer = { handleAnswer(it) })
        }
    }
}

@Composable
fun GameActiveView(
    score: Int,
    mistakes: Int,
    currentIcon: ImageVector,
    innerPadding: PaddingValues,
    feedbackColor: Color,
    onAnswer: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Score: $score", style = MaterialTheme.typography.titleLarge)
            Text(
                text = "Fehler: $mistakes/3",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = currentIcon,
            contentDescription = "Memory Icon",
            modifier = Modifier
                .size(150.dp)
                .background(feedbackColor, CircleShape)
                .padding(16.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { onAnswer(true) },
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Schon gesehen", fontSize = 16.sp, textAlign = TextAlign.Center)
            }

            Button(
                onClick = { onAnswer(false) },
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
            ) {
                Text("Neu", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun GameOverView(score: Int, innerPadding: PaddingValues, onRestart: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Spiel vorbei!", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Dein Score: $score", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onRestart) {
            Text("Nochmal versuchen")
        }
    }
}

@Composable
fun HistoryDialog(seenIcons: List<ImageVector>,innerPadding: PaddingValues) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Bereits Gesehen",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(seenIcons) { icon ->
                Icon(icon, contentDescription = null, modifier = Modifier.size(40.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MemoryScreenPreview() {
    GehirnJogingAppTheme {
        MemoryScreen(onBackClick = {})
    }
}
