package com.example.delfino.wazzup

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_account_info.*
import kotlinx.android.synthetic.main.activity_login.*

/*
* Should also allow Username not just email as credentials
* Should redirect to another screen after successful login.
*
* Add progressbar / loader after clicking LOGIN button
*
* */

class LoginActivity : AppCompatActivity() {

    val fbauth = FirebaseAuth.getInstance()
    val aiid = FirebaseAuth.getInstance().uid

    val fbdb = FirebaseDatabase.getInstance().getReference("/AccountInfo/$aiid")
    val fbdb2 = FirebaseDatabase.getInstance().reference

    var username_flag = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        li_btn_login.setOnClickListener {
           // checkUsername()
            loginUsers()
        }

        tv_createAccount.setOnClickListener {

            val intent_to_ai = Intent(this, AccountInfoActivity::class.java)
            startActivity(intent_to_ai)


        }
    }


    fun loginUsers(){

       // val username = ai_et_username.text.toString()
        val email = li_et_usernameEmail.text.toString()
        val password = li_et_password.text.toString()


        if(email.isEmpty()){
            li_et_usernameEmail.error = "Enter email address"
        }

        if(password.isEmpty()){
            li_et_password.error = "Enter password"
        }


        if(!email.isEmpty() && !password.isEmpty()){

            fbauth.signInWithEmailAndPassword(email, password).addOnCompleteListener {

                if (it.isSuccessful){
                    Toast.makeText(this, "Successfully logged in" , Toast.LENGTH_SHORT).show()

                }
                else{
                    Toast.makeText(this, "Email or Password is incorrect. Please check" , Toast.LENGTH_SHORT).show()

                }


            }
        }





    }


    /*
    fun checkUsername() : Boolean {

        val username = ai_et_username.text.toString()

        val query : Query = fbdb2.child("AccountInfo").orderByChild("username").equalTo(username)
        query.addListenerForSingleValueEvent(object : ValueEventListener {


            override fun onCancelled(p0: DatabaseError) {
                Log.d("AccountInfoActivity", "onCancelled executed")
            }


            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    // Toast.makeText(this@AccountInfoActivity, "Username already in use", Toast.LENGTH_SHORT).show()
                    // ai_et_username.error = "Username already in use"
                    username_flag = 1


                }
                else{
                    // addAuthentication()
                    username_flag = 0


                }
            }
        })

        // Toast.makeText(this@AccountInfoActivity, "$username_flag", Toast.LENGTH_SHORT).show()

        return if (username_flag == 1) true else false

    }
    */

}
