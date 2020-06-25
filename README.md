<h1 align="center">Social Login Helper For Android</h1>
<p align="center">
  <a href="https://android-arsenal.com/api?level=21"> <img src="https://img.shields.io/badge/API-21%2B-blue.svg?style=flat" /></a>
  <a href="https://jitpack.io/#mukeshsolanki/social-login-helper"> <img src="https://jitpack.io/v/mukeshsolanki/social-login-helper.svg" /></a>

  <br /><br />A simple andorid library that helps you integrate social login into your apps. It supports Facebook, Instagram, Twitter.
  <br /><br/>
 
</p>

## Supported Social Networks

1. Facebook
2. Instagram
3. Twitter


## How to integrate into your app?
Integrating the library into you app is extremely easy. A few changes in the build gradle and your all ready to use the library. Make the following changes.

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
```java
dependencies {
	        implementation 'com.github.socialUltimus:Social_Login:0.0.1'
	}
```

## How to use the library?
Okay seems like you integrated the library in your project but **how do you use it**? Well its really easy if you want to user facebook login just create a button and set an onClickLister to perform the following.

```java
FacebookHelper facebookHelper=new FacebookHelper(new FacebookListener() {
  @Override public void onFbSignInFail(String errorMessage) {
    
  }

  @Override public void onFbSignInSuccess(String authToken, String userId) {
    //Save the token or do what every you want here
  }

  @Override public void onFBSignOut() {

  }
})
```
That's pretty much it you can do the same for other social network login's too and your all wrapped up.

## Config

1. For facebook login you will need to add the following to your android manifest 
```xml
    <meta-data
        android:name="com.facebook.sdk.ApplicationId"
        android:value="@string/facebook_app_id" />
```

2. For google you need to add `google-services.json` to your projects root directory

3. For twitter you just need to pass it along with the helper.
