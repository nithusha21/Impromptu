package com.codebreakers.apps.impromptu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

public class RegisterActivity extends Activity
{
  private final static java.text.SimpleDateFormat SIMPLE_DATE_FORMAT = new java.text.SimpleDateFormat( "yyyy/MM/dd" );

  private EditText emailField;
  private EditText nameField;
  private EditText passwordField;
  private EditText phoneNumber;


  private Button registerButton;

  private String email;
  private String name;
  private String password;
  private String phone;


  private BackendlessUser user;

  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_register_form );

    initUI();
  }

  private void initUI()
  {emailField = (EditText) findViewById( R.id.emailField );
    nameField = (EditText) findViewById( R.id.nameField );
    passwordField = (EditText) findViewById( R.id.passwordField );
    phoneNumber = (EditText)findViewById(R.id.phoneNumber);

    registerButton = (Button) findViewById( R.id.registerButton );

    registerButton.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View view )
      {
        onRegisterButtonClicked();
      }
    } );
  }

  private void onRegisterButtonClicked()
  {
    String emailText = emailField.getText().toString().trim();
    String nameText = nameField.getText().toString().trim();
    String passwordText = passwordField.getText().toString().trim();
    String phoneText = phoneNumber.getText().toString().trim();



    if ( emailText.isEmpty() )
    {
      showToast( "Field 'email' cannot be empty." );
      return;
    }

    if ( passwordText.isEmpty() )
    {
      showToast( "Field 'password' cannot be empty." );
      return;
    }
    if ( phoneText.isEmpty() )
    {
      showToast( "Field 'phonenumber' cannot be empty." );
      return;
    }



    if( !emailText.isEmpty() )
    {
      email = emailText;
    }

    if( !nameText.isEmpty() )
    {
      name = nameText;
    }

    if( !phoneText.isEmpty() )
    {
      phone = phoneText;
    }



    if( !passwordText.isEmpty() )
    {
      password = passwordText;
    }

    user = new BackendlessUser();



    if( email != null )
    {
      user.setProperty( "email", email );
    }

    if( phone != null )
    {
      user.setProperty("phonenumber",phone);
    }



    if( name != null )
    {
      user.setProperty( "name", name );
    }

    if( password != null )
    {
      user.setProperty( "password", password );
    }

    Backendless.UserService.register( user, new DefaultCallback<BackendlessUser>( RegisterActivity.this )
    {
      @Override
      public void handleResponse( BackendlessUser response )
      {
        super.handleResponse( response );
        startActivity( new Intent( RegisterActivity.this, RegistrationSuccessActivity.class ) );
        finish();
      }
    } );
  }

  private void showToast( String msg )
  {
    Toast.makeText( this, msg, Toast.LENGTH_SHORT ).show();
  }
}