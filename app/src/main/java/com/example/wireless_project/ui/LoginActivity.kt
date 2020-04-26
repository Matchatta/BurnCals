package com.example.wireless_project.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.wireless_project.Injection
import com.example.wireless_project.R
import com.example.wireless_project.database.entity.Exercises
import com.example.wireless_project.database.entity.Food
import com.example.wireless_project.database.entity.User
import com.example.wireless_project.ui.model.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(){

    //Google Login Request Code
    private val RC_SIGN_IN = 7
    //Google Sign In Client

    //Room Database
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewFoodModelFactory: FoodModelFactory
    private lateinit var viewExercisesModelFactory: ExercisesModelFactory
    private val viewModel: UserViewModel by viewModels{ viewModelFactory}
    private val viewFoodModel: FoodViewModel by viewModels{viewFoodModelFactory}
    private val viewExercisesModel: ExercisesViewModel by viewModels{viewExercisesModelFactory}
    private val disposable = CompositeDisposable()

    companion object{
        private lateinit var mGoogleSignInClient: GoogleSignInClient
        //Firebase Auth
        private lateinit var auth: FirebaseAuth
        fun getLaunchIntent(from: Context) : Intent{
            auth.signOut()
            mGoogleSignInClient.signOut()
            return Intent(from, LoginActivity::class.java)
        }
        fun refresh(from: Context): Intent{
            return Intent(from, LoginActivity::class.java)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        viewModelFactory = Injection.provideViewModelFactory(this)
        viewFoodModelFactory = Injection.provideFoodViewModelFactory(this)
        viewExercisesModelFactory = Injection.provideExercisesViewModelFactory(this)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        auth = FirebaseAuth.getInstance()
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso)
        google_button.setOnClickListener{
            signIn()
        }
    }
    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("Login", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("Login", "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Login", "signInWithCredential:success")
                    val user = auth.currentUser
//                    updateUI(user)
                    checkUser(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Login", "signInWithCredential:failure", task.exception)
                    Toast.makeText(this,"Auth Failed",Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun checkUser(user: FirebaseUser?){
        disposable.add(viewModel.getUser(user?.email.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {if(it.isNotEmpty()){
                updateUI(it.first())
            } else{
                startActivity(RegisterActivity.getLaunchIntent(this, auth))
            }})
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }
    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            checkUser(currentUser)
        }
    }

    private fun updateUI(user: User){
        var foodList: MutableList<Food> = mutableListOf()
        var exercisesList: MutableList<Exercises> = mutableListOf()
        disposable.add(viewFoodModel.getFood(user!!.email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if(foodList.isEmpty()){
                    foodList.addAll(it)
                }
            })
        disposable.add(viewExercisesModel.getExercises(user!!.email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if(exercisesList.isEmpty()){
                    exercisesList.addAll(it)
                }
                startActivity(MainActivity.getLaunchIntent(this, user, foodList, exercisesList))
            })
    }
}