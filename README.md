<h1 align="center">Social Login Helper For Android</h1>
<p align="center">
  <a href="https://android-arsenal.com/api?level=17"> <img src="https://img.shields.io/badge/API-17%2B-blue.svg?style=flat" /></a>
  <a href="https://jitpack.io/#mukeshsolanki/social-login-helper"> <img src="https://jitpack.io/v/mukeshsolanki/social-login-helper.svg" /></a>
  <a href="https://travis-ci.org/mukeshsolanki/social-login-helper"> <img src="https://travis-ci.org/mukeshsolanki/social-login-helper.svg?branch=master" /></a>
  <a href="https://android-arsenal.com/details/1/5600"><img src="https://img.shields.io/badge/Android%20Arsenal-Social%20Login%20Helper-brightgreen.svg?style=flat" border="0" alt="Android Arsenal"></a>
  <a href="https://www.paypal.me/mukeshsolanki"> <img src="https://img.shields.io/badge/paypal-donate-yellow.svg" /></a>
  <br /><br />A simple andorid library that helps you integrate social login into your apps. It supports Facebook, Google, Twitter. Lots more will be added later.
  <br /><br/>
  <img src="https://raw.githubusercontent.com/mukeshsolanki/social-login-helper/master/login.png" width="480" />
</p>

## Supported Social Networks

1. Google
2. Facebook
3. Twitter
4. Instagram

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
    compile 'com.github.mukeshsolanki:social-login-helper:1.0.2'
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
