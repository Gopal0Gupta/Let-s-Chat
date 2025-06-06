package com.gopal.letschat

import android.icu.util.Calendar
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.ktx.toObject

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.FirebaseStorage
import com.gopal.letschat.data.ChatData
import com.gopal.letschat.data.ChatUser
import com.gopal.letschat.data.Chats
import com.gopal.letschat.data.Event
import com.gopal.letschat.data.MESSAGE
import com.gopal.letschat.data.Message
import com.gopal.letschat.data.UserData
import com.gopal.letschat.data.user_node
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlin.Exception

@HiltViewModel
class LCViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore,
    val storage: FirebaseStorage
) : ViewModel() {

    var inProcess by mutableStateOf(false)
    var inProcessChats by mutableStateOf(false)
    var eventmutablestate by mutableStateOf<Event<String>?>(null)
    var signIn by mutableStateOf(false)
    var userdata by mutableStateOf<com.gopal.letschat.data.UserData?>(null)
    val chats = mutableStateOf<List<ChatData>>(listOf())
    var chatMessages by mutableStateOf<List<Message>>(listOf())
    var inProgressChatMessage by mutableStateOf(false)
    var currentChatMessageListener : ListenerRegistration? = null


    init {
        val currentuser = auth.currentUser
        signIn = currentuser != null
        currentuser?.uid?.let {
            getUserDate(it)
        }
    }

    fun populateMessage(chatId: String){
        inProgressChatMessage = true
        currentChatMessageListener = db.collection(Chats).document(chatId).collection(MESSAGE).addSnapshotListener{
            value , error ->
            if(error!=null){
                handleException(error)
            }
            if (value!=null){
                chatMessages = value.documents.mapNotNull {
                    it.toObject<Message>()
                }.sortedBy { it.timestamp }
                inProgressChatMessage = false
            }
        }
    }

    fun dePopulateMessage(){
        chatMessages = listOf()
        currentChatMessageListener = null
    }

    fun populateChats(){
        inProcessChats = true
        db.collection(Chats).where(
            Filter.or(
                Filter.equalTo("user1.userId",userdata?.userId),
                Filter.equalTo("user2.userId",userdata?.userId),
            )
        ).addSnapshotListener{
            value,error->
            if (error!=null){
                handleException(error)
            }
            if(value!=null){
                chats.value = value.documents.mapNotNull {
                    it.toObject<ChatData>()
                }
                inProcessChats = false
            }
        }
    }

    fun onSendReply(chatId:String,message:String){
        var time = Calendar.getInstance().time.toString()
        val msg = Message(userdata?.userId,message,time)
        db.collection(Chats).document(chatId).collection(MESSAGE).document().set(msg)
    }

    fun signUp(
        name: String,
        number: String,
        email: String,
        password: String,
    ) {
        inProcess = true
        if (name.isEmpty() or number.isEmpty() or email.isEmpty() or password.isEmpty()) {
            handleException(customMessage = "Please Fill All Fields")
            return
        }
        inProcess = true
        db.collection(user_node).whereEqualTo("number", number).get().addOnSuccessListener {
            if (it.isEmpty) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        signIn = true
                        createOrUpdateProfile(name, number)
                    } else {
                        handleException(it.exception, customMessage = "Sign Up Failed")
                    }
                }
            } else {
                handleException(customMessage = "Number Already Exists")
                inProcess = false
            }
        }
    }

    fun logIn(email: String, password: String) {
        if (email.isEmpty() or password.isEmpty()) {
            handleException(customMessage = "Please Fill All Fields")
            return
        } else {
            inProcess = true
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    signIn = true
                    inProcess = false
                    auth.currentUser?.uid?.let {
                        getUserDate(it)
                    }
                } else {
                    handleException(exception = it.exception, customMessage = "Login Failed")
                }
            }
        }
    }

    fun uploadProfileImage(uri: Uri) {
        UploadImage(uri) {
            createOrUpdateProfile(imageurl = it.toString())
        }
    }

    fun UploadImage(uri: Uri, onSuccess: (Uri) -> Unit) {
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

    fun createOrUpdateProfile(
        name: String? = null,
        number: String? = null,
        imageurl: String? = null
    ) {
        val uid = auth.currentUser?.uid
        val userData = com.gopal.letschat.data.UserData(
            userId = uid,
            name = name ?: userdata?.name,
            number = number ?: userdata?.number,
            imageUrl = imageurl ?: userdata?.imageUrl
        )

        uid?.let {
            inProcess = true
            db.collection(user_node).document(uid).get().addOnSuccessListener {
                if (it.exists()) {
                    //updatedata
                    db.collection(user_node).document(uid)
                        .update(
                            mapOf(
                                "name" to userData.name,
                                "number" to userData.number,
                                "imageUrl" to userData.imageUrl
                            )
                        )
                        .addOnSuccessListener {
                            inProcess = false
                            getUserDate(uid)
                        }
                        .addOnFailureListener {
                            inProcess = false
                            handleException(it, "Failed to update profile")
                        }
                } else {
                    db.collection(user_node).document(uid).set(userData)
                    inProcess = false
                    getUserDate(uid)
                }
            }.addOnFailureListener {
                handleException(it, "Cannot Retrive User")
            }
        }

    }

    private fun getUserDate(uid: String) {
        inProcess = true
        db.collection(user_node).document(uid).addSnapshotListener { value, error ->
            if (error != null) {
                handleException(error, "Cannot Retrive User")
            }
            if (value != null) {
                val user = value.toObject(UserData::class.java)
                userdata = user
                inProcess = false
                populateChats()
            }
        }
    }

    fun handleException(exception: Exception? = null, customMessage: String = "") {
        Log.e("Let's Chat app", "Let's Chat exception : ", exception)
        exception?.printStackTrace()
        val errormsg = exception?.localizedMessage ?: ""
        val message = if (customMessage.isNullOrEmpty()) errormsg else customMessage
        eventmutablestate = Event(message)
        inProcess = false
    }

    fun Logout() {
        auth.signOut()
        signIn = false
        userdata = null
        dePopulateMessage()
        currentChatMessageListener = null
        eventmutablestate = Event("Logged Out")
    }

    fun onAddChat(number: String) {
        if (number.isEmpty() or !number.isDigitsOnly()) {
            handleException(customMessage = "Number Must Contain Digits Only")
            return
        }
        db.collection(Chats).where(
                Filter.or(
                    Filter.and(
                        Filter.equalTo("user1.number", number),
                        Filter.equalTo("user2.number", userdata?.number)
                    ),
                    Filter.and(
                        Filter.equalTo("user1.number", userdata?.number),
                        Filter.equalTo("user2.number", number)
                    )
                )
            ).get().addOnSuccessListener {
                if (it.isEmpty) {
                    db.collection(user_node).whereEqualTo("number", number).get()
                        .addOnSuccessListener {
                            if(it.isEmpty){
                                handleException(customMessage = "Number Not Found")
                            }else{
                                val chatPartner = it.toObjects<UserData>()[0]
                                val id = db.collection(Chats).document().id
                                val chat = ChatData(
                                    chatId = id,
                                    ChatUser(userdata?.userId,userdata?.name,userdata?.imageUrl,userdata?.number),
                                    ChatUser(chatPartner.userId,chatPartner.name,chatPartner.imageUrl,chatPartner.number)
                                )
                                db.collection(Chats).document(id).set(chat)
                            }
                        }.addOnFailureListener {
                            handleException(it)
                        }
                } else {
                    handleException(customMessage = "Chat Already Exists")
                }
            }
    }
}

