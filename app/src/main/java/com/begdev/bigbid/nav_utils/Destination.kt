package com.begdev.bigbid.nav_utils


sealed class Destination(protected val route: String, vararg params: String) {
    val fullRoute: String = if (params.isEmpty()) route else {
        val builder = StringBuilder(route)
        params.forEach { builder.append("/{${it}}") }
        builder.toString()
    }

    sealed class NoArgumentsDestination(route: String) : Destination(route) {
        operator fun invoke(): String = route
    }

    object LoginScreen : NoArgumentsDestination("login")
    object MarketScreen : NoArgumentsDestination("market")
    object MainScreen : NoArgumentsDestination("main")
    object ProfileScreen : NoArgumentsDestination("profile")

    object ItemScreen : Destination("item", "itemId") {
        const val ITEM_ID = "item"
        operator fun invoke(itemId: Int): String = route.appendParams(
            ITEM_ID to itemId,
        )
    }
}

internal fun String.appendParams(vararg params: Pair<String, Any?>): String {
    val builder = StringBuilder(this)

    params.forEach {
        it.second?.toString()?.let { arg ->
            builder.append("/$arg")
        }
    }

    return builder.toString()
}