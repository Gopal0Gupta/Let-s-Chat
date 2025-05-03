package com.gopal.letschat

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.gopal.letschat.data.Event
import com.gopal.letschat.data.UserData
import com.gopal.letschat.data.user_node
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlin.Exception

@HiltViewModel
class LCViewModel @Inject constructor(
    val auth : FirebaseAuth,
    val db: FirebaseFirestore,
    val storage: FirebaseStorage
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
    fun signUp(
        name: String,
        number: String,
        email: String,
        password: String,
    ){
        inProcess = true
        if(name.isEmpty() or number.isEmpty() or email.isEmpty() or password.isEmpty()){
            handleException(customMessage = "Please Fill All Fields")
            return
        }
        inProcess = true
        db.collection(user_node).whereEqualTo("number",number).get().addOnSuccessListener {
            if(it.isEmpty){
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful){
                        signIn = true
                        createOrUpdateProfile(name,number)
                    }else{
                        handleException(it.exception, customMessage = "Sign Up Failed")
                    }
                }
            }else{
                handleException(customMessage = "Number Already Exists")
                inProcess = false
            }
        }
    }

    fun logIn(email: String,password: String){
        if(email.isEmpty() or password.isEmpty()){
            handleException(customMessage = "Please Fill All Fields")
            return
        }else{
            inProcess = true
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                if(it.isSuccessful){
                    signIn = true
                    inProcess = false
                    auth.currentUser?.uid?.let {
                        getUserDate(it)
                    }
                }else{
                    handleException(exception = it.exception,customMessage = "Login Failed")
                }
            }
        }
    }

    fun uploadProfileImage(uri : Uri){
        UploadImage(uri){
            createOrUpdateProfile(imageurl = it.toString())
        }
    }

    fun UploadImage(uri: Uri, onSuccess:(Uri)->Unit){
        inProcess = true
        val StorageRef = storage.reference
        val uuid = UUID.randomUUID()
        val imageRef = StorageRef.child("images/${uuid}")
        val uploadTask = imageRef.putFile(uri)
        uploadTask.addOnSuccessListener {
            val result = it.metadata?.reference?.downloadUrl
            result?.addOnSuccessListener(onSuccess)
            inProcess = false
        }.addOnFailureListener {
            handleException(it)
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

