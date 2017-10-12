# BluetoothWrapper

[![platform: Android](https://img.shields.io/badge/platform-Android-yellowgreen.svg)](https://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-19%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=19)
[![Build Status](https://travis-ci.org/GeorgiosGoniotakis/BluetoothWrapper.svg?branch=master)](https://travis-ci.org/GeorgiosGoniotakis/BluetoothWrapper)
[![codecov](https://codecov.io/gh/GeorgiosGoniotakis/BluetoothWrapper/branch/master/graph/badge.svg)](https://codecov.io/gh/GeorgiosGoniotakis/BluetoothWrapper)
[![Known Vulnerabilities](https://snyk.io/test/github/GeorgiosGoniotakis/BluetoothWrapper/badge.svg)](https://snyk.io/test/github/GeorgiosGoniotakis/BluetoothWrapper)
[ ![Download](https://api.bintray.com/packages/georgiosgoniotakis/maven/BluetoothWrapper/images/download.svg) ](https://bintray.com/georgiosgoniotakis/maven/BluetoothWrapper/_latestVersion)
[![Gitter](https://badges.gitter.im/BluetoothWrapperAndroid/Lobby.svg)](https://gitter.im/BluetoothWrapperAndroid/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=body_badge)
[![license: MPL 2.0](https://img.shields.io/badge/license-MPL%202.0-orange.svg)](https://opensource.org/licenses/MPL-2.0)

<p align="center">
  <img src="https://i.imgur.com/3MvVOE0.png" alt="BluetoothWrapper Library's Logo"/>
</p>

### What is BluetoothWrapper

BluetoothWrapper, as its name calls, is a wrapper library around Android's Bluetooth built-in classes. It aims to enable quick and easy Bluetooth integration in your Android project without you having to read the official Android documentation. <br /><br />


### Advantages of using this Bluetooth library:

* No learning curve. The developer can use simple commands like connect, send and disconnect to control the connection. Thus, you are free to focus on other things that matter most.
* Lightweight. It is a simple wrapper library which includes only the necessary methods for controlling the connection to a Bluetooth device.
* No need to search for hours on the Internet on how to implement a way of receiving incoming Bluetooth messages in your custom class. We have done this for you. More details in the included example.
* Easy subscription to notifications about the Bluetooth adapter's and connection's states
* Debug options which give you more control over your project
* Many utility methods for outputting the list of available devices
* Well documented. Includes both Javadoc documentation with thorough commenting and a detailed application example
* Scalable. The necessary things are already covered but you can very easily extend the library's capabilities depending on your project's requirements.<br /><br />


### Instructions

The library is available in JCenter and can be easily used by adding the following line into your project's dependencies on the bottom of the *build.gradle* file.


```
compile 'io.github.georgiosgoniotakis.bluetoothwrapper:bluetoothwrapper:1.1'
```

<br />
A first step is to include the library into your project's dependencies. After this step is over, you need to initialize a new object which is responsible for discovering the Bluetooth devices. Add in your preferred class, which is going to control the Bluetooth connection, the following line:

```java
BTExplorer btExplorer = BTExplorer.getInstance(handler);
``` 

<br />
Get the list of Bluetooth device as a Set<BluetoothDevice> using the following command:

```java
Set<BluetoothDevice> btDevices = btExplorer.pairedDevices();
```

<br />

Alternatively you can acquire a list of *DEVICE_NAME*, *MAC_ADRESS* String pairs using:

```java
String[][] btDevices = btExplorer.deviceList(true);
```
<br />

**NB: By setting this to true, the list is being printed in the logcat's information view.**

<br />

After the user chooses the device of preferrence, initialize a connection by calling:

```java
btExplorer.connect("00:00:00:00:00", Mode.SECURE);
```

_Where **00:00:00:00:00** is the device's MAC address._

<br />

Upon successful connection to this device, the method which is reading the incoming messages is activated and the handler starts reporting these back to the class.

<br />

To send a message to an active device use:

```java
btExplorer.send("MESSAGE");
```

<br />

Finally, to disconnect from an active device use:

```java
btExplorer.disconnect();
```

<br />

To receive notifications about the connection, subscribe to the custom BroadcastReceivers ([BTAdapterReceiver](https://github.com/GeorgiosGoniotakis/BluetoothWrapper/blob/master/bluetoothwrapper/src/main/java/io/github/georgiosgoniotakis/bluetoothwrapper/library/receivers/BTAdapterReceiver.java) and [BTDeviceReceiver](https://github.com/GeorgiosGoniotakis/BluetoothWrapper/blob/master/bluetoothwrapper/src/main/java/io/github/georgiosgoniotakis/bluetoothwrapper/library/receivers/BTDeviceReceiver.java)). 

If you have a current Context object the whole process is simplified by calling:

```java
BTReceivers btReceivers = new BTReceivers(this, true);
btReceivers.registerReceivers();
```

And for unregistering the active receivers

```java
btReceivers.unregisterReceivers();
```

<br/>

An example of using all of the library's features in an Android application, and more detailed explanations can be found [here](https://github.com/GeorgiosGoniotakis/BluetoothWrapper/blob/master/example/src/main/java/io/github/georgiosgoniotakis/bluetoothwrapper/example/MainActivity.java) 

To gain a better insight of the library, please read the provided [documentation](https://github.com/GeorgiosGoniotakis/BluetoothWrapper/tree/master/docs) <br /><br />


### Complementary Information

For any issues or recommendations, please do not hesitate to use the repository's issue tracker or the [Gitter channel](https://gitter.im/BluetoothWrapperAndroid/Lobby). If you want to contribute to this project, please check [CONTRIBUTING.md](https://github.com/GeorgiosGoniotakis/BluetoothWrapper/blob/master/CONTRIBUTING.md)

A full example of how to use this library with an Arduino board and a Bluetooth module can be found on this [gist](https://gist.github.com/GeorgiosGoniotakis/1e39a39e12a2aa095fea1ec682b0933d)

Special thanks to [Logomakr](https://logomakr.com/) for the logo's resources
