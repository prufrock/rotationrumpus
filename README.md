# Rotation Rumpus

A project to test out the various methods for rotation objects in 3D. It uses [OpenRNDR](https://github.com/openrndr/openrndr) for rendering.

My plan is to implement the methods described in [gamemath.com Chapter 8 Rotation in Three Dimensions](https://gamemath.com/book/orient.html).

So far I've implemented:

8.7.2 Converting a Matrix to Euler Angles (skipping 8.7.1 Converting Euler Angles to a Matrix because it's pretty straightforward)

My implementation as Matrix33Exts.eulerAngles()

This lead me to some questions how about to get the matrix back to being orthogonal. I implemented the methods in [gamemath.com Chapter 6](https://gamemath.com/book/matrixmore.html#orthonogal_matrices_orthogonalizing).

Graham-Schmidt orthogonalization implemented as [Matrix33Exts.orthogonalize()](https://github.com/prufrock/rotationrumpus/blob/main/src/main/kotlin/com/dkanen/rotationrumpus/math/Matrix33Exts.kt)

Non-biased incremental orthogonalization implemented as [Matrix33Exts.orthoganlizeNonbiased(k: Double)](https://github.com/prufrock/rotationrumpus/blob/main/src/main/kotlin/com/dkanen/rotationrumpus/math/Matrix33Exts.kt3)

I experimented with ["renormalization"](https://varunagrawal.github.io/2020/02/11/fast-orthogonalization/) but feel even less confident about than I do the other methods.
