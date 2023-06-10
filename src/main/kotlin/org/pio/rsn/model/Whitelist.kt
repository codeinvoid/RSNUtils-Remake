package org.pio.rsn.model

import kotlinx.serialization.Serializable

@Serializable
data class Whitelist(val active: Boolean, val time: Long)

