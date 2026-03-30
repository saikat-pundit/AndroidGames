package com.example.chainreact

data class Cell(var owner: Int = 0, var mass: Int = 0)

class GameEngine(val cols: Int = 6, val rows: Int = 9) {
    val grid = Array(cols) { Array(rows) { Cell() } }
    var currentPlayer = 1 // 1 = Red, 2 = Blue
    var isGameOver = false

    fun play(x: Int, y: Int): Boolean {
        if (isGameOver) return false
        if (grid[x][y].owner != 0 && grid[x][y].owner != currentPlayer) return false

        grid[x][y].owner = currentPlayer
        grid[x][y].mass++
        
        var exploded = checkExplosions()
        while(exploded) {
            exploded = checkExplosions()
        }

        if (checkWinCondition()) {
            isGameOver = true
            return true
        }

        currentPlayer = if (currentPlayer == 1) 2 else 1
        return true
    }

    private fun checkExplosions(): Boolean {
        var didExplode = false
        val explosions = mutableListOf<Pair<Int, Int>>()

        // Find all unstable cells
        for (i in 0 until cols) {
            for (j in 0 until rows) {
                val maxMass = getNeighbors(i, j).size
                if (grid[i][j].mass >= maxMass) {
                    explosions.add(Pair(i, j))
                }
            }
        }

        // Resolve explosions
        for ((x, y) in explosions) {
            didExplode = true
            val owner = grid[x][y].owner
            grid[x][y].mass = 0
            grid[x][y].owner = 0

            for ((nx, ny) in getNeighbors(x, y)) {
                grid[nx][ny].owner = owner
                grid[nx][ny].mass++
            }
        }
        return didExplode
    }

    private fun getNeighbors(x: Int, y: Int): List<Pair<Int, Int>> {
        val neighbors = mutableListOf<Pair<Int, Int>>()
        if (x > 0) neighbors.add(Pair(x - 1, y))
        if (x < cols - 1) neighbors.add(Pair(x + 1, y))
        if (y > 0) neighbors.add(Pair(x, y - 1))
        if (y < rows - 1) neighbors.add(Pair(x, y + 1))
        return neighbors
    }

    private fun checkWinCondition(): Boolean {
        var hasPlayer1 = false
        var hasPlayer2 = false
        var totalMass = 0

        for (i in 0 until cols) {
            for (j in 0 until rows) {
                totalMass += grid[i][j].mass
                if (grid[i][j].owner == 1) hasPlayer1 = true
                if (grid[i][j].owner == 2) hasPlayer2 = true
            }
        }
        // Game can only be won after the first round (totalMass > 1)
        return totalMass > 1 && (!hasPlayer1 || !hasPlayer2)
    }

    fun getAIMove(): Pair<Int, Int>? {
        val validMoves = mutableListOf<Pair<Int, Int>>()
        var bestMove: Pair<Int, Int>? = null
        var bestScore = Int.MIN_VALUE

        for (i in 0 until cols) {
            for (j in 0 until rows) {
                if (grid[i][j].owner == 0 || grid[i][j].owner == 2) {
                    validMoves.add(Pair(i, j))
                    val simulatedEngine = this.clone()
                    simulatedEngine.play(i, j)
                    if (simulatedEngine.isGameOver && simulatedEngine.currentPlayer == 2) {
                        return Pair(i, j)
                    }
                    val score = evaluateBoard(simulatedEngine)
                    if (score > bestScore || (score == bestScore && Math.random() > 0.5)) {
                        bestScore = score
                        bestMove = Pair(i, j)
                    }
                }
            }
        }
        
        return bestMove ?: if (validMoves.isNotEmpty()) validMoves.random() else null
    }
    private fun clone(): GameEngine {
        val copy = GameEngine(cols, rows)
        copy.currentPlayer = this.currentPlayer
        copy.isGameOver = this.isGameOver
        for (i in 0 until cols) {
            for (j in 0 until rows) {
                copy.grid[i][j].owner = this.grid[i][j].owner
                copy.grid[i][j].mass = this.grid[i][j].mass
            }
        }
        return copy
    }
    private fun evaluateBoard(simulatedEngine: GameEngine): Int {
        var myAtoms = 0
        var enemyAtoms = 0
        var vulnerability = 0
        
        for (i in 0 until cols) {
            for (j in 0 until rows) {
                val cell = simulatedEngine.grid[i][j]
                
                if (cell.owner == 2) { // Computer's atoms
                    myAtoms += cell.mass
                    for ((nx, ny) in simulatedEngine.getNeighbors(i, j)) {
                        val neighbor = simulatedEngine.grid[nx][ny]
                        val neighborCriticalMass = simulatedEngine.getNeighbors(nx, ny).size - 1
                        
                        if (neighbor.owner == 1 && neighbor.mass == neighborCriticalMass) {
                            vulnerability += 20 // HUGE penalty for leaving atoms in danger
                        }
                    }
                } else if (cell.owner == 1) { // Player's atoms
                    enemyAtoms += cell.mass
                }
            }
        }
        return (myAtoms * 2) - enemyAtoms - vulnerability
    }
}
