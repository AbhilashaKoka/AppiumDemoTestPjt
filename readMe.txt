WinAppDriver (Windows Application Driver) is a tool developed by Microsoft that
enables UI test automation for Windows applications.
 It functions as an Appium-compatible server,
 allowing users to automate interactions with Universal Windows Platform (UWP),
 Windows Presentation Foundation (WPF),
 Windows Forms (WinForms),
 and classic Windows applications.


Key aspects of WinAppDriver:


Requirements:
Windows 10 or Windows 11 operating system.
Developer Mode enabled in Windows settings.
A test runner that supports Appium,
such as MSTest with Visual Studio,
 or other frameworks like Selenium or Appium.


Functionality:
It exposes a WebDriver-like API for interacting with UI elements of Windows applications.
Allows identification of application elements, design of test scenarios,
and creation of test scripts using various programming languages (e.g., Python, C#, Java).
Supports execution of test scripts to simulate user interactions and capture results for analysis.

Installation and Setup:
Enable Developer Mode in Windows settings.
Download the Windows Application Driver (WinAppDriver) installer from the official Microsoft
GitHub releases page.
Install the downloaded .msi file.
Run WinAppDriver.exe from its installed location (typically C:\Program Files (x86)\Windows Application Driver).



Usage:
Test scripts are written to interact with application elements using methods like FindElementByAccessibilityId().
Sessions are created to connect to the WinAppDriver server, and actions are performed within these sessions.
The WinAppDriver can be used as a standalone tool or integrated with existing Appium or Selenium frameworks.


node.toml

Full File Overview:
This file configures a Selenium Grid Node that relays requests to an Appium server running locally on port 4723

Section Purpose
[server]->Configures the node`s own port(not Appium
[node]->Prevents auto-detecting local browser drivers
[relay]->Forwards Selenium Grid requests to your Appium server
config->Define what kind of sessions this node can run

[server]

port =4444

port=4444:Sets the port where  the selenium node itself will run(not the grid hub).
This is typically used if this node acts as a standalone or for internal communication.
if you`re using this Grid Hub (usually on port 4444), it`s often fine.

[node] Section
[node]
detect-drivers=false
detch-drivers=false:tells Selenium not to auto-detect any browser drivers(like Chromeriver, GeckoDriver).
This is important when you`re not using traditional Browsers directly but instead relaying to Appium, which manages the devices

[relay] Section
[relay]
url="http://localhost:4723/wb/hub"
status-endpoint="/status"
url:The Appium server`s endpoint to which requests will be forwarded
Here, Appium is running locally on port 4723

status-endpoint:Path to chek the node`s status(Standard for Appium)

configs Array
configs=[
    "1","{\"browserName\":\"Chrome\",\"platformName\":\"ANDROID\"}",
    "1","{\"browserName\":\"Safari\",\"platformName\":\"IOS\"}"
]
This defines the capabilities that the Appium node supports
Each config has:
"1"-> indicates the max number of concurrent sessions for that capability
A JSON String describing the Appium desired capabilities
so:
1 This node can hanle 1 session for Android Chrome on version 12
And 1 session for Android Chrome on version 12.1
These capabilities must match what your emulator or devices supports and what your Appium server can handle


These capabilities must match what your emulator or devices supports and what your Appium server can handle
These capabilities must match what your emulator or devices supports and what your Appium server can handle

c:\users\..\AppData\Local\Android\sdk\emulator
emulator -avd Pixel6 -feature -Vulkan