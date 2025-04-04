package org.example.utility.locatorUtility;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

public class MobileElementLocatorUtility {

    // Generic locator by Accessibility ID
    public static By byAccessibilityId(String accessibilityId) {
        return MobileBy.AccessibilityId(accessibilityId);
    }

    // Generic locator by Resource ID
    public static By byResourceId(String resourceId) {
        return By.id(resourceId);
    }

    // Generic locator by XPath
    public static By byXPath(String xpath) {
        return By.xpath(xpath);
    }

    // Generic locator by Class Name
    public static By byClassName(String className) {
        return By.className(className);
    }

    // Generic locator by Name (deprecated but supported in older versions)
    public static By byName(String name) {
        return MobileBy.name(name);
    }

    // Locator using text (for Android-specific UIAutomator)
    public static By byAndroidText(String text) {
        return MobileBy.AndroidUIAutomator("new UiSelector().text(\"" + text + "\")");
    }

    // Locator using partial text (Android-specific)
    public static By byAndroidPartialText(String partialText) {
        return MobileBy.AndroidUIAutomator("new UiSelector().textContains(\"" + partialText + "\")");
    }

    // Locator using iOS Predicate Strings
    public static By byIosPredicate(String predicateString) {
        return MobileBy.iOSNsPredicateString(predicateString);
    }

    // Locator using iOS Class Chain
    public static By byIosClassChain(String classChain) {
        return MobileBy.iOSClassChain(classChain);
    }

    // Locator by Tag Name (useful for web views)
    public static By byTagName(String tagName) {
        return By.tagName(tagName);
    }
}