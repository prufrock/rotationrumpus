package com.dkanen.rotationrumpus.game.map

data class MapData(
    val width: Int,
    val tiles: List<Tile>,
    val things: List<Thing>,
)
