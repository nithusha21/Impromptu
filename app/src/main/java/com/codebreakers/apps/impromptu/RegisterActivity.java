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
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class RegisterActivity extends Activity
{
  private final static java.text.SimpleDateFormat SIMPLE_DATE_FORMAT = new java.text.SimpleDateFormat( "yyyy/MM/dd" );

  private EditText email;
  private EditText nameField;
  private EditText password;
  private EditText phoneNumber;


  private Button registerButton;

  private String emailstring;
  private String name;
  private String passwordstring;
  private String phone;


  private BackendlessUser user;

  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_register_form );

    initUI();
  }

  private void initUI()
  {
    email = (EditText) findViewById( R.id.email );
    nameField = (EditText) findViewById( R.id.nameField );
    password = (EditText) findViewById( R.id.password );
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
    String emailText = email.getText().toString().trim();
    String nameText = nameField.getText().toString().trim();
    String passwordText = password.getText().toString().trim();
    String phoneText = phoneNumber.getText().toString().trim();

    Backendless.Messaging.registerDevice("543151196737","default", new AsyncCallback<Void>() {
      @Override
      public void handleResponse(Void aVoid) {
        Toast.makeText(RegisterActivity.this, "Registered", Toast.LENGTH_SHORT).show();
      }

      @Override
      public void handleFault(BackendlessFault backendlessFault) {
        Toast.makeText(RegisterActivity.this, backendlessFault.getMessage(), Toast.LENGTH_SHORT).show();
      }
    });

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
      emailstring = emailText;
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
      passwordstring = passwordText;
    }

    user = new BackendlessUser();



    if( email != null )
    {
      user.setProperty( "email", emailstring );
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
      user.setProperty( "password", passwordstring );
    }

    user.setProperty("deviceID", Backendless.Messaging.getDeviceRegistration().getDeviceId());

    Backendless.UserService.register( user, new DefaultCallback<BackendlessUser>( RegisterActivity.this )
    {
      @Override
      public void handleResponse( BackendlessUser response )
      {
        super.handleResponse( response );
        startActivity( new Intent( RegisterActivity.this, MainControlActivity.class ) );
        finish();
      }
    } );
  }

  private void showToast( String msg )
  {
    Toast.makeText( this, msg, Toast.LENGTH_SHORT ).show();
  }
}