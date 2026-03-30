package com.example.chainreact

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

class BoardView(context: Context, private val engine: GameEngine, private val onCellClicked: (Int, Int) -> Unit) : View(context) {

    private val linePaint = Paint().apply {
        color = Color.DKGRAY
        strokeWidth = 5f
        style = Paint.Style.STROKE
    }

    private val p1Paint = Paint().apply { color = Color.parseColor("#FF5252") }
    private val p2Paint = Paint().apply { color = Color.parseColor("#448AFF") }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cellWidth = width.toFloat() / engine.cols
        val cellHeight = height.toFloat() / engine.rows

        // Draw Grid
        for (i in 0..engine.cols) {
            canvas.drawLine(i * cellWidth, 0f, i * cellWidth, height.toFloat(), linePaint)
        }
        for (j in 0..engine.rows) {
            canvas.drawLine(0f, j * cellHeight, width.toFloat(), j * cellHeight, linePaint)
        }

        // Draw Atoms/Mass
        val radius = Math.min(cellWidth, cellHeight) / 4f
        for (i in 0 until engine.cols) {
            for (j in 0 until engine.rows) {
                val cell = engine.grid[i][j]
                if (cell.mass > 0) {
                    val paint = if (cell.owner == 1) p1Paint else p2Paint
                    val cx = i * cellWidth + cellWidth / 2
                    val cy = j * cellHeight + cellHeight / 2
                    
                    // Simple offset drawing for multiple mass
                    when (cell.mass) {
                        1 -> canvas.drawCircle(cx, cy, radius, paint)
                        2 -> {
                            canvas.drawCircle(cx - radius/2, cy, radius, paint)
                            canvas.drawCircle(cx + radius/2, cy, radius, paint)
                        }
                        3 -> {
                            canvas.drawCircle(cx, cy - radius/2, radius, paint)
                            canvas.drawCircle(cx - radius/2, cy + radius/2, radius, paint)
                            canvas.drawCircle(cx + radius/2, cy + radius/2, radius, paint)
                        }
                    }
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val cellWidth = width / engine.cols
            val cellHeight = height / engine.rows
            val x = (event.x / cellWidth).toInt().coerceIn(0, engine.cols - 1)
            val y = (event.y / cellHeight).toInt().coerceIn(0, engine.rows - 1)
            onCellClicked(x, y)
            return true
        }
        return super.onTouchEvent(event)
    }
}
