package com.dkanen.rotationrumpus.collections.graphs

/**
 * Connects to vertices. It is the only way!
 */
data class Edge<T: Any> (
    val source: Vertex<T>,
    val destination: Vertex<T>,
    val weight: Double? = null
)