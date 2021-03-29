package com.khangle.mediaplayerapp.exception

class NoResultException: Exception() {
    override val message: String?
        get() = "No result"
}