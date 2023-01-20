package com.femi.lasustudentreg.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference

class MainActivityRepository(
    private val firebaseAuth: FirebaseAuth,
    private val collectionReference: CollectionReference
) {
    fun getCollectionReference() = collectionReference
    fun getFirebaseAuth() = firebaseAuth
}