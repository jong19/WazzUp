package com.example.delfino.wazzup

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_account_info.*


/*
    FUNC
     * Encrypt password when added to firebase_database
     * Can add existing username if (USERNAME_ALREADY_EXIST && EMAIL_NOT_EXIST)

     *
     UI
     * Password show/hide icon overlaps with error message icon
     *
     *
     * */

class AccountInfoActivity : AppCompatActivity() {

    val fbauth = FirebaseAuth.getInstance()

    val fbdb2 = FirebaseDatabase.getInstance().reference

    //var current_user : String = ""
    var current_user = fbauth.currentUser?.uid

    var username : String =  ""
    var email : String = ""
    var password : String = ""

    var username_flag = 0
    var email_flag = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_info)

        ai_iv_btnArrowNext.setOnClickListener {

            Toast.makeText(this@AccountInfoActivity, "BEFORE ADDING $current_user", Toast.LENGTH_SHORT).show()

            checkUsername()

            checkEmail()

            addAuthentication()

            // saveAccountInfo()

        }


    }


    fun checkUsername() : Boolean {

        username = ai_et_username.text.toString()

        val query : Query = fbdb2.child("AccountInfo").orderByChild("username").equalTo(username)
        query.addListenerForSingleValueEvent(object : ValueEventListener {


            override fun onCancelled(p0: DatabaseError) {
                Log.d("AccountInfoActivity", "onCancelled executed")
            }


            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    // Toast.makeText(this@AccountInfoActivity, "Username already in use", Toast.LENGTH_SHORT).show()
                    // ai_et_username.error = "Username already in use"
                    username_flag = 0


                }
                else{
                    // addAuthentication()s
                    username_flag = 1


                }
            }
        })

        // Toast.makeText(this@AccountInfoActivity, "$username_flag", Toast.LENGTH_SHORT).show()

        return if (username_flag == 1) true else false

    }


    fun checkEmail() : Boolean {

        email = ai_et_email.text.toString()

        val query : Query = fbdb2.child("AccountInfo").orderByChild("email").equalTo(email)

        query.addListenerForSingleValueEvent(object : ValueEventListener {


            override fun onCancelled(p0: DatabaseError) {
                Log.d("AccountInfoActivity", "onCancelled executed")
            }


            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    // Toast.makeText(this@AccountInfoActivity, "Username already in use", Toast.LENGTH_SHORT).show()
                    // ai_et_username.error = "Username already in use"
                    email_flag = 0


                }
                else{
                    // addAuthentication()
                    email_flag = 1


                }
            }
        })

        // Toast.makeText(this@AccountInfoActivity, "$username_flag", Toast.LENGTH_SHORT).show()

        return if (email_flag == 1) true else false

    }



    fun addAuthentication(){


        username = ai_et_username.text.toString()
        email = ai_et_email.text.toString()
        password = ai_et_password.text.toString()

        if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty()) {

            ai_pb_progressBar.setVisibility(View.VISIBLE)


            fbauth.createUserWithEmailAndPassword(email, password)

                .addOnCompleteListener{

                    if (!checkUsername()){
                        ai_pb_progressBar.setVisibility(View.INVISIBLE)
                        ai_et_username.error = "Username already in use"
                        //  Toast.makeText(this@AccountInfoActivity, "username_flag is $username_flag AND checkUsername() is ${checkUsername()}", Toast.LENGTH_SHORT).show()

                    }

                    if (!it.isSuccessful) { // as long as this evaluates to true, this automatically saves email in the Firebase Authentication not in Db regardless if username already exist

                        ai_pb_progressBar.setVisibility(View.INVISIBLE)

                        try {
                            throw it.exception!! //  THIS ONE THROWS NULL POINTER EXCEPTION. Thrown when USERNAME=EXIST && EMAIL=!EXIST

                        }

                        catch (emailAlreadyExist: FirebaseAuthUserCollisionException) {
                            //Toast.makeText(this, emailAlreadyExist.message, Toast.LENGTH_SHORT).show()
                            ai_et_email.error = "Email Address already in use"
                            // auth_flag = 0
                        }

                        catch (emailMalformed: FirebaseAuthInvalidCredentialsException) {
                            //Toast.makeText(this, emailMalformed.message, Toast.LENGTH_SHORT).show()
                            ai_et_email.error = "Invalid email address format"
                            // auth_flag = 0
                        }

                    }

                    current_user = fbauth.currentUser?.uid.toString()
                    Toast.makeText(this@AccountInfoActivity, "AFTER ADDING NEW $current_user", Toast.LENGTH_LONG).show()


                    if (checkUsername() && checkEmail() && it.isSuccessful){
                        // if USERNAME_ALREADY_EXIST && EMAIL/PASSWORD_IS_OKAY. It still saves into DB.
                        // as long as it.isSuccessful() evaluates to true, this automatically saves email in the Firebase Authentication. It will not save a copy in Firebase DB
                        saveAccountInfo()

                    }

                }

        }
        else{
            Toast.makeText(this, "Please fillup all the fields", Toast.LENGTH_SHORT).show()

        }


    }


    fun saveAccountInfo(){

        val fbdb = FirebaseDatabase.getInstance().getReference("/AccountInfo/$current_user")

        val accountInfo = AccountInfo(
            current_user.toString(),
            username,
            email,
            password
        )


        fbdb.setValue(accountInfo)
            .addOnCompleteListener {

                if (it.isSuccessful) {
                    ai_pb_progressBar.setVisibility(View.INVISIBLE)
                    Toast.makeText(this, "${ai_et_username.text.toString()} successfully saved", Toast.LENGTH_SHORT)
                        .show()
                    ai_clear()
                    ai_et_username.requestFocus()
                    Log.d("AccountInfoActivity", "Successfully added user to database")

                    val to_ui = Intent(this, UserInfoActivity::class.java)
                    startActivity(to_ui)
                }
            }


    }


    fun ai_clear(){
        ai_et_username.text.clear()
        ai_et_email.text.clear()
        ai_et_password.text.clear()


    }




}

