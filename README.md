# myket_iap

[![pub package](https://img.shields.io/pub/v/myket_iap.svg)](https://pub.dev/packages/myket_iap)
[![license](https://img.shields.io/badge/license-BSD--3--Clause-blue.svg)](LICENSE)
[![platform](https://img.shields.io/badge/platform-android-green.svg)](https://flutter.dev)

A Flutter plugin for integrating **Myket In-App Billing (IAP)** into Android applications. This plugin provides a seamless, developer-friendly interface to initialize the billing service, query inventory and SKU details, launch secure purchase flows, and handle consumable and non-consumable products.

> **Note:** This plugin supports **Android only**, as Myket is an Android application store.

---

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Android Setup](#android-setup)
  - [1. Add Myket Maven Repository](#1-add-myket-maven-repository)
  - [2. Configure Manifest Placeholders](#2-configure-manifest-placeholders)
  - [3. Multi-Store & Flavor Support](#3-multi-store--flavor-support)
  - [4. Migration Guide (from 1.0.x to 1.1.0+)](#4-migration-guide-from-10x-to-110)
- [Usage Guide](#usage-guide)
  - [Step 1: Import the Package](#step-1-import-the-package)
  - [Step 2: Initialize Billing Service](#step-2-initialize-billing-service)
  - [Step 3: Query Inventory & Product Details](#step-3-query-inventory--product-details)
  - [Step 4: Launch Purchase Flow](#step-4-launch-purchase-flow)
  - [Step 5: Consume Purchases (Consumable Items)](#step-5-consume-purchases-consumable-items)
  - [Step 6: Check Purchase Status](#step-6-check-purchase-status)
  - [Step 7: Dispose Resources](#step-7-dispose-resources)
- [Security Best Practices](#security-best-practices)
- [Data Models & API Reference](#data-models--api-reference)
- [Response & Error Codes](#response--error-codes)
- [Example Application](#example-application)
- [Troubleshooting](#troubleshooting)
- [License](#license)

---

## Features

- **Easy Setup**: Minimal configuration required in your Android project.
- **Full In-App Billing Support**: Support for one-time purchases (consumables and non-consumables).
- **Inventory & Pricing Queries**: Query user-owned products and SKU metadata (price, title, description).
- **Multi-Store Ready**: Switch between app stores dynamically using Gradle flavors and manifest placeholders.
- **Robust Error Handling**: Standardized result codes with descriptive messages.
- **Modern Android & Flutter Support**: Fully compatible with Flutter 3+, Android Gradle Plugin 8+, Kotlin 2+, and Android SDK 34+.

---

## Prerequisites

1. A registered developer account on the [Myket Developer Portal](https://developer.myket.ir/).
2. An application registered in your Myket developer panel.
3. Your app's **RSA Public Key** copied from the Myket developer console.
4. Products (SKUs) defined in your application console on Myket.
5. The **Myket App Store** installed on the testing device or emulator.

---

## Installation

Add `myket_iap` to your Flutter project's dependencies:

```bash
flutter pub add myket_iap
```

Or add it directly to your `pubspec.yaml`:

```yaml
dependencies:
  flutter:
    sdk: flutter
  myket_iap: ^2.0.1
```

Then run:

```bash
flutter pub get
```

---

## Android Setup

### 1. Add Myket Maven Repository

The native billing library is hosted on Myket's Maven repository. Ensure that `https://maven.myket.ir` is present in your Android repository configuration.

#### For modern Flutter projects (`android/settings.gradle.kts`):

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.myket.ir")
    }
}
```

#### For Groovy DSL (`android/build.gradle`):

```groovy
allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://maven.myket.ir' }
    }
}
```

---

### 2. Configure Manifest Placeholders

Add the required manifest placeholders to your application's `defaultConfig` in `android/app/build.gradle` or `android/app/build.gradle.kts`.

#### Kotlin DSL (`android/app/build.gradle.kts`):

```kotlin
android {
    ...
    defaultConfig {
        ...
        val marketApplicationId = "ir.mservices.market"
        val marketBindAddress = "ir.mservices.market.InAppBillingService.BIND"
        manifestPlaceholders.putAll(
            mapOf(
                "marketApplicationId" to marketApplicationId,
                "marketBindAddress" to marketBindAddress,
                "marketPermission" to "$marketApplicationId.BILLING"
            )
        )
    }
}
```

#### Groovy DSL (`android/app/build.gradle`):

```groovy
android {
    ...
    defaultConfig {
        ...
        def marketApplicationId = "ir.mservices.market"
        def marketBindAddress = "ir.mservices.market.InAppBillingService.BIND"
        manifestPlaceholders += [
            marketApplicationId: "${marketApplicationId}",
            marketBindAddress  : "${marketBindAddress}",
            marketPermission   : "${marketApplicationId}.BILLING"
        ]
    }
}
```

---

### 3. Multi-Store & Flavor Support

If you distribute your app across multiple Android stores (e.g., Myket, Cafe Bazaar, Google Play), you can define product flavors in your `android/app/build.gradle` or `build.gradle.kts`:

```groovy
android {
    ...
    flavorDimensions "store"
    productFlavors {
        myket {
            dimension "store"
            def marketApplicationId = "ir.mservices.market"
            def marketBindAddress = "ir.mservices.market.InAppBillingService.BIND"
            manifestPlaceholders = [
                marketApplicationId: "${marketApplicationId}",
                marketBindAddress  : "${marketBindAddress}",
                marketPermission   : "${marketApplicationId}.BILLING"
            ]
        }
        bazaar {
            dimension "store"
            def marketApplicationId = "com.farsitel.bazaar"
            def marketBindAddress = "ir.cafebazaar.pardakht.InAppBillingService.BIND"
            manifestPlaceholders = [
                marketApplicationId: "${marketApplicationId}",
                marketBindAddress  : "${marketBindAddress}",
                marketPermission   : "${marketApplicationId}.permission.PAY_THROUGH_BAZAAR"
            ]
        }
    }
}
```

---

### 4. Migration Guide (from 1.0.x to 1.1.0+)

If you are upgrading from `1.0.6` or earlier:

1. **Remove manual permissions** from `android/app/src/main/AndroidManifest.xml`:
   ```xml
   <!-- REMOVE THIS -->
   <uses-permission android:name="ir.mservices.market.BILLING" />
   ```

2. **Remove `<queries>` declaration** from `android/app/src/main/AndroidManifest.xml`:
   ```xml
   <!-- REMOVE THIS -->
   <queries>
       <package android:name="ir.mservices.market" />
       <intent>
           <action android:name="ir.mservices.market.InAppBillingService.BIND" />
           <data android:mimeType="*/*" />
       </intent>
   </queries>
   ```

3. **Add the manifest placeholders** in your `build.gradle` / `build.gradle.kts` as described in [Step 2](#2-configure-manifest-placeholders). The plugin handles permissions and package queries automatically.

---

## Usage Guide

All methods on `MyketIAP` are static and return `Future`s.

### Step 1: Import the Package

```dart
import 'package:myket_iap/myket_iap.dart';
import 'package:myket_iap/util/iab_result.dart';
import 'package:myket_iap/util/purchase.dart';
import 'package:myket_iap/util/inventory.dart';
import 'package:myket_iap/util/sku_details.dart';
```

---

### Step 2: Initialize Billing Service

Initialize the billing service early in your application lifecycle (for example, in your stateful widget's `initState`):

```dart
@override
void initState() {
  super.initState();
  _initializeBilling();
}

Future<void> _initializeBilling() async {
  const String myketRsaKey = "YOUR_RSA_KEY_FROM_MYKET_PANEL";

  IabResult? result = await MyketIAP.init(
    rsaKey: myketRsaKey,
    enableDebugLogging: true, // Set to false in production
  );

  if (result != null && result.isSuccess()) {
    print("Billing setup successful: ${result.mMessage}");
    // Proceed to query inventory
    _checkInventory();
  } else {
    print("Billing setup failed: ${result?.mMessage}");
  }
}
```

---

### Step 3: Query Inventory & Product Details

Query purchases already owned by the user, and optionally fetch SKU details (price, title, description):

```dart
Future<void> _checkInventory() async {
  final Map<dynamic, dynamic> response = await MyketIAP.queryInventory(
    querySkuDetails: true,
    skus: ["gas", "premium_upgrade"],
  );

  IabResult? result = response[MyketIAP.RESULT];
  Inventory? inventory = response[MyketIAP.INVENTORY];

  if (result != null && result.isSuccess() && inventory != null) {
    // 1. Inspect SKU details
    SkuDetails? gasDetails = inventory.mSkuMap["gas"];
    if (gasDetails != null) {
      print("Gas Price: ${gasDetails.mPrice}");
      print("Gas Title: ${gasDetails.mTitle}");
    }

    // 2. Check owned purchases
    Purchase? premiumPurchase = inventory.mPurchaseMap["premium_upgrade"];
    if (premiumPurchase != null) {
      print("User owns premium upgrade: ${premiumPurchase.mOrderId}");
    }

    // 3. Handle consumable items that were bought but not yet consumed
    Purchase? gasPurchase = inventory.mPurchaseMap["gas"];
    if (gasPurchase != null) {
      await _consumeProduct(gasPurchase);
    }
  } else {
    print("Failed to query inventory: ${result?.mMessage}");
  }
}
```

---

### Step 4: Launch Purchase Flow

To initiate a purchase flow for a product:

```dart
Future<void> _buyProduct(String sku) async {
  // Developer payload can be used for server-side verification and fraud prevention
  const String developerPayload = "custom_payload_or_order_token";

  final Map<dynamic, dynamic> response = await MyketIAP.launchPurchaseFlow(
    sku: sku,
    payload: developerPayload,
  );

  IabResult? result = response[MyketIAP.RESULT];
  Purchase? purchase = response[MyketIAP.PURCHASE];

  if (result != null && result.isSuccess() && purchase != null) {
    print("Purchase successful for SKU: ${purchase.mSku}");
    print("Order ID: ${purchase.mOrderId}");
    print("Purchase Token: ${purchase.mToken}");

    // If the product is consumable, consume it immediately
    if (purchase.mSku == "gas") {
      await _consumeProduct(purchase);
    } else {
      // Non-consumable product (e.g. permanent upgrade)
      _unlockFeature();
    }
  } else {
    print("Purchase cancelled or failed: ${result?.mMessage}");
  }
}
```

---

### Step 5: Consume Purchases (Consumable Items)

For consumable products (such as virtual currency, coins, extra lives), you must call `consume` after purchase. Once consumed, the item can be purchased again.

```dart
Future<void> _consumeProduct(Purchase purchase) async {
  final Map<dynamic, dynamic> response = await MyketIAP.consume(purchase: purchase);

  IabResult? result = response[MyketIAP.RESULT];
  Purchase? consumedPurchase = response[MyketIAP.PURCHASE];

  if (result != null && result.isSuccess()) {
    print("Item consumed successfully: ${consumedPurchase?.mSku}");
    // Credit virtual items / rewards to the user
  } else {
    print("Failed to consume item: ${result?.mMessage}");
  }
}
```

---

### Step 6: Check Purchase Status

To verify if a specific SKU is already owned:

```dart
Future<void> _checkPurchaseStatus(String sku) async {
  final Map<dynamic, dynamic> response = await MyketIAP.getPurchase(
    sku: sku,
    querySkuDetails: true,
  );

  IabResult? result = response[MyketIAP.RESULT];
  Purchase? purchase = response[MyketIAP.PURCHASE];

  if (result != null && result.isSuccess() && purchase != null) {
    print("SKU $sku is owned: ${purchase.mOrderId}");
  } else {
    print("SKU $sku is not owned or query failed.");
  }
}
```

---

### Step 7: Dispose Resources

Release underlying services, receivers, and memory resources when the billing helper is no longer needed (e.g., in your widget's `dispose()`):

```dart
@override
void dispose() {
  MyketIAP.dispose();
  super.dispose();
}
```

---

## Security Best Practices

1. **Verify Developer Payload**: Always pass a unique, cryptographically secure identifier or session token in `payload` and verify that `purchase.mDeveloperPayload` matches what you expect.
2. **Server-Side Signature Verification**: For critical transactions, send `purchase.mOriginalJson` and `purchase.mSignature` to your secure backend to verify authenticity with your RSA Public Key using Myket's billing verification API.
3. **Do Not Commit Raw RSA Keys**: Avoid committing unencrypted RSA public keys into public version control. Consider using environment configurations or secure asset storage.

---

## Data Models & API Reference

### `MyketIAP`

| Method | Parameters | Return Type | Description |
|---|---|---|---|
| `init` | `required String rsaKey`, `bool enableDebugLogging = false` | `Future<IabResult?>` | Initializes the IAB helper and establishes connection to the billing service. |
| `launchPurchaseFlow` | `required String sku`, `String? payload` | `Future<Map>` | Launches the in-app billing checkout flow on Myket. |
| `consume` | `required Purchase purchase` | `Future<Map>` | Consumes an owned item so it can be re-purchased. |
| `queryInventory` | `bool querySkuDetails = false`, `List<String>? skus` | `Future<Map>` | Queries all owned purchases and optionally queries SKU metadata. |
| `getPurchase` | `required String sku`, `bool querySkuDetails = false` | `Future<Map>` | Queries ownership and details for a single SKU. |
| `dispose` | None | `Future` | Closes service connections and releases native resources. |

---

### `IabResult`

Represents the result of a billing operation:

- `int mResponse`: Response code (see [Response Codes](#response--error-codes)).
- `String mMessage`: Descriptive message detailing the outcome.
- `bool isSuccess()`: Returns `true` if `mResponse == Constants.BILLING_RESPONSE_RESULT_OK`.
- `bool isFailure()`: Returns `true` if the operation failed.

---

### `Purchase`

Represents an in-app purchase:

- `String mSku`: Product identifier.
- `String mOrderId`: Unique transaction ID generated by Myket.
- `String mPackageName`: Package name of the application.
- `int mPurchaseTime`: Timestamp (epoch milliseconds) of the purchase.
- `int mPurchaseState`: Purchase state (0 = purchased, 1 = cancelled, 2 = refunded).
- `String? mDeveloperPayload`: Additional developer-supplied payload.
- `String mToken`: Unique token identifying the purchase.
- `String mOriginalJson`: Raw JSON string returned from the billing service.
- `String mSignature`: Cryptographic signature for signature verification.

---

### `SkuDetails`

Contains product metadata configured in the Myket developer console:

- `String mSku`: The product SKU.
- `String mTitle`: Product title/name.
- `String mPrice`: Localized formatted price string (e.g. `۱۰,۰۰۰ ریال`).
- `String? mDescription`: Product description.
- `String mType`: Item type (`inapp` or `subs`).

---

### `Inventory`

Container for queried purchases and SKU details:

- `Map<String, Purchase> mPurchaseMap`: Map of product SKU to `Purchase` object.
- `Map<String, SkuDetails> mSkuMap`: Map of product SKU to `SkuDetails` object.

---

## Response & Error Codes

You can access these constants via `Constants`:

### Billing Response Codes

| Code | Constant | Meaning |
|---|---|---|
| `0` | `BILLING_RESPONSE_RESULT_OK` | Success. |
| `1` | `BILLING_RESPONSE_RESULT_USER_CANCELED` | User cancelled the checkout flow. |
| `3` | `BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE` | Billing service unavailable or Myket app not installed. |
| `4` | `BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE` | Requested SKU is not available for purchase. |
| `5` | `BILLING_RESPONSE_RESULT_DEVELOPER_ERROR` | Invalid arguments provided to the API. |
| `6` | `BILLING_RESPONSE_RESULT_ERROR` | Fatal error during the purchase process. |
| `7` | `BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED` | Failure to purchase because the item is already owned. |
| `8` | `BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED` | Failure to consume because the item is not owned. |

### IAB Helper Error Codes

| Code | Constant | Meaning |
|---|---|---|
| `-1001` | `IABHELPER_REMOTE_EXCEPTION` | Remote exception while communicating with Myket. |
| `-1002` | `IABHELPER_BAD_RESPONSE` | Bad or malformed response received from billing service. |
| `-1003` | `IABHELPER_VERIFICATION_FAILED` | Purchase signature verification failed. |
| `-1004` | `IABHELPER_SEND_INTENT_FAILED` | Failed to send checkout intent to Myket. |
| `-1005` | `IABHELPER_USER_CANCELLED` | User cancelled the purchase flow. |
| `-1008` | `IABHELPER_UNKNOWN_ERROR` | Unknown error occurred. |
| `-1010` | `IABHELPER_INVALID_CONSUMPTION` | Attempted to consume an invalid or unowned item. |

---

## Example Application

A complete example implementing a sample game ("Trivial Drive") with gas purchases (consumable) and premium membership (non-consumable) is included in the [`example`](example) directory.

To run the example app:

```bash
cd example
flutter run
```

---

## Troubleshooting

### 1. `BILLING_UNAVAILABLE` (Code 3)
- Ensure the **Myket Store application** is installed and updated on the test device.
- Ensure the user is logged into their Myket account.

### 2. `ITEM_UNAVAILABLE` (Code 4)
- Verify that the SKU ID passed to `launchPurchaseFlow` exactly matches the SKU ID created in the Myket Developer Panel.
- Ensure the product is set to "Active" / published in the Myket developer console.
- Confirm your application's `applicationId` (package name) matches the registered app on Myket.

### 3. Verification Failed (Code -1003)
- Ensure you passed the correct RSA Public Key to `MyketIAP.init()`.

---

## License

This project is licensed under the BSD 3-Clause License. See the [LICENSE](LICENSE) file for details.
