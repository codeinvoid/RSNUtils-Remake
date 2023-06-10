package org.pio.rsn.model

import kotlinx.serialization.Serializable

@Serializable
data class Banned(val active: Boolean, val time: Long, val reason: String, val operator: String, val nanoid: String)
