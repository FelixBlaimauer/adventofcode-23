import java.io.File

fun getResourceFile(path: String) = File("src/main/resources/$path")

enum class RectDirection {UP, RIGHT, DOWN, LEFT}

enum class Direction { UP, UP_RIGHT, RIGHT, DOWN_RIGHT, DOWN, DOWN_LEFT, LEFT, UP_LEFT }

val DirectionVectors = mapOf(
    Direction.UP to Vec2(0, -1),
    Direction.UP_RIGHT to Vec2(1, -1),
    Direction.RIGHT to Vec2(1, 0),
    Direction.DOWN_RIGHT to Vec2(1, 1),
    Direction.DOWN to Vec2(0, 1),
    Direction.DOWN_LEFT to Vec2(-1, 1),
    Direction.LEFT to Vec2(-1, 0),
    Direction.UP_LEFT to Vec2(-1, -1)
)

val RectDirectionVectors = mapOf(
    RectDirection.UP to Vec2(0, -1),
    RectDirection.RIGHT to Vec2(1, 0),
    RectDirection.DOWN to Vec2(0, 1),
    RectDirection.LEFT to Vec2(-1, 0),
)

val OpposingRectDirection = mapOf(
    RectDirection.UP to RectDirection.DOWN,
    RectDirection.RIGHT to RectDirection.LEFT,
    RectDirection.DOWN to RectDirection.UP,
    RectDirection.LEFT to RectDirection.RIGHT
)

data class Vec2(val x: Int, val y: Int) {
    companion object {
        fun add(a: Vec2, b: Vec2) = Vec2(a.x + b.x, a.y + b.y)

        fun getNeighbors(vec: Vec2) = DirectionVectors.values.map { vec.add(it) }

        fun getRectNeighbors(vec: Vec2) = RectDirectionVectors.entries.associate { (key, value) -> key to vec.add(value) }

        fun getSafeNeighbors(vec: Vec2) = getNeighbors(vec).filter { it.x >= 0 && it.y >= 0 }

        fun getSafeRectNeighbors(vec: Vec2) = getRectNeighbors(vec).filterValues { it.x >= 0 && it.y >= 0 }

        fun getLineNeighbors(a: Vec2, b: Vec2): List<Vec2> {
            if (a == b) return a.getSafeNeighbors()

            val neighbors = mutableListOf<Vec2>()

            for (x in a.x..b.x) {
                val vec = Vec2(x, a.y)
                neighbors += vec.getSafeNeighbors()
            }

            return neighbors.distinct()
                .filter { it != a && it != b && (if (it.y != a.y) true else it.x < a.x || it.x > b.x) }
        }
    }

    fun add(vec: Vec2) = Companion.add(this, vec)

    fun getNeighbors() = Companion.getNeighbors(this)

    fun getRectNeighbors() = Companion.getRectNeighbors(this)

    fun getSafeNeighbors() = Companion.getSafeNeighbors(this)

    fun getSafeRectNeighbors() = Companion.getSafeRectNeighbors(this)
}
