<h1 align="center">Social Login Helper For Android</h1>
<p align="center">
  <a href="https://android-arsenal.com/api?level=21"> <img src="https://img.shields.io/badge/API-21%2B-blue.svg?style=flat" /></a>
  <a href="https://jitpack.io/#socialUltimus/Social_Login/"> <img src="https://jitpack.io/v/socialUltimus/Social_Login.svg" /></a>

  <br /><br />A simple andorid library that helps you integrate social login into your apps. It supports Facebook, Instagram, Twitter.
  <br /><br/>
  <img src="https://github.com/socialUltimus/Social_Login/blob/master/app/src/main/res/drawable/image.JPEG" width="350" />
</p>

## Supported Social Networks

1. Facebook
2. Instagram
3. Twitter


## How to integrate into your app?
A few changes in the build gradle and your all ready to use the library. Make the following changes.

Step 1. Add the JitPack repository to your build file. Add it in your root build.gradle at the end of repositories:

```java
allprojects {
  repositories {
    ...
    maven { url "https://jitpack.io" }
  }
}
```
Step 2. Add the dependency
```kotlin
dependencies {
	        implementation 'com.github.socialUltimus:Social_Login:0.0.1'
	}
```

## How to use the library?
Okay seems like you integrated the library in your project but **how do you use it**? Well its really easy if you want to use any login within above three just create a button and set an onClickLister to perform the following.

**Facebook**

1. For facebook login you will need to add the following to your android manifest
```xml
    <meta-data
        android:name="com.facebook.sdk.ApplicationId"
        android:value="@string/facebook_app_id" />
```
2. create helper instance in onCreate method, then register onActivity result and implement FacebookListener
```kotlin
 val mFacebook = FacebookHelper(this)
```
override onActivityResult and put below one line code as mention

```kotlin
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mFacebook?.onActivityResult(requestCode, resultCode, data)
    }
```

3. Done, all you have to do is call below method from where you want to perform facebook login operation
```kotlin

 mFacebook?.performSignIn(this)

```
appropriate result reflate in according listener

```kotlin
  override fun onFbSignInFail(errorMessage: String?) {
         data_received_text_view.setText(errorMessage)
     }

     override fun onFbSignInSuccess(
         accessToken: String,
         userId: String?,
         name: String,
         email: String,
         avatar: String
     ) {


     }

     override fun onFBSignOut() {
     }

```


**Instagram**

1. create helper instance in onCreate method and implement InstagramListener

```kotlin
val mInstagram = InstagramHelper(
            this,
            this,
            "Client Id",
            "Client Secret Id",
            "callback url"
        )
        // you can use  https://instagram.com/ as callback url
```

2. Done, all you have to do is call below method from where you want to perform instagram login operation

```kotlin
 mInstagram?.performSignIn()

```

appropriate result reflate in according listener

```kotlin
   override fun onInstagramSignInFail(errorMessage: String?) {


      }

      override fun onInstagramSignInSuccess(
          authToken: String?,
          userId: String?,
          userName: String?,
          userEmail: String?,
          avatar: String?
      ) {



      }

```

**Twitter**

1. create helper instance in onCreate method and implement TwitterListener

```kotlin
  mTwitter = TwitterHelper(this, this, "CONSUMER_KEY Key", "CONSUMER_SECRET Key")

```
2. Done, all you have to do is call below method from where you want to perform instagram login operation

```kotlin
 mTwitter?.performSignIn()

```

appropriate result reflate in according listener

```kotlin
   override fun onTwitterError(errorMessage: String?) {

       }

       override fun onTwitterSignIn(authToken: String?, secret: String?, userId: Long) {


       }

```

