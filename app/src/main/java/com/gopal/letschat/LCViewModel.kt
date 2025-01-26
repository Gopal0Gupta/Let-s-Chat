package com.gopal.letschat

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.gopal.letschat.data.Event
import com.gopal.letschat.data.UserData
import com.gopal.letschat.data.user_node
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.Exception

@HiltViewModel
class LCViewModel @Inject constructor(
    val auth : FirebaseAuth,
    val db: FirebaseFirestore
) : ViewModel() {

    var inProcess by mutableStateOf(false)
    var eventmutablestate by mutableStateOf<Event<String>?>(null)
    var signIn by mutableStateOf(false)
    var userdata by mutableStateOf<com.gopal.letschat.data.UserData?>(null)
    init {
        val currentuser = auth.currentUser
        signIn = currentuser != null
        currentuser?.uid?.let {
            getUserDate(it)
        }
    }
    fun signUp(name : String, number : String ,email : String, password : String){
        inProcess = true
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            if (it.isSuccessful){
                signIn = true
                createOrUpdateProfile(name,number)
            }else{
                handleException(it.exception, customMessage = "Sign Up Failed")
            }
        }
    }

    fun createOrUpdateProfile(name: String?=null, number: String?=null,imageurl : String?=null) {
        val uid = auth.currentUser?.uid
        val userData = com.gopal.letschat.data.UserData(
            userId = uid,
            name = name?:userdata?.name,
            number = number?:userdata?.number,
            imageUrl = imageurl?:userdata?.imageUrl
        )

        uid?.let {
            inProcess = true
            db.collection(user_node).document(uid).get().addOnSuccessListener {
                if(it.exists()){
                    //updatedata
                }else{
                    db.collection(user_node).document(uid).set(userData)
                    inProcess = false
                    getUserDate(uid)
                }
            }.addOnFailureListener{
                handleException(it,"Cannot Retrive User")
            }
        }

    }

    private fun getUserDate(uid: String) {
        inProcess = true
        db.collection(user_node).document(uid).addSnapshotListener { value, error ->
            if(error!=null){
                handleException(error,"Cannot Retrive User")
            }
            if (value!=null){
                val user = value.toObject(UserData::class.java)
                userdata=user
                inProcess = false
            }
        }
    }

    fun handleException(exception: Exception?=null,customMessage : String = ""){
        Log.e("Let's Chat app","Let's Chat exception : ",exception)
        exception?.printStackTrace()
        val errormsg = exception?.localizedMessage?:""
        val message = if (customMessage.isNullOrEmpty()) errormsg else customMessage

        eventmutablestate = Event(message)
        inProcess = false
    }
}

