package com.karthihegde.readlist.ui.stats

import android.graphics.Paint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.atan2
import kotlin.math.min
import kotlin.math.roundToInt


/**
 * PieChart Composable
 *
 * @param modifier Modifier of the pie chart
 * @param stats Map of Point Title and Points
 * @param colors List of Colors of pie chart
 * @param isDonut Is donut pie chart of not
 * @param percentColor Color of the Percentage text
 */
@Composable
fun CustomPieChart(
    modifier: Modifier,
    stats: Map<String, Float>,
    colors: List<Color>,
    isDonut: Boolean = false,
    percentColor: Color = Color.White
) {

    if (stats.isEmpty() || stats.size != colors.size) return

    val keys = stats.keys.toList()
    val total = stats.map { it.value }.sum()
    val proportions = stats.map {
        it.value * 100 / total
    }
    val angleProgress = proportions.map {
        360 * it / 100
    }

    val progressSize = mutableListOf<Float>()
    progressSize.add(angleProgress.first())

    for (x in 1 until angleProgress.size)
        progressSize.add(angleProgress[x] + progressSize[x - 1])

    var activePie by remember {
        mutableStateOf(-1)
    }

    var startAngle = 270f

    BoxWithConstraints(
        modifier = modifier
    ) {

        val sideSize = min(constraints.maxWidth, constraints.maxHeight)
        val padding = (sideSize * if (isDonut) 30 else 20) / 100f


        val pathPortion = remember {
            Animatable(initialValue = 0f)
        }
        LaunchedEffect(key1 = true) {
            pathPortion.animateTo(
                1f, animationSpec = tween(1000)
            )
        }

        val size = Size(sideSize.toFloat() - padding, sideSize.toFloat() - padding)

        Canvas(
            modifier = Modifier
                .width(sideSize.dp)
                .height(sideSize.dp)
                .pointerInput(true) {

                    if (!isDonut)
                        return@pointerInput

                    detectTapGestures {
                        val clickedAngle = convertTouchEventPointToAngle(
                            sideSize.toFloat(),
                            sideSize.toFloat(),
                            it.x,
                            it.y
                        )
                        progressSize.forEachIndexed { index, item ->
                            if (clickedAngle <= item) {
                                if (activePie != index)
                                    activePie = index

                                return@detectTapGestures
                            }
                        }
                    }
                }
        ) {

            angleProgress.forEachIndexed { index, arcProgress ->
                drawPie(
                    colors[index],
                    startAngle,
                    arcProgress * pathPortion.value,
                    size,
                    padding = padding,
                    isDonut = isDonut,
                    isActive = activePie == index
                )
                startAngle += arcProgress
            }

            if (activePie != -1)
                drawContext.canvas.nativeCanvas.apply {
                    val fontSize = 60.toDp().toPx()
                    drawText(
                        "${keys[activePie]}\n${proportions[activePie].roundToInt()}%",
                        (sideSize / 2) + fontSize / 4, (sideSize / 2) + fontSize / 3,
                        Paint().apply {
                            color = percentColor.toArgb()
                            textSize = fontSize
                            textAlign = Paint.Align.CENTER
                        }
                    )
                }
        }
    }

}

/**
 * An Extension to Draw PieChart
 *
 * @param color Color of the pie piece
 * @param startAngle angle where pie should start
 * @param arcProgress Increase in angle
 * @param size Size of the Pie
 * @param padding Padding
 * @param isActive Is the pie piece is clicked or not
 * @param isDonut Is piechart a donut or not
 */
private fun DrawScope.drawPie(
    color: Color,
    startAngle: Float,
    arcProgress: Float,
    size: Size,
    padding: Float,
    isDonut: Boolean = false,
    isActive: Boolean = false
): Path {

    return Path().apply {
        drawArc(
            color = color,
            startAngle = startAngle,
            sweepAngle = arcProgress,
            useCenter = !isDonut,
            size = size,
            style = if (isDonut) Stroke(
                width = if (isActive) 120f else 100f,
            ) else Fill,

            topLeft = Offset(padding / 2, padding / 2)
        )
    }
}

/**
 * Convert touch event points to angle
 *
 * @param width Width of the pie
 * @param height height of the pie
 * @param xPos Xpos of the touch
 * @param yPos ypos of the touch
 */
private fun convertTouchEventPointToAngle(
    width: Float,
    height: Float,
    xPos: Float,
    yPos: Float
): Double {
    val x = xPos - (width * 0.5f)
    val y = yPos - (height * 0.5f)

    var angle = Math.toDegrees(atan2(y.toDouble(), x.toDouble()) + Math.PI / 2)
    angle = if (angle < 0) angle + 360 else angle
    return angle
}
