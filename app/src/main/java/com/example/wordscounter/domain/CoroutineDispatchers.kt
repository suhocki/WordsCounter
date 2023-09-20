/*
 * Copyright © 2022 MyPizza. All Rights Reserved.
 */

package com.example.wordscounter.domain

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatchers {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
}
