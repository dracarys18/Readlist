package com.karthihegde.readlist.ui.stats

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.karthihegde.readlist.viewmodels.BookViewModel
import com.karthihegde.readlist.utils.groupByStatus

/**
 * Main view of Stat Screen
 *
 * @param viewModel ViewModel
 * @param navController
 */
@Composable
fun StatScreen(viewModel: BookViewModel, navController: NavController) {
    Scaffold(topBar = { TopAppBar(navController = navController) }) {
        val books by viewModel.getAllData.collectAsState(initial = null)
        if (!books.isNullOrEmpty()) {
            val group = books!!.groupByStatus()
            val mapStats = group.map { (key, value) -> key to value.size.toFloat() }
                .sortedBy { (_, valu) -> valu }.toMap()
            val keys = mapStats.keys.toList()
            val color = MaterialTheme.colors.onBackground
            val pieChartColors = listOf(
                Color(0xFFbf95d4),
                Color(0xFFf4ac1a),
                Color(0xFF8b0a50),
            ).take(mapStats.values.size)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize()
                    .padding(10.dp)
            ) {
                Column(modifier = Modifier.align(Alignment.TopCenter)) {
                    pieChartColors.forEachIndexed { index, color ->
                        Row(modifier = Modifier.padding(10.dp)) {
                            Canvas(modifier = Modifier.size(30.dp)) {
                                drawRect(
                                    color = color,
                                    size = size
                                )
                            }
                            Text(
                                text = keys[index],
                                color = MaterialTheme.colors.onBackground,
                                modifier = Modifier.padding(10.dp),
                                style = MaterialTheme.typography.caption,
                                maxLines = 1
                            )
                        }
                    }
                    CustomPieChart(
                        modifier = Modifier,
                        stats = mapStats,
                        isDonut = true,
                        colors = pieChartColors,
                        percentColor = color
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "You didn't read any books so far",
                    fontSize = 25.sp,
                    style = MaterialTheme.typography.overline,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * TopAppBar of Stat Screen
 *
 * @param navController
 */
@Composable
fun TopAppBar(navController: NavController) {
    Column(modifier = Modifier.fillMaxWidth()) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = null)
        }
        Text(
            text = "Your Reading Stats",
            style = MaterialTheme.typography.h3,
            modifier = Modifier
                .padding(bottom = 30.dp)
                .align(Alignment.CenterHorizontally),
            fontWeight = FontWeight.Bold
        )
    }
}
