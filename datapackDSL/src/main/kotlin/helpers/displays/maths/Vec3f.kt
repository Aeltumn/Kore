package helpers.displays.maths

import arguments.Vec3
import kotlinx.serialization.Serializable
import org.joml.Vector3f
import kotlin.math.absoluteValue
import kotlin.math.sqrt

@Serializable
data class Vec3f(var x: Float = 0f, var y: Float = 0f, var z: Float = 0f) {
	constructor(x: Number, y: Number, z: Number) : this(x.toFloat(), y.toFloat(), z.toFloat())
	constructor(vec3: Vec3) : this(vec3.x.value, vec3.y.value, vec3.z.value)
	constructor(vec3: Vector3f) : this(vec3.x, vec3.y, vec3.z)

	val length get() = sqrt(x * x + y * y + z * z)
	val lengthSquared get() = x * x + y * y + z * z

	operator fun unaryMinus() = Vec3f(-x, -y, -z)
	operator fun plus(other: Vec3f) = Vec3f(x + other.x, y + other.y, z + other.z)
	operator fun minus(other: Vec3f) = Vec3f(x - other.x, y - other.y, z - other.z)
	operator fun times(scalar: Float) = Vec3f(x * scalar, y * scalar, z * scalar)
	operator fun div(scalar: Float) = Vec3f(x / scalar, y / scalar, z / scalar)

	fun abs() = Vec3f(x.absoluteValue, y.absoluteValue, z.absoluteValue)

	fun cross(other: Vec3f) = Vec3f(
		y * other.z - z * other.y,
		z * other.x - x * other.z,
		x * other.y - y * other.x
	)

	fun distance(other: Vec3f) = (this - other).length
	fun distanceSquared(other: Vec3f) = (this - other).lengthSquared

	fun dot(other: Vec3f) = x * other.x + y * other.y + z * other.z
	fun lerp(other: Vec3f, t: Float) = this + (other - this) * t

	fun negate() = -this

	fun normalize() = when (val length = length) {
		0f -> ZERO
		else -> this / length
	}

	fun toQuaternion() = Quaternion.fromEulerAngles(this)
	fun reciprocal() = Vec3f(1f / x, 1f / y, 1f / z)

	companion object {
		val ZERO = Vec3f(0f, 0f, 0f)
		val X_AXIS = Vec3f(1f, 0f, 0f)
		val Y_AXIS = Vec3f(0f, 1f, 0f)
		val Z_AXIS = Vec3f(0f, 0f, 1f)

		val LEFT = X_AXIS
		val RIGHT = -X_AXIS
		val UP = Y_AXIS
		val DOWN = -Y_AXIS
		val FORWARD = Z_AXIS
		val BACKWARD = -Z_AXIS
	}
}
